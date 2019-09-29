public class EmployeedBees {
    private CandidateSet foodSource;
    public EmployeedBees(ScoutBee scoutBee){
        this.foodSource = scoutBee.getFoodSource();

    }



    public CandidateSet getFoodSource() {
        return foodSource;
    }

    public void setFoodSource(CandidateSet foodSource) {
        this.foodSource = foodSource;
    }
}
