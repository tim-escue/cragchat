package chat.crag.cragchat.descriptor;

public class Area extends Displayable {

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_LATITUDE = 2;
    public static final int COLUMN_LONGITUDE = 3;
    public static final int COLUMN_REVISION = 4;

    private String name;
    private double latitude;
    private double longitude;
    private int id;

    public Area(int id, String name, double latitude, double longitude, int revision) {
        super(id, revision);
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int getId() {
        return id;
    }
}
