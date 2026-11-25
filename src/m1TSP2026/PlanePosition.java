package m1TSP2026;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;



public class PlanePosition {
    private final int x;
    private final int y;


    public int getX(){return this.x;}
    public int getY(){return this.y;}

    public PlanePosition(int squareSize){

        int x_generated = ThreadLocalRandom.current().nextInt(0, squareSize);
        int y_generated = ThreadLocalRandom.current().nextInt(0, squareSize);

        this.x = x_generated;
        this.y = y_generated;
    }

    public PlanePosition(){
        this(100);
    }


    public PlanePosition(int x, int y){
        this.x = x;
        this.y = y;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanePosition other = (PlanePosition) o;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public double distanceWith(PlanePosition p){
        return Math.abs((Math.sqrt(Math.pow((p.x - this.x), 2) + Math.pow((p.y - this.y), 2))));
    }

    public int distanceIntSqWith(PlanePosition p){
        return Math.abs((p.x - this.x) + (p.y - this.y));
    }
}


