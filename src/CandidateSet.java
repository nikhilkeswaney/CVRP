//******************************************************************************
//
//  File:    CandidateSet.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implements the candidate set for the Artificial bee colony
//  algorithm
//******************************************************************************
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class CandidateSet {
    private Random rand = new Random();
    private int[] nodes = null;
    private Path[] pathsInCandidate = null;
    private double nectarQuality;
    private boolean valid = true;
    private int costs;

    /**
     * Constructor for object initialization
     * @param rand Random number generator
     * @param nodes nodes that are ther in candidate set
     * @param pathsInCandidate paths in candidate
     * @param costs cost of the candidate set
     * @param nectarQuality nectar quality of candidate set
     */
    public CandidateSet(Random rand, int[] nodes, Path[] pathsInCandidate,
                        int costs, double nectarQuality){
        this.rand = rand;
        this.nodes = nodes;
        this.pathsInCandidate = pathsInCandidate;
        this.valid = isValid();
        this.costs = costs;
        this.nectarQuality = nectarQuality;
    }

    /**
     * Constructor for object initialization
     * @param nodes nodes that are ther in candidate set
     */
    public CandidateSet(int[] nodes) {
        TruckManager truckManager = CVRP.getTruckManager();
        NodeManager nodeManager = CVRP.getNodeManager();
        this.pathsInCandidate = new Path[truckManager.getNumberOfTrucks()];
        boolean feasible = false;
        while(!feasible) {
            this.nodes = shuffle(nodes);
            feasible = sepearteToTrucks(truckManager, nodeManager);
        }
        this.costs = calculateCandidateCost();
        valid = isValid();
    }

    /**
     * This function is used to calculate the candidate cost
     * @return total cost of the candidate set
     */
    public int calculateCandidateCost(){
        int cost = 0;
        for(Path i: pathsInCandidate){
            cost += i.calculateCost();
        }
        return cost;
    }

    /**
     * Calculate the constraint of the truck
     * @return total items in candidate set
     */
    public int calculateCapactiyConstraint(){
        int demandRequirement = 0;
        for(Path i: pathsInCandidate){
            demandRequirement += i.calculatePathRequirements();
        }
        return demandRequirement;
    }

    /**
     * This function is the fitness function of the algorithm
     *
     * @param index iteration index of the algorithm
     * @return fitness value of the solution
     */
    public double calculateNectar(int index){
        this.costs = calculateCandidateCost();
        return this.costs + BeeColony.currentIndex() * BeeColony.getMaxItterations() * calculateCapactiyConstraint();
    }

    /**
     * Given a set of nodes try and divvide them into trucks
     * @param truckManager truck manager object
     * @param nodeManager node manager objecy
     * @return are these nodes feasible?
     */
    private boolean sepearteToTrucks(TruckManager truckManager, NodeManager nodeManager) {
        ArrayList<Integer> path = new ArrayList<>();
        int cost = 0, j = 0;
        for(int i: nodes){
            if(cost + nodeManager.getDemand(i) > truckManager.getCapacity()) {
                this.pathsInCandidate[j] = new Path(path);
                path = new ArrayList<Integer>();
                path.add(i);
                cost = nodeManager.getDemand(i);
                j++;
                if(j >= truckManager.getNumberOfTrucks())
                    return false;
            }
            else {
                path.add(i);
                cost += nodeManager.getDemand(i);
            }
        }
        this.pathsInCandidate[j] = new Path(path);
        return true;
    }

    /**
     * given a range give random number between them
     */
    private int randRange(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    /**
     * Shuffle the given array
     */
    public int[] shuffle(int[] array) {
        int[] copyArray = Arrays.copyOf(array, array.length);
        for (int i = 0; i < copyArray.length; i++) {
            swapAt(i, randRange(i, copyArray.length), array);
        }
        return copyArray;
    }

    /**
     * Swap elements in the array at the given indices
     */
    private void swapAt(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * swap members from diffrent trucks
     */
    public void swap() {
        int first, second;
        do{
            first = rand.nextInt(pathsInCandidate.length);
            second = rand.nextInt(pathsInCandidate.length);
        }while (first == second);

        pathsInCandidate[first].exchange(pathsInCandidate[second]);

    }

    /**
     * This operator is used to generate a neighbour of a given solution
     */
    public void BMX() {

        int best = Integer.MAX_VALUE;
        Path bestPath = null;
        for (Path i: pathsInCandidate){
            if(i.getCost() < best){
                bestPath = i;
                best = i.getCost();
            }
        }

        assert bestPath != null;
        createPathWithBMX(bestPath);

    }

    /**
     * With the best path given create a neighbour
     * @param bestPath best path in the node
     */
    public void createPathWithBMX(Path bestPath){
        NodeManager nodeManager = CVRP.getNodeManager();
        TruckManager truckManager = CVRP.getTruckManager();
        HashSet<Integer> setOfPath = new HashSet<>(bestPath.getPath());
        ArrayList<Integer> path = new ArrayList<>();
        int cost = 0, j = 1;
        this.pathsInCandidate[0] = bestPath;
        for(int i: nodes){
            if(!setOfPath.contains(i)) {
                if (
                        (cost + nodeManager.getDemand(i) > truckManager.getCapacity()) &&
                        (j != this.pathsInCandidate.length - 1)
                ) {
                    this.pathsInCandidate[j] = new Path(path);
                    path = new ArrayList<Integer>();
                    path.add(i);
                    cost = nodeManager.getDemand(i);
                    j++;
                } else {
                    path.add(i);
                    cost += nodeManager.getDemand(i);
                }
            }
        }
        this.pathsInCandidate[j] = new Path(path);
    }

    /**
     * Create a deep copy of the candidate set
     * @return
     */
    public CandidateSet deepCopy(){
        Path[] pathCopy = new Path[pathsInCandidate.length];
        for(int i = 0; i < pathsInCandidate.length; i++){
            Path pTemp = pathsInCandidate[i].deepCopy();
            pathCopy[i] = pTemp;
        }
        return new CandidateSet(rand, Arrays.copyOf(nodes, nodes.length), pathCopy, costs, nectarQuality);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Path i: pathsInCandidate){
            if(i == null)
                sb.append("[null]");
            else
                sb.append("[").append(i.toString()).append("]");
        }
        return sb.toString();
    }




    /**
     *  GETTERS AND SETTERS
     */
    public Path[] getPathsInCandidate() {
        return pathsInCandidate;
    }

    public void setPathsInCandidate(Path[] pathsInCandidate) {
        this.pathsInCandidate = pathsInCandidate;
    }

    public int[] getNodes() {
        return nodes;
    }

    public void setNodes(int[] nodes) {
        this.nodes = nodes;
    }

    public double getNectarQuality() {
        return nectarQuality;
    }

    public void setNectarQuality(int nectarQuality) {
        this.nectarQuality = nectarQuality;
    }

    public boolean isValid() {
        boolean validity = true;
        for(Path i: pathsInCandidate){
            validity = valid && i.isValid();
        }
        return validity;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public CandidateSet findNeighbour(CandidateSet foodSource) {
        CandidateSet newFoodSource = foodSource.deepCopy();
        if (rand.nextBoolean()) {
            newFoodSource.swap();
        }
        else {
            newFoodSource.BMX();
        }
        newFoodSource.calculateNectar(BeeColony.currentIndex());
        return newFoodSource;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }
}
