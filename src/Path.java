import java.util.ArrayList;
import java.util.Random;

public class Path {
    private ArrayList<Integer> path;
    private boolean valid;
    private int cost = 0;
    private int demand = 0;
    private NodeManager nodeManager = null;
    private TruckManager truckManager = null;


    public Path(){
        this.truckManager = CVRP.getTruckManager();
        this.nodeManager = CVRP.getNodeManager();
    }

    public Path(ArrayList<Integer> path){
        truckManager = CVRP.getTruckManager();
        nodeManager = CVRP.getNodeManager();
        this.path = path;
        recalculateTheCostsAndDemands();
    }

    public void recalculateTheCostsAndDemands(){
        this.demand = calculateDemand();
        this.cost = calculateCost();
        setValid(this.demand <= this.truckManager.getCapacity());
    }

    public int calculateCost() {
        int costTemp = 0;
        int from = 0;
        for (int i = 0; i < path.size(); i++){
            costTemp += nodeManager.getNodeDistance(from, path.get(i));
            from = path.get(i);
        }
        costTemp += nodeManager.getNodeDistance(path.get(path.size() - 1), 0);
        return costTemp;
    }

    public int calculateDemand(){
        int sum = 0;
        for (int i: path){
            sum += nodeManager.getDemand(i);
        }
        return sum;
    }
    public int calculatePathRequirements(){

        return calculateDemand() - truckManager.getCapacity();
    }

    public void exchange(Path path) {
        Random rand = new Random();

        int first = rand.nextInt(getPath().size());
        int second = rand.nextInt(path.getPath().size());

        int temp = getPath().get(first);
        getPath().set(first, path.getPath().get(second));
        path.getPath().set(second, temp);

        this.recalculateTheCostsAndDemands();
        path.recalculateTheCostsAndDemands();
    }

    public Path deepCopy(){
        Path copy = new Path();
        copy.setPath(new ArrayList<>(getPath()));
        copy.setDemand(getDemand());
        copy.setValid(isValid());
        copy.setCost(getCost());
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("0").append(" ");
        for (int i: path){
            sb.append(i).append(" ");
        }
        sb.append("0");
        return sb.toString();
    }


    /*
     * GETTERS AND SETTERS
     */
    public boolean isValid() {
        int cap = 0;
        for (int i: path){
            cap += CVRP.getNodeManager().getDemand(i);
        }
        return cap <= CVRP.getTruckManager().getCapacity();
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }
}
