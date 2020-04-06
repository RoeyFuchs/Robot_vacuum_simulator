public class LoggerMessageMaker {
    public static String checkPoint(Point p){
        return ("Check (" + p.getX()+"," + p.getY() + ")");
    }
    public static String notifyWithPlace(String str,Point p) {
        String cord = "Agent at ("+ p.getX() +","
                + p.getY() +") ";
        return (cord +str+"\n");
    }
}
