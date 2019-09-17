import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Subset {
    private int sizeOfEachSubset = 0;
    private int capacity = 35;
    private ArrayList<ArrayList<ArrayList<Integer>>> subsets = null;
    public Subset(int sizeOfEachSubset){
        this.sizeOfEachSubset = sizeOfEachSubset;
        subsets = new ArrayList<>();
    }

    public void oneSubset(int size){
        ArrayList<ArrayList<Integer>> subset = new ArrayList<>();
        subset.add(new ArrayList<Integer>());
        for (int i = 1; i <= size; i++){
            subset.get(getSizeOfEachSubset() - 1).add(i);
        }
        verifyAndAddtoSubsets(subset);
    }

    public void sameRowColumn() {
        ArrayList<ArrayList<Integer>> subset = new ArrayList<>();
        for(int i = 0; i < getSizeOfEachSubset(); i++){
            subset.add(new ArrayList<Integer>());
            subset.get(i).add(i + 1);
        }
        verifyAndAddtoSubsets(subset);
    }

    public void topDP(Subset previousButSameK, int newElement, int k){
        ArrayList<ArrayList<ArrayList<Integer>>> subsetTocopy =
                previousButSameK.getSubsets();
        for(ArrayList<ArrayList<Integer>> subset: subsetTocopy) {
            for (int i = 0; i < k; i++) {
                ArrayList<ArrayList<Integer>> clonedSubset =  cloneSet(subset);
//                subset.get(i).add(newElement);
//                boolean validity = verifyPath(subset.get(i));
//                subset.get(i).remove(subset.get(i).size() - 1);
//                if(validity){
//                    ArrayList<ArrayList<Integer>> clonedSubset = cloneSet(subset.get(i));
//                }
//                else {
//                    break;
//                }
                clonedSubset.get(i).add(newElement);
                verifyAndAddtoSubsets(clonedSubset);
            }
        }
    }
    public void crossedDP(Subset previousButdiffrentK, int newElement, int k){
        ArrayList<ArrayList<ArrayList<Integer>>> subsetTocopy =
                previousButdiffrentK.getSubsets();

        for(ArrayList<ArrayList<Integer>> subset: subsetTocopy) {
            ArrayList<ArrayList<Integer>> clonedSubset = cloneSet(subset);
            ArrayList<Integer> singleElement = new ArrayList<>();
            singleElement.add(newElement);
            clonedSubset.add(singleElement);
            verifyAndAddtoSubsets(clonedSubset);
        }
    }
    public void verifyAndAddtoSubsets(ArrayList<ArrayList<Integer>> subset){
        for (ArrayList<Integer> path: subset){
            if (!CVRP.getTruckManager().validity(path)){
                return;
            }
        }
        this.subsets.add(subset);
    }



    public ArrayList<ArrayList<Integer>> cloneSet(ArrayList<ArrayList<Integer>> set) {
        ArrayList<ArrayList<Integer>> clonedSet = new ArrayList<>();
        ArrayList<Integer> inside;
        for(int i = 0; i < set.size(); i++){
            inside = new ArrayList<>(set.get(i));
            clonedSet.add(inside);
        }
        return clonedSet;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> clone(){

        ArrayList<ArrayList<ArrayList<Integer>>> cloneArray = new ArrayList<>();

        for (int i = 0; i < this.subsets.size(); i++) {

            cloneArray.add(new ArrayList<ArrayList<Integer>>());
            for (int j = 0; j < this.subsets.get(i).size(); j++) {

                cloneArray.get(i).add(new ArrayList<Integer>());
                for (int k = 0; k < this.subsets.get(i).get(j).size(); k++) {
                    cloneArray.get(i).get(k).add(subsets.get(i).get(j).get(k));
                }
            }
        }
        return cloneArray;
    }

    public int getSizeOfEachSubset() {
        return sizeOfEachSubset;
    }



    public ArrayList<ArrayList<ArrayList<Integer>>> getSubsets() {
        return subsets;
    }

    @Override
    public String toString() {
        return this.subsets.toString();
    }
}
