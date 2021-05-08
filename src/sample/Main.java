package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {
    private int n;
    private int m;
    private double width;
    private double wallWidth;
    private Paint path = Color.LIGHTGRAY;
    private Paint wall = Color.BLACK;
    private boolean show = true;
    private Timeline timeline = new Timeline();
    private Timeline stopwatch = new Timeline();
    private Region[][] squares;
    private int x = 0;
    private int y = 0;
    private int time = 0;
    private EventHandler<KeyEvent> keyEvent;
    private Maze maze;

    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();
        StackPane stackPane = new StackPane(vBox);
        Scene scene = new Scene(stackPane, 600,600);
        primaryStage.setTitle("Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame(primaryStage, vBox, scene, stackPane);
    }

    public void startGame(Stage primaryStage, VBox vBox, Scene scene, StackPane stackPane){
        stackPane.getChildren().clear();
        vBox.getChildren().clear();
        Label text = new Label("Enter the dimensions for your maze!");
        Label enterX = new Label("X: ");
        Label enterY = new Label("     Y: ");
        TextField xInput = new TextField();
        TextField yInput = new TextField();
        Button submit = new Button("Generate!");
        submit.setId("submit");

        xInput.setMaxWidth(100);
        yInput.setMaxWidth(100);
        xInput.setMinWidth(100);
        yInput.setMinWidth(100);

        Font font = new Font(20);
        text.setFont(font);
        enterX.setFont(font);
        enterY.setFont(font);
        xInput.setFont(font);
        yInput.setFont(font);
        submit.setFont(font);

        EventHandler<MouseEvent> handler = mouseEvent -> {
            Button temp = (Button) mouseEvent.getSource();
            String id = temp.getId();
            if(id.equals("submit")){
                try{
                    this.n = Integer.parseInt(xInput.getText());
                    this.m = Integer.parseInt(yInput.getText());
//                    if ((this.m == 1 || this.n == 1) || (this.m == 2 && this.n == 2)) {
//                        Alert alert = new Alert(Alert.AlertType.WARNING);
//                        alert.setTitle("Error");
//                        alert.setHeaderText("Invalid size");
//                        alert.setContentText("Maze does not generate 1 by n, n by 1, or 2 by 2 maze!");
//                        alert.showAndWait();
//                        throw new IllegalArgumentException();
//                    } if (this.m < 2 || this.n < 2) throw new IllegalArgumentException(); // 0,0 does not raise an error
                    this.width = (this.n >= this.m) ? 500.0/this.n : 500.0/this.m;
                    this.wallWidth = this.width / 10.0;
                    this.squares = new Region[this.m][this.n];
                    generateMaze(primaryStage, vBox, scene, stackPane);
                } catch (Exception e){
                    text.setText("Please enter valid numbers!");
                }
            }
        };

        submit.setOnMouseClicked(handler);

        HBox h1 = new HBox(enterX, xInput, enterY, yInput);
        h1.setAlignment(Pos.CENTER);

        vBox.getChildren().add(text);
        vBox.getChildren().add(h1);
        vBox.getChildren().add(submit);

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        stackPane.getChildren().add(vBox);
    }

    public void generateMaze(Stage primaryStage, VBox vBox1, Scene scene, StackPane stackPane) {
        vBox1.getChildren().clear();
        Maze maze = new Maze(this.n, this.m);

        Task<List<Integer>> task = new Task<>() {
                @Override
                protected List<Integer> call() {
                    return maze.getWallPosition();
                }
            };
        task.setOnSucceeded(e -> {
            this.maze = maze;
            showMaze(primaryStage, vBox1, task.getValue(), scene, stackPane);
            this.timeline.stop();
        });
        new Thread(task).start();
        Label generatingLabel = new Label("Generating . . .");
        Button button = new Button("BACK");
        button.setOnMouseClicked(mouseEvent -> {
            stackPane.getChildren().clear();
            this.timeline.stop();
            this.timeline.getKeyFrames().clear();
            vBox1.getChildren().clear();
            startGame(primaryStage, vBox1, scene, stackPane);
        });
        generatingLabel.setFont(new Font(20));
        vBox1.getChildren().add(generatingLabel);
        AtomicInteger iter = new AtomicInteger();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(350), actionEvent -> {
            vBox1.getChildren().clear();
            vBox1.getChildren().add(generatingLabel);
           if(generatingLabel.getText().length() == 16){
               generatingLabel.setText("Generating");
           } else if(generatingLabel.getText().length() == 14){
               generatingLabel.setText("Generating . . .");
           } else if(generatingLabel.getText().length() == 12){
               generatingLabel.setText("Generating . .");
           } else if(generatingLabel.getText().length() == 10){
               generatingLabel.setText("Generating .");
           }
           iter.getAndIncrement();
           if(iter.get() >= 10){
               Label msg = new Label("Please be patient!\nIf you generate a maze greater than 50 by 50,\nit could take a long time!");
               vBox1.getChildren().add(msg);
               vBox1.getChildren().add(button);
           }
        });
        this.timeline.getKeyFrames().add(keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    public List<Integer> wallsInRow(List<Integer> pos, int rowNum) {
        List<Integer> rowOfWalls = new ArrayList<>();
        for(int x: pos){
            if(x > this.n * this.m - this.m && (x - (this.n *this.m - this.m)) <= this.n * (rowNum + 1) && (x - (this.n * this.m - this.m)) > this.n * rowNum){ //Horizontal walls
                rowOfWalls.add((x - (this.n * this.m - this.m)) - (this.n*rowNum) + this.n - 1);
            } else if(x <= this.n * this.m - this.m && x <= (this.n - 1) * (rowNum + 1) && x > (this.n - 1) * rowNum){ //Vertical walls
                if(x % (this.n - 1) == 0){
                    rowOfWalls.add(this.n - 1);
                } else{
                    rowOfWalls.add(x % (this.n - 1));
                }
            }
        }
        return rowOfWalls;
    }

    public void showMaze(Stage primaryStage, VBox vBox1, List<Integer> pos, Scene scene, StackPane stackPane){
        this.x = 0;
        this.y = 0;
        this.time = 0;
        VBox vBox = new VBox();
        vBox1.getChildren().clear();
        Rectangle upperBorder = new Rectangle(500 + 2 * this.wallWidth, this.wallWidth);
        upperBorder.setFill(this.wall);
        vBox.getChildren().add(upperBorder);

        // Generate maze row by row

        for(int i = 0; i < this.m; i++){
            List<Integer> rowOfWalls = wallsInRow(pos, i);

            // Creating regions with borders as walls

            HBox hBox = new HBox();
            Region firstSquare = new Region(); // First square of each row
            firstSquare.setMaxSize(this.width,this.width);
            firstSquare.setMinSize(this.width, this.width);
            firstSquare.setPrefSize(this.width, this.width);
            firstSquare.setBackground(new Background(new BackgroundFill(this.path, CornerRadii.EMPTY, null)));
            if(rowOfWalls.contains(this.n)){
                firstSquare.setStyle("-fx-border-color: transparent transparent black transparent; -fx-border-width: " + this.wallWidth);
            }
            this.squares[i][0] = firstSquare;
            Rectangle vWall = new Rectangle(this.wallWidth,  this.width, this.wall);
            hBox.getChildren().add(vWall);
            hBox.getChildren().add(firstSquare);
            for(int j = 0; j < n - 1; j++){
                Region square = new Region();
                this.squares[i][j + 1] = square;
                square.setMaxSize(this.width, this.width);
                square.setMinSize(this.width, this.width);
                square.setPrefSize(this.width, this.width);
                square.setBackground(new Background(new BackgroundFill(this.path, CornerRadii.EMPTY, null)));
                if(rowOfWalls.contains((j + 1))){
                    square.setStyle("-fx-border-color: transparent transparent transparent black; -fx-border-width: " + wallWidth);
                    if(rowOfWalls.contains(j + n + 1)){
                        square.setStyle("-fx-border-color: transparent transparent black black; -fx-border-width: " + wallWidth);
                    }
                } else if(rowOfWalls.contains(j + n + 1)){
                    square.setStyle("-fx-border-color: transparent transparent black transparent; -fx-border-width: " + wallWidth);
                }
                hBox.getChildren().add(square);
                hBox.setAlignment(Pos.CENTER);
            }
            Rectangle endBorder = new Rectangle(this.wallWidth,  this.width, this.wall);
            hBox.getChildren().add(endBorder);
            vBox.getChildren().add(hBox);
        }
        Rectangle bottomBorder = new Rectangle(500 + 2 * this.wallWidth, this.wallWidth);
        bottomBorder.setFill(this.wall);
        vBox.getChildren().add(bottomBorder);

        vBox1.getChildren().add(vBox);
        vBox1.getChildren().add(initOptions(scene, primaryStage, vBox1, stackPane));
        vBox1.setSpacing(10);
        vBox.setSpacing(0);
        vBox.setAlignment(Pos.CENTER);
        initKeyEvent(stackPane, pos);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.keyEvent);
        this.squares[this.y][this.x].setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        this.squares[this.m - 1][this.n - 1].setBackground(new Background(new BackgroundFill(Color.LIGHTSEAGREEN, null, null)));
    }

    public HBox initOptions(Scene scene, Stage primaryStage, VBox vBox, StackPane stackPane) {
        Label timeLabel = new Label("0:00");
        timeLabel.setFont(new Font(20));
        Button button = new Button("BACK");
        button.setFont(new Font(16));
        button.setOnMouseClicked(mouseEvent -> {
            this.stopwatch.stop();
            this.stopwatch.getKeyFrames().clear();
            scene.removeEventFilter(KeyEvent.KEY_PRESSED, this.keyEvent);
            stackPane.getChildren().clear();
            startGame(primaryStage, vBox, scene, stackPane);
        });
        CheckBox checkBox = new CheckBox("Show route");
        checkBox.setSelected(true);
        checkBox.setOnAction(actionEvent -> {
            this.show = checkBox.isSelected();
            if(!this.show){
                for(int i = 0; i < this.squares.length; i++){
                    for(int j = 0; j < this.squares[i].length; j++){
                        if(!(i == this.y && j == this.x)) {
                            this.squares[i][j].setBackground(new Background(new BackgroundFill(this.path, null, null)));
                        }
                    }
                }
            } else {
                this.squares[this.m - 1][this.n - 1].setBackground(new Background(new BackgroundFill(Color.LIGHTSEAGREEN, null, null)));
            }
        });
        HBox hBox = new HBox(timeLabel, button, checkBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(25);
        startTiming(timeLabel);
        return hBox;
    }

    public void startTiming(Label timeLabel) {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), actionEvent -> {
            this.time++;
            int minutes = this.time / 60;
            int seconds = this.time % 60;
            timeLabel.setText(String.format("%d:%02d", minutes, seconds));
        });
        this.time = 0;
        this.stopwatch.getKeyFrames().add(keyFrame);
        this.stopwatch.setCycleCount(Animation.INDEFINITE);
        this.stopwatch.play();
    }

    public void initKeyEvent (StackPane stackPane, List<Integer> finalPos) {
        this.keyEvent = keyEvent -> {
            HashMap<Integer, List<Integer>> squaresOriginal = this.maze.getSquares();
            int squareNum = this.y * this.n + this.x + 1;
            List<Integer> walls = squaresOriginal.get(squareNum);
            boolean up = true;
            boolean down = true;
            boolean left = true;
            boolean right = true;
            for(int i: walls){
                if(finalPos.contains(i)){
                    if(i == ((squareNum * (this.n - 1) + 1) / this.n) + 1){
                        right = false;
                    }
                    if(i == ((squareNum * (this.n - 1) + 1) / this.n)){
                        left = false;
                        if(squareNum % this.n == 1){
                            right = false;
                        }
                    }
                    if(i == squareNum + this.n * this.m - this.m){
                        down = false;
                    }
                    if(i == squareNum + this.n * this.m - this.m - this.n){
                        up = false;
                    }
                }
            }

            if (keyEvent.getCode() == KeyCode.UP) {
                if (this.y > 0 && up) {
                    update(this.x, this.y - 1, stackPane);
                }
            } else if (keyEvent.getCode() == KeyCode.DOWN) {
                if (this.y < this.m - 1 && down) {
                    update(this.x, this.y + 1, stackPane);
                }
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                if (this.x > 0 && left) {
                    update(this.x - 1, this.y, stackPane);
                }
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                if (this.x < this.n - 1 && right) {
                    update(this.x + 1, this.y, stackPane);
                }
            }
        };
    }

    public void update(int newX, int newY, StackPane stackPane){

        this.squares[newY][newX].setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        if(this.show) {
            this.squares[this.y][this.x].setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
        } else {
            this.squares[this.y][this.x].setBackground(new Background(new BackgroundFill(this.path, null, null)));
        }
        this.x = newX;
        this.y = newY;
        if(this.x == this.n - 1 && this.y == this.m - 1){ // Reached last square
            this.stopwatch.stop();
            this.stopwatch.getKeyFrames().clear();
            Label winLabel = new Label("You won!\nYou took " + time + " seconds.");
            winLabel.setTextAlignment(TextAlignment.CENTER);
            winLabel.setFont(new Font(40));
            stackPane.getChildren().add(winLabel);
            stackPane.getChildren().get(1).setOpacity(0);
            Timeline animation = new Timeline();
            AtomicReference<Double> opacityMsg = new AtomicReference<>(1.0);
            AtomicReference<Double> opacityStackPane = new AtomicReference<>((double) 0);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(7), actionEvent -> {
                VBox vBox = (VBox) stackPane.getChildren().get(0);
                vBox.getChildren().get(0).setOpacity(opacityMsg.get());
                stackPane.getChildren().get(1).setOpacity(opacityStackPane.get());
                opacityStackPane.updateAndGet(v -> v + 0.007);
                opacityMsg.updateAndGet(v -> v - 0.005175);
            });
            animation.getKeyFrames().add(keyFrame);
            animation.setCycleCount(150);
            animation.play();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
