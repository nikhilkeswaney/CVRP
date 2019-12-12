import java.util.ArrayList;

/**
 * This class implemnets the tuple for travelling salesman
 */
class TravelingSalesmanTuple {
    private int cost;
    ArrayList<Integer> path = null;

    public TravelingSalesmanTuple(int cost){
        this.cost = cost;
        path = new ArrayList<>();
    }

    /**
     * Add node to the path
     * @param node node to add to the path
     */
    public void addToPath(int node){
        path.add(node);
    }


    /**
     * GETTERS AND SETTERS
     */
    public int getCost(){
        return this.cost;
    }

    public TravelingSalesmanTuple lessThanEqualTo(TravelingSalesmanTuple other){
        if(other.cost > this.cost){
            return this;
        }
        else {
            return other;
        }
    }

    public ArrayList<Integer> getPath(){
        ArrayList<Integer> ans = new ArrayList<>();
        for(int i = path.size() - 1; i >= 0; i--){
            ans.add(path.get(i));
        }
        return ans;
    }
}