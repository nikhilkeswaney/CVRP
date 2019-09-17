import java.util.ArrayList;

public class TravelingSalesman {
    public TravelingSalesmanTuple tspMinCostBackTracking(ArrayList<Integer> nodesToVisit,
                                                         int size,
                                                         int current,
                                                         int depth,
                                                         int currentCost){
        if(depth == size){
            TravelingSalesmanTuple end = new TravelingSalesmanTuple(currentCost + CVRP.getNodeManager().getNodeDistance(current, 0));
            end.addToPath(0);
            end.addToPath(current);
            return end;
        }
        else{
            TravelingSalesmanTuple minCost = new TravelingSalesmanTuple(Integer.MAX_VALUE);
            for(int i = 0; i < nodesToVisit.size(); i++){
                int element = nodesToVisit.get(i);
                nodesToVisit.remove(i);
                TravelingSalesmanTuple costUptillNow = tspMinCostBackTracking (
                                                                nodesToVisit,
                                                                size,
                                                                element,
                                                                depth + 1,
                                                                currentCost + CVRP.getNodeManager().getNodeDistance(current, element)
                                                        );
                minCost = minCost.lessThanEqualTo(costUptillNow);

                nodesToVisit.add(i, element);
            }
            minCost.addToPath(current);
            return minCost;

        }

    }

    public TravelingSalesmanTuple tspMinCost(ArrayList<Integer> nodesToVisit){
        return tspMinCostBackTracking (nodesToVisit,
                                       nodesToVisit.size(),
                                       0,
                                       0,
                                       0
                                       );
    }
}
