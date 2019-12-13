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
    private int swarmSize = 1000;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;

    /**
     * Main Function
     */
    public void main(String[] args) throws Exception {
        if(args.length < 1) {
            usage();
            return;
        }
        String dataSet = null;
        String algorithm = null, parallel = null;
        for (int i = 0; i < args.length; i++){
            String[] split = args[i].split("=");
            String flag, value;
            try {
                flag = split[0];
                value = split[1];
            }
            catch (Exception e){
                usage();
                return;
            }
            switch (flag){
                case "dataset":{
                    dataSet = value;
                    break;
                }
                case "algorithm":{
                    if(!value.equals("exact") && !value.equals("approximate")){
                        usage();
                        return;
                    }
                    algorithm = value;
                    break;
                }
                case "parallel":{
                    if(!value.equals("yes") && !value.equals("no")){
                        usage();
                        return;
                    }
                    parallel = value;
                    break;
                }
                case "swarm_size":{
                    int temp;
                    try {
                        temp = Integer.parseInt(value);
                    }
                    catch (Exception e){
                        usage();
                        return;
                    }
                    swarmSize = temp;
                    break;
                }
            }
        }

        FileReader f = new FileReader(dataSet);
        f.readFile();

        if(
                algorithm == null ||
                (algorithm.equals("approximate") && parallel == null) ||
                swarmSize <= 0
            ){
            usage();
            return;
        }


        if (algorithm.equals("exact")) {
            ExactAlgorithm ea = new ExactAlgorithm();
            ea.start();
            ea.printBestRoutes();
        }
        else if(algorithm.equals("approximate") && parallel.equals("no")) {
            BeeColony beeColony = new BeeColony();
            beeColony.setSwarmSize(swarmSize);
            beeColony.startCollectingFood();

            beeColony.printAns();
        }
        else {
            employeedBees = new EmployeedBees[swarmSize / 2];
            onlookerBees = new OnlookerBee[swarmSize / 2];
            scoutBee = new ScoutBee(swarmSize / 2);

            // assign neighbourhood solution to all employeed bees
            parallelFor(0, (int) (swarmSize / 2) - 1).exec(new Loop() {
                @Override
                public void run(int i) throws Exception {
                    employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
                }
            });


            EmployeedBees.TRIAL_MAX = (int) (0.5 * swarmSize * CVRP.getTruckManager().getNumberOfTrucks());


            while (BeeColony.currentIndex() < BeeColony.getMaxItterations()){
                IntVbl totalMaxCost = new IntVbl.Sum(0);

                // Employeed bee phase where they go out and find better solutions
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

                // create roulete wheel for onlooker bees
                createRoulleteWheel(totalMaxCost.item);

                // using roullete wheel select the neighbourhood
                for (int i = 0; i < (int) swarmSize / 2; i++) {
                    onlookerBees[i] = new OnlookerBee(employeedBees, new Random());

                    onlookerBees[i].sendOnlookerBees();

                }

                scoutBee.checkAdRefill();

                for (int i = 0; i < (int) swarmSize / 2; i++) {
                    employeedBees[i].update = false;
                    if(employeedBees[i].getBestCost() < bestAns){
                        bestAns = employeedBees[i].getBestCost();
                        bestSet = employeedBees[i];
                    }
                }

                // If some answer is exhausted use scout bee to reffil
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

//    /**
//     * Function that implements the bee colony in parallel (I wanted to
//     * implement it in another class but PJ2 acts wierd if we implement parallel
//     * function in another class that is not main class)
//     */
//    public void beeColonyParallel(){
//
//    }

    /**
     * This function gives the Truck Manager to other classes
     * @return truck manager
     */
    public static TruckManager getTruckManager() {
        return truckManager;
    }

    /**
     * This function sets the truck manager
     * @param truckManager
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


    protected static int coresRequired(){
        return 1;
    }

    private static void usage() {
        System.out.println("Usage: java pj2 CVRP tracker=None cores=<no_of_cores> dataset=<relative_path_for_dataset> algorithm=<exact/approximate> parallel=<yes/no> swarm_size=<number_of_bees_to_employee(max = Integer.MAX_VALUE)>");
    }
}
