public class Max implements DistanceMetric{
    @Override
    public int distance(Nodes from, Nodes to) {
        return Math.max(
                    Math.max(
                        Math.abs(from.getX() - to.getX()) ,
                        Math.abs(from.getY() - to.getY())
                    ),
                    Math.abs(from.getZ() - to.getZ())
                );
    }
}
