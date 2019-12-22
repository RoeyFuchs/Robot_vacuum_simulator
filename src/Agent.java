
public interface Agent {

    public Point getLocation();
    public void setLocation(Point p);
    public Point selectStep(PartialMap m);
}
