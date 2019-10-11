public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 100, INDEX = 1;
    private int swarmSize = 50;

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
        System.out.println("Sending scout Bees to find possible food sources");
        scoutBee = new ScoutBee(swarmSize / 2);
        for(int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i] = new EmployeedBees(scoutBee, i + 1);
        }


        int totalCost = 0;
        for(int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i].findGoodNeighbour();
            totalCost += employeedBees[i].getBestCost();
        }

        createRoulleteWheel(totalCost);




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
            start += employeedBees[i].makeRange(start, totalMaxCost);
        }
    }

}
