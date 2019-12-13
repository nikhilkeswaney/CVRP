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
    private int swarmSize;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;

    /**
     * Main Function
     */
    public void main(String[] args) throws Exception {
        if(args.length < 2) {
            usage();
            return;
        }

        String dataSet = args[0];
        String algorithm = args[1];
        try {
            swarmSize = Integer.parseInt(args[2]);
        }
        catch (Exception e){
            swarmSize = 1000;

        }

        if(!algorithm.equals("Exact")
            &&  !algorithm.equals("ApproximateSequential")
            && !algorithm.equals("ApproximateParallel")
        ){
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

            while (BeeColony.currentIndex() < BeeColony.getMaxItterations()){
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

                BeeColony.incrementIndex();
            }
            bestSet.printTrucksandCost();
        }
    }

    /**
     * This function gives the Truck Manager to other classes
     * @return truck manager
     */
    public static TruckManager getTruckManager() {
        return truckManager;
    }

    /**
     * This function gives the Truck Manager to other classes
     * @return truck manager
     */
    public static void setTruckManager(TruckManager truckManager) {
        CVRP.truckManager = truckManager;
    }

    /**
     * This function provides the node manager to all the classes
     * @return node manager
     */
    public static NodeManager getNodeManager() {
        return nodeManager;
    }

    /**
     * This function is used to set the node manager
     * @param nodeManager
     */
    public static void setNodeManager(NodeManager nodeManager) {
        CVRP.nodeManager = nodeManager;
    }


    /**
     * This function is used to set the optimal value
     * @param optimalValue
     */
    public static void setOptimalValue(int optimalValue) {
        CVRP.optimalValue = optimalValue;
    }

    /**
     * This function is used to create a roullete wheel for the parallel algorithm
     * @param totalCost total cost for all thrucks
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


    private static void usage() {
        System.out.println("Usage: java pj2 cores=[Optional: number_of_cores] CVRP <dataset> <algorithm> <Optional: swarm_size>");
        System.out.println("1. <dataset> The dataset you want to use");
        System.out.println("2. <algorithm> Exact | ApproximateSequential | ApproximateParallel");
        System.out.println("3. <Optional: swarm_size> swarm size(Default: 1000)");
    }

    protected static int coresRequired()
    {
        return 1;
    }
}
