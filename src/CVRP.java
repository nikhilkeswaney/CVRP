import java.io.IOException;

public class CVRP {

    private static NodeManager nodeManager = null;
    private static TruckManager truckManager = null;
    private static int optimalValue = 0;
    public static void main(String[] args) throws IOException {
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
//        double end = System.currentTimeMillis();
//        ea.printBestRoutes();
//        System.out.println("N = " + CVRP.getNodeManager().getNodeSize() + "|| Time Taken: " + (end - start) + " ms");
//        BeeColony beeColony = new BeeColony();
//        beeColony.startCollectingFood();

        BeeColony beeColony = new BeeColony();
        beeColony.startCollectingFood();


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
