package sample;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void test(int n, int m) {
        System.out.printf("Generating a %d by %d maze......%n", n, m);
        System.out.println("_____________________________________________");
        Maze maze = new Maze(n, m);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm ss SSS");
        LocalDateTime start = LocalDateTime.now();
        System.out.println("Starting time (HH:mm ss ms): " + start.format(format));
        List<Integer> list = maze.getWallPositon();
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Ending time (HH:mm ss ms): " + end.format(format));
        System.out.println("_____________________________________________");

        Long duration = Duration.between(start, end).toMillis();
        int minutes = 0;
        int seconds = 0;
        while (duration >= 1000) {
            duration -= 1000;
            seconds ++;
        }
        while (seconds >= 60) {
            seconds -= 60;
            minutes ++;
        }

        System.out.printf("Maze generated in %d minutes, %d seconds and %d milliseconds.%n", minutes, seconds, duration);
        Scanner in = new Scanner(System.in);
        if(list.size() > 30) {
            System.out.print("Do you want to view the maze? ");
            String res = in.next();
            if (res.equalsIgnoreCase("y") || res.equalsIgnoreCase("yes")) {
                System.out.println("Maze created: ");
                printMaze(list);
            }
        } else {
            printMaze(list);
        }
    }
    private static void printMaze(List<Integer> list) {
        System.out.print("[ ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != list.size() - 1) {
                System.out.print(", ");
            }
            if (i % 10 == 9) {
                System.out.print("\n");
            }
        }
        System.out.println(" ]");
    }
}
