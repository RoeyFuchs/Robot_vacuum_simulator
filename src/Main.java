import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Good luck");
        Map map = null;
        try {
            map = Map.CreateMap("map.txt");
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace());
        }
            Agent agent = new DummyAgent();
            agent.setLocation(map.getAgentLocation());


    }
}
