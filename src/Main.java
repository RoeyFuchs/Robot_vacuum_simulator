public class Main {
    public static void main(String[] args) {
        System.out.println("Good luck");
        Map map = null;
        try {
            map = Map.CreateMap("map.txt");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace());
        }
        Agent agent = new DummyAgent();
        Administrator admin = new Administrator(map, agent);

        int numberOfSteps = 5;
        for(int i = 0 ; i < numberOfSteps; i++) {//we need to change it
            map.printMap();
            admin.doOneStep();
            System.out.println();
        }


    }
}
