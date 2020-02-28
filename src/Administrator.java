import java.util.LinkedList;
import java.util.List;

public class Administrator {
    private Map map;
    private List<Agent> agent = new LinkedList<>();


    public Administrator(Map map, Agent agent) {
        this.map = map;
        agent.setLocation(map.getAgentLocation());
        this.agent.add(agent);
    }

/*
    public void doOneStep() {
        for (Agent a : this.agent) {
            Point newPoint = a.selectStep(this.map);
            if (this.map.legalMove(newPoint)) {
                Point oldPoint = a.getLocation();
                this.map.agentMove(a, oldPoint, newPoint);
                a.setLocation(newPoint);
            }
        }
    }*/

}
