import java.util.Random;

public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 100, INDEX = 1;
    private int swarmSize = 70;
    private OnlookerBee[] onlookerBees;
    public static synchronized int getMaxItterations() {
        return MAX_ITTERATIONS;
    }

    public static int currentIndex() {
        return INDEX;
    }

    public static void incrementIndex() {
        INDEX++;
    }

    public void startCollectingFood(){
        employeedBees = new EmployeedBees[swarmSize / 2];
        onlookerBees = new OnlookerBee[swarmSize / 2];
        scoutBee = new ScoutBee(swarmSize / 2);
        System.out.println("Sending scout Bees to find possible food sources");

        for (int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
        }

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

            if(scoutBee.getQueueSize() < swarmSize / 2){
                scoutBee.refillfoodSource();
            }

            for (int i = 0; i < (int) swarmSize / 2; i++) {
                if (employeedBees[i].isExhausted()) {
                    employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
                }
            }
            for (int i = 0; i < (int) swarmSize / 2; i++) {
                System.out.println("best ans for " + (i + 1) + " = " + employeedBees[i].getBestCost());
            }
            System.out.println("--------------------------------------------");
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

}
