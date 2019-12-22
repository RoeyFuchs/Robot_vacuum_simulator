import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Map {
    private Point[][] matrix;

    public static Integer REGULAR = 1;
    public static Integer BEEN_HERE = 2;
    public static Integer BORDER = 3;
    public static Integer VACUUM = 9;

    public static HashMap<Integer, Boolean> hash_map = new HashMap<>();


    private Map(Point[][] matrix) {
        this.matrix = matrix;
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
        Point matrix[][] = new Point[width][height];
        for (int i = 0; i < height; ++i) {
            str = br.readLine();
            List<String> info = Arrays.asList(str.trim().split(" "));
            for (int j = 0; j < width; ++j) {
                if (!Map.isValidNumber(Integer.parseInt(info.get(j)))) {
                    throw new IOException("Invalid number");
                }
                Point p = new Point(j, i);
                p.setValue(Integer.parseInt(info.get(j)));
                matrix[j][i] = p;
            }
        }
        br.close();
        return new Map(matrix);
    }

    public void printMap() {
        for (int i =0; i < this.matrix[0].length; i++) {
            System.out.println();
            for(int j = 0 ; j<this.matrix.length; j++){
                System.out.print(this.matrix[j][i].getValue()+ " ");
            }
        }
    }

    public Boolean legalMove(Point p) {
        return (this.matrix[p.getX()][ p.getY()].getValue() != BORDER);
    }

    public void agentMove(Agent a,Point oldPoint, Point newPoint) {
        this.matrix[oldPoint.getX()][oldPoint.getY()].setValue(BEEN_HERE);
        this.matrix[newPoint.getX()][newPoint.getY()].setValue(VACUUM);
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



}
