import java.util.ArrayList;
import java.util.Random;

public class CandidateSet {
    private Random rand = new Random();
    private int[] nodes = null;
    private Path[] pathsInCandidate = null;

    public CandidateSet(int[] nodes) {
        TruckManager truckManager = CVRP.getTruckManager();
        NodeManager nodeManager = CVRP.getNodeManager();
        this.pathsInCandidate = new Path[truckManager.getNumberOfTrucks()];
        boolean feasible = false;
        while(!feasible) {
            this.nodes = shuffle(nodes);
            feasible = sepearteToTrucks(truckManager, nodeManager);
        }
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
}
