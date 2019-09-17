import java.util.HashMap;

public class NodeManager {

    Node[] nodes = null;
    private int size = 0;
    private TruckManager truckManager = null;
    int[][] distanceBetweenNodes = null;
    private int depot = 0;
    private DistanceMetric distanceMetric = null;
    private HashMap<Integer, Node> NodeRefrence = null;
    public NodeManager(DistanceMetric distanceMetric, int size){
        this.distanceMetric = distanceMetric;
        this.size = size;
        nodes = new Node[size];
        this.NodeRefrence = new HashMap<Integer, Node>();
    }

    public DistanceMetric getDistanceMetric() {
        return distanceMetric;
    }

    public void createNodes(String[] cordinates){
        for(String i: cordinates){
            String[] coord = i.strip().split(" ");
            int nodeNumber = Integer.parseInt(coord[0]);
            int nodeX = Integer.parseInt(coord[1]);
            int nodeY = Integer.parseInt(coord[2]);
            nodes[nodeNumber - 1] = new Node(nodeX, nodeY);
        }
    }

    public void setDemands(String[] demands){
        for(String i: demands){
            String[] demand = i.strip().split(" ");
            int nodeNumber = Integer.parseInt(demand[0]);
            int demandQuantity = Integer.parseInt(demand[1]);
            nodes[nodeNumber - 1].setDemand(demandQuantity);
        }
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
}
