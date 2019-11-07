import edu.rit.pj2.Task;

public class BeeColonySmp extends Task {
    private ScoutBee scoutBee;
    private EmployeedBees[] employeedBees;
    private static int MAX_ITTERATIONS = 1500, INDEX = 1;
    private int swarmSize = 70;
    private EmployeedBees bestSet;
    private int bestAns = Integer.MAX_VALUE;
    private OnlookerBee[] onlookerBees;
    public static synchronized int getMaxItterations() {
        return MAX_ITTERATIONS;
    }
    public BeeColonySmp(){
        INDEX = 1;
    }

    @Override
    public void main(String[] strings) throws Exception {
        employeedBees = new EmployeedBees[swarmSize / 2];
        onlookerBees = new OnlookerBee[swarmSize / 2];
        scoutBee = new ScoutBee();
        scoutBee.main(new String[]{"" + swarmSize / 2});



    }
}
