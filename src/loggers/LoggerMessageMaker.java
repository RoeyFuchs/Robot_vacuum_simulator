package loggers;

import tools.Point;

public class LoggerMessageMaker {
    public static String CHECK = "Check ";
    public static String COMPARE = "Compare ";

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
