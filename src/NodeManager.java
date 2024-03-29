//******************************************************************************
//
//  File:    NodeManager.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implements the node manager which is used to manage all the nodes
//  in the whole search space
//******************************************************************************
import java.util.HashMap;

/**
 * This class manages all the nodes in the search space
 */
public class NodeManager {

    private Node[] nodes = null;
    private int size = 0;
    private TruckManager truckManager = null;
    private int[][] distanceBetweenNodes = null;
    private int depot = 0;
    private DistanceMetric distanceMetric = null;
    private HashMap<Integer, Node> NodeRefrence = null;

    public NodeManager(DistanceMetric distanceMetric, int size){
        this.distanceMetric = distanceMetric;
        this.size = size;
        nodes = new Node[size];
        distanceBetweenNodes = new int[size][size];
        this.NodeRefrence = new HashMap<Integer, Node>();
    }

    /**
     * Given a set of cordinates create nodes
     * @param cordinates cornidates of the nodes
     */
    public void createNodes(String[] cordinates){
        for(String i: cordinates){
            String[] coord = i.strip().split(" ");
            int nodeNumber = Integer.parseInt(coord[0]);
            int nodeX = Integer.parseInt(coord[1]);
            int nodeY = Integer.parseInt(coord[2]);
            nodes[nodeNumber - 1] = new Node(nodeX, nodeY);
        }

        calculateDistanceForAllNodes();
    }

    /**
     * Find the distance of all nodes with one another
     */
    private void calculateDistanceForAllNodes() {
        for (int i = 0; i < nodes.length; i++){
            for (int j = 0; j <= i; j++){
                if(i == j) {
                    distanceBetweenNodes[i][j] = Integer.MAX_VALUE;
                }
                else {
                    int distBetweenIAndJ = distanceMetric.distance(nodes[i], nodes[j]);
                    distanceBetweenNodes[i][j] = distBetweenIAndJ;
                    distanceBetweenNodes[j][i] = distBetweenIAndJ;
                }
            }
        }
    }

    /**
     * Given nodes set their individual demanands
     * @param demands
     */
    public void setDemands(String[] demands){
        for(String i: demands){
            String[] demand = i.strip().split(" ");
            int nodeNumber = Integer.parseInt(demand[0]);
            int demandQuantity = Integer.parseInt(demand[1]);
            nodes[nodeNumber - 1].setDemand(demandQuantity);
        }
    }

    /**
     * Getters and setters
     */
    public int getDemand(int nodeNumber){
        return nodes[nodeNumber].getDemand();
    }

    public int getQuantityToDeliver(int nodeNumber){
        return nodes[nodeNumber].getDemand();
    }

    public int getDepot() {
        return depot;
    }

    public void setDepot(int depot) {
        this.depot = depot;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public TruckManager getTruckManager() {
        return truckManager;
    }

    public void setTruckManager(TruckManager truckManager) {
        this.truckManager = truckManager;
    }


    public HashMap<Integer, Node> getNodeRefrence() {
        return NodeRefrence;
    }

    public void setNodeRefrence(HashMap<Integer, Node> nodeRefrence) {
        NodeRefrence = nodeRefrence;
    }

    public int getNodeSize(){
        return nodes.length;
    }

    public DistanceMetric getDistanceMetric() {
        return distanceMetric;
    }

    public int getNodeDistance(int from, int to){
        return this.distanceBetweenNodes[from][to];
    }
}
