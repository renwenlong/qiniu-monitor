package com.test.app.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by renwenlong on 16/6/20.
 */
@Entity
@Table(name = "MedicalRecord_Affix")
public class MedicalRecordAffix {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int userId;
    private String medicalRecordUid;
    private String filePath;
    private Date serverCreateTime;
    private int aFileSize;
    private int status;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMedicalRecordUid() {
        return medicalRecordUid;
    }

    public void setMedicalRecordUid(String medicalRecordUid) {
        this.medicalRecordUid = medicalRecordUid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Date serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public int getaFileSize() {
        return aFileSize;
    }

    public void setaFileSize(int aFileSize) {
        this.aFileSize = aFileSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
