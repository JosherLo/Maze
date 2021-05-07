package sample;

import sample.CreateMaze;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) {
        test(100, 100);
    }

    public static void test(int n, int m) {
        System.out.printf("Generating a %d by %d maze......%n", n, m);
        System.out.println("_______________________________________");
        CreateMaze createMaze = new CreateMaze(n, m);
        new DateTimeFormatter().format("HH/")
        LocalDateTime.now()
        long start = System.currentTimeMillis();
        createMaze.getWallPositon();
        System.out.println(System.currentTimeMillis() - start);

    }
}
