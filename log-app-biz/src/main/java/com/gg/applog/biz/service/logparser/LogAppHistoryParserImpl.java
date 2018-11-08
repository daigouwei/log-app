package com.gg.applog.biz.service.logparser;

import com.gg.applog.biz.domain.LogAppParserDetail;
import com.gg.applog.common.service.BaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author daigouwei
 * @date 2018/11/1
 */
@Service
public class LogAppHistoryParserImpl extends BaseService {
    public LogAppParserDetail parseLog(String logLine) {
        return parseDetailLog(logLine);
    }

    private LogAppParserDetail parseDetailLog(String logLine) {
        if (StringUtils.isBlank(logLine)) {
            return null;
        }
        try {
            LogAppParserDetail logAppParserDetail = new LogAppParserDetail();
            String[] appLogFields = logLine.split(",");
            if (10 != appLogFields.length) {
                return null;
            }
            if (!"-".equals(appLogFields[0]) && !"-".equals(appLogFields[1])) {
                logAppParserDetail.setCpid(appLogFields[0] + "@" + appLogFields[1]);
            }
            logAppParserDetail.setTime(appLogFields[4]);
            logAppParserDetail.setUserPkId(appLogFields[2]);
            logAppParserDetail.setIp("-".equals(appLogFields[3]) ? null : appLogFields[3]);
            if (!"-".equals(appLogFields[6])) {
                logAppParserDetail.setProdId(appLogFields[6]);
                return logAppParserDetail;
            }
            if (!"-".equals(appLogFields[8])) {
                logAppParserDetail.setCatCode(appLogFields[8]);
                return logAppParserDetail;
            }
            if (!"-".equals(appLogFields[9])) {
                logAppParserDetail.setKeyword(appLogFields[9].trim());
                return logAppParserDetail;
            }
        }
        catch (Exception e) {
            LOG.error("logLine==" + logLine + ", 处理异常", e);
        }
        return null;
    }
}
