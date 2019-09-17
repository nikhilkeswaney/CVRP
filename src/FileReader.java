import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {
    private static EgdeType egdeType;
    public String dataSet;
    public FileReader(String dataset){
        this.dataSet = dataset;
    }
    public void readFile() throws IOException {
        String currentLine;
        int size = 0;
        BufferedReader objReader = new BufferedReader(new java.io.FileReader(this.dataSet));
        while ((currentLine = objReader.readLine()) != null){
            String[] currentLineSeperated = currentLine.split(":");
            String tag = currentLineSeperated[0].strip();
            switch (tag){
                case "COMMENT": {
                    String[] truckNumber = currentLineSeperated[2].split(",");
                    CVRP.setTruckManager(new TruckManager(Integer.parseInt(truckNumber[0].strip())));
                    CVRP.setOptimalValue(Integer.parseInt(currentLineSeperated[3].strip().replace(")", "")));
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
                        CVRP.setNodeManager(new NodeManager(new Euclidean(), size));
                    }
                    else if(distanceType.equals("MAN_2D") || distanceType.equals("MAN_3D")) {
                        egdeType = EgdeType.Euclidean;
                        CVRP.setNodeManager(new NodeManager(new Manhattan(), size));
                    }
                    else if(distanceType.equals("MAX_2D") || distanceType.equals("MAX_3D")) {
                        egdeType = EgdeType.Max;
                        CVRP.setNodeManager(new NodeManager(new Max(), size));
                    }
                    break;
                }
                case "CAPACITY":{
                    int capacity = Integer.parseInt(currentLineSeperated[1].strip());
                    CVRP.getTruckManager().initializeEachTruck(capacity);
                    CVRP.getTruckManager().setCapacity(capacity);
                    break;
                }
                case "NODE_COORD_SECTION":{
                    String[] nodesCordinates = getChunk(objReader, size);
                    CVRP.getNodeManager().createNodes(nodesCordinates);
                    break;
                }

                case "DEMAND_SECTION":{
                    String[] demand = getChunk(objReader, size);
                    CVRP.getNodeManager().setDemands(demand);
                    break;
                }

                case "DEPOT_SECTION": {
                    currentLine = objReader.readLine();
                    CVRP.getNodeManager().setDepot(Integer.parseInt(currentLine.strip()) - 1);
                    break;
                }
            }
        }

        CVRP.getNodeManager().setTruckManager(CVRP.getTruckManager());
        CVRP.getTruckManager().setNodeManager(CVRP.getNodeManager());
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
}
