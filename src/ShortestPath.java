import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class ShortestPath {
    private Map map;
    public ShortestPath(Map map){
        this.map=map;
    }

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

    // These arrays are used to get row and column
// numbers of 4 neighbours of a given cell 
    static int rowNum[] = {-1, 0, 0, 1};
    static int colNum[] = {0, -1, 1, 0};

    private Stack<Point> GetPath(VisitedPoint[][] visitedPoints, Point src, Point dst){
        Stack<Point> path=new Stack<>();
        Point currentPoint=dst;
        path.push(dst);
        while (!(currentPoint.getX()==src.getX()&&currentPoint.getY()==src.getY())){
            path.push(visitedPoints[currentPoint.getX()][currentPoint.getY()].GetCameFrom());
            currentPoint=visitedPoints[currentPoint.getX()][currentPoint.getY()].GetCameFrom();
        }
        //path.push(src);
        return path;
    }

    public Stack<Point> BFS(Point src,
                   Point dest) {
        VisitedPoint[][] visited = new VisitedPoint[map.getRowsNumber()][map.getColumnsNumber()];
        for (int i=0;i<map.getRowsNumber();i++){
            for (int j=0;j<map.getRowsNumber();j++){
                visited[i][j]=new VisitedPoint();
            }
            }
        visited[src.getX()][src.getY()].SetIsVisited(true);
        Queue<Point> q = new LinkedList<>();
        q.add(src); // Enqueue source cell

        // BFS searching
        while (!q.isEmpty()) {
            Point pt = q.peek();

            if (pt.getX() == dest.getX() && pt.getY() == dest.getY())
                return GetPath(visited,src,dest);
            q.remove();
            for (int i = 0; i < 4; i++) {
                int row = pt.getX() + rowNum[i];
                int col = pt.getY() + colNum[i];


                if (isValid(row, col) &&map.legalMove(new Point(row,col)) &&!visited[row][col].GetIsVisited()) {
                    // mark cell as visited and enqueue it
                    visited[row][col].SetIsVisited(true);
                    Point Adjcell =(new Point(row, col));
                    visited[row][col].SetCameFrom(pt);
                    q.add(Adjcell);
                }
            }
        }
        return null;
    }

}