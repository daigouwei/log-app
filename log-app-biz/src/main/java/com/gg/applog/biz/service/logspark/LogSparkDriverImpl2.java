package com.gg.applog.biz.service.logspark;

import com.gg.applog.biz.domain.LogAppDBField;
import com.gg.applog.biz.domain.LogAppParserDetail;
import com.gg.applog.biz.service.logparser.LogAppDBFieldTransImpl;
import com.gg.applog.biz.service.logparser.LogAppHistoryParserImpl;
import com.gg.applog.common.service.BaseService;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Serializable;
import scala.Tuple2;

/**
 * @author daigouwei
 * @date 2018/11/12
 */
@Service
public class LogSparkDriverImpl2 extends BaseService implements Serializable {
    @Autowired
    private LogAppHistoryParserImpl logAppHistoryParserImpl;

    @Autowired
    private LogAppDBFieldTransImpl logAppDbFieldTransImpl;

    @Autowired
    private LogAppSparkSqlImpl logAppSparkSqlImpl;

    public void handleAppLog() {
        SparkSession sparkSession = initSpark();
        JavaPairRDD<String, LogAppDBField> logAppDBFieldJavaPairRDD =
            importAndHandleLog(sparkSession, "./log-app-biz/src/resources/nginxlog/*");
        store2DB(sparkSession, logAppDBFieldJavaPairRDD);
        stopSpark(sparkSession);
        LOG.info("over!!!!!!");
    }

    private SparkSession initSpark() {
        LOG.info("Spark init...");
        return SparkSession.builder().appName("APP LOG").master("local").getOrCreate();
    }

    private void stopSpark(SparkSession sparkSession) {
        LOG.info("Spark stop...");
        sparkSession.stop();
    }

    private JavaPairRDD<String, LogAppDBField> importAndHandleLog(SparkSession sparkSession, String logPath) {
        LOG.info("import and handle app log...");
        JavaRDD<String> lines = sparkSession.read().textFile(logPath).javaRDD();
        return lines.filter(line -> 10 == line.split(",").length)
            .mapToPair(line -> new Tuple2<String, String>(line.split(",")[2], line)).mapValues(line -> {
                LogAppParserDetail logAppParserDetail = logAppHistoryParserImpl.parseLog(line);
                LogAppDBField logAppDBField = logAppDbFieldTransImpl.transLogParserDetail2DBField(logAppParserDetail);
                if (null == logAppDBField) {
                    LOG.info(line + "不能转换为DBField，丢弃");
                    return null;
                }
                return logAppDBField;
            }).filter(tuple -> null != tuple._2()).reduceByKey((logAppDBField1, logAppDBField2) -> {
                if (StringUtils.isNotBlank(logAppDBField1.getUserId()) && StringUtils
                    .isBlank(logAppDBField2.getUserId())) {
                    logAppDBField2.setUserId(logAppDBField1.getUserId());
                }
                if (logAppDBField2.getProfile()
                    .equals(logAppDBField1.getProfile().substring(logAppDBField1.getProfile().lastIndexOf(",") + 1))) {
                    logAppDBField2.setProfile(logAppDBField1.getProfile());
                }
                else {
                    logAppDBField2.setProfile(logAppDBField1.getProfile() + "," + logAppDBField2.getProfile());
                }
                String profile = logAppDBField2.getProfile()
                    .equals(logAppDBField1.getProfile().substring(logAppDBField1.getProfile().lastIndexOf(",") + 1)) ?
                    logAppDBField1.getProfile() : logAppDBField1.getProfile() + "," + logAppDBField2.getProfile();
                logAppDBField2.setProfile(profile);
                return logAppDBField2;
            });
    }

    private void store2DB(SparkSession sparkSession, JavaPairRDD<String, LogAppDBField> logAppDBFieldJavaPairRDD) {
        LOG.info("store into mysql...");
        //转换为RDD
        JavaRDD<LogAppDBField> logAppDBFieldJavaRDD = logAppDBFieldJavaPairRDD.map(Tuple2::_2);
        Dataset<Row> logAppDBFieldDF = sparkSession.createDataFrame(logAppDBFieldJavaRDD, LogAppDBField.class);
        logAppDBFieldDF.write().mode("append")
            .option("createTableColumnTypes", "app_id VARCHAR(50), user_id VARCHAR(50), profile VARCHAR(4000)")
            .jdbc(logAppSparkSqlImpl.getUrl(), logAppSparkSqlImpl.getTable(), logAppSparkSqlImpl.getConnProperties());
    }
}
