# Coverage simulator
coverage simulator is a java programe that let you load map from txt file, and run coverage strategy.
The programe will output the path of the strategy and the map in every step.

## Usage
Run the programe with these arguments:
'''
 -l <number>                 limit for iteretions. if didn't set, won't be limit
 -lf <file>                  Log file. if didn't set - will print to console.
 -lm <file>                  Map log file. if didn't set - will print to console.
 -m <file>                   Map file
 -s <dfs/greedy/wavefront>   Strategy
 '''
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
