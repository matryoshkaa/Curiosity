package com.example.curiosity;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Vaccination {

    private String vaccineName;
    private String clinicName;
    private Date date;

    public Vaccination(){
        //empty constructor
    }

    public Vaccination(String vaccineName,String clinicName, Date date){
        this.vaccineName=vaccineName;
        this.clinicName=clinicName;
        this.date=date;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public Date getDate() {
        return date;
    }


}
