public class Manhattan implements DistanceMetric{
    @Override
    public int distance(Nodes from, Nodes to) {
        return Math.abs(from.getX() - to.getX()) +
                Math.abs(from.getY() - to.getY()) +
                Math.abs(from.getZ() - to.getZ());
    }
}
