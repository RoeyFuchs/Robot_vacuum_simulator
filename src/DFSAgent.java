import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class DFSAgent implements MyAgent {
    private ShortestPath shortestPath;
    private Map originalMap;
    private Queue<Point> steps;
    public DFSAgent(Map originalMap){
        this.originalMap=originalMap;
        shortestPath=new ShortestPath(this.originalMap);
        steps=new LinkedList<>();
        dfs();
    }

    private void dfs(){
        Map map= new Map(originalMap);
        int rows = map.getRowsNumber();
        int columns = map.getColumnsNumber();
        if(rows>0&&columns>0) {
            Stack<Point> stack = new Stack<>();
            stack.push(map.getAgentLocation());
            while (stack.empty() == false) {
                Point point = stack.pop();
                if (point.getX() < 0 || point.getY() < 0 || point.getX() > rows ||
                        point.getY() > columns || map.isBeenHere(point))
                    continue;
                Stack<Point> path = shortestPath.BFS(map.getAgentLocation(), point);
                while (!path.empty()) {
                    Point p = path.peek();
                    if (!(p.isSameLocation(map.getAgentLocation()))) {
                        map.agentMove(this, map.getAgentLocation(), p);
                        steps.add(p);
                    }
                    path.pop();
                }
                //north
                if (map.legalMove(new Point(point.getX() - 1, point.getY())))
                    stack.push(new Point(point.getX() - 1, point.getY()));
                //northEast
                if (map.legalMove(new Point(point.getX() - 1, point.getY() + 1))) {
                    stack.push(new Point(point.getX() - 1, point.getY() + 1));
                }
                //east
                if (map.legalMove(new Point(point.getX(), point.getY() + 1)))
                    stack.push(new Point(point.getX(), point.getY() + 1));
                //south east
                if (map.legalMove(new Point(point.getX() + 1, point.getY() + 1))) {
                    stack.push(new Point(point.getX() + 1, point.getY() + 1));
                }
                //south
                if (map.legalMove(new Point(point.getX() + 1, point.getY())))
                    stack.push(new Point(point.getX() + 1, point.getY()));
                //south west
                if (map.legalMove(new Point(point.getX() + 1, point.getY() - 1)))
                    stack.push(new Point(point.getX() + 1, point.getY() - 1));
                //west
                if (map.legalMove(new Point(point.getX(), point.getY() - 1)))
                    stack.push(new Point(point.getX(), point.getY() - 1));
                //north west
                if (map.legalMove(new Point(point.getX() - 1, point.getY() - 1)))
                    stack.push(new Point(point.getX() - 1, point.getY() - 1));

            }
        }
        System.out.println("Map after DFS scan:");
        System.out.println(originalMap.getMapAsString());
    }

    @Override
    public Point getLocation() {
        return originalMap.getAgentLocation();
    }

    @Override
    public void setLocation(Point p) {
        originalMap.agentMove(this,originalMap.getAgentLocation(),p);
    }

    @Override
    public Point doStep() {
        if (steps.size()>0){
            return steps.remove();
        }
        return null;
    }
}
