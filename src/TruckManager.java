import java.util.ArrayList;

public class TruckManager {
    private Truck[] trucks;
    private int numberOfTrucks = 0;
    private NodeManager nodeManager= null;
    private int capacity = 0;
    public TruckManager(int numberOfTrucks){
        this.numberOfTrucks = numberOfTrucks;
        trucks = new Truck[numberOfTrucks];
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void initializeEachTruck(int capacity){
        for(int i = 0; i < getNumberOfTrucks(); i++){
            trucks[i] = new Truck(capacity);
        }
    }

    public boolean validity(ArrayList<Integer> singlePath){
        int sumWeight = 0;
        for(int i: singlePath){
            sumWeight += nodeManager.getQuantityToDeliver(i);
            if (sumWeight > capacity)
                return false;
        }
        return true;
    }

    public int getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public void setNumberOfTrucks(int numberOfTrucks) {
        this.numberOfTrucks = numberOfTrucks;
    }

    public Truck[] getTrucks() {
        return trucks;
    }

    public void setTrucks(Truck[] trucks) {
        this.trucks = trucks;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }
}
