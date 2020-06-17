# Coverage simulator
coverage simulator is a java programe that let you load map from txt file, and run coverage strategy.
The programe will output the path of the strategy and the map in every step.

## Usage
Run the programe with these arguments:
```
 -l <number>                 limit for iteretions. if didn't set, won't be limit
 -lf <file>                  Log file. if didn't set - will print to console.
 -lm <file>                  Map log file. if didn't set - will print to console.
 -m <file>                   Map file
 -s <dfs/greedy/wavefront>   Strategy
 ```
 ### Strategy
 The programe support greedy heuristic, dfs and wavefront (base on http://pinkwink.kr/attachment/cfile3.uf@1354654A4E8945BD13FE77.pdf and https://www.sciencedirect.com/science/article/pii/S092188901300167X)
 ### Map Structer
 The map file will contain in the first row two number with comma between them the represent that rows and columns of map.
 Then, use numbers to define what the point is:
 ```
 9 = agent starting point
 3 = border (point that the agent can't step in to it)
 1 = regular point (agent can step in to it)
 2 = point that agent aleardy been in it
 ```
 
 Example:
 ```
 5,10
3 3 3 3 3 3 3 3 3 3
3 1 9 1 1 1 3 1 1 3
3 1 1 1 3 1 1 1 1 3
3 1 1 1 1 1 1 1 1 3
3 3 3 3 3 3 3 3 3 3

 
 ```
