//******************************************************************************
//
//  File:    OnlookerBee.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implements all the functionalities of the Onlooker bee bees
//******************************************************************************
import java.util.Random;

/**
 * This class has all the functionality of onlooker bee
 */
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

    /**
     * send onlooker bees to their respective neighbourhood
     */
    public void sendOnlookerBees(){
        this.employeedBees.findGoodNeighbour();
    }


    public void sendAllOnlookerBees(){
        this.clonedEmployeedBee = this.employeedBees.deepCopy();
        this.clonedEmployeedBee.findGoodNeighbour();
    }

    /**
     * Select its own employeed bee
     * @return select the part
     */
    public int roulleteSelection(){
        return rand.nextInt(360);
    }


    /**
     * GETTERS AND SETTERS
     */
    public EmployeedBees getEmployeedBees() {
        return employeedBees;
    }


    public void reduceCopy(){
        employeedBees.reduce(clonedEmployeedBee);
    }
}
