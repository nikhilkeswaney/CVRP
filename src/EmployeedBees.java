//******************************************************************************
//
//  File:    EmployeedBees.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implements all the functionalities of the employeed bees
//******************************************************************************
import java.util.Arrays;

/**
 * This class has all the functionalities of the employeed bees
 */
public class EmployeedBees{
    private int ID;
    private CandidateSet foodSource;
    private double bestNectar;
    private boolean exhausted = false;
    private int trial = 0;
    private int bestCost;
    private double roulleteSize;
    public static int TRIAL_MAX = 30;
    private double[] range = new double[2];
    boolean update = false;

    public EmployeedBees(){}

    /**
     * This is a constructor for the employeed bee
     * @param scoutBee scout bee for initial seed solution
     * @param ID ID of the employeed bee
     */
    public EmployeedBees(ScoutBee scoutBee, int ID){
        this.foodSource = scoutBee.getFoodSource();
        this.bestNectar = foodSource.calculateNectar(BeeColony.currentIndex());
        this.bestCost = foodSource.getCosts();
        this.ID = ID;
    }


    /**
     * This function is used to find a good neighbour for the employyed bee
     */
    public void findGoodNeighbour() {
        CandidateSet newFoodSource;
        for(int i = 0; i < 6; i++) {
            newFoodSource = foodSource.findNeighbour(foodSource);

            if(newFoodSource.calculateNectar(BeeColony.currentIndex()) < foodSource.calculateNectar(BeeColony.currentIndex())){
                this.foodSource = newFoodSource;
                this.bestNectar = newFoodSource.calculateNectar(BeeColony.currentIndex());
                this.bestCost = newFoodSource.getCosts();
                trial = 0;
            }
            else
                this.trial++;

            if(this.trial >= TRIAL_MAX)
                setExhausted(true);

        }
        bestCost = foodSource.getCosts();

    }


    /**
     * To reduce multiple bees
     * @param copy copy of the bee to reduce to
     */
    public void reduce(EmployeedBees copy){
        if(copy.bestCost < this.bestCost) {
            update = true;
            this.foodSource = copy.foodSource;
            this.bestNectar = copy.bestNectar;
            this.exhausted = copy.exhausted;
            this.trial = copy.trial;
            this.bestCost = copy.bestCost;
            this.roulleteSize = copy.roulleteSize;
            this.range = Arrays.copyOf(copy.range, copy.range.length);
        }

        if (!update){
            this.trial += copy.trial;
        }
    }


    /**
     * Is the given number in the range of the roullete wheel size that the bee
     * contributes to
     * @param number number to check
     * @return yes or no
     */
    public boolean inRange(int number){
        return getStart() >= number && number < getEnd();
    }

    /**
     * This function sets the size the employeed bee will contrubute on the
     * roullete wheel
     * @param roulleteSize
     */
    public void setRoulleteSize(int roulleteSize) {
        this.roulleteSize = (double) 1 / getBestCost() * roulleteSize;
    }


    /**
     * GETTERS AND SETTERS
     */
    public double getRoulleteSize() {
        return roulleteSize;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    public int getBestCost() {
        return bestCost;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public double getStart(){
        return range[0];
    }

    public double getEnd(){
        return range[1];
    }

    public EmployeedBees deepCopy(){
        EmployeedBees copy = new EmployeedBees();
        copy.ID = this.ID;
        copy.foodSource = this.foodSource.deepCopy();
        copy.bestNectar = this.bestNectar;
        copy.exhausted = this.exhausted;
        copy.trial = this.trial;
        copy.bestCost = this.bestCost;
        copy.roulleteSize = this.roulleteSize;
        copy.range = Arrays.copyOf(range, range.length);

        return copy;
    }

    public int getID() {
        return ID;
    }

    public double makeRange(double start, double totalCost) {
        double end = start + getRoulleteSize() / (double) totalCost * 360;
        setRange(start, end);
        return end;
    }

    public void setRange(double start, double end) {
        range[0] = start;
        range[1] = end;

    }

    public void printTrucksandCost() {
        int j = 1;
        System.out.println("Cost: " + bestCost);
        for (Path i: foodSource.getPathsInCandidate()){
            System.out.println("Truck " + j + ": " + i.toString());
            j++;
        }
    }
}
