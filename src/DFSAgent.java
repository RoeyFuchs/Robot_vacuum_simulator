import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class DFSAgent extends BasicAgent  {

    private ShortestPath shortestPath;
    public DFSAgent(Map map){

        super(map);
    }

    @Override
    public int checkValueOfPoint(Point p) {
        return 0;
    }

    public List<String> calculateSteps(){
        List<String> mapSteps= new LinkedList<>();
        shortestPath=new ShortestPath(map);
        return dfs(mapSteps);
    }

    private List<String> dfs(List<String> mapSteps){
        int rows=super.map.getRowsNumber();
        if(rows==0)
            return null;
        int columns=super.map.getColumnsNumber();
        Stack<Point> stack=new Stack<>();
        stack.push(super.map.getAgentLocation());
        while (stack.empty()==false){
            Point point=stack.pop();
            if(point.getX()<0||point.getY()<0||point.getX()>rows||point.getY()>columns||super.isBeenHere(point))
                continue;
            Stack<Point>path=shortestPath.BFS(super.map.getAgentLocation(),point);
            while (!path.empty()){
                Point p=path.peek();
                if(!(p.isSameLocation(super.map.getAgentLocation()))) {
                    map.agentMove(this, super.map.getAgentLocation(), p);
                    mapSteps.add(map.getMapAsString());
                }
                path.pop();
            }
            //go left
            if(super.legalMove(new Point(point.getX(),point.getY()-1)))
                stack.push(new Point(point.getX(),point.getY()-1));
            //go right
            if(super.legalMove(new Point(point.getX(),point.getY()+1)))
                stack.push(new Point(point.getX(),point.getY()+1));
            //go up
            if(super.legalMove(new Point(point.getX()-1,point.getY())))
                stack.push(new Point(point.getX()-1,point.getY()));
            //go down
            if(super.legalMove(new Point(point.getX()+1,point.getY())))
                stack.push(new Point(point.getX()+1,point.getY()));
        }
        return mapSteps;
    }
}
