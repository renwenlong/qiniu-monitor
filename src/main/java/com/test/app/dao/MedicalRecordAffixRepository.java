package com.test.app.dao;

import com.test.app.domain.MedicalRecordAffix;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by renwenlong on 16/6/20.
 */
public interface MedicalRecordAffixRepository  extends CrudRepository<MedicalRecordAffix, Long> {

    @Query("select u from MedicalRecordAffix u where u.serverCreateTime >= ?1 and u.serverCreateTime <= ?2")
    List<MedicalRecordAffix> findServerCreateTimeYesterday(Date yesterdayBegin, Date yesterdayEnd);

}
