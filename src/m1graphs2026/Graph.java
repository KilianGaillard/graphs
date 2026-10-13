package m1graphs2026;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    Map<Node, List<Edge>> adjEdList;

    /**
     * The default constructor which builds an empty graph
     */
    public Graph(){
        this.adjEdList = new HashMap<>();
    }


    /**
     * Builds a graph from its Successor Array (SA) representation.
     * The successors of node 1 are listed first, terminated by a 0, then those
     * of node 2 terminated by a 0, and so on. The number of nodes is therefore
     * the number of 0s in the array. Only unweighted graphs can be built this way.
     *
     * @param sa the successor array (also accepts an unspecified number of ints)
     * @throws IllegalArgumentException if sa is null, does not end with 0,
     * or contains a negative value or a successor id greater than the number of nodes
     */
    public Graph(int... sa){
        this();
        if(sa == null){
            throw new IllegalArgumentException("Successor array is null");
        }
        if(sa.length > 0 && sa[sa.length - 1] != 0){
            throw new IllegalArgumentException("Successor array must end with 0");
        }

        // the number of nodes is the number of 0s
        int nbNodes = 0;
        for(int v : sa){
            if(v < 0){
                throw new IllegalArgumentException("Negative value in successor array: " + v);
            }
            if(v == 0){
                nbNodes++;
            }
        }

        // create the nodes 1..nbNodes (nodes[i] is the node with id i)
        Node[] nodes = new Node[nbNodes + 1];
        for(int i = 1; i <= nbNodes; i++){
            nodes[i] = new Node(i, this);
            this.adjEdList.put(nodes[i], new ArrayList<>());
        }

        // create the edges
        int current = 1;
        for(int v : sa){
            if(v == 0){
                current++;
            } else {
                if(v > nbNodes){
                    throw new IllegalArgumentException("Unknown successor id: " + v);
                }
                this.adjEdList.get(nodes[current]).add(new Edge(nodes[current], nodes[v]));
            }
        }
    }



    public int nbNodes(){return adjEdList.size();}
    public boolean usesNode(Node n){return true;}
    public boolean holdsNode(Node n){return adjEdList.containsKey(n);}
    public Node getNode(int id){
        for(Node n : adjEdList.keySet()){
            if(n.getId()==id){
                return n;
            }
        }
        return null;
    }
    public boolean addNode(Node n){
        if(adjEdList.containsKey(n)) return false;
        adjEdList.put(n, new ArrayList<>());
        return true;
    }
    public boolean removeNode(Node n){return true;}
    public List<Node> getAllNodes(){return new ArrayList<>(adjEdList.keySet());}
    public int largestNodeId(){return 0;}
    public int smallestNodeId(){return 0;}
    public List<Node> getSuccessors(Node n){return null;}
    public List<Node> getSuccessorsMulti(Node n){return null;}
    public boolean adjacent(Node u, Node v){return true;}
    public int inDegree(Node n){return n.inDegree();}
    public int outDegree(Node n){return n.outDegree();}
    public int degree(Node n){return n.degree();}
    public int nbEdges(){return 0;}
    public boolean existsEdge(Node u, Node v){return true;}
    public boolean isMultiEdge(Node u, Node v){return true;}
    public void addEdge(Node from, Node to){}
    public boolean removeEdge(Node from, Node to){return true;}
    public List<Edge> getOutEdges(Node n){
        List<Edge> edges = adjEdList.get(n);
        return edges == null ? new ArrayList<>() : new ArrayList<>(edges);
    }
    public List<Edge> getInEdges(Node n){return null;}
    public List<Edge> getIncidentEdges(Node n){return null;}
    public List<Edge> getEdges(Node u, Node v){return null;}
    public List<Edge> getAllEdges(){return null;}
    public int [] toSuccessorArray(){return null;}
    public int [] [] toAdjMatrix(){return null;}
    public Graph getReverse(){return null;}
    public Graph getTransitiveClosure(){return null;}
    public boolean isMultiGraph(){return true;}
    public boolean isSimpleGraph(){return true;}
    public boolean hasSelfLoops(){return true;}
    public Graph toSimpleGraph(){return null;}
    public Graph copy(){return null;}
    public List<Node> getDFS(){return null;}
    public List<Node> getDFS(Node u){return null;}
    public List<Node> getBFS(){return null;}
    public List<Node> getBFS(Node u){return null;}
    // public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){return null;}
    // public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){return null;}
    public static Graph fromDotFile(String filename){return null;}
    public static Graph fromDotFile(String filename, String extension){return null;}
    public String toDotString(){return null;}
    public void toDotFile(String fileName){}
    public void toDotFile(String fileName, String extension){}



}
