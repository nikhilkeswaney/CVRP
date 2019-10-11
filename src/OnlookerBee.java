import java.util.Random;

public class OnlookerBee {
    private EmployeedBees employeedBees;
    private Random rand;
    public OnlookerBee(EmployeedBees[] employeedBees, Random rand){
        this.rand = rand;
        int number = roulleteSelection();
        for (EmployeedBees bee: employeedBees) {
            if (bee.inRange(number)) {
                this.employeedBees = bee;
            }
        }
    }

    public void sendOnlookerBees(){
        this.employeedBees.findGoodNeighbour();
    }
    

    public int roulleteSelection(){
        return rand.nextInt(360);
    }

    public EmployeedBees getEmployeedBees() {
        return employeedBees;
    }
}
