package agents;

import java.util.*;

import loggers.LoggerMessageMaker;
import tools.Map;
import tools.Point;


public class GreedyHeuristic extends Observable implements Agent,Observer {
    private ShortestPath shortestPath;
    private Map originalMap;
    private Queue<Point> steps;
    private boolean preProcessed;

    GreedyHeuristic(Map map){
        this.originalMap=map;
        shortestPath=new ShortestPath(map);
        steps=new LinkedList<>();
        preProcessed=false;
        shortestPath.addObserver(this);
    }
    private boolean checkNorthSurround(Point currentLoc, int radius){
        if((currentLoc.getX()-radius>=0))
            return true;
        return false;
    }
    private boolean checkSouthSurround(Point currentLoc, int radius, Map map){
        if((currentLoc.getX()+radius<map.getRowsNumber()))
            return true;
        return false;
    }
    private boolean checkEastSurround(Point currentLoc, int radius, Map map){
        if((currentLoc.getY()+radius<map.getColumnsNumber()))
            return true;
        return false;
    }
    private boolean checkWestSurround(Point currentLoc, int radius){
        if((currentLoc.getY()-radius>=0))
            return true;
        return false;
    }
    private void scanEastSurround(int radius, LinkedList<Point>surrounding, Map map){
        Point agentLoc=map.getAgentLocation();
        for (int j = agentLoc.getX() - radius+1;
             j < agentLoc.getX() + radius; j++) {
            surrounding.add(new Point(j,agentLoc.getY() +radius));
        }
    }
    private void scanWestSurround(int radius, LinkedList<Point>surrounding, Map map){
        Point agentLoc=map.getAgentLocation();
        for (int j = agentLoc.getX() + radius-1;
             j > agentLoc.getX() - radius; j--) {
            surrounding.add(new Point(j,agentLoc.getY() -radius));
        }
    }
    private void scanNorthSurrond(int radius, LinkedList<Point>surrounding, Map map){
        Point agentLoc=map.getAgentLocation();
        for (int i = agentLoc.getY() - radius;
             i <= agentLoc.getY() + radius; i++) {
            surrounding.add(new Point(agentLoc.getX() -radius,i));
        }
    }
    private void scanSouthSurround(int radius, LinkedList<Point>surrounding, Map map){
        Point agentLoc=map.getAgentLocation();
        for (int i =agentLoc.getY() + radius;
             i >=agentLoc.getY() - radius; i--) {
            surrounding.add(new Point(agentLoc.getX() +radius,i));
        }
    }
    private boolean isLegalPoint(Point p, Map map){
        if((p.getX()>=0&&p.getX()<map.getRowsNumber())&&
        p.getY()>=0&&p.getY()<map.getColumnsNumber()){
            return true;
        }
        return false;
    }
    private LinkedList<Point>getSurrounding(int radius, Point agentLoc, Map map){
        //scan surrounding from upper right corner according to clockwise
        LinkedList<Point> surrounding=new LinkedList<>();
        if(checkNorthSurround(agentLoc,radius))
            scanNorthSurrond(radius,surrounding,map);
        if(checkEastSurround(agentLoc,radius,map))
            scanEastSurround(radius,surrounding,map);
        if(checkSouthSurround(agentLoc,radius,map))
            scanSouthSurround(radius,surrounding,map);
        if(checkWestSurround(agentLoc,radius))
            scanWestSurround(radius,surrounding,map);
        /*for (tools.Point p:environment) {
            System.out.println("x: "+p.getX()+" y: "+p.getY());
        }*/
        return surrounding;
    }

    private LinkedList<Point> getRelevantSurroundingPoints(List<Point>environment, Map map, Point agentLoc){
        LinkedList<Point> relevantPoints=new LinkedList<>();
        for (Point p:environment) {
            checkPoint(p,agentLoc);
            if(isLegalPoint(p,map)&&map.legalMove(p)&&!map.isBeenHere(p)){
                relevantPoints.add(p);
            }
        }
        return relevantPoints;
    }
    private boolean checkSurrounding(Point currentLoc, int radius, Map map){
        if(checkEastSurround(currentLoc,radius,map)||checkNorthSurround(currentLoc,radius)
                ||checkSouthSurround(currentLoc,radius,map)||checkWestSurround(currentLoc,radius)){
            return true;
        }
        return false;
    }
    private void checkPoint(Point p, Point agentLoc) {
        notifyWithPlace(LoggerMessageMaker.checkPoint(p),agentLoc);
    }
    private void notifyWithPlace(String str, Point p) {
        super.setChanged();
        super.notifyObservers(LoggerMessageMaker.notifyWithPlace(str,p));
    }

    private void calculateSteps() {
        Map map=new Map(originalMap);
        int radius=1;
        Point currentLoc=map.getAgentLocation();
        while (checkSurrounding(currentLoc,radius,map)){
            Queue<Point> surroundingPoints=getRelevantSurroundingPoints(getSurrounding(radius,currentLoc,map),map,currentLoc);
            if (surroundingPoints.size()==0){
                radius++;
            }else {
                Point point=surroundingPoints.remove();
                Stack<Point> path=shortestPath.BFS(currentLoc,point);
                while (!path.empty()){
                    Point p=path.peek();
                    if(!(p.isSameLocation(currentLoc))) {
                        map.agentMove(this,currentLoc, p);
                        currentLoc=p;
                        steps.add(currentLoc);
                    }
                    path.pop();
                }
                radius=1;
            }
        }
    }

    @Override
    public Point getLocation() {
        return this.originalMap.getAgentLocation();
    }

    @Override
    public void setLocation(Point p) {
        this.originalMap.agentMove(this,getLocation(),p);
    }

    @Override
    public Point doStep() {
        if(!preProcessed){
            preProcessed=true;
            calculateSteps();
        }
        if(steps.size()>0){
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
