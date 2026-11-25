package m1TSP2026;

import java.util.Collections;
import java.util.HashSet;
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
}
