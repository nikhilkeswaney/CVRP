public class EmployeedBees {
    private CandidateSet foodSource;
    private int bestNectar;
    public EmployeedBees(ScoutBee scoutBee){
        this.foodSource = scoutBee.getFoodSource();
        this.bestNectar = foodSource.calculateNectar(BeeColony.currentIndex());
    }


    public CandidateSet getFoodSource() {
        return foodSource;
    }

    public void setFoodSource(CandidateSet foodSource) {
        this.foodSource = foodSource;
    }

    public void findGoodNeighbour() {
        CandidateSet newFoodSource;
        for(int i = 0; i < 3; i++){
            newFoodSource = foodSource.findNeighbour(foodSource);

            if(newFoodSource.getNectarQuality() < foodSource.getNectarQuality()){
                this.foodSource = newFoodSource;
                this.bestNectar = newFoodSource.getNectarQuality();
            }
        }
    }
}
