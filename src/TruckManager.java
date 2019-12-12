//******************************************************************************
//
//  File:    EmployeedBees.java
//  Author: Nikhil Haresh Keswaney
//
//  This file is the truck manager that manages all the trucks
//******************************************************************************
import java.util.ArrayList;

/**
 * This class manages all the trucks
 */
public class TruckManager {
    private Truck[] trucks;
    private int numberOfTrucks = 0;
    private NodeManager nodeManager= null;
    private int capacity = 0;
    public TruckManager(int numberOfTrucks){
        this.numberOfTrucks = numberOfTrucks;
        trucks = new Truck[numberOfTrucks];
    }

    /**
     * Inititalize the value of all trucks
     * @param capacity capacity to initialize with
     */
    public void initializeEachTruck(int capacity){
        for(int i = 0; i < getNumberOfTrucks(); i++){
            trucks[i] = new Truck(capacity);
        }
    }

    /**
     * Check if the given path satisfies the truck constraint
     * @param singlePath path to dilever to
     * @return yes or no
     */
    public boolean validity(ArrayList<Integer> singlePath){
        int sumWeight = 0;
        for(int i: singlePath){
            sumWeight += nodeManager.getQuantityToDeliver(i);
            if (sumWeight > capacity)
                return false;
        }
        return true;
    }


    /**
     * GETTERS AND SETTERS
     */
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

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public int getCapacity(){
        return this.capacity;
    }
}
