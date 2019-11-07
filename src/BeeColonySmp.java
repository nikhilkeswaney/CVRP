import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.pj2.vbl.IntVbl;

public class BeeColonySmp extends Task {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 1500, INDEX = 1;
    private int swarmSize = 70;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;

    public BeeColonySmp(){
        INDEX = 1;
    }

    @Override
    public void main(String[] strings) throws Exception {
        employeedBees = new EmployeedBees[swarmSize / 2];
        onlookerBees = new OnlookerBee[swarmSize / 2];
        scoutBee = new ScoutBee(swarmSize / 2);


        parallelFor(0, (int) (swarmSize / 2) - 1).exec(new Loop() {
            @Override
            public void run(int i) throws Exception {
            employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
            int bestValue = employeedBees[i].getBestCost();
            if(bestValue < 0) {
                System.out.println("here");
            }
            }
        });


            EmployeedBees.TRIAL_MAX = (int) (0.5 * swarmSize * CVRP.getTruckManager().getNumberOfTrucks());
            IntVbl totalMaxCost = new IntVbl.Sum(0);

            System.out.println("start Employeed bee phase");

            // Employeed bee phase
            parallelFor(0, (int) (swarmSize/2) - 1).exec(new Loop() {
                DoubleVbl localTotalMaxCost;
                @Override
                public void start() throws Exception {
                    localTotalMaxCost = threadLocal(totalMaxCost);
                }

                @Override
                public void run(int i) throws Exception {

                    employeedBees[i].findGoodNeighbour();
                    int bestValue = employeedBees[i].getBestCost();
                    if(bestValue < 0) {
                        System.out.println("here");
                    }
                    localTotalMaxCost.item += bestValue;
                }
            });

            createRoulleteWheel(totalMaxCost.item);

            

            System.out.println("Total Cost : " + totalMaxCost.item);
    }

    public void createRoulleteWheel(int totalCost){
        double totalMaxCost = 0;

        for(int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i].setRoulleteSize(totalCost);
            totalMaxCost += employeedBees[i].getRoulleteSize();
        }

        double start  = 0.0f;

        for(int i = 0; i < (int) swarmSize / 2; i++) {
            start = employeedBees[i].makeRange(start, totalMaxCost);
        }
    }
    public static int currentIndex() {
        return INDEX;
    }

    public static void incrementIndex() {
        INDEX++;
    }

    public int bestCost(){
        return bestSet.getBestCost();
    }

    public void printAns(){
        bestSet.printTrucksandCost();
    }

    public static synchronized int getMaxItterations() {
        return MAX_ITTERATIONS;
    }
}
