package m1TSP2026;

import java.util.Objects;
import m1graphs2026.Graph;
import m1graphs2026.Node;

public class NodeInPlane extends Node{
    private final PlanePosition position;

    // Constructors for fixed free position (with integers)

    public NodeInPlane(int id, int x, int y){
        super(id,null);
        this.position = new PlanePosition(x,y);
    }

    public NodeInPlane(int id, String name, int x, int y){
        super(id, name, null);
        this.position = new PlanePosition(x,y);
    }

    public NodeInPlane(int id, Graph graph, int x, int y){
        super(id,null);
        this.position = new PlanePosition(x,y);
    }

    public NodeInPlane(int id, String name, Graph graph, int x, int y){
        super(id,name, graph);
        this.position = new PlanePosition(x,y);
    }

    // Constructors for fixed free position (with PlanePosition)

    public NodeInPlane(int id, PlanePosition position){
        super(id,null);
        this.position = position;
    }

    public NodeInPlane(int id, String name, PlanePosition position){
        super(id, name, null);
        this.position = position;
    }

    public NodeInPlane(int id, Graph graph, PlanePosition position){
        super(id,null);
        this.position = position;
    }

    public NodeInPlane(int id, String name, Graph graph, PlanePosition position){
        super(id,name, graph);
        this.position = position;
    }

    // Constructors for random position (without a size in parameter)

    public NodeInPlane(int id){
        super(id,null);
        this.position = new PlanePosition();
    }

    public NodeInPlane(int id, String name){
        super(id, name, null);
        this.position = new PlanePosition();
    }

    public NodeInPlane(int id, Graph graph){
        super(id,null);
        this.position = new PlanePosition();
    }

    public NodeInPlane(int id, String name, Graph graph){
        super(id,name, graph);
        this.position = new PlanePosition();
    }

    // Constructors for random position (with a size in parameter)

    public NodeInPlane(int id, int squareSize){
        super(id,null);
        this.position = new PlanePosition(squareSize);
    }

    public NodeInPlane(int id, String name, int squareSize){
        super(id, name, null);
        this.position = new PlanePosition(squareSize);
    }

    public NodeInPlane(int id, Graph graph, int squareSize){
        super(id,null);
        this.position = new PlanePosition(squareSize);
    }

    public NodeInPlane(int id, String name, Graph graph, int squareSize){
        super(id,name, graph);
        this.position = new PlanePosition(squareSize);
    }

    public PlanePosition getPosition(){return this.position;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeInPlane other = (NodeInPlane) o;
        return this.position.getX() == other.position.getX() && this.position.getY() == other.position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return super.toString() + " " + position.toString();
    }

    public double distanceWith(NodeInPlane o){
        return position.distanceWith(o.position);
    }

    public int distanceIntSqWith(NodeInPlane o){
        return position.distanceIntSqWith(o.position);
    }
}
