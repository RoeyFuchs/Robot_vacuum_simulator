package tools;

import agents.Agent;

import java.util.LinkedList;
import java.util.List;

public class Administrator {
    private Map map;
    private List<Agent> agent = new LinkedList<>(); //can handle more than one agent


    public Administrator(Map map, Agent agent) {
        this.map = map;
        agent.setLocation(map.getAgentLocation());
        this.agent.add(agent);
    }

    public void doOneStep() {
        for (Agent a : this.agent) {
            Point newPoint = a.doStep();
            if (newPoint!= null &&this.map.legalMove(newPoint)) { //check if that what the agent wants to do is legal
                Point oldPoint = a.getLocation();
                this.map.agentMove(a, oldPoint, newPoint);
                a.setLocation(newPoint);
            }
        }
    }

}
