package sample;

import java.util.*;
public class Maze {
    private final int n;
    private final int m;

    private List<Integer> walls = new ArrayList<>();
    private HashMap<Integer, List<Integer>> squares = new HashMap<>();
    private HashMap<Integer, List<Integer>> squaresOriginal = new HashMap<>();

    public Maze(int n, int m){
        this.n = n;
        this.m = m;
    }

    public List<Integer> getWallPosition(){
        initSquares();
        if (n < 2 || m < 2) {
            return new ArrayList<>();
        } else {
            createMaze();
            List<Integer> lst = new ArrayList<>();
            for (int i : this.squares.keySet()) {
                lst = this.squares.get(i);
                break;
            }
            if (lst.size() >= ((this.n * this.m) / 500) + 1 + 2 * (this.n + this.m) && (this.m * this.n) / 500 > 0) {
                int times = new Random().nextInt((this.n * this.m) / 500) + 1 + (this.n + this.m);
                for (int i = 0; i < times; i++) {
                    lst.remove(new Random().nextInt(lst.size() - 1));
                }
            }
            return lst;
        }
    }

    public void initSquares(){
        for(int i = 1; i < 2 * this.n * this.m - this.n - this.m + 1; i++){
            this.walls.add(i);
        }
        for(int i = 1; i < this.n * this.m + 1; i++){
            this.squares.put(i, new ArrayList<Integer>());
            this.squaresOriginal.put(i, new ArrayList<Integer>());
        }
        for(int i: this.walls){
            if(i < this.n * this.m - this.m + 1){ //Vertical
                squares.get(i + (i - 1)/ (this.n - 1)).add(i);
                squares.get(i + 1 + (i - 1)/ (this.n - 1)).add(i);
                squaresOriginal.get(i + (i - 1)/ (this.n - 1)).add(i);
                squaresOriginal.get(i + 1 + (i - 1)/ (this.n - 1)).add(i);
            } else{ //Horizontal
                squares.get(i - (this.n * this.m) + this.m).add(i);
                squares.get(i - (this.n * this.m) + this.n + this.m).add(i);
                squaresOriginal.get(i - (this.n * this.m) + this.m).add(i);
                squaresOriginal.get(i - (this.n * this.m) + this.n + this.m).add(i);
            }
        }
    }

    public HashMap<Integer, List<Integer>> getSquares(){
        return this.squaresOriginal;
    }

    public List<Integer> getSquaresList(int num){
        List<Integer> lst = new ArrayList<>();
        for (int i : squares.keySet()) {
            if (squares.get(i).contains(num)) {
                lst.add(i);
            } else continue;
            if (lst.size() == 2) {
                break;
            }
        }
        return lst;
    }

    public void makeSame(List<Integer> lst, int num){
        for(int i: squares.get(lst.get(0))){
            System.out.println("MAKESAME" + i);
            if(!squares.get(lst.get(1)).contains(i)) {
                squares.get(lst.get(1)).add(i);
            }
        }
        squares.get(lst.get(1)).remove(squares.get(lst.get(1)).indexOf(num));
        Collections.sort(squares.get(lst.get(1)));
        squares.remove(lst.get(0));
    }

    public void createMaze(){
        List<Integer> keys = new ArrayList<>(squares.keySet());
        Collections.shuffle(keys);
        Random random = new Random();
        while(squares.size() != 1) {
            for (int i: keys) {
                try {
                    int num = squares.get(i).get(random.nextInt(squares.get(i).size() - 1));
                    System.out.println(num + " num " + num);
                    System.out.println(squares);
                    List<Integer> lst = getSquaresList(num);
                    if (!squares.get(lst.get(0)).equals(squares.get(lst.get(1))) ||
                       (squares.size() == 2 && squares.get(lst.get(0)).equals(squares.get(lst.get(1))))) {
                        makeSame(lst, num);
                    }
                    keys = new ArrayList<>(squares.keySet());
                } catch (Exception ignored) { }
            }
        }
    }
}