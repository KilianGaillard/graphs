package m1graphs2026;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Enumeration representing the color state of a node during graph traversal.
 */
enum NodeColor { WHITE, GRAY, BLACK }


/**
 * Enumeration representing the classification of an edge during an enriched Depth-First Search (DFS) traversal[cite: 31].
 */
enum EdgeVisitType { 
    /** Tree edge in the DFS forest[cite: 7, 31]. */
    TREE, 
    /** Back edge leading to an ancestor in the DFS tree[cite: 7, 31]. */
    BACKWARD, 
    /** Forward edge leading to a non-child descendant in the DFS tree[cite: 7, 31]. */
    FORWARD, 
    /** Cross edge leading to a node already completed or in another tree[cite: 7, 31]. */
    CROSS 
}

/**
 * Encapsulates graph traversal metadata for a node during an enriched Depth-First Search (DFS)[cite: 31].
 * Stores node color, predecessor in the search tree, discovery timestamp, and finish timestamp[cite: 2, 31].
 */
class NodeVisitInfo {
    private NodeColor color;
    private Node predecessor;
    private Integer discoveryTime;
    private Integer finishTime;

    /**
     * Constructs a new NodeVisitInfo instance with specified traversal attributes[cite: 31].
     *
     * @param color the current color state of the node (WHITE, GRAY, or BLACK)[cite: 2, 31]
     * @param predecessor the parent node in the DFS search forest, or null if root/unvisited[cite: 2, 31]
     * @param discoveryTime the timestamp when the node was first discovered[cite: 2, 31]
     * @param finishTime the timestamp when the node processing was completed[cite: 2, 31]
     */
    public NodeVisitInfo(NodeColor color, Node predecessor, Integer discoveryTime, Integer finishTime) {
        this.color = color;
        this.predecessor = predecessor;
        this.discoveryTime = discoveryTime;
        this.finishTime = finishTime;
    }

    /**
     * Gets the current color state of the node[cite: 31].
     *
     * @return the node color[cite: 31]
     */
    public NodeColor getColor() { 
        return color; 
    }

    /**
     * Sets the color state of the node[cite: 31].
     *
     * @param color the new color state[cite: 31]
     */
    public void setColor(NodeColor color) { 
        this.color = color; 
    }

    /**
     * Gets the predecessor node in the DFS tree[cite: 31].
     *
     * @return the predecessor node, or null if none[cite: 31]
     */
    public Node getPredecessor() { 
        return predecessor; 
    }

    /**
     * Sets the predecessor node in the DFS tree[cite: 31].
     *
     * @param predecessor the parent node[cite: 31]
     */
    public void setPredecessor(Node predecessor) { 
        this.predecessor = predecessor; 
    }

    /**
     * Gets the discovery timestamp of the node[cite: 31].
     *
     * @return the discovery timestamp, or null if unvisited[cite: 31]
     */
    public Integer getDiscoveryTime() { 
        return discoveryTime; 
    }

    /**
     * Sets the discovery timestamp of the node[cite: 31].
     *
     * @param discoveryTime the time when the node was discovered[cite: 31]
     */
    public void setDiscoveryTime(Integer discoveryTime) { 
        this.discoveryTime = discoveryTime; 
    }

    /**
     * Gets the finish timestamp of the node[cite: 31].
     *
     * @return the finish timestamp, or null if processing is incomplete[cite: 31]
     */
    public Integer getFinishTime() { 
        return finishTime; 
    }

