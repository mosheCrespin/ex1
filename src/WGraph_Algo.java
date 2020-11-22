package ex1.src;
import java.io.*;
import java.util.*;
public class WGraph_Algo implements weighted_graph_algorithms,java.io.Serializable{
    private weighted_graph myAlgoGraph;
    //constructor
    public WGraph_Algo(){
        this.myAlgoGraph=new WGraph_DS();
    }
    public WGraph_Algo(weighted_graph Graph_Algo) {
        if (Graph_Algo == null)
            this.myAlgoGraph = new WGraph_DS();
        else this.myAlgoGraph = Graph_Algo;
    }

    /**
     *  this method Initializing the graph of this class to work with the given graph;
     * @param g the init graph
     */

    public void init(weighted_graph g) {
        if(g==null) return;
        this.myAlgoGraph=g;
    }

    /**
     * @return the graph
     */

    public weighted_graph getGraph() {
        return myAlgoGraph;
    }

    /**
     * this method returns an all new graph (deep copy)
     * running time is O(n^2)
     * @return a deep copied graph
     */

    public weighted_graph copy() {
            weighted_graph new_copy = new WGraph_DS();
            for (node_info curr : myAlgoGraph.getV()) {//O(n)
                    new_copy.addNode(curr.getKey());//add the new node to the graph
                    node_info thisNode= new_copy.getNode(curr.getKey());
                    thisNode.setTag(curr.getTag());
                    thisNode.setInfo(curr.getInfo());
            }
            for (node_info curr : myAlgoGraph.getV()) {//This loop is responsible for connecting the vertices
                for(node_info NiOfCurr: myAlgoGraph.getV(curr.getKey()))//O(n)*O(n)=O(n^2)
                {
                    new_copy.connect(curr.getKey(),NiOfCurr.getKey(),myAlgoGraph.getEdge(curr.getKey(), NiOfCurr.getKey()));
                }
                }
            return new_copy;

    }

    /**
     * this method check if this graph is connected using BFS algorithm
     * running time is O(V+E)
     * @return true if the graph is connected, `false` otherwise
     */

    public boolean isConnected() {
        int Node_size = myAlgoGraph.nodeSize();
        if (Node_size < 2)//if the number of nodes is less than 2 the graph is connected
            return true;
        //if the graph is connected it should have at least n-1 edges
        if(myAlgoGraph.edgeSize()<Node_size-1)
            return false;
        if(myAlgoGraph.edgeSize()== (myAlgoGraph.nodeSize()*(myAlgoGraph.nodeSize()-1)/2))
            return true;
        //we should start from some place
        int starter = myAlgoGraph.getV().iterator().next().getKey();//should start from somewhere
        node_info start = myAlgoGraph.getNode(starter);
        initTags();//initializes all the tags to -1
        node_info curr;
        Queue<node_info> q = new LinkedList<>();
        q.add(start);
        start.setTag(1);
        int connectedNodesCounter = 1;
        while (!q.isEmpty()) {
            curr = q.poll();
            for (node_info adj : myAlgoGraph.getV(curr.getKey())) {
                if (adj.getTag() == -1) {
                    q.add(adj);
                    adj.setTag(1);
                    connectedNodesCounter++;
                }
            }
        }
        //if the number of the seen vertices is equal to nodeSize than the graph is connected
        return connectedNodesCounter == Node_size;

    }

    /**
     * this method init all the tags of the nodes belongs to the graph
     * running time is O(n)
     */
    private void initTags(){
        for(node_info curr: myAlgoGraph.getV()){
            curr.setTag(-1);
        }
    }

