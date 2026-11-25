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

    // Constructors for fixed free position (with PlanePosition)

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
}
