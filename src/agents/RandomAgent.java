package agents;

import loggers.Logger;
import loggers.LoggerMessageMaker;
import tools.Map;
import tools.Point;

import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomAgent implements Agent {
    private Map map;
    private Point current;
    private PropertyChangeSupport pcs;


    public RandomAgent(Map map) {
        this.pcs = new PropertyChangeSupport(this);
        this.map = new Map(map);
    }

    @Override
    public Point getLocation() {
        return this.current;
    }

    @Override
    public void setLocation(Point p) {
        if (this.current != null) //for first time
            this.current.setValue(Map.BEEN_HERE);
        this.current = this.map.getLocation(p);
    }

    @Override
    public Point doStep() {
        int y = current.getY();
        int x = current.getX();

        LinkedList<Point> relevantPoints = new LinkedList<>();
        LinkedList<Point> yetReachedPoints = new LinkedList<>();
        //check all the options, clockwise, from north.
        if (y > 0)//op. 1
            addPointToLists(this.map.getLocation(new Point(x, y-1)), relevantPoints, yetReachedPoints);

        if (y > 0 && x < this.map.getRowsNumber() - 1)  //op. 2
            addPointToLists(this.map.getLocation(new Point(x+1, y-1)), relevantPoints, yetReachedPoints);

        if (x < this.map.getRowsNumber() - 1) //op. 3
            addPointToLists(this.map.getLocation(new Point(x+1, y)), relevantPoints, yetReachedPoints);

        if (y < this.map.getColumnsNumber() - 1 && x < this.map.getRowsNumber() - 1)  // op. 4
            addPointToLists(this.map.getLocation(new Point(x+1, y+1)), relevantPoints, yetReachedPoints);

        if (y < this.map.getColumnsNumber() - 1)  //op. 5
            addPointToLists(this.map.getLocation(new Point(x, y+1)), relevantPoints, yetReachedPoints);

        if (y < this.map.getColumnsNumber() - 1 && x > 0)  //op. 6
            addPointToLists(this.map.getLocation(new Point(x-1, y+1)), relevantPoints, yetReachedPoints);

        if (x > 0)  // op. 7
            addPointToLists(this.map.getLocation(new Point(x-1, y)), relevantPoints, yetReachedPoints);

        if (y > 0 && x > 0) //op. 8
            addPointToLists(this.map.getLocation(new Point(x-1, y-1)), relevantPoints, yetReachedPoints);


        Random rand = new Random();
        Point ret;
        if (yetReachedPoints.size() > 0 ){ //if one or more of points didn't reach yet
            int randNum = rand.nextInt(yetReachedPoints.size());
            ret = this.map.getLocation(yetReachedPoints.get(randNum));

        } else { //if the agent was in the all points, so choose one randomly
            int randNum = rand.nextInt(relevantPoints.size());
            ret = this.map.getLocation(relevantPoints.get(randNum));
        }
        return ret;
    }

    private void addPointToLists(Point p, List<Point> relevantList, List<Point> yetReachedList) {
        int value = this.getPointState(p);
        if (value == Map.REGULAR)
            yetReachedList.add(p);
        if (value != Map.BORDER)
            relevantList.add(p);
    }

    private int getPointState(Point p) {
        notifyWithPlace(LoggerMessageMaker.checkPoint(p));
        return p.getValue();
    }

    @Override
    public void addObserver(Logger logger) {
        pcs.addPropertyChangeListener("Logger", logger);
    }

    private void notifyWithPlace(String str) {
        pcs.firePropertyChange("Logger",null,
                LoggerMessageMaker.notifyWithPlace(str,this.current));

    }
}
