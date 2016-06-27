package com.test.app.service.filter;

import com.test.app.dao.MedicalRecordAffixRepository;
import com.test.app.domain.MedicalRecordAffix;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by renwenlong on 16/6/20.
 */
@Component
public class AffixFilterOne {
    private static final Log LOGGER = LogFactory.getLog(AffixFilterOne.class);

    @Autowired
    private MedicalRecordAffixRepository medicalRecordAffixRepository;

    @Value("${action.qiuniu.pubUrl}")
    private String pubUrl;

    @Value("${action.qiuniu.prodUrl}")
    private String prodUrl;

    public List<String> execute() {
        DateTime today = new DateTime(new Date());
        DateTime yesterday = today.minusDays(1);

        DateTime yesterdayBegin = yesterday.withHourOfDay(20).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime yesterdayEnd = yesterday.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);

        LOGGER.info("查询出1天前用户上传的图片记录, 查询时间段为: " + yesterdayBegin + " 至 " + yesterdayEnd);

        List<MedicalRecordAffix> affixList = medicalRecordAffixRepository.findServerCreateTimeYesterday
                (yesterdayBegin.toDate(), yesterdayEnd.toDate());

        LOGGER.info("查询出1天前用户上传的所有图片记录条数为: " + affixList.size());


        //测试环境下拼接七牛图片访问路径,上线后换成线上域名prodUrl
        List<String> urlList = new ArrayList<String>();

        if (affixList != null && affixList.size() > 0) {
            for (MedicalRecordAffix affix : affixList) {
                urlList.add(prodUrl + affix.getUserId() + "/" + affix.getMedicalRecordUid() + "/" + affix.getFilePath() + "/");
            }
            return urlList;
        } else {
            return new ArrayList<String>();
        }

    }

}
