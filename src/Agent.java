import java.util.List;

public interface Agent {

    public Point getLocation();
    public void setLocation(Point p);
    public List<String> calculateSteps();

}
