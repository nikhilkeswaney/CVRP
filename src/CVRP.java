//******************************************************************************
//
//  File:    CVRP.java
//  Author: Nikhil Haresh Keswaney
//
//  In this file the code execution begins in which you can choose multiple ways
//  to run the program. which are as follows
//  1. Exact algorithm: This uses the subset finder and traveling salesman to
//     solve the CVRP problem
//  2. Approximate algorithm: This uses the Bee colony algorithm to solve the
//     CVRP problem
//  3. Parallel Bee colony: this uses a parallel architecture to solve the given
//     problem
//
//  Note: This file has a dependency of PJ2 library to run.
//******************************************************************************
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.IntVbl;

import java.io.IOException;
import java.util.Random;

/**
 * This is the main class for the CVRP.
 */
public class CVRP extends Task {

    private static NodeManager nodeManager = null;
    private static TruckManager truckManager = null;
    private static int optimalValue = 0;

    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 1500, INDEX = 1;
    private int swarmSize;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;
    public void main(String[] args) throws Exception {
        if(args.length < 2) {
            usage();
            return;
        }

        try {
            swarmSize = Integer.parseInt(args[2]);
        }
        catch (Exception e){
            swarmSize = 1000;
        }

        String dataSet = args[0];


        String algorithm = args[1];
        if(!algorithm.equals("Exact")
                &&  !algorithm.equals("ApproximateSequential")
                && !algorithm.equals("ApproximateParallel")){
            usage();
            return;
        }

        try {
            FileReader f = new FileReader(dataSet);
            f.readFile();
        }
        catch (Exception e){
            usage();
            return;
        }

        try {
            swarmSize = Integer.parseInt(args[2]);
        }
        catch (Exception e){
            swarmSize = 1000;
        }


        if (algorithm.equals("Exact")) {
            ExactAlgorithm ea = new ExactAlgorithm();
            ea.start();
            ea.printBestRoutes();
        }

        else if(algorithm.equals("ApproximateSequential")) {
            BeeColony beeColony = new BeeColony();
            beeColony.setSwarmSize(swarmSize);
            beeColony.startCollectingFood();
            beeColony.printAns();
        }

        else {
            employeedBees = new EmployeedBees[swarmSize / 2];
            onlookerBees = new OnlookerBee[swarmSize / 2];
            scoutBee = new ScoutBee(swarmSize / 2);


            parallelFor(0, (int) (swarmSize / 2) - 1).exec(new Loop() {
                @Override
                public void run(int i) throws Exception {
                    employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
                }
            });


            EmployeedBees.TRIAL_MAX = (int) (0.5 * swarmSize * CVRP.getTruckManager().getNumberOfTrucks());

            while (currentIndex() < MAX_ITTERATIONS){
                IntVbl totalMaxCost = new IntVbl.Sum(0);

                // Employeed bee phase
                parallelFor(0, (int) (swarmSize/2) - 1).exec(new Loop() {
                    IntVbl localTotalMaxCost;

                    @Override
                    public void start() throws Exception {
                        localTotalMaxCost = threadLocal(totalMaxCost);
                    }

                    @Override
                    public void run(int i) throws Exception {

                        employeedBees[i].findGoodNeighbour();
                        int bestValue = employeedBees[i].getBestCost();
                        localTotalMaxCost.item += bestValue;
                    }
                });

                createRoulleteWheel(totalMaxCost.item);

                Random rand = new Random();

                // onlooker bee phase
                parallelFor(0, (int)(swarmSize / 2) - 1).exec(new Loop() {
                    @Override
                    public void run(int i) throws Exception {
                        onlookerBees[i] = new OnlookerBee(employeedBees, rand);
                        onlookerBees[i].sendAllOnlookerBees();
                    }
                });

                for (OnlookerBee onlookerBee: onlookerBees){
                    onlookerBee.reduceCopy();
                }

                scoutBee.checkAdRefill();

                for (int i = 0; i < (int) swarmSize / 2; i++) {
                    employeedBees[i].update = false;
                    if(employeedBees[i].getBestCost() < bestAns){
                        bestAns = employeedBees[i].getBestCost();
                        bestSet = employeedBees[i];
                    }
                }


                parallelFor(0, (int)(swarmSize / 2) - 1).exec(new Loop() {

                    @Override
                    public void run(int i) throws Exception {
                        if (employeedBees[i].isExhausted()) {
                            employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
                        }
                    }
                });

                incrementIndex();
            }
            bestSet.printTrucksandCost();
        }
    }

    public static TruckManager getTruckManager() {
        return truckManager;
    }


    public static void setTruckManager(TruckManager truckManager) {
        CVRP.truckManager = truckManager;
    }

    public static NodeManager getNodeManager() {
        return nodeManager;
    }

    public static void setNodeManager(NodeManager nodeManager) {
        CVRP.nodeManager = nodeManager;
    }

    public static int getOptimalValue() {
        return optimalValue;
    }

    public static void setOptimalValue(int optimalValue) {
        CVRP.optimalValue = optimalValue;
    }

    private static void usage() {
        System.out.println("Usage: java CVRP <dataset>");
        System.out.println("1. <dataset> The dataset you want to use");
    }

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
    public static int currentIndex() {
        return INDEX;
    }

    public static void incrementIndex() {
        INDEX++;
    }
    public int bestCost(){
        return bestSet.getBestCost();
    }

    public void printAns(){
        bestSet.printTrucksandCost();
    }

    public static synchronized int getMaxItterations() {
        return MAX_ITTERATIONS;
    }


    protected static int coresRequired()
    {
        return 1;
    }
}
