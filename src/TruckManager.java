public class TruckManager {
    private Truck[] trucks;
    private int numberOfTrucks = 0;
    public TruckManager(int numberOfTrucks){
        this.numberOfTrucks = numberOfTrucks;
        trucks = new Truck[numberOfTrucks];
    }

    public void initializeEachTruck(int capacity){
        for(int i = 0; i < getNumberOfTrucks(); i++){
            trucks[i] = new Truck(capacity);
        }
    }

    public int getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public void setNumberOfTrucks(int numberOfTrucks) {
        this.numberOfTrucks = numberOfTrucks;
    }

    public Truck[] getTrucks() {
        return trucks;
    }

    public void setTrucks(Truck[] trucks) {
        this.trucks = trucks;
    }
}
