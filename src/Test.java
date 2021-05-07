import sample.CreateMaze;

public class Test {
    public static void main(String[] args) {
        CreateMaze createMaze = new CreateMaze(160, 160);
        long start = System.currentTimeMillis();
        createMaze.getWallPositon();
        System.out.println(System.currentTimeMillis() - start);
    }

}
