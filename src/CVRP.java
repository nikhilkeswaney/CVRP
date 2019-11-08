import edu.rit.pj2.Task;

import java.io.IOException;

public class CVRP extends Task {

    private static NodeManager nodeManager = null;
    private static TruckManager truckManager = null;
    private static int optimalValue = 0;
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
            BeeColonySmp beeColony;
            beeColony = new BeeColonySmp();
            beeColony.main(args);

            System.out.println("N = " + getNodeManager().getSize());
            System.out.println("Best ans: " + getOptimalValue());
            System.out.println("Min cost:" + beeColony.bestCost());

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
}
