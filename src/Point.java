

public class Point {
    private int x;
    private int y;
    private Integer value;
    private int beenHereCounter = 0;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public Point(Integer value) {
        this(0, 0);
        this.value = value;
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

    public void here() {
        this.beenHereCounter++;
    }

    public boolean isSameLocation(Point p){
        if(this.getX()==p.getX()&& this.getY()==p.getY()){
            return true;
        }
        return false;
    }



}
