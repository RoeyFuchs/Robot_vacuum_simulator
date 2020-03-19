
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Map extends Observable {
    private Point[][] matrix;


    private long notReachYet = 0;

    public static Integer REGULAR = 1;
    public static Integer BEEN_HERE = 2;
    public static Integer BORDER = 3;
    public static Integer VACUUM = 9;

    public static HashMap<Integer, Boolean> hash_map = new HashMap<>();


    private Map(Point[][] matrix) {
        this.matrix = matrix;
        this.notReachYet = this.countNotReachYet();
    }

    public Map(Map map) {
        this.matrix = map.getMatrix().clone();
    }

    //file start with  height, width and  and then matrix of integer as defined above
    public static Map CreateMap(String fileName) throws IOException {
        File file = new File(fileName);
        String str;
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));
        str = br.readLine();
        List<String> size = Arrays.asList(str.trim().split(","));
        Integer height = Integer.parseInt(size.get(0));
        Integer width = Integer.parseInt(size.get(1));
        Point matrix[][] = new Point[height][width];
        for (int i = 0; i < height; ++i) {
            str = br.readLine();
            List<String> info = Arrays.asList(str.trim().split(" "));
            for (int j = 0; j < width; ++j) {
                if (!Map.isValidNumber(Integer.parseInt(info.get(j)))) {
                    throw new IOException("Invalid number");
                }
                Point p = new Point(i, j);
                p.setValue(Integer.parseInt(info.get(j)));
                matrix[i][j] = p;
            }
        }
        br.close();
        return new Map(matrix);
    }

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


    public Boolean legalMove(Point p) {
        return (this.matrix[p.getX()][p.getY()].getValue() != BORDER);
    }

    public void agentMove(MyAgent a, Point oldPoint, Point newPoint) {
        this.matrix[oldPoint.getX()][oldPoint.getY()].setValue(BEEN_HERE);
        if (this.getLocation(newPoint).getValue() == Map.REGULAR) {
            this.notReachYet--;
        }
        this.matrix[newPoint.getX()][newPoint.getY()].setValue(VACUUM);
        this.notifyObservers();
    }

    private long countNotReachYet() {
        long counter = 0;
        for (int i = 0; i < this.matrix.length; ++i) {
            for (int j =0; j < this.matrix[0].length; ++j) {
                if (this.matrix[i][j].getValue() == Map.REGULAR)
                    counter++;
            }
        }
        return counter;
    }
    private static Boolean isValidNumber(Integer i) {
        Map.createValidationMap();
        return Map.hash_map.containsKey(i);
    }

    private static void createValidationMap() {
        if (!Map.hash_map.isEmpty()) {
            return;
        }
        Map.hash_map.put(Map.REGULAR, true);
        Map.hash_map.put(Map.VACUUM, true);
        Map.hash_map.put(Map.BEEN_HERE, true);
        Map.hash_map.put(Map.BORDER, true);
    }

    public Point getAgentLocation() {
        for (int i = 0; i < this.matrix.length; ++i) {
            for (int j = 0; j < this.matrix[i].length; ++j)
                if (this.matrix[i][j].getValue() == VACUUM)
                    return new Point(i, j);
        }
        return new Point(-1, -1);
    }

    public Point getLocation(Point point) {
        try {
            return matrix[point.getX()][point.getY()];
        } catch (Exception E) {
            System.out.println("@!#");
        }
        return null;
    }

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

    @Override
    public void notifyObservers() {
        super.notifyObservers(this.getMapAsString());
    }

    public long getNotReachYet() {
        return this.notReachYet;
    }
}
