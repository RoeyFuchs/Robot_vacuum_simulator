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

    @Override
    //agent strategy
    public Point selectStep(PartialMap m) {
        return new Point(this.x+1, this.y);
    }

}
