package chat.crag.cragchat.descriptor;


public class Rating implements Datable {

    public static int COLUMN_ROUTE_ID = 0;
    public static int COLUMN_YDS = 1;
    public static int COLUMN_STARS = 2;
    public static int COLUMN_DATE = 3;
    public static int COLUMN_USERNAME = 4;

    private int yds;
    private int stars;
    private int routeId;
    private String date;
    private String userName;

    public Rating(int routeId, int yds, int stars, String userName, String date) {
        this.routeId = routeId;
        this.yds = yds;
        this.stars = stars;
        this.userName = userName;
        this.date = date;
    }

    public int getYds() {
        return yds;
    }

    public int getStars() {
        return stars;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }

}
