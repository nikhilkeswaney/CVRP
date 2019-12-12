//******************************************************************************
//
//  File:    CVRP.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implements the bee colony algorithm in sequential
//******************************************************************************

import java.util.Random;

/**
 * This class has all the functions required to implement the bee colony algorithm
 */
public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 1500, INDEX = 1;

    private int swarmSize = 1000;

    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;

    public BeeColony(){
        INDEX = 1;
    }

    /**
     * In this function the bee colony sequential version is implemented
     */
    public void startCollectingFood(){
        employeedBees = new EmployeedBees[swarmSize / 2];
        onlookerBees = new OnlookerBee[swarmSize / 2];

        // Scout bees go out and find initial solution
        scoutBee = new ScoutBee(swarmSize / 2);

        // In this phase employeed bees are assigned their neighbourhoods
        for (int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
        }
        EmployeedBees.TRIAL_MAX = (int) (0.5 * swarmSize * CVRP.getTruckManager().getNumberOfTrucks());
        while (currentIndex() < MAX_ITTERATIONS){

            int totalCost = 0;

            // Find the nearest neighbour for that employeed bee
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                employeedBees[i].findGoodNeighbour();
                totalCost += employeedBees[i].getBestCost();
            }

            // Create roullete wheel
            createRoulleteWheel(totalCost);
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                onlookerBees[i] = new OnlookerBee(employeedBees, new Random());

                onlookerBees[i].sendOnlookerBees();

            }

            scoutBee.checkAdRefill();

            // Find best answer
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                if(employeedBees[i].getBestCost() < bestAns){
                    bestAns = employeedBees[i].getBestCost();
                    bestSet = employeedBees[i];
                }
            }

            // If any solution is exhausted replace it with new solution
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                if (employeedBees[i].isExhausted()) {
                    employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
                }
            }
            incrementIndex();
        }
    }
    public EmployeedBees[] getEmployeedBees() {
        return employeedBees;
    }

    /**
     * Create the roullete wheel for selection
     * @param totalCost total cost for all trucks
     */
    public void createRoulleteWheel(int totalCost){
        double totalMaxCost = 0;

        for(int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i].setRoulleteSize(totalCost);
            totalMaxCost += employeedBees[i].getRoulleteSize();
        }

        double start  = 0.0f;

        for(int i = 0; i < (int) swarmSize / 2; i++) {
            start = employeedBees[i].makeRange(start, totalMaxCost);
        }
    }

    /**
     * this function provides current index of the iteration
     * @return current index
     */
    public static int currentIndex() {
        return INDEX;
    }

    /**
     * This function is used to increase the index of iterations
     */
    public static void incrementIndex() {
        INDEX++;
    }


    public void printAns(){
        bestSet.printTrucksandCost();
    }

    /**
     * Get maximum number of iterations
     * @return get max iterations
     */
    public static synchronized int getMaxItterations() {
        return MAX_ITTERATIONS;
    }

    /**
     * Set the swarm size;
     * @param swarmSize
     */
    public void setSwarmSize(int swarmSize) {
        this.swarmSize = swarmSize;
    }
}
