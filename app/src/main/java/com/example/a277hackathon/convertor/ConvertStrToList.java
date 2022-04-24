package com.example.a277hackathon.convertor;

import java.util.ArrayList;
import java.util.List;

public class ConvertStrToList {
    public List<List<Float>> converStrTolist(String datapoint) {
        String after = datapoint.substring(1, datapoint.length() - 1);
        String[] parts = after.split(",");

        List<List<Float>> sum = new ArrayList<>();
        List<Float> cur = null;
        for (String pp : parts) {
            char[] chars = pp.toCharArray();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < chars.length; i++) {
                char curchar = chars[i];
                if (i == chars.length - 1 && curchar != ']') {
                    Float curNum = Float.valueOf(sb.toString());
                    cur.add(curNum);
                    continue;
                }
                if(curchar == '['){
                    cur = new ArrayList<>();
                    continue;
                }
                if(curchar == ']') {
                    Float curNum = Float.valueOf(sb.toString());
                    cur.add(curNum);
                    sum.add(new ArrayList<>(cur));
                    cur.clear();
                }
                sb.append(curchar);
            }
        }
        return sum;
    }
}