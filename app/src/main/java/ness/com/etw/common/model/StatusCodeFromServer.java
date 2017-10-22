package ness.com.etw.common.model;


public class StatusCodeFromServer {
    public static int getStatudi() {
        return statudi;
    }

    public static void setStatudi(int statudi) {
        StatusCodeFromServer.statudi = statudi;
    }

    public static String getStatusid() {
        return Statusid;
    }

    public static void setStatusid(String statusid) {
        Statusid = statusid;
    }

    public static int statudi = 0;
    public static String Statusid = null;
}
