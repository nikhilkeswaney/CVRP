import java.util.Random;

public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 1500, INDEX = 1;
    private int swarmSize = 20000;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;
    public BeeColony(){
        INDEX = 1;
    }
    public void startCollectingFood(){
        employeedBees = new EmployeedBees[swarmSize / 2];
        onlookerBees = new OnlookerBee[swarmSize / 2];
        scoutBee = new ScoutBee(swarmSize / 2);

        for (int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
        }
        EmployeedBees.TRIAL_MAX = (int) (0.5 * swarmSize * CVRP.getTruckManager().getNumberOfTrucks());
        while (currentIndex() < MAX_ITTERATIONS){

            int totalCost = 0;
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                employeedBees[i].findGoodNeighbour();
                totalCost += employeedBees[i].getBestCost();
            }

            createRoulleteWheel(totalCost);
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                onlookerBees[i] = new OnlookerBee(employeedBees, new Random());

                onlookerBees[i].sendOnlookerBees();

            }

            scoutBee.checkAdRefill();

            for (int i = 0; i < (int) swarmSize / 2; i++) {
                if(employeedBees[i].getBestCost() < bestAns){
                    bestAns = employeedBees[i].getBestCost();
                    bestSet = employeedBees[i];
                }
            }
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                if (employeedBees[i].isExhausted()) {
                    employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
                }
            }
            incrementIndex();
        }
    }

    public EmployeedBees[] getEmployeedBees() {
        return employeedBees;
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
