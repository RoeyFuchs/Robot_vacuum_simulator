package agents;

import loggers.Logger;
import loggers.LoggerMessageMaker;
import tools.Map;
import tools.Point;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

//base on http://pinkwink.kr/attachment/cfile3.uf@1354654A4E8945BD13FE77.pdf and https://www.sciencedirect.com/science/article/pii/S092188901300167X
public class WavefrontAgent extends Observable implements Agent {
    private Map map;
    private Point current;
    private Stack<Point> prev = new Stack<>();


    public WavefrontAgent(Map map) {
        this.map = new Map(map);
        new WaveFrontPreProcess().preProcess();
    }

    public Point doStep() {
        int y = current.getY();
        int x = current.getX();
        Point max = null;

        //check all the options, clockwise, from north.
        //will find the point with max info, as set in pre process
        if (y > 0)//op. 1
            if (canGo(new Point(x, y - 1)))
                max = compare(max, this.map.getLocation(new Point(x, y - 1)));

        if (y > 0 && x < this.map.getRowsNumber() - 1)  //op. 2
            if (canGo(new Point(x + 1, y - 1)))
                max = compare(max, this.map.getLocation(new Point(x + 1, y - 1)));


        if (x < this.map.getRowsNumber() - 1) //op. 3
            if (canGo(new Point(x + 1, y)))
                max = compare(max, this.map.getLocation(new Point(x + 1, y)));

        if (y < this.map.getColumnsNumber() - 1 && x < this.map.getRowsNumber() - 1)  // op. 4
            if (canGo(new Point(x + 1, y + 1)))
                max = compare(max, this.map.getLocation(new Point(x + 1, y + 1)));


        if (y < this.map.getColumnsNumber() - 1)  //op. 5
            if (canGo(new Point(x, y + 1)))
                max = compare(max, this.map.getLocation(new Point(x, y + 1)));


        if (y < this.map.getColumnsNumber() - 1 && x > 0)  //op. 6
            if (canGo(new Point(x - 1, y + 1)))
                max = compare(max, this.map.getLocation(new Point(x - 1, y + 1)));


        if (x > 0)  // op. 7
            if (canGo(new Point(x - 1, y)))
                max = compare(max, this.map.getLocation(new Point(x - 1, y)));

        if (y > 0 && x > 0) //op. 8
            if (canGo(new Point(x - 1, y - 1)))
                max = compare(max, this.map.getLocation(new Point(x - 1, y - 1)));

        Point ret;
        if (max == null) { //if there no where to go, go back
            ret = this.prev.peek();
        } else {
            ret = max;
        }
        return ret;
    }

    @Override
    public void addObserver(Logger logger) {
        super.addObserver(logger);
    }

    //compare by info
    private Point compare(Point a, Point b) {
        if (a == null)
            return b;
        notifyWithPlace(LoggerMessageMaker.comparePoints(a,b));
        if (a.getInfo() >= b.getInfo())
            return a;
        return b;
    }

    //check if is possible to go to point (if the point didn't reach yet)
    private boolean canGo(Point p) {
        notifyWithPlace(LoggerMessageMaker.checkPoint(p));
        return this.map.getLocation(p).getValue().equals(Map.REGULAR);
    }

    private void notifyWithPlace(String str) {
        super.setChanged();
        super.notifyObservers(LoggerMessageMaker.notifyWithPlace(str,this.current));
    }


    private class WaveFrontPreProcess {

        public void preProcess() {
            Random rand = new Random();
            int maxRows = WavefrontAgent.this.map.getRowsNumber();
            int maxCulmns = WavefrontAgent.this.map.getColumnsNumber();

            Point start, end; //find random place to start and finish
            do {
                start = WavefrontAgent.this.map.getLocation(WavefrontAgent.this.map.getAgentLocation());
                end = WavefrontAgent.this.map.getLocation(new Point(rand.nextInt(maxRows), rand.nextInt(maxCulmns)));
            } while (start.isSameLocation(end) || start.getValue().equals(Map.BORDER) || end.getValue().equals(Map.BORDER));

            WavefrontAgent.this.map.getLocation(end).setInfo(0);
            WavefrontAgent.this.current = start;
            this.setInfos(end);
        }
        //set info values for points. Point p is the first point to go from (the end point)
        public void setInfos(Point p) {
            Queue<Point> pointsList = new LinkedList<>();
            pointsList.add(p);
            while (!pointsList.isEmpty()) {
                p = pointsList.poll();
                int info = p.getInfo() + 1; //wavefront, adding 1 every step
                int x = p.getX();
                int y = p.getY();
                //adding all points
                //if's for don't get out the map
                if (y > 0) {
                    this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x, y - 1)), info, pointsList);
                    if (x > 0)
                        this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x - 1, y - 1)), info, pointsList);
                    if (x < WavefrontAgent.this.map.getRowsNumber() - 1)
                        this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x + 1, y - 1)), info, pointsList);
                }
                if (y < WavefrontAgent.this.map.getColumnsNumber() - 1) {
                    this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x, y + 1)), info, pointsList);
                    if (x > 0)
                        this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x - 1, y + 1)), info, pointsList);
                    if (x < WavefrontAgent.this.map.getRowsNumber() - 1)
                        this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x + 1, y + 1)), info, pointsList);
                }
                if (x > 0)
                    this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x - 1, y)), info, pointsList);
                if (x < WavefrontAgent.this.map.getRowsNumber() - 1)
                    this.checkAndAddPoint(WavefrontAgent.this.map.getLocation(new Point(x + 1, y)), info, pointsList);
            }
        }

        private void checkAndAddPoint(Point p, int info, Queue<Point> list) {
            if (p.getInfo().equals(Point.NO_INFO) && !p.getValue().equals(Map.BORDER))
                list.add(p.setInfo(info));
        }
    }


    //new location for agent
    public void setLocation(Point p) {
        //if the previus location and the current location are same, we need to go back.
        if (!this.prev.empty() && p.isSameLocation(this.prev.peek())) {
            this.prev.pop();
        } else {
            this.prev.push(current);
        }
        this.current.setValue(Map.BEEN_HERE);
        this.current = this.map.getLocation(p);
    }

    public Point getLocation() {
        return this.current;
    }

}
