package com.gg.applog.biz.service.logparser;

import com.gg.applog.biz.constant.LogAppProfileMark;
import com.gg.applog.biz.domain.LogAppParserDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author daigouwei
 * @date 2018/11/7
 */
@Deprecated
@Service
public class LogAppProfileCombImpl {
    public String transLogParserDetail2Str(LogAppParserDetail logAppParserDetail) {
        if (null == logAppParserDetail) {
            return null;
        }
        if (StringUtils.isNotBlank(logAppParserDetail.getProdId())) {
            return LogAppProfileMark.PROFILE_PROD_ID + logAppParserDetail.getProdId();
        }
        if (StringUtils.isNotBlank(logAppParserDetail.getCatCode())) {
            return LogAppProfileMark.PROFILE_CAT_CODE + logAppParserDetail.getCatCode();
        }
        if (StringUtils.isNotBlank(logAppParserDetail.getKeyword())) {
            return LogAppProfileMark.PROFILE_KEYWORD + logAppParserDetail.getKeyword();
        }
        return null;
    }
}