    /**
     * Sets the finish timestamp of the node[cite: 31].
     *
     * @param finishTime the time when node exploration was completed[cite: 31]
     */
    public void setFinishTime(Integer finishTime) { 
        this.finishTime = finishTime; 
    }
}

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

    public boolean addNode(int id) {
    if (usesNode(id)) {
        return false;
    }
    return addNode(new Node(id, this));
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


    public List<Node> getSuccessors(int id){
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


    public boolean adjacent(Node u, Node v){
        return (existsEdge(u, v)|| existsEdge(v, u));
    }

    public int inDegree(Node n){return n.inDegree();}

    public int inDegree(int id) { return inDegree(getNode(id)); }


    public int outDegree(Node n){return n.outDegree();}

    public int outDegree(int id) { return outDegree(getNode(id)); }

    public int degree(Node n){return n.degree();}

    public int degree(int id) { return degree(getNode(id)); }


    public int nbEdges(){
        int count = 0;
        for(List<Edge> edges : adjEdList.values()){
            for(Edge e : edges){
                count++;
            }
        }
        return count;
    }


    public boolean existsEdge(Node u, Node v) {
        if (u == null || v == null || !adjEdList.containsKey(u)) {
            return false;
        }
        for (Edge e : adjEdList.get(u)) {
            if (e.to().equals(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsEdge(int id1, int id2) {
        Node u = getNode(id1);
        Node v = getNode(id2);
        if (u == null || v == null) {
            return false;
        }
        return existsEdge(u, v);
    }

    public boolean existsEdge(Edge e) {
        if (e == null) {
            return false;
        }
        return existsEdge(e.from(), e.to());
    }


    public boolean isMultiEdge(Node u, Node v) {
        if (u == null || v == null || !adjEdList.containsKey(u)) {
            return false;
        }
        int count = 0;
        for (Edge e : adjEdList.get(u)) {
            if (e.to().equals(v)) {
                count++;
                if (count >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMultiEdge(int id1, int id2) {
        Node u = getNode(id1);
        Node v = getNode(id2);
        if (u == null || v == null) {
            return false;
        }
        return isMultiEdge(u, v);
    }

    public boolean isMultiEdge(Edge e) {
        if (e == null) {
            return false;
        }
        return isMultiEdge(e.from(), e.to());
    }


    public void addEdge(Node from, Node to){
        if(!usesNode(from)){
            addNode(from);
        }
        if(!usesNode(to)){
            addNode(to);
        }
        Edge e = new Edge(from, to);
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            Node node = entry.getKey();
            if(node.equals(from)){
                this.adjEdList.get(node).add(e);
            }
        }

    }


    public void addEdge(Node from, Node to, int weight){
        if(!usesNode(from)){
            addNode(from);
        }
        if(!usesNode(to)){
            addNode(to);
        }
        Edge e = new Edge(from, to,weight);
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            Node node = entry.getKey();
            if(node.equals(from)){
                this.adjEdList.get(node).add(e);
            }
        }
    }


    public void addEdge(int id1, int id2){
        if(!usesNode(id1)){
            Node from = new Node(id1,this);
            addNode(from);
            if(!usesNode(id2)){
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(from,to);
            }
            else {
                addEdge(from,this.getNode(id2));
            }
        }
        else {
            if(!usesNode(id2)){
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(getNode(id1),to);
            }else {
                addEdge(getNode(id1), getNode(id2));
            }
            
        }
    }

    public void addEdge(int id1, int id2, int weight){
        if(!usesNode(id1)){
            Node from = new Node(id1,this);
            addNode(from);
            if(!usesNode(id2)){
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(from,to,weight);
            }
            else {
                addEdge(from,this.getNode(id2),weight);
            }
        }
        else {
            if(!usesNode(id2)){
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(getNode(id1),to,weight);
            }else {
                addEdge(getNode(id1), getNode(id2),weight);
            }
            
        }
    }

    public void addEdge(Edge e){
        if(!usesNode(e.from())){
           addNode(e.from());
        }
        if(!usesNode(e.to())){
            addNode(e.to());
        }
        addEdge(e.from(), e.to());
    }

    public void addEdge(Edge e, int weight){
        if(!usesNode(e.from())){
           addNode(e.from());
        }
        if(!usesNode(e.to())){
            addNode(e.to());
        }
        addEdge(e.from(), e.to(), weight);
    }


    /**
     * Removes the first edge found from node 'from' to node 'to', regardless of its weight.
     *
     * @param from the source node
     * @param to the target node
     * @return true if an edge was found and removed, false otherwise
     */
    public boolean removeEdge(Node from, Node to) {
        if (from == null || to == null || !adjEdList.containsKey(from)) {
            return false;
        }
        List<Edge> edges = adjEdList.get(from);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).to().equals(to)) {
                edges.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes an edge between nodes 'from' and 'to' matching a specific weight.
     *
     * @param from the source node
     * @param to the target node
     * @param weight the weight of the edge to remove (can be null for unweighted)
     * @return true if a matching edge was found and removed, false otherwise
     */
    public boolean removeEdge(Node from, Node to, Integer weight) {
        if (from == null || to == null || !adjEdList.containsKey(from)) {
            return false;
        }
        List<Edge> edges = adjEdList.get(from);
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if (e.to().equals(to) && java.util.Objects.equals(e.getWeight(), weight)) {
                edges.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Overload for removing an edge by node IDs (without specifying weight).
     */
    public boolean removeEdge(int id1, int id2) {
        return removeEdge(getNode(id1), getNode(id2));
    }

    /**
     * Overload for removing an edge by node IDs and a specific weight.
     */
    public boolean removeEdge(int id1, int id2, Integer weight) {
        return removeEdge(getNode(id1), getNode(id2), weight);
    }

    /**
     * Overload for removing an edge using an Edge instance reference.
     */
    public boolean removeEdge(Edge e) {
        if (e == null) return false;
        return removeEdge(e.from(), e.to(), e.getWeight());
    }

    /**
     * Overload for removing an edge using an Edge instance reference and an explicit weight override.
     */
    public boolean removeEdge(Edge e, Integer weight) {
        if (e == null) return false;
        return removeEdge(e.from(), e.to(), weight);
    }
    


    public List<Edge> getOutEdges(Node n){
        List<Edge> edges = adjEdList.get(n);
        return edges == null ? new ArrayList<>() : new ArrayList<>(edges);
    }

    public List<Edge> getOutEdges(int id){
        return getOutEdges(getNode(id));
    }


    public List<Edge> getInEdges(Node n){
        List<Edge> res = new ArrayList<>();
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            for(Edge e : entry.getValue()){
                    if(e.to().equals(n)){
                        res.add(e);
                    }
            }
        }
        return res;
    }

    public List<Edge> getInEdges(int id){
        return getInEdges(getNode(id));
    }


    public List<Edge> getIncidentEdges(Node n){
        List<Edge> in = getInEdges(n);
        List<Edge> res = getOutEdges(n);
        for(Edge e : in){
            res.add(e);
        }
        return res;
    }
    public List<Edge> getIncidentEdges(int id){
        return getIncidentEdges(getNode(id));
    }

    public List<Edge> getEdges(Node u, Node v){
        List<Edge> out_u = getOutEdges(u);
        List<Edge> res = new ArrayList<>();
        for(Edge e : out_u){
            if(e.to().equals(v)){
                res.add(e);
            }
        }
        return res;
    }

    public List<Edge> getEdges(int id1, int id2) {
        return getEdges(getNode(id1), getNode(id2));
    }

    public List<Edge> getAllEdges(){
        List<Edge> res = new ArrayList<>();
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            for(Edge e : entry.getValue()){
                res.add(e);
            }
        }
        return res;
    }


    public int[] toSuccessorArray() {
        int[] res = new int[nbEdges() + nbNodes()];
        int insertion = 0;

        List<Node> nodes = getAllNodes();
        Collections.sort(nodes); // Exigence Section 5.6

        for (Node u : nodes) {
            List<Edge> edges = getOutEdges(u);
            Collections.sort(edges); // Exigence Section 5.6

            for (Edge e : edges) {
                res[insertion++] = e.to().getId();
            }
            res[insertion++] = 0;
        }
        return res;
    }
    
    public int[][] toAdjMatrix() {
        int n = nbNodes();
        int[][] matrix = new int[n][n];

        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node fromNode = entry.getKey();
            int rowIndex = fromNode.getId() - 1;

            for (Edge edge : entry.getValue()) {
                int colIndex = edge.to().getId() - 1;
                matrix[rowIndex][colIndex]++;
            }
        }

        return matrix;
    }


    public Graph getReverse() {
        Graph reverseGraph = new Graph();

        for (Node n : this.getAllNodes()) {
            reverseGraph.addNode(new Node(n.getId(), reverseGraph));
        }

        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            for (Edge edge : entry.getValue()) {
                if (edge.isWeighted()) {
                    reverseGraph.addEdge(edge.to().getId(), edge.from().getId(), edge.getWeight());
                } else {
                    reverseGraph.addEdge(edge.to().getId(), edge.from().getId());
                }
            }
        }

        return reverseGraph;
    }


    
    public Graph getTransitiveClosure() {
        Graph closure = this.copy();
        List<Node> nodes = closure.getAllNodes();

        for (Node k : nodes) {
            for (Node i : nodes) {
                for (Node j : nodes) {
                    // Si un chemin existe de i à k ET de k à j
                    if (closure.existsEdge(i, k) && closure.existsEdge(k, j)) {
                        // Et qu'il n'y a pas déjà d'arc direct de i à j
                        if (!closure.existsEdge(i, j)) {
                            closure.addEdge(i, j);
                        }
                    }
                }
            }
        }

        return closure;
    }


    public boolean isMultiGraph(){
        List<Edge> edges = getAllEdges();
        for(Edge e : edges){
            if(isMultiEdge(e)){
                return true;
            }
        }
        return false;
    }


    public boolean isSimpleGraph(){
        return !isMultiGraph() && !hasSelfLoops();
    }


    public boolean hasSelfLoops(){
        List<Edge> edges = getAllEdges();
        for(Edge e : edges){
            if(e.from().equals(e.to())){
                return true;
            }
        }
        return false;
    }


    public Graph toSimpleGraph() {
        if (this.isSimpleGraph()) {
            return this.copy();
        }
        Graph simple = new Graph();

        for (Node n : this.getAllNodes()) {
            simple.addNode(new Node(n.getId(), simple));
        }

        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node u = entry.getKey();

            for (Edge e : entry.getValue()) {
                Node v = e.to();

                if (!u.equals(v) && !simple.existsEdge(u.getId(), v.getId())) {
                    if (e.isWeighted()) {
                        simple.addEdge(u.getId(), v.getId(), e.getWeight());
                    } else {
                        simple.addEdge(u.getId(), v.getId());
                    }
                }
            }
        }
        return simple;
    }
    
    public Graph copy() {
        Graph copyGraph = new Graph();
        for (Node n : getAllNodes()) {
            copyGraph.addNode(new Node(n.getId(), copyGraph));
        }
        for (Edge e : getAllEdges()) {
            if (e.isWeighted()) {
                copyGraph.addEdge(e.from().getId(), e.to().getId(), e.getWeight());
            } else {
                copyGraph.addEdge(e.from().getId(), e.to().getId());
            }
        }
        return copyGraph;
    }

    
    /**
     * Performs a Depth-First Search (DFS) traversal of the entire graph,
     * starting by default from the node with the lowest id.
     *
     * @return a list of nodes in the order they were first discovered during the traversal
     */
    public List<Node> getDFS() {
        List<Node> nodes = getAllNodes();
        if (nodes.isEmpty()) {
            return new ArrayList<>();
        }
        Collections.sort(nodes); 
        return getDFS(nodes.get(0));
    }

    /**
     * Performs a Depth-First Search (DFS) traversal of the graph starting from a specified node.
     * Unvisited nodes from other connected components are visited afterwards.
     * Those methods are inspired from the course of Graf of this year, presented by Ms SONIGA
     *
     * @param u the starting node for the traversal
     * @return a list of nodes in the order they were first discovered during the traversal,
     *         or an empty list if the node does not belong to the graph
     */
    public List<Node> getDFS(Node u) {
        List<Node> result = new ArrayList<>();
        if (u == null || !holdsNode(u)) {
            return result;
        }

        Map<Node, NodeColor> colorMap = new HashMap<>();
        List<Node> allNodes = getAllNodes();
        
        for (Node n : allNodes) {
            colorMap.put(n, NodeColor.WHITE);
        }

        dfsVisit(u, colorMap, result);

        Collections.sort(allNodes);
        for (Node n : allNodes) {
            if (colorMap.get(n) == NodeColor.WHITE) {
                dfsVisit(n, colorMap, result);
            }
        }

        return result;
    }

    /**
     * Performs a Depth-First Search (DFS) traversal of the graph starting from the node with the given id.
     *
     * @param id the identifier of the starting node
     * @return a list of nodes in the order they were first discovered during the traversal
     */
    public List<Node> getDFS(int id) {
        return getDFS(getNode(id));
    }

    /**
     * Auxiliary recursive method for visiting a node during Depth-First Search.
     *
     * @param u the node currently being visited
     * @param colorMap a map tracking the color state of each node
     * @param result the list collecting nodes in discovery order
     */
    private void dfsVisit(Node u, Map<Node, NodeColor> colorMap, List<Node> result) {
        colorMap.put(u, NodeColor.GRAY);
        result.add(u);

        List<Edge> outEdges = getOutEdges(u);
        Collections.sort(outEdges);

        for (Edge e : outEdges) {
            Node v = e.to();
            if (colorMap.get(v) == NodeColor.WHITE) {
                dfsVisit(v, colorMap, result);
            }
        }

        colorMap.put(u, NodeColor.BLACK);
    }


    /**
     * Performs a Breadth-First Search (BFS) traversal of the entire graph,
     * starting by default from the node with the lowest id.
     *
     * @return a list of nodes in the order they were visited during the traversal[cite: 24]
     */
    public List<Node> getBFS() {
        List<Node> nodes = getAllNodes();
        if (nodes.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort nodes by increasing order of id for output reproducibility (Section 5.6)[cite: 24]
        Collections.sort(nodes);

        // Start traversal from the node with the lowest id[cite: 24]
        return getBFS(nodes.get(0));
    }

    /**
     * Performs a Breadth-First Search (BFS) traversal of the graph starting from a specified node[cite: 24].
     * Unvisited nodes from other connected components are visited afterwards.
     *
     * @param u the starting node for the traversal[cite: 24]
     * @return a list of nodes in the order they were visited during the traversal[cite: 24],
     *         or an empty list if the node does not belong to the graph
     */
    public List<Node> getBFS(Node u) {
        List<Node> result = new ArrayList<>();
        if (u == null || !holdsNode(u)) {
            return result;
        }

        Map<Node, NodeColor> colorMap = new HashMap<>();
        List<Node> allNodes = getAllNodes();

        // Initialize all nodes to WHITE (unvisited)
        for (Node n : allNodes) {
            colorMap.put(n, NodeColor.WHITE);
        }

        // 1. Visit starting from node u
        bfsVisit(u, colorMap, result);

        // 2. Visit any remaining unvisited nodes (handles disconnected components)
        Collections.sort(allNodes); // Ensure deterministic traversal order (Section 5.6)[cite: 24]
        for (Node n : allNodes) {
            if (colorMap.get(n) == NodeColor.WHITE) {
                bfsVisit(n, colorMap, result);
            }
        }

        return result;
    }

    /**
     * Performs a Breadth-First Search (BFS) traversal of the graph starting from the node with the given id[cite: 24].
     *
     * @param id the identifier of the starting node[cite: 24]
     * @return a list of nodes in the order they were visited during the traversal[cite: 24]
     */
    public List<Node> getBFS(int id) {
        return getBFS(getNode(id));
    }

    /**
     * Auxiliary method for visiting nodes level by level using a FIFO queue.
     *
     * @param start the node from which to start the BFS iteration
     * @param colorMap a map tracking the color state of each node
     * @param result the list collecting nodes in discovery order
     */
    private void bfsVisit(Node start, Map<Node, NodeColor> colorMap, List<Node> result) {
        Queue<Node> queue = new ArrayDeque<>();

        // Discover starting node
        colorMap.put(start, NodeColor.GRAY);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node u = queue.poll();
            result.add(u);

            // Sort outgoing edges to guarantee deterministic traversal order (Section 5.6)[cite: 24]
            List<Edge> outEdges = getOutEdges(u);
            Collections.sort(outEdges);

            for (Edge e : outEdges) {
                Node v = e.to();
                if (colorMap.get(v) == NodeColor.WHITE) {
                    colorMap.put(v, NodeColor.GRAY); // Mark as discovered
                    queue.add(v);
                }
            }

            colorMap.put(u, NodeColor.BLACK); // Mark as fully processed
        }
    }

    
    // Global counter for timestamps in DFS with visit info
    private static int dfsTime = 0;

    /**
     * Performs an enriched Depth-First Search (DFS) traversal starting from the node with the lowest id[cite: 31].
     * Populates nodeVisit and edgeVisit maps with traversal metrics (colors, timestamps, predecessors, edge types)[cite: 31].
     *
     * @param nodeVisit map associating each node with its visit metadata[cite: 31]
     * @param edgeVisit map associating each edge with its DFS classification[cite: 31]
     * @return a list of nodes in discovery order[cite: 31]
     */
    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        List<Node> nodes = getAllNodes();
        if (nodes.isEmpty()) {
            return new ArrayList<>();
        }
        Collections.sort(nodes); // Reproducibility requirement (Section 5.6)[cite: 32]
        return getDFSWithVisitInfo(nodes.get(0), nodeVisit, edgeVisit);
    }

    /**
     * Performs an enriched Depth-First Search (DFS) traversal starting from a given node u[cite: 31].
     *
     * @param u the starting node for the traversal[cite: 31]
     * @param nodeVisit map associating each node with its visit metadata[cite: 31]
     * @param edgeVisit map associating each edge with its DFS classification[cite: 31]
     * @return a list of nodes in discovery order[cite: 31]
     */
    public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        List<Node> result = new ArrayList<>();
        if (u == null || !holdsNode(u) || nodeVisit == null || edgeVisit == null) {
            return result;
        }

        nodeVisit.clear();
        edgeVisit.clear();
        dfsTime = 0;

        List<Node> allNodes = getAllNodes();
        for (Node n : allNodes) {
            nodeVisit.put(n, new NodeVisitInfo(NodeColor.WHITE, null, null, null));
        }

        // 1. Visit starting from u
        dfsVisitWithInfo(u, nodeVisit, edgeVisit, result);

        // 2. Visit remaining unvisited nodes
        Collections.sort(allNodes); // Section 5.6[cite: 32]
        for (Node n : allNodes) {
            if (nodeVisit.get(n).getColor() == NodeColor.WHITE) {
                dfsVisitWithInfo(n, nodeVisit, edgeVisit, result);
            }
        }

        return result;
    }

    /**
     * Auxiliary recursive method for enriched DFS traversal.
     */
    private void dfsVisitWithInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit, List<Node> result) {
        dfsTime++;
        NodeVisitInfo uInfo = nodeVisit.get(u);
        uInfo.setColor(NodeColor.GRAY);
        uInfo.setDiscoveryTime(dfsTime);
        result.add(u);

        List<Edge> outEdges = getOutEdges(u);
        Collections.sort(outEdges); // Section 5.6[cite: 32]

        for (Edge e : outEdges) {
            Node v = e.to();
            NodeVisitInfo vInfo = nodeVisit.get(v);

            if (vInfo.getColor() == NodeColor.WHITE) {
                edgeVisit.put(e, EdgeVisitType.TREE);
                vInfo.setPredecessor(u);
                dfsVisitWithInfo(v, nodeVisit, edgeVisit, result);
            } else if (vInfo.getColor() == NodeColor.GRAY) {
                edgeVisit.put(e, EdgeVisitType.BACKWARD);
            } else if (vInfo.getColor() == NodeColor.BLACK) {
                if (uInfo.getDiscoveryTime() < vInfo.getDiscoveryTime()) {
                    edgeVisit.put(e, EdgeVisitType.FORWARD);
                } else {
                    edgeVisit.put(e, EdgeVisitType.CROSS);
                }
            }
        }

        uInfo.setColor(NodeColor.BLACK);
        dfsTime++;
        uInfo.setFinishTime(dfsTime);
    }

    // ==========================================
    // 5.5 Graph Import and Export (DOT Format)
    // ==========================================

    /**
     * Imports a graph from a restricted DOT format file with default extension '.gv'[cite: 31].
     *
     * @param filename absolute path to the file without extension[cite: 31]
     * @return the constructed Graph instance[cite: 31]
     */
    public static Graph fromDotFile(String filename) {
        return fromDotFile(filename, "gv");
    }

    /**
     * Imports a graph from a restricted DOT format file with a custom extension[cite: 31].
     *
     * @param filename absolute path to the file without extension[cite: 31]
     * @param extension file extension (e.g., "dot" or "gv")[cite: 31]
     * @return the constructed Graph instance[cite: 31]
     */
    public static Graph fromDotFile(String filename, String extension) {
        Graph graph = new Graph();
        String fullPath = filename + "." + extension;

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fullPath))) {
            String line;
            java.util.regex.Pattern edgePattern = java.util.regex.Pattern.compile(
                "^\\s*(\\d+)\\s*->\\s*(\\d+)(?:\\s*\\[label=(\\d+),\\s*len=\\d+\\])?\\s*;?\\s*$"
            );

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                java.util.regex.Matcher matcher = edgePattern.matcher(line);
                if (matcher.find()) {
                    int fromId = Integer.parseInt(matcher.group(1));
                    int toId = Integer.parseInt(matcher.group(2));

                    if (matcher.group(3) != null) {
                        int weight = Integer.parseInt(matcher.group(3));
                        graph.addEdge(fromId, toId, weight);
                    } else {
                        graph.addEdge(fromId, toId);
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    /**
     * Exports the graph as a String using standard DOT language format[cite: 32].
     *
     * @return DOT formatted String representation of the graph[cite: 32]
     */
    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");

        List<Node> nodes = getAllNodes();
        Collections.sort(nodes); // Reproducibility requirement (Section 5.6)[cite: 32]

        for (Node u : nodes) {
            List<Edge> edges = getOutEdges(u);
            Collections.sort(edges); // Reproducibility requirement (Section 5.6)[cite: 32]

            for (Edge e : edges) {
                sb.append("  ").append(e.from().getId()).append(" -> ").append(e.to().getId());
                if (e.isWeighted()) {
                    sb.append(" [label=").append(e.getWeight()).append(", len=").append(e.getWeight()).append("]");
                }
                sb.append("\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Exports the graph to a file in DOT syntax with default extension '.gv'[cite: 32].
     *
     * @param fileName absolute path without extension[cite: 32]
     */
    public void toDotFile(String fileName) {
        toDotFile(fileName, "gv");
    }

    /**
     * Exports the graph to a file in DOT syntax with a custom extension[cite: 32].
     *
     * @param fileName absolute path without extension[cite: 32]
     * @param extension custom file extension[cite: 32]
     */
    public void toDotFile(String fileName, String extension) {
        String fullPath = fileName + "." + extension;
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(fullPath))) {
            writer.print(toDotString());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }



}
