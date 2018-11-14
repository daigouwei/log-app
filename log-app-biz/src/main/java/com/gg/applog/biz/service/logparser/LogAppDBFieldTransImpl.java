package com.gg.applog.biz.service.logparser;

import com.gg.applog.biz.constant.LogAppProfileMark;
import com.gg.applog.biz.domain.LogAppDBField;
import com.gg.applog.biz.domain.LogAppParserDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import scala.Serializable;

/**
 * @author daigouwei
 * @date 2018/11/8
 */
@Service
public class LogAppDBFieldTransImpl implements Serializable{
    public LogAppDBField transLogParserDetail2DBField(LogAppParserDetail logAppParserDetail) {
        if (null == logAppParserDetail) {
            return null;
        }
        LogAppDBField logAppDBField = new LogAppDBField();
        if (StringUtils.isNotBlank(logAppParserDetail.getUserPkId())) {
            logAppDBField.setAppId(logAppParserDetail.getUserPkId());
        }
        else {
            return null;
        }
        if (StringUtils.isNotBlank(logAppParserDetail.getCpid())) {
            logAppDBField.setUserId(logAppParserDetail.getCpid());
        }

        if (StringUtils.isNotBlank(logAppParserDetail.getProdId())) {
            logAppDBField.setProfile(LogAppProfileMark.PROFILE_PROD_ID + logAppParserDetail.getProdId());
            return logAppDBField;
        }
        if (StringUtils.isNotBlank(logAppParserDetail.getCatCode())) {
            logAppDBField.setProfile(LogAppProfileMark.PROFILE_CAT_CODE + logAppParserDetail.getCatCode());
            return logAppDBField;
        }
        if (StringUtils.isNotBlank(logAppParserDetail.getKeyword())) {
            logAppDBField.setProfile(LogAppProfileMark.PROFILE_KEYWORD + logAppParserDetail.getKeyword());
            return logAppDBField;
        }
        return null;
    }
}
