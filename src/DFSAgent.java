import java.util.*;

public class DFSAgent extends Observable implements MyAgent,Observer {
    private ShortestPath shortestPath;
    private Map originalMap;
    private Queue<Point> steps;
    private boolean preProcess;
    public DFSAgent(Map originalMap){
        this.originalMap=originalMap;
        shortestPath=new ShortestPath(this.originalMap);
        steps=new LinkedList<>();
        preProcess=false;
        shortestPath.addObserver(this);
    }

    private void checkPoint(Point p,Point agentLoc) {
        notifyWithPlace(LoggerMessageMaker.checkPoint(p),agentLoc);
    }
    private void notifyWithPlace(String str,Point p) {
        super.setChanged();
        super.notifyObservers(LoggerMessageMaker.notifyWithPlace(str,p));
    }

    private void dfs(){
        //Scans from north clockwise
        Map map= new Map(originalMap);
        int rows = map.getRowsNumber();
        int columns = map.getColumnsNumber();
        if(rows>0&&columns>0) {
            Stack<Point> stack = new Stack<>();
            stack.push(map.getAgentLocation());
            while (stack.empty() == false) {
                Point point = stack.pop();
                checkPoint(point,map.getAgentLocation());
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
        if(!preProcess){
            preProcess=true;
            dfs();
        }
        if (steps.size()>0){
            return steps.remove();
        }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }
}
