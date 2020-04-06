import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class WavefrontAgent extends Observable implements MyAgent   {
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
        if (max == null) {
            ret = this.prev.peek();
        } else {
            ret = max;
        }
        return ret;
    }

    private Point compare(Point a, Point b) {
        if (a == null)
            return b;
        notifyWithPlace(LoggerMessageMaker.comparePoints(a,b));
        if (a.getInfo() >= b.getInfo())
            return a;
        return b;
    }

    private boolean canGo(Point p) {
        notifyWithPlace(LoggerMessageMaker.checkPoint(p));
        return this.map.getLocation(p).getValue() == Map.REGULAR;
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

            Point start, end;
            do {
                start = WavefrontAgent.this.map.getLocation(WavefrontAgent.this.map.getAgentLocation());
                end = WavefrontAgent.this.map.getLocation(new Point(rand.nextInt(maxRows), rand.nextInt(maxCulmns)));
            }
            while (start.isSameLocation(end) || start.getValue() == Map.BORDER || end.getValue() == Map.BORDER);

            WavefrontAgent.this.map.getLocation(end).setInfo(0);
            WavefrontAgent.this.current = start;
            this.setInfos(end);
        }

        public void setInfos(Point p) {
            Queue<Point> pointsList = new LinkedList<>();
            pointsList.add(p);
            while (!pointsList.isEmpty()) {
                p = pointsList.poll();
                int info = p.getInfo() + 1;
                int x = p.getX();
                int y = p.getY();

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
            WavefrontAgent.this.map.printMapInfo();
        }

        private void checkAndAddPoint(Point p, int info, Queue<Point> list) {
            if (p.getInfo() == Point.NO_INFO && p.getValue() != Map.BORDER)
                list.add(p.setInfo(info));
        }
    }


    public void setLocation(Point p) {
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
