import java.util.LinkedList;
import java.util.Queue;

public class ScoutBee {
    private Queue<CandidateSet> possibleFoodSourses;
    private int queueSize = 0;
    private int[] nodes;

    public ScoutBee(int queueSize){
        this.possibleFoodSourses = new LinkedList<>();
        this.queueSize = queueSize;
        initializeNode();
        while (possibleFoodSourses.size() < queueSize){
            flyAndFindFoodSource();
        }
    }

    private synchronized void flyAndFindFoodSource(){
        CandidateSet c = new CandidateSet(nodes);
        possibleFoodSourses.add(c);
    }

    private void initializeNode() {
        NodeManager nodeManager = CVRP.getNodeManager();
        nodes = new int[nodeManager.getNodeSize() - 1];
        for(int i = 1; i < nodeManager.getNodeSize(); i++){
            nodes[i - 1] = i;
        }
    }
    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public CandidateSet getFoodSource(){
        return possibleFoodSourses.poll();
    }
}
