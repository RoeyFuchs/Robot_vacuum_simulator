import agents.WavefrontAgent;
import loggers.Logger;
import tools.Administrator;
import tools.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("Good luck");
        Map map = null;
        try {
            map = Map.CreateMap("map.txt");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace());
        }
        WavefrontAgent agent = new WavefrontAgent(map);
        Logger logger = new Logger("test2.xml");
        agent.addObserver(logger);
        Administrator admin = new Administrator(map, agent);
        int i=1;

        while(map.getNotReachYet() != 0 ){
            admin.doOneStep();
            System.out.println(i);
            map.printMap();
            i++;
        }
        logger.save();


    }
}
