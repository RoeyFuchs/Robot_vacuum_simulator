import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class WavefrontAgent {

    private Map map;


    public WavefrontAgent(Map map, Object logger) { // dont forget to change the logger type
        this.map = new Map(map);
        this.preProcess();
    }

    public void preProcess() {
        Random rand = new Random();
        int maxRows = this.map.getRowsNumber();
        int maxCulmns = this.map.getColumnsNumber();

        Point start, end;
        do {
            start = this.map.getLocation(new Point(rand.nextInt(maxRows), rand.nextInt(maxCulmns)));
            end = this.map.getLocation(new Point(rand.nextInt(maxRows), rand.nextInt(maxCulmns)));
        } while(start.isSameLocation(end));

        this.map.getLocation(end).setInfo(0);
        this.setInfos(end);


    }

    public void setInfos(Point p){
        Queue<Point> pointsList = new LinkedList<>();
        pointsList.add(p);
        while(!pointsList.isEmpty()) {
            p = pointsList.poll();
            int info = p.getInfo()+1;
            int x = p.getX();
            int y = p.getY();

            if (y > 0) {
                this.checkAndAddPoint(this.map.getLocation(new Point(x, y - 1)), info, pointsList);
                if (x > 0)
                    this.checkAndAddPoint(this.map.getLocation(new Point(x - 1, y - 1)),info, pointsList);
                if (x < this.map.getRowsNumber()-1)
                    this.checkAndAddPoint(this.map.getLocation(new Point(x + 1, y - 1)), info, pointsList);
            }
            if (y < this.map.getColumnsNumber()-1) {
                this.checkAndAddPoint(this.map.getLocation(new Point(x, y + 1)), info, pointsList);
                if (x > 0)
                    this.checkAndAddPoint(this.map.getLocation(new Point(x-1, y + 1)), info, pointsList);
                if (x < this.map.getRowsNumber()-1)
                    this.checkAndAddPoint(this.map.getLocation(new Point(x+1, y +1)), info, pointsList);
            }
            if (x > 0)
                this.checkAndAddPoint(this.map.getLocation(new Point(x-1, y)), info, pointsList);
            if (x < this.map.getRowsNumber()-1)
                this.checkAndAddPoint(this.map.getLocation(new Point(x+1, y)), info, pointsList);
        }
        this.map.printMapInfo();
    }

    private void checkAndAddPoint(Point p, int info, Queue<Point> list) {
        if(p.getInfo() == Point.NO_INFO)
            list.add(p.setInfo(info));
    }

}
