import java.util.ArrayList;

public class ExactAlgorithm {
    private ArrayList<ArrayList<Integer>> bestRoutesForTrucks = null;
    private int minCost;

    public ExactAlgorithm(){
        this.minCost = Integer.MAX_VALUE;
        bestRoutesForTrucks = new ArrayList<>();
    }

    public void FindBestRoutesForTrucks(Subset candidateSets){
        TravelingSalesman tsp = new TravelingSalesman();
        for(ArrayList<ArrayList<Integer>> candidateSet: candidateSets.getSubsets()){
            int totalCosForWholeRoute = 0;
            ArrayList<ArrayList<Integer>> trucksPath = new ArrayList<>();
            for (ArrayList<Integer> eachTruckRoute: candidateSet) {
                TravelingSalesmanTuple routeCost = tsp.tspMinCost(eachTruckRoute);
                totalCosForWholeRoute += routeCost.getCost();
                if (totalCosForWholeRoute > minCost){
                    break;
                }
                trucksPath.add(routeCost.getPath());
            }
            if (totalCosForWholeRoute < minCost){
                minCost = totalCosForWholeRoute;
                bestRoutesForTrucks = trucksPath;
            }

        }
    }

    public Subset findCandidateSets(){
        SubsetFinder finder = new SubsetFinder();
        Subset s = finder.finder(CVRP.getNodeManager().getNodeSize() - 1, CVRP.getTruckManager().getNumberOfTrucks());
        return s;
    }

    public void start(){
        Subset candidateSets = findCandidateSets();
        FindBestRoutesForTrucks(candidateSets);
    }

    public ArrayList<ArrayList<Integer>> getBestRoutesForTrucks() {
        return bestRoutesForTrucks;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setBestRoutesForTrucks(ArrayList<ArrayList<Integer>> bestRoutesForTrucks) {
        this.bestRoutesForTrucks = bestRoutesForTrucks;
    }
}
