package com.example.ecoleenligne.utils;

public class Utils {

    public static String like(String query, String str) {
        str = str.toLowerCase();
        int endSubIndex = Math.min(3, query.length());
        String expr = query.substring(0,endSubIndex);
        if(str.matches(expr+".*") || query.matches(str)){
            return str;
        }else if(query.equalsIgnoreCase("all")){
            return str;
        }
        return "";
    }
}
