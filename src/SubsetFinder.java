import java.util.ArrayList;

/**
 * This class implements the logic of actually finding the subset
 */
public class SubsetFinder {


    /**
     * This method implements the recurence relation of
     * S(n, i) = K x S(n - 1, i) + S(n - 1, i - 1)
     * @param n number of nodes
     * @param k number of subsets to divide into
     * @return all subsets of n after dividing them into k subsets
     */
    public Subset finder(int n, int k){

        ArrayList<Subset> current = new ArrayList<Subset>();
        current.add(new Subset(0));
        ArrayList<Subset> previous = null;
        for (int i = 1; i <= n; i++){
            previous = current;
            current = new ArrayList<Subset>();
            for (int j = 0; j <= i && j <= k; j++){
                if(j == 0){
                    current.add(new Subset(j));
                }
                else if(i == 1){
                    current.add(new Subset(j));
                    current.get(current.size() - 1).oneSubset(i);
                }
                else if(j == i){
                    current.add(new Subset(j));
                    current.get(current.size() - 1).sameRowColumn();
                }
                else {
                    current.add(new Subset(j));
                    current.get(current.size() - 1).topDP(previous.get(j), i, j);
                    current.get(current.size() - 1).crossedDP(previous.get(j - 1), i, j);
                }
            }
        }
        return current.get(current.size() - 1);
    }
}
