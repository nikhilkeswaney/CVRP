//******************************************************************************
//
//  File:    ExactAlgorithm.java
//  Author: Nikhil Haresh Keswaney
//
//  This file has the implementation of the exact algorithm
//******************************************************************************

import java.util.ArrayList;

/**
 * This class implements the exact algorithm
 */
public class ExactAlgorithm {
    private ArrayList<ArrayList<Integer>> bestRoutesForTrucks = null;
    private int minCost;

    /**
     * Constructor
     */
    public ExactAlgorithm(){
        this.minCost = Integer.MAX_VALUE;
        bestRoutesForTrucks = new ArrayList<>();
    }

    /**
     * In a given subset the function applies TSP on all paths
     * @param candidateSets Candidate set
     */
    public void FindBestRoutesForTrucks(Subset candidateSets){
        TravelingSalesman tsp = new TravelingSalesman();
        int i = 0;
        TravelingSalesmanTuple routeCost;
        ArrayList<ArrayList<Integer>> trucksPath;
        for(ArrayList<ArrayList<Integer>> candidateSet: candidateSets.getSubsets()){
            int totalCosForWholeRoute = 0;
            trucksPath = new ArrayList<>();

            // For each rout in candidate set
            for (ArrayList<Integer> eachTruckRoute: candidateSet) {

                // apply tsp
                routeCost = tsp.tspMinCost(eachTruckRoute);
                totalCosForWholeRoute += routeCost.getCost();

                // if the cost till now is more than the min cost no need to check further
                if (totalCosForWholeRoute > minCost){
                    break;
                }
                trucksPath.add(routeCost.getPath());
            }
            // If it is less then this is new candidate set
            if (totalCosForWholeRoute < minCost){
                minCost = totalCosForWholeRoute;
                bestRoutesForTrucks = trucksPath;
            }
            i++;

        }
    }

    /**
     * Call the subset finder algorithm
     * @return all the possible subsets
     */
    public Subset findCandidateSets(){
        SubsetFinder finder = new SubsetFinder();
        Subset s = finder.finder(CVRP.getNodeManager().getNodeSize() - 1, CVRP.getTruckManager().getNumberOfTrucks());
        return s;
    }

    /**
     * To start the exact algorithm
     */
    public void start(){
        Subset candidateSets = findCandidateSets();
        FindBestRoutesForTrucks(candidateSets);
    }

    /**
     * GETTERS AND SETTERS
     */
    public ArrayList<ArrayList<Integer>> getBestRoutesForTrucks() {
        return bestRoutesForTrucks;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setBestRoutesForTrucks(ArrayList<ArrayList<Integer>> bestRoutesForTrucks) {
        this.bestRoutesForTrucks = bestRoutesForTrucks;
    }

    public static ExactAlgorithm getInstance(){
        return new ExactAlgorithm();
    }

    public void printBestRoutes() {
        System.out.println("Min Cost = " + getMinCost());
        for(int i = 0; i < getBestRoutesForTrucks().size(); i++){
            System.out.println("Truck " + (i + 1) + ":" + getBestRoutesForTrucks().get(i));
        }
    }
}
