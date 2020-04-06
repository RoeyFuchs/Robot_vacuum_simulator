import java.util.List;
import java.util.Observable;

public interface MyAgent   {

    public Point getLocation();
    public void setLocation(Point p);
    public Point doStep();

}
