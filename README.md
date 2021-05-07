# Maze

Generates a maze using Randomised Kruskal's Algorithm. Displays maze with JavaFX, and allows the user to interact with it.

Has 2 classes, **Maze** and **Test**.

**Main** runs the game.

## Maze.java

Class that generates maze.

### Methods

**Constructor**

```python
Maze maze = new Maze(n, m);
```

Where it generates a CreateMaze object of dimensions n by m.

```n``` by ```m``` is the same as ```x``` by ```y```.

**getWallPosition()**

```python
List<Integer> list = maze.getWallPosition();
```

Returns a List of integers representing walls.

For a 3 by 3 maze,

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/mazePicture.png" alt="Walls of maze" width=250 height=250/>


**getSquares()**

Returns a 

```python
HashMap<Integer, List<Integer>>
```

Where the key is the square number (starting from 1) and the value is a list of integers representing the walls surrounding it.

For the same 3 by 3 grid, 
```python
maze.getSquares();
``` 
returns:

```python
{
    1: [1, 7],
    2: [1, 2, 8],
    3: [2, 9],
    4: [3, 10],
    5: [3, 4, 11],
    6: [4, 12],
    7: [5, 10],
    8: [5, 6, 11],
    9: [6, 12]
}
```
Removing a wall and merging 2 squares just merges 2 list together and removes the common number. (e.g. ```[1, 7]``` and ```[1, 2, 8]``` becomes ```[2, 7, 8]``` when square ```1``` and ```2``` are merged) 


### Randomised kruskal's algorithm

Randomised Kruskal's Algorithm works by removing the walls of the maze and merging each cell until there is only 1 cell remaining.

For example, take the 3 by 3 grid above. Say we remove wall 8, squares 2 and 5 are now merged to form a cell:
<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze1stMerge.png" alt="Walls of maze" width=250 height=250/>

Now, we remove wall 3 and merge square 4 with the previously merged cell:

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze2ndMerge.png" alt="Walls of maze" width=250 height=250/>

Then, we remove wall 11 and merge square 8 with the cell:

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze3rdMerge.png" alt="Walls of maze" width=250 height=250/>

We remove wall 10 and merge square 7 with the cell:

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze4thMerge.png" alt="Walls of maze" width=250 height=250/>

Now we select to remove wall 5. However we cannot do this because square 7 and 8 are already merged to a single cell.

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze5thMerge.png" alt="Walls of maze" width=250 height=250/>

Repeat until all the squares are merged into one cell.


## Test.java

Testing class to test speed of program.

### Method

**test**

```c#
public static void test(int n, int m)
```

Outputs the time taken for the maze to generate.

(100 by 100 maze takes aroun 2.5 seconds)

## Main.java

Sample screenshots of a 20 by 20 maze

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze1.png" alt="Maze with path" width=500 height=500/>

Maze with previous route shown.

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze2.png" alt="Maze without previous path" width=500 height=500/>

Maze with previous route hidden.

<img src="https://github.com/JosherLo/Maze/blob/master/Screenshots/maze3.png" alt="Maze solved" width=500 height=500/>

Mazed solved

## Note

```diff
-Maze does not generate 1 by something or something by 1 or 2 by 2 maze!
```
