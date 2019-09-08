public class Nodes {
    private int MAX_CORD = 3;
    private int x, y, z;

    public Nodes(int x, int y){
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Nodes(int x, int y, int z){
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
}
