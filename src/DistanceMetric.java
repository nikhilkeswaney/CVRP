/**
 * This interface is created to be reused as we just need to overide with implementation
 * and it can change to anything
 */
public interface DistanceMetric {
    public int distance(Node from, Node to);
}
