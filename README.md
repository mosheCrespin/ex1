#ex1

*this project is used for an implementation of Undirected (positive) Weighted Graph*

**info:**
*there are 3 classes: NodeInfo, WGraph_DS, WGraph_Algo

**NodeInfo**

*this class is private inner class. Each node has 3  instance variables: `key`-a unique  id for this node. `info`- the information that the node holds, `tag`- usually used for the algorithm class;

*In this class there are getters and setters(there is no setter for key) and there is `toString()` method;

////////////////////////////

**WGraph_DS**

*this class represents Undirected (positive) Weighted Graph. this class holds NodeInfo as inner class. each object of this class contains a data structure who holds the nodes of the graph(in Hash-Map). in addition, there are 3 instance variables: `nodeSize`- the amount of nodes in the graph, `edgeSize`- the amount of edges in the graph, `amountOfChanges`- the amount of changes made on the graph;

*In this class there are getters for the Instance variables and `toString()` method;

*the `addNode(key)` method adds the node with the given id to the graph, if the node with the given id already exist in the graph, the function does not add this node again;

*the `hasEdge(key1,key2)` method, check if the given nodes has edge who connect them.

*the `connect(key1,key2,weight)` method connect between the given nodes. this method uses the `hasEdge()` method to check if the two nodes already has an edge between them. if they are already connected then the method will update only the weight, if the weight is the same as before, then the method will do nothing;

*the `getEdge(node1,node2)` method uses `hasEdge()` method to check if there is an edge between the given nodes, if not the method returns -1. if the answer is yes then the method returns the weight between the given nodes(the weight between node to itself is 0);

*the `removeNode(key)` method removes the node from the graph and all of his edges;

*the `removeEdge(key1,key2)` method uses `hasEdge()` method to check if there is an edge between the given nodes. if the answer is yes then the method removes the edge between the given nodes. else the answer is no, so the method do nothing;

*the `getV()` method returns a Collection of all the nodes in the graph;

*the `getV(ket)` method returns a Collection of all the neighbors nodes of the given node;

///////////////////////////

**WGraph_Algo**

*this class represents the Theory algorithms for an undirected weighted graph;

*the `init(graph)` method Initializing the graph of this class to work with the given graph;

*the `getGraph()` method simply returns the graph;

*the `copy() method returns an all new graph (deep-copy);

*the `isConnected()` method returns true if the graph is connected (using BFS algorithm), if there is only 1 node in the graph the method also returns true;

*the `shortestPathDist(src, dest)` method returns the length of the shortest path between `src` to `dest` via weight using Dijkstra's algorithm. if there is no path then the method returns -1. if src is equal to dest then the method returns 0;

*the `shortestPath(src, dest)` method, this method using Dijkstra's algorithm. it returns an actual path between `src` to `dest` via List of node_info. if `src` or `dest` are not nodes in the graph, than the method returns `null`. if `src` is equal to `dest` then the method returns a list with only the node who belongs to`src`. if there is no path between the nodes, then the method returns an empty list; 

*the `save(dest)` method simply save the graph using `java.io.Serializable` with the name of `dest`, the method returns `true` or `false` depends on if the process succeeded;

*the `load(dest)` method simply load the `dest` file using `java.io.Serializable` the loaded graph will be the class graph, if there is no such file(dest) in the memory then the graph will remain with no differences. the method returns `true` or `false` depends on if the process succeeded;
