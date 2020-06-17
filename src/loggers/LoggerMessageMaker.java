package loggers;

import tools.Point;

//this class using for making same xml
public class LoggerMessageMaker {
    public static final String CHECK = "Check ";
    public static final String COMPARE = "Compare ";

    public static String checkPoint(Point p){
        return ("Check (" + p.getX()+"," + p.getY() + ")");
    }

    public static String comparePoints(Point a, Point b) {
        return ("Compare (" + a.getX()+"," + a.getY() + ") & ("+b.getX()+"," + b.getY()+")");
    }
    public static String notifyWithPlace(String str, Point p) {
        String cord = "agents.Agent at ("+ p.getX() +","
                + p.getY() +") ";
        return (cord +str+"\n");
    }
}
