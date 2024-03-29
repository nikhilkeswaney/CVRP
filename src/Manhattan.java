/**
 * Manhattan implementation of distance metric
 */
public class Manhattan implements DistanceMetric{
    @Override
    public int distance(Node from, Node to) {
        return Math.abs(from.getX() - to.getX()) +
                Math.abs(from.getY() - to.getY()) +
                Math.abs(from.getZ() - to.getZ());
    }
}
