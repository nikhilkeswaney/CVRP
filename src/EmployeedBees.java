public class EmployeedBees {
    private int ID;
    private CandidateSet foodSource;
    private int bestNectar;
    private boolean exhausted = false;
    private int trial = 0;
    private int bestCost;
    private static int TRIAL_MAX = 30;
    private int[] range = new int[2];
    public EmployeedBees(ScoutBee scoutBee, int ID){
        this.foodSource = scoutBee.getFoodSource();
        this.bestNectar = foodSource.calculateNectar(BeeColony.currentIndex());
        this.ID = ID;
    }


    public CandidateSet getFoodSource() {
        return foodSource;
    }

    public void setFoodSource(CandidateSet foodSource) {
        this.foodSource = foodSource;
    }

    public void findGoodNeighbour() {
        CandidateSet newFoodSource;
        for(int i = 0; i < 3; i++) {
            newFoodSource = foodSource.findNeighbour(foodSource);

            if(newFoodSource.getNectarQuality() < foodSource.getNectarQuality()){
                this.foodSource = newFoodSource;
                this.bestNectar = newFoodSource.getNectarQuality();
                trial = 0;
            }
            else
                this.trial++;

            if(this.trial >= TRIAL_MAX)
                setExhausted(true);

        }
        bestCost = foodSource.getCosts();

    }

    public int getBestNectar() {
        return bestNectar;
    }

    public void setBestNectar(int bestNectar) {
        this.bestNectar = bestNectar;
    }

    public int getID() {
        return ID;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    public int makeRannge(int start, int totalCost) {
        int end = start + (int)(
                            getBestCost() / (double) totalCost * 360
        );
        setRange(start, end);
        return end;
    }

    public void setRange(int start, int end) {
        range[0] = start;
        range[1] = end;

    }
    public int[] getRange(){
        return this.range;
    }
    public int getBestCost() {
        return bestCost;
    }

    public void setBestCost(int bestCost) {
        this.bestCost = bestCost;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public int getStart(){
        return range[0];
    }

    public int getEnd(){
        return range[1];
    }

    public boolean inRange(int number){
        return getStart() >= number && number < getEnd();
    }
}
