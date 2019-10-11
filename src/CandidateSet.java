import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class CandidateSet {
    private Random rand = new Random();
    private int[] nodes = null;
    private Path[] pathsInCandidate = null;
    private int nectarQuality;
    private boolean valid = true;
    private int costs;

    public CandidateSet(Random rand, int[] nodes, Path[] pathsInCandidate,
                        int costs, int nectarQuality){
        this.rand = rand;
        this.nodes = nodes;
        this.pathsInCandidate = pathsInCandidate;
        this.valid = isValid();
        this.costs = costs;
        this.nectarQuality = nectarQuality;
    }

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


    public int calculateCandidateCost(){
        int cost = 0;
        for(Path i: pathsInCandidate){
            cost += i.calculateCost();
        }
        return cost;
    }
    public int calculateCapactiyConstraint(){
        int demandRequirement = 0;
        for(Path i: pathsInCandidate){
            demandRequirement += i.calculatePathRequirements();
        }
        return demandRequirement;
    }

    public int calculateNectar(int index){
        this.costs = calculateCandidateCost();
        this.nectarQuality = this.costs +
                index * BeeColony.getMaxItterations() * calculateCapactiyConstraint();
        return this.nectarQuality;
    }

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

    private int randRange(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    public int[] shuffle(int[] array) {
        for (int i = 0; i < array.length; i++) {
            swapAt(i, randRange(i, array.length), array);
        }
        return array;
    }

    private void swapAt(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public void swap() {
        int first, second;
        do{
            first = rand.nextInt(pathsInCandidate.length);
            second = rand.nextInt(pathsInCandidate.length);
        }while (first == second);

        pathsInCandidate[first].exchange(pathsInCandidate[second]);

    }

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

    public int getNectarQuality() {
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
