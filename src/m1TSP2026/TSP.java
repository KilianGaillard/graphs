package m1TSP2026;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TSP {

    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_SQUARE_SIZE = 100;

    
    private final Set<NodeInPlane> nodes;

    public TSP(NodeInPlane... nodes){
        this.nodes = new HashSet();
        if(nodes!= null) {
            Collections.addAll(this.nodes, nodes);
        }
    }

    public TSP(int size, int squareSize) {
        if(size < 0 || squareSize <=0){
            throw new IllegalArgumentException("Invalid size (must be postive) or squareSize (must be strictly positive");
        }

        if(size > squareSize * squareSize){
            throw new IllegalArgumentException("Not enough distinct coordinates to create all of the nodes");
        }

        this.nodes = new HashSet<>();
        int id = 1;
        while(this.nodes.size() < size) {
            NodeInPlane to_add = new NodeInPlane(id,squareSize);
            if(this.nodes.add(to_add)){
                id++;
            }
        }
    }

    public TSP(int size){
        this(size, DEFAULT_SQUARE_SIZE);
    }

    public TSP(){
        this(DEFAULT_SIZE, DEFAULT_SQUARE_SIZE);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("graph {\n");

        List<NodeInPlane> sortedNodes = new ArrayList<>(this.nodes);
        Collections.sort(sortedNodes);

        for(NodeInPlane n : sortedNodes) {
            sb.append(" ").append(n.getId()).append(" [pos=\"").append(n.getPosition().getX()).append(" ,").append(n.getPosition().getY()).append("!\"]\n");
        }

        sb.append("}\n");

        return sb.toString();
    }
}
