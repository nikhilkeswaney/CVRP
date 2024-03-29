/**
 * Eucledian implementation of distance metric
 */
public class Euclidean implements DistanceMetric{
    @Override
    public int distance(Node from, Node to) {
        return (int) Math.sqrt(
                Math.pow(from.getX() - to.getX(), 2) +
                Math.pow(from.getY() - to.getY(), 2) +
                Math.pow(from.getZ() - to.getZ(), 2)
        );
    }
}
