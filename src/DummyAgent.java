public class DummyAgent implements Agent {
    private int x;
    private int y;


    public Point getLocation() {
        return new Point(this.x, this.y);
    }
    public void setLocation(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

}
