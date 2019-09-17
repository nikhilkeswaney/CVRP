import java.util.ArrayList;

class TravelingSalesmanTuple {
    private int cost;
    ArrayList<Integer> path = null;

    public TravelingSalesmanTuple(int cost){
        this.cost = cost;
        path = new ArrayList<>();
    }

    public void addToPath(int node){
        path.add(node);
    }

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