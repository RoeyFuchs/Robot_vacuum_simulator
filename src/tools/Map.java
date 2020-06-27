package tools;

import agents.Agent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Map {
    private Point[][] matrix; //matrix of the map
    private long notReachYet = 0; //how many points didn't reach yet
    private PropertyChangeSupport pcs;

    public static final Integer REGULAR = 1; //point that didn't reach yes
    public static final Integer BEEN_HERE = 2; //point that already reached
    public static final Integer BORDER = 3; //point that agent cant step on, like the border of the map.
    public static final Integer AGENT = 9; //agent location

    public static HashMap<Integer, Boolean> hash_map = new HashMap<>(); //using to check if the the number (REGULAR/BEEN_HERE/BORDER/AGENT) is correct

    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("Map", l);
    }
    //create map from matrix
    private Map(Point[][] matrix) {
        pcs = new  PropertyChangeSupport(this);
        this.matrix = matrix;
        this.notReachYet = this.countNotReachYet();
    }

    //copy constructor
    public Map(Map map) {
        this.pcs=map.pcs;
        this.matrix = new Point[map.getRowsNumber()][map.getColumnsNumber()];
        for (int i = 0; i < map.getRowsNumber(); i++) {
            for(int j = 0; j < map.getColumnsNumber(); j++) {
                this.matrix[i][j] = new Point(map.getLocation(new Point(i, j)));
            }
        }
        this.notReachYet = map.getNotReachYet();
    }

    //file start with  height, width and  and then matrix of integer as defined above
    public static Map CreateMap(String fileName) throws IOException {
        File file = new File(fileName);
        String str;
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));
        str = br.readLine();
        List<String> size = Arrays.asList(str.trim().split(",")); //get height and width
        Integer height = Integer.parseInt(size.get(0));
        Integer width = Integer.parseInt(size.get(1));
        Point matrix[][] = new Point[height][width];
        for (int i = 0; i < height; ++i) {
            str = br.readLine();
            List<String> info = Arrays.asList(str.trim().split(" ")); //get map information
            for (int j = 0; j < width; ++j) {
                if (!Map.isValidNumber(Integer.parseInt(info.get(j)))) {
                    throw new IOException("Invalid number");
                }
                Point p = new Point(i, j);
                p.setValue(Integer.parseInt(info.get(j))); //set value
                matrix[i][j] = p; //add to matrix
            }
        }
        br.close();
        return new Map(matrix);
    }

    //get the entire map as string
    public String getMapAsString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.getRowsNumber(); i++) {
            s.append('\n');
            for (int j = 0; j < this.getColumnsNumber(); j++) {
                s.append(this.matrix[i][j].getValue());
                s.append('\t');
            }
        }
        return s.toString();
    }

    //get the entire map  but _with info_ and _not_ value
    public String getInfoMapAsString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.getRowsNumber(); i++) {
            s.append('\n');
            for (int j = 0; j < this.getColumnsNumber(); j++) {
                s.append(this.matrix[i][j].getInfo());
                s.append('\t');
            }
        }
        return s.toString();
    }

    public void printMap() {
        System.out.println(getMapAsString());
    }

    public void printMapInfo() {
        System.out.println(getInfoMapAsString());
    }

    //check if move in legal (check if there is no border)
    public Boolean legalMove(Point p) {
        return (!this.matrix[p.getX()][p.getY()].getValue().equals(BORDER));
    }


    //change the agent location
    public void agentMove(Agent a, Point oldPoint, Point newPoint) {
        this.matrix[oldPoint.getX()][oldPoint.getY()].setValue(BEEN_HERE);
        if (this.getLocation(newPoint).getValue().equals(Map.REGULAR)) {
            this.notReachYet--;
        }
        this.matrix[newPoint.getX()][newPoint.getY()].setValue(AGENT);
        this.updateVisit(newPoint);
        pcs.firePropertyChange("Map",null,this.getMapAsString());
    }

    //send the map to everyone
    public void announceBegin() {
        pcs.firePropertyChange("Map",null,this.getMapAsString());
    }

    //count how many point didn't reach yet
    private long countNotReachYet() {
        long counter = 0;
        for (Point[] points : this.matrix) {
            for (int j = 0; j < this.matrix[0].length; ++j) {
                if (points[j].getValue().equals(Map.REGULAR))
                    counter++;
            }
        }
        return counter;
    }

    //to check if a number is describe a correct value
    private static Boolean isValidNumber(Integer i) {
        Map.createValidationMap();
        return Map.hash_map.containsKey(i);
    }

    private static void createValidationMap() {
        if (!Map.hash_map.isEmpty()) {
            return;
        }
        Map.hash_map.put(Map.REGULAR, true);
        Map.hash_map.put(Map.AGENT, true);
        Map.hash_map.put(Map.BEEN_HERE, true);
        Map.hash_map.put(Map.BORDER, true);
    }

    public Point getAgentLocation() {
        for (int i = 0; i < this.matrix.length; ++i) {
            for (int j = 0; j < this.matrix[i].length; ++j)
                if (this.matrix[i][j].getValue().equals(AGENT))
                    return new Point(i, j);
        }
        throw new RuntimeException("Can't find Agent in map");
    }
    //get the original point from map
    public Point getLocation(Point point) {
        try {
            return matrix[point.getX()][point.getY()];
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    //update that agent was here
    public void updateVisit(Point point) {
        matrix[point.getX()][point.getY()].here();
    }

    public boolean isBeenHere(Point point) {
        if (matrix[point.getX()][point.getY()].getValue() == BEEN_HERE)
            return true;
        return false;
    }

    public int getRowsNumber() {
        return matrix.length;
    }

    public int getColumnsNumber() {
        return matrix[0].length;
    }

    public Point[][] getMatrix() {
        return this.matrix;
    }


    public long getNotReachYet() {
        return this.notReachYet;
    }
}
