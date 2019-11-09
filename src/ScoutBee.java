import edu.rit.pj2.Loop;
import edu.rit.util.Queue;
import java.util.Arrays;

public class ScoutBee {
    private Queue<CandidateSet> possibleFoodSourses;
    private int queueSize = 0;
    private int[] nodes;

    public ScoutBee(){}

    public ScoutBee(int queueSize){
        this.possibleFoodSourses = new Queue<CandidateSet>();
        this.queueSize = queueSize;
        initializeNode();
        refillfoodSource();
    }

    public void refillfoodSource(){
        while (possibleFoodSourses.size() < queueSize){
            flyAndFindFoodSource();
        }
    }
    private void flyAndFindFoodSource(){
        CandidateSet c = new CandidateSet(nodes);
        possibleFoodSourses.push(c);
    }

    private void initializeNode() {
        NodeManager nodeManager = CVRP.getNodeManager();
        nodes = new int[nodeManager.getNodeSize() - 1];
        for(int i = 1; i < nodeManager.getNodeSize(); i++){
            nodes[i - 1] = i;
        }
    }
    public int getQueueSize() {
        return possibleFoodSourses != null ? possibleFoodSourses.size(): 0;
    }

    public int getQueueMainSize(){
        return queueSize;
    }


    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public CandidateSet getFoodSource(){
        CandidateSet returnedVal;
        if(this.possibleFoodSourses.size() == 0)
            refillfoodSource();

        while ((returnedVal = possibleFoodSourses.pop()) == null);

        return returnedVal;
    }

    public Queue<CandidateSet> getQueue(){
        return possibleFoodSourses;
    }

//    @Override
//    public void main(String[] strings) throws Exception {
//        this.possibleFoodSourses = new Queue<>();
//        this.queueSize = Integer.parseInt(strings[0]);
//        initializeNode();
//        allFlyandFindFoodSource();
//    }
//
//    public void allFlyandFindFoodSource(){
//        parallelFor(0, queueSize).exec(new Loop() {
//            @Override
//            public void run(int i) throws Exception {
//                CandidateSet c = new CandidateSet(nodes);
//                possibleFoodSourses.push(c);
//            }
//        });
//    }

    public void checkAdRefill() {
        if(getQueueSize() < queueSize){
            refillfoodSource();
        }
    }
}
