import java.io.BufferedReader;
import java.io.FileReader;
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
        readFile(dataSet);
    }

    private static void readFile(String dataSet) throws IOException {
        String currentLine;
        BufferedReader objReader = new BufferedReader(new FileReader(dataSet));
        while ((currentLine = objReader.readLine()) != null){
            String[] currentLineSeperated = currentLine.split(":");
            String tag = currentLineSeperated[0].strip();
            switch (tag){
                case "COMMENT":{
                    String[] truckNumber = currentLineSeperated[2].split(",");
                    truckManager = new TruckManager(Integer.parseInt(truckNumber[0].strip()));
                    optimalValue = Integer.parseInt(currentLineSeperated[3].strip());
                    break;
                }

//                case "EDGE_WEIGHT_TYPE":{
//
//                    break;
//                }
            }
        }

    }

    private static void usage() {
        System.out.println("Usage: java CVRP <dataset>");
        System.out.println("1. <dataset> The dataset you want to use");
    }


    public TruckManager getTruckManager() {
        return truckManager;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

}
