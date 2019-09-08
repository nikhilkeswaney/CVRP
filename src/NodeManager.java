public class NodeManager {

    Nodes[] nodes = null;
    int[][] distanceBetweenNodes = null;
    private DistanceMetric distanceMetric = null;

    public NodeManager(DistanceMetric distanceMetric){
        this.distanceMetric = distanceMetric;
    }

    public DistanceMetric getDistanceMetric() {
        return distanceMetric;
    }
}
