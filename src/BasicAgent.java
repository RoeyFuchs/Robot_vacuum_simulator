import java.util.LinkedList;
import java.util.List;

abstract public class BasicAgent implements Agent {
    protected Map map;

    public BasicAgent(Map map){
        this.map=map;
    }

    public Point getLocation(){
        return map.getAgentLocation();
    }

    public void setLocation(Point p){
        map.agentMove(this,map.getAgentLocation(),p);
    }
    public void calculateShortestPath(Point s, Point t){

    }



  /* private List<Point> getNeighborsLocations(){
       Point agentLocation=map.getAgentLocation();

       List<Point> neighborsLocations=new LinkedList<>();
       //northWest
       neighborsLocations.add(new Point(agentLocation.getX()-1,agentLocation.getY()-1));
       //north
       neighborsLocations.add(new Point(agentLocation.getX(),agentLocation.getY()-1));
       //northEast
       neighborsLocations.add(new Point(agentLocation.getX()+1,agentLocation.getY()-1));
       //west
       neighborsLocations.add(new Point(agentLocation.getX()-1,agentLocation.getY()));
       //east
       neighborsLocations.add(new Point(agentLocation.getX(),agentLocation.getY()+1));
       //south west
       neighborsLocations.add(new Point(agentLocation.getX()-1,agentLocation.getY()+1));
       //south
       neighborsLocations.add(new Point(agentLocation.getX(),agentLocation.getY()+1));
       //south east
       neighborsLocations.add(new Point(agentLocation.getX()+1,agentLocation.getY()+1));

       return neighborsLocations;
   }
    public List<Point> getLegalNeighbors(){
        List<Point> neighbors=new LinkedList<>();
        List<Point> neighborsLocations=this.getNeighborsLocations();
        for (Point neighbor:neighborsLocations) {
           if(map.legalMove(neighbor)){
               neighbors.add(map.getLocation(neighbor));
           }
        }
        return neighbors;
    }*/
    public void updateVisit(Point point){
        map.updateVisit(point);
    }

}
