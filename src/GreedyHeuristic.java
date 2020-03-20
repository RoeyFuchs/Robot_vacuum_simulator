import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

 /*
public class GreedyHeuristic extends BasicAgent {
    private ShortestPath shortestPath;

    GreedyHeuristic(Map map){
        super(map);
        shortestPath=new ShortestPath(map);
    }
    private boolean checkRadiusWithinTheRoom(Point currentLoc,int radius){
        if((currentLoc.getX()+radius<map.getRowsNumber())||
                (currentLoc.getX()-radius>=0)||
                (currentLoc.getY()+radius<map.getColumnsNumber())||
                (currentLoc.getY()-radius>=0))
            return true;
        return false;
    }
    private List<Point>getLoctionsPerRadius(int radius){
        int surround[]={-radius,radius};
        List<Point> environment=new LinkedList<>();
        Point agentLoc=super.getLocation();
        //scan verical surround
        for (int i=0; i<surround.length;i++){
            for (int j=agentLoc.getY()-radius;
            j<=agentLoc.getY()+radius;j++){
                environment.add(new Point(agentLoc.getX()+surround[i],j));
            }
        }
        //scan vertical surround
        for (int j=0; j<surround.length;j++){
            for (int i=agentLoc.getX()-radius;
                 i<=agentLoc.getX()+radius;i++){
                environment.add(new Point(i,agentLoc.getY()+surround[j]));
            }
        }
        for (Point p:environment) {
            System.out.println("x: "+p.getX()+" y: "+p.getY());
        }
        return environment;
    }
    private List<Point> changePriorityOfEnvironment(List<Point>environment){
        return null;
    }
    private List<Point> getRelevantSurroundingPoints(List<Point>environment){
        List<Point> relevantPoints=new LinkedList<>();
        for (Point p:environment) {
            if(super.legalMove(p)&&!super.isBeenHere(p)){
                relevantPoints.add(p);
            }
        }
        return relevantPoints;
    }
    @Override
    public List<String> calculateSteps() {
        int radius=0;
        Point currentLoc=super.map.getLocation();
        while (checkRadiusWithinTheRoom(currentLoc,radius)){
            List<Point> surroundingPoints=getRelevantSurroundingPoints(getLoctionsPerRadius(radius));
            if (surroundingPoints.size()==0){
                radius++;
            }else {
                Stack<Point> path=shortestPath.BFS(super.map.getLocation(),point);
                while (!path.empty()){
                    Point p=path.peek();
                    if(!(p.isSameLocation(super.map.getLocation()))) {
                        super.agentMove( super.map.getLocation(), p);
                        mapSteps.add(map.getMapAsString());
                    }
                    path.pop();
                }
            }
        }

        return null;
    }
}
*/