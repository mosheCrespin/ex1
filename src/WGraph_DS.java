package ex1.src;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph, java.io.Serializable{
    private HashMap<Integer, node_info> myGraph;//the graph+Ni for everyNode
    private  HashMap<node_info,HashMap<node_info,Double>> neighbors;//every node associate with its Neighbors and with weight for every Ni;
    private int amountOfChanges;
    private int nodeSize;
    private  int edgeSize;
    //constructors
    public WGraph_DS()
    {
        this.myGraph=new HashMap<>();
        this.neighbors=new HashMap<>();
        amountOfChanges=0;
        nodeSize=0;
        edgeSize=0;
    }

    /**
     * this method returns the node that pair to the given key, null if there is no such node on the graph
     * running time is O(1)
     * @param key - the node_id
     * @return the node
     */
    public node_info getNode(int key) {
        return myGraph.get(key);
    }


    /**
     * this method returns an HashMap contains all the neighbors of the requested node with a pair of weight
     * if the requested node is not in the graph then the method returns `null`
     * running time is O(1)
     * @param req_node
     * @return Hashmap
     */
    private HashMap<node_info,Double> getNeighbors(int req_node){
        return neighbors.get(getNode(req_node));
    }
    /**
     * this method check if two vertices are connected.
     * running time is O(1)
     * @param node1 the first vertax
     * @param node2 the second vertax
     * @return true if the nodes are connected, false otherwise
     */

    public boolean hasEdge(int node1, int node2) {
        if(getNode(node1)==null||getNode(node2)==null)
            return false;
        return getNeighbors(node1).containsKey(getNode(node2));
    }

    /**
     * this method returns the weight between the given nodes.
     * running time is O(1)
     * @param node1
     * @param node2
     * @return the weight if nodes are connected, 0 if node1==node2, -1 if the two nodes are not connected
     */
    public double getEdge(int node1, int node2) {
        if(node1==node2&& myGraph.containsKey(node1))
            return 0;
        if(!hasEdge(node1,node2)) return -1;
        return getNeighbors(node1).get(getNode(node2));
    }

    /**
     * this method add node to the graph, if the node already exist then return.
     * running time is O(1)
     * @param key
     */
    public void addNode(int key) {
        if(myGraph.containsKey(key)) return;
        node_info curr=new NodeInfo(key);
        myGraph.put(key,curr);
        neighbors.put(curr,new HashMap<>());
        amountOfChanges++;
        nodeSize++;
    }

    /**
     * this method connect between the given nodes and weight. if the input is incorrect then the method return.
     * if there is already an edge between the given nodes then the method updates the weight
     * running time is O(1)
     * @param node1
     * @param node2
     * @param w the weight
     */
    public void connect(int node1, int node2, double w) {
        if(getNode(node1)==null||getNode(node2)==null||w<0)//incorrect input
            return;
        if(node1==node2) return;//there is no loop in this graph
        if(hasEdge(node1,node2))
        {
            if(getNeighbors(node1).get(getNode(node2))==w) return;//there is no need to update
            getNeighbors(node1).put(getNode(node2),w);//just updates the weight
            getNeighbors(node2).put(getNode(node1),w);
            amountOfChanges++;
            return;
        }
        getNeighbors(node1).put(getNode(node2),w);
        getNeighbors(node2).put(getNode(node1),w);
        amountOfChanges++;
        edgeSize++;
    }

    /**
     * this method returns a collection of all the nodes of the graph
     * running time is O(1)
     * @return Collection of the nodes in the graph
     */

    public Collection<node_info> getV() {
        return myGraph.values();
    }
    /**
     * this method returns a collection of all the neighbors of the given node,null if the given node does not exist
     * running time is O(1)
     * @return collection or null
     */

    public Collection<node_info> getV(int node_id) {
        HashMap<node_info,Double> temp=getNeighbors(node_id);
        if(temp==null) return null;
        return temp.keySet();//set of all the nodes without there weights
    }

    /**
     * this method removes the given node from the graph with all of his edges
     * running time is O(k) while k is the number of neighbors of the given node
     * @param key
     * @return the node who removed from the graph
     */
    public node_info removeNode(int key) {
        node_info shouldDelete=getNode(key);
        if (shouldDelete == null) return null;
        HashMap<node_info, Double> currNeighbors = getNeighbors(key);
        if (currNeighbors.size() == 0)//this node does not have Neighbors
        {
            myGraph.remove(shouldDelete.getKey());
            neighbors.remove(shouldDelete);
            amountOfChanges++;
            nodeSize--;
            return shouldDelete;
        }
        for(node_info curr: currNeighbors.keySet()){//iterator running on all the neighbors of the given node and removes the connection between the nodes
            getNeighbors(curr.getKey()).remove(shouldDelete);
            amountOfChanges++;
        }
        myGraph.remove(shouldDelete.getKey());
        neighbors.remove(shouldDelete);
        nodeSize--;
        amountOfChanges++;
        edgeSize-= currNeighbors.size();
        return shouldDelete;
    }

    /**
     * this method removes the edge(if there is any) between two nodes
     * running time is O(1)
     * @param node1
     * @param node2
     */
    public void removeEdge(int node1, int node2) {
        if(!hasEdge(node1,node2)) return;
        getNeighbors(node1).remove(getNode(node2));
        getNeighbors((node2)).remove(getNode(node1));
        amountOfChanges++;
        edgeSize--;
    }

    /**
     * this method returns the number of nodes in this graph
     * running time is O(1)
     * @return node size
     */

    public int nodeSize() {
        return nodeSize;
    }
    /**
     * this method returns the number of edges in this graph
     * running time is O(1)
     * @return edge size
     */


    public int edgeSize() {
        return edgeSize;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        WGraph_DS wGraph_ds = (WGraph_DS) o;
//        boolean flag=false;
//        if(nodeSize == wGraph_ds.nodeSize &&
//                edgeSize == wGraph_ds.edgeSize){
//            flag=true;
//            for(int nodeOfThisGraph: myGraph.keySet()){
//                for(node_info Ni: getV(nodeOfThisGraph))
//                {
//                    if(getEdge(nodeOfThisGraph,Ni.getKey())!=((WGraph_DS) o).getEdge(nodeOfThisGraph,Ni.getKey()))
//                        return false;
//                }
//
//            }
//        }
//        return flag;
//
//    }
    /**
     * this method returns the amount of changes that made on this graph
     * running time is O(1)
     * @return edge size
     */
    public int getMC() {
        return amountOfChanges;
    }

    /**
     * regular toSting method
     * @return String of all the graph including neighbors for each node
     */
    public String toString(){
        StringBuilder str= new StringBuilder();
        for(node_info curr:neighbors.keySet()){
            str.append(curr.getKey()).append("->");
            for(node_info currNi : getNeighbors(curr.getKey()).keySet())
                str.append(" ").append(currNi.getKey()).append(", ");
            str.append("| \n");
        }
        return str +"\n" + "nodes " + nodeSize +", edges: " + edgeSize+ ", changes: " + getMC();
    }





    private static class NodeInfo implements node_info, java.io.Serializable{
        private int id;
        private String info;
        private double tag;
        //constructors
        public NodeInfo(int key)
        {
            this.id=key;
            this.info= "";
            this.tag=0;
        }

        /**
         * @return the node id
         */

        public int getKey() {
            return this.id;
        }

        /**
         * @return the info associate with this node
         */
        public String getInfo() {
            return this.info;
        }

        /**
         * this method allow to set the info of the associate node
         * @param s the new value of info
         */

        public void setInfo(String s) {
            this.info=s;
        }

        /**
         * @return the tag associate with this node
         */

        public double getTag() {
            return tag;
        }

        /**
         * this method allow to set the tag of the associate node
         * @param t the new value of tag
         */
        public void setTag(double t) {
            this.tag=t;
        }
        /**
         * regular toSting method
         * @return String of the node info: including `id`, `tag` and `info`.
         */
        public String toString(){
            return " id: " + getKey() + ", tag: " +getTag() + ",info: " + getInfo();
        }
    }
}
