# CW-TEMPLATE

Extra Files Added in student folder:

* Map - Explore Class
* Vertex - Working tile class for Explore Map stage
* MapGold - Escape Class
* VertexGold - Working tile class for Escape Map stage

Both Explore and Escape will ALWAYS work.

I have run out of time so final refactoring, cleanup, comments are simply not there.
Optimized gold collection is not finished. My idea was to have adjustable A* algorithm to have "closer to destination" distance on nodes that have most gold. Collection for detecting gold mapGold has nodes sorted by value descending.

Having different multiplier on recalculation would allow to include more nodes of value as the path to destination is calculated. Map could be calculated several times prior to actually traversing before final run. To do that I would also need to understand how time is substracted based on steps. Again run out of time to investigate.

Code is quite fast (i believe). -n 1000 explore/escapes takes about 30 seconds on my PC and is clogged mostly by substantial debug text output.

Total hours coding this assignment amounted to about 30 spread out over one week. Which was not enough. 

I very much enjoyed this assignment and I am certain more optimizations and shortcuts are possible.

