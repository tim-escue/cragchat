package chat.crag.cragchat.descriptor;


import org.json.JSONObject;

public class Send implements Datable {

    public static int COLUMN_ROUTE_ID = 0;
    public static int COLUMN_PITCHES = 1;
    public static int COLUMN_DATE = 2;
    public static int COLUMN_SEND_TYPE = 3;
    public static int COLUMN_ATTEMPTS = 4;
    public static int COLUMN_STYLE = 5;
    public static int COLUMN_USERNAME = 6;


    private int routeId;
    private int pitches;
    private String sendType;
    private int attempts;
    private String style;
    private String date;
    private String userName;

    public Send(int routeId, int pitches, String date, String sendType, int attempts, String style, String userName) {
        this.routeId = routeId;
        this.pitches = pitches;
        this.date = date;
        this.sendType = sendType;
        this.attempts = attempts;
        this.style = style;
        this.userName = userName;
    }

    public static Send decode(JSONObject json) {
        try {
            return new Send(json.getInt("routeId"), json.getInt("pitches"), json.getString("date"),json.getString("sendType"),
                    json.getInt("attempts"), json.getString("style"), json.getString("username"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getRouteId() {
        return routeId;
    }

    public int getPitches() {
        return pitches;
    }

    public String getSendType() {
        return sendType;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }
}
