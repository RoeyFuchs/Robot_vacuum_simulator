import java.util.List;

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


    //agent strategy
    public Point selectStep(Map m) {
        return new Point(this.x+1, this.y);
    }

    @Override
    public List<String> calculateSteps(){
        return null;
    }


}
