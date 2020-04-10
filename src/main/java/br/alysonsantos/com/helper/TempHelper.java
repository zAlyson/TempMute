package br.alysonsantos.com.helper;

public class TempHelper {

    private static TempHelper instance;

    public static TempHelper getInstance() {
        if (instance == null) {
            instance = new TempHelper();
        }
        return instance;
    }

    public String getTime(long time) {
        time = time / 1000;
        int hours = time > 3600 ? (int) (time / 3600) : 0;
        int minutes = time > 60 ? (int) (time / 60) : 0;
        int seconds = (int) time;
        return (hours > 0 ? hours + " hours " : "") + (minutes > 0 ? minutes + " minutes " : "") + (seconds > 0 ? seconds
                + " seconds" : "");
    }
}
