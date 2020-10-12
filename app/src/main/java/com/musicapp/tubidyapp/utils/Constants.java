package com.musicapp.tubidyapp.utils;

public class Constants {

    static String  statususer;
    static  String  ads;
    static String  key;
    static  String  statusapp;
    static  String  appupdate;

    static String serverurl="https://fando.id/soundcloud/get.php?id=";//dont change





    public static void setStatususer(String statususer) {
        Constants.statususer = statususer;
    }



    public static void setAds(String ads) {
        Constants.ads = ads;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        Constants.key = key;
    }



    public static String getStatusapp() {
        return statusapp;
    }

    public static void setStatusapp(String statusapp) {
        Constants.statusapp = statusapp;
    }

    public static String getAppupdate() {
        return appupdate;
    }

    public static void setAppupdate(String appupdate) {
        Constants.appupdate = appupdate;
    }

    public static String getServerurl() {
        return serverurl;
    }



}
