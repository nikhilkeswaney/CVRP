public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 100, INDEX = 0;

    public static int getMaxItterations() {
        return MAX_ITTERATIONS;
    }

    public static int currentIndex() {
        return INDEX;
    }

    public void startCollectingFood(){
        int swarmSize = 50;
        employeedBees = new EmployeedBees[swarmSize / 2];
        System.out.println("Sending scout Bees to find possible food sources");
        scoutBee = new ScoutBee(25);

        for(int i = 0; i < (int) swarmSize / 2; i++) {
            employeedBees[i] = new EmployeedBees(scoutBee);
            employeedBees[i].findGoodNeighbour();
        }

    }

    public EmployeedBees[] getEmployeedBees() {
        return employeedBees;
    }

}
