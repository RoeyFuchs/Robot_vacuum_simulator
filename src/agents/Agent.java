package agents;

import java.util.List;
import java.util.Observable;

import loggers.Logger;
import tools.Point;

public interface Agent {

    public Point getLocation();
    public void setLocation(Point p);
    public Point doStep();

    void addObserver(Logger logger);
}
