package com.example.a277hackathon.Model;

import androidx.annotation.NonNull;

import com.example.a277hackathon.convertor.ConvertStrToList;

import java.util.List;

public class Trade {
    private String url;
    private String datapoint;

    @NonNull
    @Override
    public String toString() {
        return "Trade{"+
                "url=" + url+
                ", data=" + datapoint +
                "}";
    }

    public List<List<Float>> toList() {
        ConvertStrToList convertStrToList = new ConvertStrToList();
        return convertStrToList.converStrTolist(datapoint);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDatapoint(String datapoint) {
        this.datapoint = datapoint;
    }

    public String getUrl() {
        return url;
    }
    public String getDatapoint() {
        return datapoint;
    }
}