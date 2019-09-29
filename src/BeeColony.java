public class BeeColony {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    int maxItterations = 100;
    public void startCollectingFood(){
        int swarmSize = 50;
        employeedBees = new EmployeedBees[swarmSize / 2];
        System.out.println("Sending scout Bees to find possible food sources");
        scoutBee = new ScoutBee(25);

        for(int i = 0; i < swarmSize / 2; i++) {
            employeedBees[i] = new EmployeedBees(scoutBee);
        }

    }

    public EmployeedBees[] getEmployeedBees() {
        return employeedBees;
    }
}
