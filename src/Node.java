/**
 * This class represents the colcation of the customers
 */
public class Node {
    private int MAX_CORD = 3;
    private int x, y, z;
    private int demand = 0;

    public Node(int x, int y){
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Node(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getZ(){
        return this.z;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }
}
