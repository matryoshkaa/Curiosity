package com.example.curiosity;

import com.google.firebase.database.Exclude;

public class WeightData {

    String date;
    String weight;
    private String documentId;


    public WeightData(){
        //empty
    }

    public WeightData(String date, String weight){
        this.date=date;
        this.weight=weight;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
