package agents;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.Stack;

import loggers.LoggerMessageMaker;
import tools.Map;
import tools.Point;
//get shortest path from source to target based on BFS scan
class ShortestPath {
    private PropertyChangeSupport pcs;
    private Map map;
    private final int NUM_OF_NEIGBORS=8;
    public ShortestPath(Map map){
        this.map=map;
        pcs = new  PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener l){
        this.pcs.addPropertyChangeListener("",l);
    }

    //check if the location is in the map
     private boolean isValid(int row, int col) {
        return (row >= 0) && (row < map.getRowsNumber()) &&
                (col >= 0) && (col < map.getColumnsNumber());
    }
    class VisitedPoint {
        private boolean isVisited;
        private Point cameFrom;

        public VisitedPoint(){
            this.isVisited=false;
            this.cameFrom=null;
        }

        public boolean GetIsVisited(){
            return isVisited;
        }
        public void SetIsVisited(Boolean isVisited){
            this.isVisited=isVisited;
        }
        public Point GetCameFrom(){
            return this.cameFrom;
        }
        public void SetCameFrom(Point point){
            this.cameFrom=point;
        }
    }
    //notify observers: new point has been checked
    private void checkPoint(Point p, Point agentLoc) {
        notifyWithPlace(LoggerMessageMaker.checkPoint(p),agentLoc);
    }
    private void notifyWithPlace(String str, Point p) {
        pcs.firePropertyChange("Logger",null,LoggerMessageMaker.notifyWithPlace(str,p));
    }

    //row and columns of 8 neighbors
    static int rowNum[] = {-1, -1, 0, 1,1,1,0,-1};
    static int colNum[] = {0, 1, 1, 1,0,-1,-1,-1};

    //reconstruct map: from destination to source
    private Stack<Point> GetPath(VisitedPoint[][] visitedPoints, Point src, Point dst){
        Stack<Point> path=new Stack<>();
        Point currentPoint=dst;
        path.push(dst);
        while (!(currentPoint.isSameLocation(src))){
            path.add(0,visitedPoints[currentPoint.getX()][currentPoint.getY()].GetCameFrom());
            currentPoint=visitedPoints[currentPoint.getX()][currentPoint.getY()].GetCameFrom();
        }
        //path.push(src);
        return path;
    }

    public Stack<Point> BFS(Point src,
                            Point dest) {
        VisitedPoint[][] visited = new VisitedPoint[map.getRowsNumber()][map.getColumnsNumber()];
        for (int i=0;i<map.getRowsNumber();i++){
            for (int j=0;j<map.getColumnsNumber();j++){
                visited[i][j]=new VisitedPoint();
            }
            }
        visited[src.getX()][src.getY()].SetIsVisited(true);
        Queue<Point> q = new LinkedList<>();
        q.add(src);

        // BFS searching
        while (!q.isEmpty()) {
            Point pt = q.peek();

            if (pt.isSameLocation(dest))
                return GetPath(visited,src,dest);
            q.remove();
            for (int i = 0; i < NUM_OF_NEIGBORS; i++) {
                int row = pt.getX() + rowNum[i];
                int col = pt.getY() + colNum[i];

                checkPoint(new Point(row,col), src);
                if (isValid(row, col) &&map.legalMove(new Point(row,col)) &&!visited[row][col].GetIsVisited()) {
                    // mark cell as visited and enqueue it
                    visited[row][col].SetIsVisited(true);
                    Point adjcell =(new Point(row, col));
                    visited[row][col].SetCameFrom(pt);
                    q.add(adjcell);
                }
            }
        }
        return null;
    }

}