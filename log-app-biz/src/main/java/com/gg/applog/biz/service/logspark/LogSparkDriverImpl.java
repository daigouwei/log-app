package com.gg.applog.biz.service.logspark;

import com.gg.applog.biz.domain.LogAppDBField;
import com.gg.applog.biz.domain.LogAppParserDetail;
import com.gg.applog.biz.service.logparser.LogAppDBFieldTransImpl;
import com.gg.applog.biz.service.logparser.LogAppHistoryParserImpl;
import com.gg.applog.common.service.BaseService;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

/**
 * @author daigouwei
 * @date 2018/11/8
 */
@Service
public class LogSparkDriverImpl extends BaseService {
    @Autowired
    private LogAppHistoryParserImpl logAppHistoryParserImpl;

    @Autowired
    private LogAppDBFieldTransImpl logAppDbFieldTransImpl;

    @Autowired
    private LogAppSparkSqlImpl logAppSparkSqlImpl;

    public void handleAppNginxLog() {
        JavaSparkContext sparkContext = new JavaSparkContext(new SparkConf().setAppName("APP NGINX LOG"));
        JavaRDD<String> lines = sparkContext.textFile("./src/main/resources/nginxlog/*");
        JavaPairRDD<String, LogAppDBField> pairLines = lines.filter(line -> 10 == line.split(",").length)
                .mapToPair(line -> new Tuple2<String, String>(line.split(",")[2], line)).mapValues(line -> {
                    LogAppParserDetail logAppParserDetail = logAppHistoryParserImpl.parseLog(line);
                    LogAppDBField logAppDBField =
                            logAppDbFieldTransImpl.transLogParserDetail2DBField(logAppParserDetail);
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
                    if (logAppDBField2.getProfile().equals(logAppDBField1.getProfile()
                            .substring(logAppDBField1.getProfile().lastIndexOf(",") + 1))) {
                        logAppDBField2.setProfile(logAppDBField1.getProfile());
                    }
                    else {
                        logAppDBField2.setProfile(logAppDBField1.getProfile() + "," + logAppDBField2.getProfile());
                    }
                    String profile = logAppDBField2.getProfile().equals(logAppDBField1.getProfile()
                            .substring(logAppDBField1.getProfile().lastIndexOf(",") + 1)) ?
                            logAppDBField1.getProfile() :
                            logAppDBField1.getProfile() + "," + logAppDBField2.getProfile();
                    logAppDBField2.setProfile(profile);
                    return logAppDBField2;
                });
        //todo 存mysql操作
    }
}
