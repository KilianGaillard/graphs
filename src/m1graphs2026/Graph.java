package m1graphs2026;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    public boolean usesNode(Node n){
        for(Node node : adjEdList.keySet()){
            if(node.getId()==n.getId()){
                return true;
            }
        }
        return false;
    }


    public boolean usesNode(int id){
        for(Node node : adjEdList.keySet()){
            if(node.getId()==id){
                return true;
            }
        }
        return false;
    }


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


    public boolean removeNode(Node n){
        
        if (n == null) {
            return false;
        }
        adjEdList.remove(n);

        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            List<Edge> edges = entry.getValue();
            
            edges.removeIf(edge -> edge.to().getId() == n.getId());
        }
        return true;
    }

    public boolean removeNode(int id){
        if (!this.usesNode(id)) {
            return false;
        }
        Node n = this.getNode(id);
        adjEdList.remove(n);

        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            List<Edge> edges = entry.getValue();
            
            edges.removeIf(edge -> edge.to().getId() == id);
        }
        return true;
    }


    public List<Node> getAllNodes(){return new ArrayList<>(adjEdList.keySet());}


    public int largestNodeId(){
        int max = -1;
        for(Node n : adjEdList.keySet()){
            if(max<n.getId()) {
                max = n.getId();
            }
        }
        return max;
    }


    public int smallestNodeId(){
        int min = Integer.MAX_VALUE;
        for(Node n : adjEdList.keySet()){
            if(min>n.getId()) {
                min = n.getId();
            }
        }
        return min;
    }

    
    public List<Node> getSuccessors(Node n){
        Set<Node> result = new LinkedHashSet<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if(node.equals(n)){
                List<Edge> edges = entry.getValue();
                for(Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return new ArrayList<>(result);

    }


    public List<Node> getSuccesors(int id){
        Set<Node> result = new LinkedHashSet<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if(node.getId()==id){
                List<Edge> edges = entry.getValue();
                for(Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return new ArrayList<>(result);
    }


    public List<Node> getSuccessorsMulti(Node n){
        List<Node> result = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if(node.equals(n)){
                List<Edge> edges = entry.getValue();
                for(Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return result;
    }


    public List<Node> getSuccessorsMulti(int id){
        List<Node> result = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if(node.getId()==id){
                List<Edge> edges = entry.getValue();
                for(Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return result;
    }

    
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
