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

    public void printMap() {
        for (int i = 0; i < this.matrix.length; ++i) {
            System.out.println();
            for (int j = 0; j < this.matrix[i].length; ++j)
                System.out.print(this.matrix[i][j].getValue() + " ");
        }
    }

    public Boolean legalMove(Integer i, Integer j) {
        return (i < this.matrix.length) && (j < this.matrix[i].length);
    }

    public void agentMove(Agent a,Integer i, Integer j) {
        this.matrix[a.getLocation().getX()][a.getLocation().getY()].setValue(BEEN_HERE);
        this.matrix[i][j].setValue(VACUUM);
        //to-do - set agent location, maybe with administrator
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
