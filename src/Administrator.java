import java.util.LinkedList;
import java.util.List;

public class Administrator {
    private Map map;
    private List<MyAgent> agent = new LinkedList<>();


    public Administrator(Map map, MyAgent agent) {
        this.map = map;
        agent.setLocation(map.getAgentLocation());
        this.agent.add(agent);
    }

    public void doOneStep() {
        for (MyAgent a : this.agent) {
            Point newPoint = a.doStep();
            if (newPoint!= null &&this.map.legalMove(newPoint)) {
                Point oldPoint = a.getLocation();
                this.map.agentMove(a, oldPoint, newPoint);
                a.setLocation(newPoint);
            }
        }
    }

}
