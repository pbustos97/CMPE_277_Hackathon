package com.example.a277hackathon.convertor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMap {

    public static Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static List<Map<String, Object>> getList(String jsonString) {
        List<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    public static List<List<Float>> convertJsonToListofList(String json) throws Exception{
        JSONArray jArray=new JSONArray(json);
        List<List<Float>> list = new ArrayList<>();
        int length = jArray.length();
        for(int i=0; i<length;i++) {
            List<Float> innerlist = new ArrayList<>();
            String innerJsonArrayString = jArray.get(i).toString();
            JSONArray innerJsonArray = new JSONArray(innerJsonArrayString);
            int innerLength = innerJsonArray.length();
            for(int j=0;j<innerLength;j++){
                String str = innerJsonArray.getString(j);
                innerlist.add(Float.valueOf(str));
            }
            list.add(innerlist);
        }
        return list;
    }

}