import java.util.LinkedList;
import java.util.List;

public class Report {
    private Point agentLocation;
    private List<Pair<Point,Point>> pointsCompared;
    private List<Point> pointsChecked;

    public Report(Point agentLocation){
        this.agentLocation=agentLocation;
        pointsChecked=new LinkedList<>();
        pointsCompared= new LinkedList<>();
    }
    public void AddPointCompared(Pair<Point,Point> p){
        this.pointsCompared.add(p);
    }

    public void AddPointChecked(Point p){
        this.pointsChecked.add(p);
    }
    public List<Point> GetPointsChecked(){
        return this.pointsChecked;
    }
    public List<Pair<Point,Point>> GetPointsCompared(){
        return this.pointsCompared;
    }
    public Point getAgentLocation(){return this.agentLocation;}
}
