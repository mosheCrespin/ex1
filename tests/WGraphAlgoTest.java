package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WGraphAlgoTest {


    @Test
    void isConnected() {
        weighted_graph g0 = connected_graph_creator(500000);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        Assertions.assertTrue(ga.isConnected());
        int i = g0.nodeSize() / 2;
        g0.removeNode(i);
        g0.removeEdge(i - 1, i + 1);
        Assertions.assertFalse(ga.isConnected());
    }

    @Test
    void copy() {
        weighted_graph g0 = connected_graph_creator(150000);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        weighted_graph g1 = ga.copy();
        assertEquals(g0, g1);
        g0.removeNode(g0.nodeSize() / 6 + 1);
        g1.removeNode(g1.nodeSize() / 8 - 1);
        Assertions.assertNotEquals(g0, g1);
        g1.removeEdge(g1.nodeSize() / 2, (g1.nodeSize() / 2) + 1);
        g0.removeEdge((g0.nodeSize() / 4), (g0.nodeSize() / 4 + 1));
        Assertions.assertNotEquals(g0, g1);
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = connected_graph_creator(500000);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        int src = 220002;//src has to be smaller then dest
        int dest = 450002;//you can change any number to check every possible option
        double d = ga.shortestPathDist(src, dest);
        if ((src % 2 == 0) && (dest % 2 == 0)) {
            assertEquals(d, 0.25 * (dest - src) / 2);
        } else if ((src % 2 != 0) && (dest % 2 != 0)) {
            assertEquals(d, 0.125 * (dest - src + 4));
        } else if ((src % 2 != 0) && (dest % 2 == 0)) {
            assertEquals(d, 0.125 * (dest - src + 1));
        } else
            assertEquals(d, 0.125 * (dest - src + 3));
    }
    @Test
    void chekShortestPathWithNoPath(){
        weighted_graph g0 = new WGraph_DS();
        for(int i=0; i<5;i++)
             g0.addNode(i);
        g0.connect(0,1,3);
        g0.connect(0,2,3);
        g0.connect(1,3,2);
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g0);
        assertEquals(-1,ga.shortestPathDist(0,4));
        assertNull(ga.shortestPath(0,4));
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = connected_graph_creator(100000);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        int src = 5000;//(in this test src has to be smaller than dest
        int dest = 70000;//both should start with even numbers or odd numbers
        int temp=0;
        for(int k=0;k<4;k++) {
            src+=temp;
            List<node_info> ll = ga.shortestPath(src, dest);
            int[] shortestPathArray = new int[ll.size()];
            int[] arr = new int[ll.size()];
            for (int i = 0; i < shortestPathArray.length; i++) {
                shortestPathArray[i] = ll.get(i).getKey();
            }
            if ((src % 2 == 0) && (dest % 2 == 0)) {
//                System.out.println("src even, dest even" + ", " + "dest= " + dest +", src= "+ src);
                int j = 0;
                for (int i = src; i < dest + 2; i += 2) {
                    arr[j] = i;
                    j++;
                }

                Assertions.assertTrue(equalsArray(shortestPathArray, arr));
            } else if ((src % 2 != 0) && (dest % 2 != 0)) {
//                System.out.println("src odd, dest odd" + ", " + "dest= " + dest +", src= "+ src);

                arr[0] = src;
                arr[arr.length - 1] = dest;
                int j = 1;
                for (int i = src + 1; i < dest + 3; i += 2) {
                    arr[j] = i;
                    j++;
                }
                Assertions.assertTrue(equalsArray(shortestPathArray, arr));
            } else if ((src % 2 != 0) && (dest % 2 == 0)) {
//                System.out.println("src odd, dest even" + ", " + "dest= " + dest +", src= "+ src);

                arr[0] = src;
                int j = 1;
                for (int i = src + 1; i < dest + 2; i += 2) {
                    arr[j] = i;
                    j++;
                }
                Assertions.assertTrue(equalsArray(shortestPathArray, arr));
            } else {
//                System.out.println("src even, dest odd" + ", " + "dest= " + dest +", src= "+ src);

                arr[arr.length - 1] = dest;
                int j = 0;
                for (int i = src; i < dest + 3; i += 2) {
                    arr[j] = i;
                    j++;
                }
                Assertions.assertTrue(equalsArray(shortestPathArray, arr));
            }
            dest+=k;
            temp++;
        }


    }
    @Test
    void save_load() throws FileNotFoundException {
        weighted_graph g0 =connected_graph_creator(80);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g0);
        String str = "g0.obj";
        ga.save(str);
        ga.load(str);
        assertEquals(ga.getGraph(),g0);
        ga.getGraph().removeNode(33);
        Assertions.assertNotEquals(ga.getGraph(),g0);
        weighted_graph g1=ga.getGraph();
        boolean flag=ga.load("non_exiting_file.obj");
        assertFalse(flag);
        assertSame(ga.getGraph(), g1);
    }
    @Test
    void checkConnectivityWith2And1NodesWith0Edges(){
        weighted_graph g0=new WGraph_DS();
        g0.addNode(0);
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g0);
        assertTrue(ga.isConnected());
        g0.addNode(1);
        assertFalse(ga.isConnected());
    }
    @Test
    void checkAllTheFunctionsWithWeightOf0(){
        weighted_graph g0=new WGraph_DS();
        for(int i=0;i<5;i++)
            g0.addNode(i);
        g0.connect(0,1,0.25);
        g0.connect(0,2,0);
        g0.connect(2,3,0);
        g0.connect(3,1,0);
        g0.connect(3,4,1);
        g0.connect(1,4,0);
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g0);
        assertEquals(0,ga.shortestPathDist(0,3));
        int[] arr=new int[5];
        arr[1]=2;
        arr[2]=3;
        arr[3]=1;
        arr[4]=4;
        List<node_info> ll=ga.shortestPath(0,4);
        int [] arr2=new int[ll.size()];
        int i=0;
        for(node_info temp: ll) {
            arr2[i] = temp.getKey();
            i++;
        }
        assertTrue(equalsArray(arr2,arr));
    }
    @Test
    void checkPathFromNodeToItself(){
        weighted_graph g0=connected_graph_creator(10);
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g0);
        assertEquals(0,ga.shortestPathDist(2,2));
        List<node_info> ll=ga.shortestPath(2,2);
        assertEquals(1,ll.size());
        assertEquals(2,ll.listIterator().next().getKey());
    }
    @Test
    void checkPathFromAndToNotExitingNode(){
        weighted_graph g0=connected_graph_creator(10);
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g0);
        assertNull(ga.shortestPath(12,8));
        assertNull(ga.shortestPath(8,12));
        assertEquals(-1,ga.shortestPathDist(8,12));
        assertEquals(-1,ga.shortestPathDist(12,8));
    }
    //////////////////////////////////////////////////////////////////////

    public static weighted_graph connected_graph_creator(int v_Size){
        weighted_graph g0=new WGraph_DS();
        double odd=1;
        double even=0.25;
        for(int i=0;i<v_Size;i++)
            g0.addNode(i);
        for(int i=0;i<v_Size-3;i++)
        {
            if(i%2==0) {
                g0.connect(i, i + 1, odd);
                g0.connect(i,i+2,even);
            }
            else {
                g0.connect(i, i + 1, even);
                g0.connect(i, i + 2, odd);
            }

        }
        g0.connect(v_Size-4,v_Size-3,12);
        g0.connect(v_Size-3,v_Size-2,12);
        g0.connect(v_Size-2,v_Size-1,12);

        return g0;
    }
    public boolean equalsArray(int [] a, int [] b){
//        System.out.println(Arrays.toString(a));
//        System.out.println(Arrays.toString(b));

        if(a.length!=b.length) return false;
        for(int i=0;i<a.length;i++){
            if(a[i]!=b[i])
                return false;
        }
        return true;
    }
}
