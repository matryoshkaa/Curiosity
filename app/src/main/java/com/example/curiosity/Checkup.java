package com.example.curiosity;

import java.util.Date;

public class Checkup {
    private String checkupName;
    private String clinicName;
    private Date date;
    private String summary;


public Checkup(){
    //empty constructor
}

public Checkup(String checkupName,String clinicName,Date date,String summary){
    this.checkupName=checkupName;
    this.clinicName=clinicName;
    this.date=date;
    this.summary=summary;
}

    public String getCheckupName() {
        return checkupName;
    }

    public void setCheckupName(String checkupName) {
        this.checkupName = checkupName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}