    /**
     * this method get 2 nodes and calculate the shortest path between them.
     * if there is a path than the method returns the sum of the weights of the shortest path
     * if there is no path or if one of the nodes does not exist in the graph, than returns -1.
     * if src==dest than returns 0
     * this method using Dijkstra algorithm
     * running time is O(logV(V+E))
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    public double shortestPathDist(int src, int dest) {
        //this comparator using the field tag of every node to compare
        Comparator<node_info> nodeInfoComparator= Comparator.comparingDouble(node_info::getTag);
        Queue<node_info> q=new PriorityQueue<>(nodeInfoComparator);
        initTags();//init all the tags to -1
        node_info start=myAlgoGraph.getNode(src);
        node_info end=myAlgoGraph.getNode(dest);
        if(start==null||end==null) return -1;//if one or two of the nodes does not exist in the graph
        if(src==dest) {//if true then returns a 0
            return 0;
        }
        node_info curr;
        start.setTag(0);//the distance between node to itself is 0
        q.add(start);
        while(!q.isEmpty()){
            curr=q.poll();//take a node
           for(node_info Ni: myAlgoGraph.getV(curr.getKey())) {//run for all of his Ni. O(logE)
               if (Ni.getTag() == -1)//if the node never got visited
               {
                   Ni.setTag(curr.getTag() + myAlgoGraph.getEdge(Ni.getKey(), curr.getKey()));//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                   q.add(Ni);//add this node to the queue
               } else {//the node already got visited
                    //take the minimum between the old path to the new one
                   Ni.setTag(Math.min(Ni.getTag(), curr.getTag() + myAlgoGraph.getEdge(Ni.getKey(), curr.getKey())));
               }
           }
        }
        //if there is no path then the dest node never got visited so his taf equals to -1, otherwise the tag contains the shortest path
        return end.getTag();
    }

    /**
     *  /**
     * this method get 2 nodes and calculate the shortest path between them.
     * if there is a path than the method returns the the actual path using List</node_info>
     * if there is no path then the method returns an empty list.
     * if one of the nodes does not exist in the graph, than returns null.
     * if src==dest than returns a list with only the src node
     * this method using Dijkstra algorithm
     * running time is O(logV(V+E))
     * @param src - start node
     * @param dest - end (target) node
     * @return list with the actual path
     */
    public List<node_info> shortestPath(int src, int dest) {
        //this comparator using the field tag of every node to compare
        Comparator<node_info> nodeInfoComparator= Comparator.comparingDouble(node_info::getTag);
        Queue<node_info> q=new PriorityQueue<>(nodeInfoComparator);
        HashMap<node_info,node_info> father=new HashMap<>();//this hashmap is using to recover the path
        initTags();//init all the tags to -1
        node_info start=myAlgoGraph.getNode(src);
        node_info end=myAlgoGraph.getNode(dest);
        if(start==null||end==null) return null;
        if(src==dest) {//if true then returns a list with only the start node
            List<node_info> temp= new LinkedList<>();
            temp.add(start);
            return temp;
        }
        node_info curr;
        start.setTag(0);//the distance between node to itself is 0
        q.add(start);
        boolean flag=false;
        while(!q.isEmpty()){
            curr=q.poll();//take a node
            for(node_info Ni: myAlgoGraph.getV(curr.getKey())) {//run for all of his Ni
                if (Ni.getKey() == dest) flag = true;//if flag==true then there is a path
                if (Ni.getTag()==-1) {//if the Ni never got visited
                    Ni.setTag(curr.getTag() + myAlgoGraph.getEdge(Ni.getKey(), curr.getKey()));//the tag of this node is his father tag(recursive)+the weight of the edge who connects between the father to him.
                    father.put(Ni, curr);//the HashMap builds in this path--> <the neighbor, his father>
                    q.add(Ni);//O(logV)
                } else {//if the Ni already got visited
                    //take the minimum between the Ni tag to the new path that found.
                    double temp=Math.min(Ni.getTag(), curr.getTag() + myAlgoGraph.getEdge(Ni.getKey(), curr.getKey()));
                    if(temp!=Ni.getTag()) {//if the new path is better
                        father.put(Ni, curr);//set the new father of Ni
                        Ni.setTag(temp);//set the new path of Ni
                    }
                }
            }

        }
        if(!flag)//if there is no path then return null
            return null;

        return buildPath(father,end);//builds path using `buildPath` and return this list
    }

    /**
     * this method makes a conversion from HashMap that holds a path, to a List of nodes
     * running time is O(k) while k is the number of the nodes in the path
     * @param father the HashMap who holds the path
     * @param dest the key of the dest node
     * @return a list of the path
     */
    private LinkedList<node_info> buildPath(HashMap<node_info,node_info> father,node_info dest){
        LinkedList<node_info> ans=new LinkedList<>();
        ans.add(dest);
        dest=father.get(dest);//O(1)
        while(dest!=null){//O(K)
            ans.addFirst(dest);
            dest=father.get(dest);
        }
        return ans;
}

    /**
     * this method saves the graph using `java.io.Serializable`
     * @param file - the file name (may include a relative path).
     * @return `true` if the process succeeded `false` otherwise
     */
    public boolean save(String file) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.myAlgoGraph);
            fileOutputStream.close();
            objectOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this method loads a graph from the given name/path.
     * the loaded graph will be the class graph.
     * if the method does not find the file than the graph will remain with no differences.
     * @param file - file name
     * @return `true` or `false` depends on if the process succeeded;
     */
    public boolean load(String file) {
        try {
            FileInputStream fileInputStream=new FileInputStream(file);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            this.myAlgoGraph= (weighted_graph)objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
