import java.util.Random;

public class OnlookerBee {
    private EmployeedBees employeedBees;
    private EmployeedBees clonedEmployeedBee;

    private Random rand;
    public OnlookerBee(EmployeedBees[] employeedBees, Random rand){
        this.rand = rand;
        int number = roulleteSelection();
        boolean selection = false;
        for (EmployeedBees bee: employeedBees) {
            if (bee.inRange(number)) {
                selection = true;
                this.employeedBees = bee;
                break;
            }
        }

        if(!selection)
            this.employeedBees = employeedBees[employeedBees.length - 1];
    }

    public void sendOnlookerBees(){
        this.employeedBees.findGoodNeighbour();
    }

    public void sendAllOnlookerBees(){
        this.clonedEmployeedBee = this.employeedBees.deepCopy();
        this.clonedEmployeedBee.findGoodNeighbour();
    }


    public int roulleteSelection(){
        return rand.nextInt(360);
    }

    public EmployeedBees getEmployeedBees() {
        return employeedBees;
    }


    public void reduceCopy(){
        employeedBees.reduce(clonedEmployeedBee);
    }
}
