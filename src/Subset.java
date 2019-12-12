//******************************************************************************
//
//  File:    Subset.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implments the functionality of dividing a given set of size n
//  into k subsets (all that are possible)
//******************************************************************************

import java.util.ArrayList;

/**
 * This file implments the functionality of dividing a given set of size n
 * into k subsets (all that are possible)
 */
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

    /**
     * Base case of the DP array where i = 0
     * @param previousButSameK previous subset in the same row
     * @param newElement New element to add
     * @param k number of subsets to create
     */
    public void topDP(Subset previousButSameK, int newElement, int k){
        ArrayList<ArrayList<ArrayList<Integer>>> subsetTocopy =
                previousButSameK.getSubsets();
        for(ArrayList<ArrayList<Integer>> subset: subsetTocopy) {
            for (int i = 0; i < k; i++) {
                ArrayList<ArrayList<Integer>> clonedSubset =  cloneSet(subset);
                clonedSubset.get(i).add(newElement);
                verifyAndAddtoSubsets(clonedSubset);
            }
        }
    }

    /**
     * Base case of the diagonal
     * @param previousButdiffrentK previous subset in the same row
     * @param newElement new element to add
     * @param k number of subsets to create
     */
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

    /**
     * To add to subsets
     * @param subset subset to add
     */
    public void verifyAndAddtoSubsets(ArrayList<ArrayList<Integer>> subset){
        for (ArrayList<Integer> path: subset){
            if (!CVRP.getTruckManager().validity(path)){
                return;
            }
        }
        this.subsets.add(subset);
    }


    /**
     * Clone the set
     * @param set set to colne
     * @return
     */
    public ArrayList<ArrayList<Integer>> cloneSet(ArrayList<ArrayList<Integer>> set) {
        ArrayList<ArrayList<Integer>> clonedSet = new ArrayList<>();
        ArrayList<Integer> inside;
        for(int i = 0; i < set.size(); i++){
            inside = new ArrayList<>(set.get(i));
            clonedSet.add(inside);
        }
        return clonedSet;
    }


    /**
     * Clone subset
     * @return colned subset
     */
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


    /**
     * GETTERS AND SETTERS
     */
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
