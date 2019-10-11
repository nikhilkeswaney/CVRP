public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 100, INDEX = 1;

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
        int swarmSize = 50;
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



        int start  = 0;

        for(int i = 0; i < (int) swarmSize / 2; i++) {
            start += employeedBees[i].makeRannge(start, totalCost);
            System.out.println(i + "cost: " + employeedBees[i].getBestCost());
            System.out.println(i + "size: " + (employeedBees[i].getRange()[1] - employeedBees[i].getRange()[0]));
            System.out.println("----------------------------------------");

        }





    }

    public EmployeedBees[] getEmployeedBees() {
        return employeedBees;
    }

}
