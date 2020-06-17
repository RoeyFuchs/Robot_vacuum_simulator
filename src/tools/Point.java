package tools;

public class Point {
    private int x;
    private int y;
    private Integer value; //value of point. may use as wight
    private int beenHereCounter = 0;
    private Integer info = NO_INFO; //info of point. may use as temp memory
    public static final Integer NO_INFO = -1;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //default constructor
    public Point() {
        this(0, 0);
    }

    public Point(Integer value) {
        this(0, 0);
        this.value = value;
    }

    //copy constructor
    public Point(Point p) {
        this.x = p.getX();
        this.y = p.getY();
        this.value = p.getValue();
        this.info = p.getInfo();
        this.beenHereCounter = p.getBeenHereCounter();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    //if agent step here. can use to check how many times agent been here
    public void here() {
        this.beenHereCounter++;
    }

    public boolean isSameLocation(Point p){
        return this.getX() == p.getX() && this.getY() == p.getY();
    }
    public Integer getInfo() {
        return info;
    }

    public Point setInfo(Integer info) {
        this.info = info;
        return this;
    }

    public int getBeenHereCounter() {
        return beenHereCounter;
    }

}
