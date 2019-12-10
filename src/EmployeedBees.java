import edu.rit.pj2.Vbl;

import java.lang.reflect.Array;
import java.util.Arrays;

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

    public EmployeedBees(ScoutBee scoutBee, int ID){
        this.foodSource = scoutBee.getFoodSource();
        this.bestNectar = foodSource.calculateNectar(BeeColony.currentIndex());
        this.bestCost = foodSource.getCosts();
        this.ID = ID;
    }


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

    public double getBestNectar() {
        return bestNectar;
    }

    public void setBestNectar(int bestNectar) {
        this.bestNectar = bestNectar;
    }

    public int getID() {
        return ID;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
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
        }
    }


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


    public double[] getRange(){
        return this.range;
    }

    public int getBestCost() {
        return bestCost;
    }
    public void setBestCost(int bestCost) {
        this.bestCost = bestCost;
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

    public boolean inRange(int number){
        return getStart() >= number && number < getEnd();
    }

    public void setRoulleteSize(int roulleteSize) {
        this.roulleteSize = (double) 1 / getBestCost() * roulleteSize;
    }

    public double getRoulleteSize() {
        return roulleteSize;
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


    public CandidateSet getFoodSource() {
        return foodSource;
    }

    public void setFoodSource(CandidateSet foodSource) {
        this.foodSource = foodSource;
    }
}
