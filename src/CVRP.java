import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.IntVbl;

import java.io.IOException;
import java.util.Random;

public class CVRP extends Task {

    private static NodeManager nodeManager = null;
    private static TruckManager truckManager = null;
    private static int optimalValue = 0;

    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 1500, INDEX = 1;
    private int swarmSize = 2000;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;
    public void main(String[] args) throws Exception {
        if(args.length < 1) {
            usage();
            return;
        }

        String dataSet = args[0];

        FileReader f = new FileReader(dataSet);
        f.readFile();
//        ExactAlgorithm ea = new ExactAlgorithm();
//        double start = System.currentTimeMillis();
//        ea.start();
//        ea.printBestRoutes();
//        double end = System.currentTimeMillis();
//
//        System.out.println("N = " + CVRP.getNodeManager().getNodeSize() + "|| Time Taken: " + (end - start) + " ms");
//        BeeColony beeColony = new BeeColony();
//        beeColony.startCollectingFood();

        int yes = Integer.parseInt(args[1]);
        if(yes == 0) {
            BeeColony beeColony;
            beeColony = new BeeColony();
            beeColony.startCollectingFood();

            System.out.println("N = " + getNodeManager().getSize());
            System.out.println("Best ans: " + getOptimalValue());
            System.out.println("Min cost:" + beeColony.bestCost());
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
