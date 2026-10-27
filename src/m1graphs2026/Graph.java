package m1graphs2026;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Enumeration representing the color state of a node during graph traversal.
 */
enum NodeColor { WHITE, GRAY, BLACK }

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


    public boolean adjacent(Node u, Node v){
        return (existsEdge(u, v)|| existsEdge(v, u));
    }

    public int inDegree(Node n){return n.inDegree();}


    public int outDegree(Node n){return n.outDegree();}


    public int degree(Node n){return n.degree();}


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


    public boolean removeEdge(Node from, Node to){
        if(!usesNode(from)||!usesNode(to)){
            return false;
        }
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            Node node = entry.getKey();
            if(node.equals(from)){
               for(Edge e : entry.getValue()){
                    if(e.from().equals(from)&&e.to().equals(to)){
                        adjEdList.remove(node,e);
                        return true;
                    }
               }
            }
        }
        return false;
    }


    public boolean removeEdge(Node from, Node to, int weight){
        return(removeEdge(from,to));
    }


    public boolean removeEdge(int id1, int id2){
        if(!usesNode(id1)||!usesNode(id2)){
            return false;
        }
        return removeEdge(getNode(id1),getNode(id2));
    }

    public boolean removeEdge(int id1, int id2, int weight){
        if(!usesNode(id1)||!usesNode(id2)){
            return false;
        }
        return removeEdge(getNode(id1),getNode(id2),weight);
    }

    public boolean removeEdge(Edge e){
        if(!existsEdge(e)){
           return false;
        }
        return removeEdge(e.from(),e.to());
    }

    public boolean removeEdge(Edge e, int weight){
        if(!existsEdge(e)){
           return false;
        }
        return removeEdge(e.from(),e.to(),weight);
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

    public List<Edge> getAllEdges(){
        List<Edge> res = new ArrayList<>();
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            for(Edge e : entry.getValue()){
                res.add(e);
            }
        }
        return res;
    }


    public int [] toSuccessorArray(){
        int[] res = new int[nbEdges()+nbNodes()];
        int insertion = 0;
        for(Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()){
            for(Edge e : entry.getValue()){
                res[insertion] = e.to().getId();
                insertion++;
            }
            res[insertion] = 0;
            insertion++;
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
        return isMultiGraph() && hasSelfLoops();
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
        return new Graph(this.toSuccessorArray());
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
