import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Map {
    private Integer[][] matrix;

    public static Integer VERY_TRASHY = 5;
    public static Integer TRASHY = 4;
    public static Integer LOW_TRASHY = 3;
    public static Integer FAILER = 2;
    public static Integer CLEAN = 1;
    public static Integer VACUUM = 9;

    public static HashMap<Integer, Boolean> hash_map = new HashMap<>();


    private Map(Integer[][] matrix) {
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
        Integer matrix[][] = new Integer[height][width];
        for (int i = 0; i < height; ++i) {
            str = br.readLine();
            List<String> info = Arrays.asList(str.trim().split(" "));
            for (int j = 0; j < width; ++j) {
                if (!Map.isValidNumber(Integer.parseInt(info.get(j)))) {
                    throw new IOException("Invalid number");
                }
                matrix[i][j] = Integer.parseInt(info.get(j));

            }
        }
        br.close();
        return new Map(matrix);
    }

    private static Boolean isValidNumber(Integer i) {
        Map.createValidationMap();
        return Map.hash_map.containsKey(i);
    }

    private static void createValidationMap() {
        if (!Map.hash_map.isEmpty()) {
            return;
        }
        Map.hash_map.put(Map.CLEAN, true);
        Map.hash_map.put(Map.FAILER, true);
        Map.hash_map.put(Map.TRASHY, true);
        Map.hash_map.put(Map.LOW_TRASHY, true);
        Map.hash_map.put(Map.VERY_TRASHY, true);
        Map.hash_map.put(Map.VACUUM, true);
    }

}
