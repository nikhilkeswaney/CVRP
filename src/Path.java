import java.util.ArrayList;

public class Path {
    private ArrayList<Integer> path;
    private boolean valid;
    private int cost = 0;
    private int demand = 0;
    private NodeManager nodeManager = null;
    private TruckManager truckManager = null;
    public Path(ArrayList<Integer> path){
        truckManager = CVRP.getTruckManager();
        nodeManager = CVRP.getNodeManager();
        this.path = path;
        calculateDemand();
        calculateCost();
    }

    private void calculateCost() {

        int from = 0;
        for (int i = 0; i < path.size(); i++){
            this.cost += nodeManager.getNodeDistance(from, path.get(i));
            from = path.get(i);
        }
        this.cost += nodeManager.getNodeDistance(path.get(path.size() - 1), 0);
    }

    private void calculateDemand() {
        for (int i: path){
            this.demand = nodeManager.getDemand(i);
        }
        setValid(this.demand <= this.truckManager.getCapacity());
    }


    public boolean isValid() {
        return valid;
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

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
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
}
