import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Good luck");
        try {
            Map.CreateMap("map.txt").printMap();
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace());
        }
    }
}
