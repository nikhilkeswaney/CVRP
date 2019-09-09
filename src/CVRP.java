import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CVRP {

    private static NodeManager nodeManager = null;
    private static TruckManager truckManager = null;
    private static int optimalValue = 0;
    private static EgdeType egdeType;
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
        int size = 0;
        BufferedReader objReader = new BufferedReader(new FileReader(dataSet));
        while ((currentLine = objReader.readLine()) != null){
            String[] currentLineSeperated = currentLine.split(":");
            String tag = currentLineSeperated[0].strip();
            switch (tag){
                case "COMMENT":{
                    String[] truckNumber = currentLineSeperated[2].split(",");
                    truckManager = new TruckManager(Integer.parseInt(truckNumber[0].strip()));
                    optimalValue = Integer.parseInt(currentLineSeperated[3].strip().replace(")", ""));
                    break;
                }
                case "DIMENSION": {
                    size = Integer.parseInt(currentLineSeperated[1].strip());
                    break;
                }
                case "EDGE_WEIGHT_TYPE":{
                    String distanceType = currentLineSeperated[1].strip();
                    if(distanceType.equals("EUC_2D") || distanceType.equals("EUC_3D")) {
                        egdeType = EgdeType.Euclidean;
                        nodeManager = new NodeManager(new Euclidean(), size);
                    }
                    else if(distanceType.equals("MAN_2D") || distanceType.equals("MAN_3D")) {
                        egdeType = EgdeType.Euclidean;
                        nodeManager = new NodeManager(new Manhattan(), size);
                    }
                    else if(distanceType.equals("MAX_2D") || distanceType.equals("MAX_3D")) {
                        egdeType = EgdeType.Max;
                        nodeManager = new NodeManager(new Max(), size);
                    }
                    break;
                }

                case "NODE_COORD_SECTION":{
                    String[] nodesCordinates = getChunk(objReader, size);
                    nodeManager.createNodes(nodesCordinates);
                    break;
                }

                case "DEMAND_SECTION":{
                    String[] demand = getChunk(objReader, size);
                    nodeManager.setDemands(demand);
                    break;
                }

                case "DEPOT_SECTION": {
                    currentLine = objReader.readLine();
                    nodeManager.setDepot(Integer.parseInt(currentLine.strip()) - 1);
                    break;
                }
            }
        }

    }

    public static String[] getChunk(BufferedReader objReader, int size) throws IOException {
        String currentLine;
        String[] chunk = new String[size];
        for (int i = 0; i < size; i++){
            currentLine = objReader.readLine();
            chunk[i] = currentLine;
        }
        return chunk;
    }

    public static int getOptimalValue() {
        return optimalValue;
    }

    public TruckManager getTruckManager() {
        return truckManager;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }
    
    private static void usage() {
        System.out.println("Usage: java CVRP <dataset>");
        System.out.println("1. <dataset> The dataset you want to use");
    }
}
