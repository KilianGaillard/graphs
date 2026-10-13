package m1graphs2026;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
* This file presents the Node class. Written in Java, that
* class implements the definition of a Node in the
* Graph representation. That definition is coded
* with some useful methods required by the subject.
Each node is represented by an unique identifier, a
graph which is the graph where he's present, and 
optionnaly a name.
*/

public class Node implements Comparable<Node>{

    /**
     * These are the three parameters representing
     * a graph. The first one is the unique identifier
     * @param id . The second one is the optionnal one,
     * the @param name . And finally, @param graph keep
     * the information of where the node is from.
     */
    private final int id;
    private String name;
    private final Graph graph;

    /**
     * The first constructor uses two parameters :
     * @param id which provides the identifier of the
     * graph, and the @param graph associated.
     */
    Node(int id, Graph graph) {
        this.id = id;
        this.graph = graph;
    }

    /**
     * The second constructor uses three parameters
     * @param id for the identifier
     * @param name for the optionnal name
     * @param graph for the Graph associated
     */
    Node(int id, String name, Graph graph) {
        this.id = id;
        this.name = name;
        this.graph = graph;
    }

    /**
     * The first override function to code.
     * Override the equals method to evaluate
     * if a node equals an other node.
     * They are equal if their id is the same,
     * or if they are the same object (@return true).
     * If not, the method @return false, and also
     * if the obect is null, or if the class of the 
     * object is not a Node.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override 
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Node other = (Node) o;
        return this.id == other.id;
    }

    /**
     * The second override method.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** 
     * The last one method to override. It compares
     * two identifiers of nodes, and @return a number,
     * changing if the identifier of the node given
     *  in parameters is smaller, equal or greater than
     * the current node.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node node_parameter) {
        // Retourne un nombre négatif si 'this' est plus petit, positif si plus grand, 0 si égal
        return Integer.compare(this.id, node_parameter.id);
    }

    /**
     * This method simply is a getter, which
     * @return id which is the id of the node
     */
    public int getId(){return id;}

    /**
     * This method simply is a getter, which
     * @return name which is the name of the node
     */
    public String getName(){return name;}

    /**
     * This method simply is a getter, which
     * @return graph which is the graph where the node is from
     */
    public Graph getGraph(){return graph;}


    public List<Node> getSuccessors(){
        Set<Node> result = new LinkedHashSet<>();   // garde l'ordre d'insertion
        for(Edge e : getOutEdges()){
            result.add(e.to());
        }
        return new ArrayList<>(result);
    }

    public List<Node> getSuccessorsMulti(){
        List<Node> result = new ArrayList<>();
        for(Edge e : getOutEdges()){
            result.add(e.to());
        }
        return result;
    }

    
    public boolean adjacent(Node u){
        List<Node> results = new ArrayList<>();
        results = this.getSuccessors();
        for(Node n : results){
            if(n==u){
                return true;
            }
        }
        return false;
    }
    public boolean adjacent(int id_other){
        List<Node> results = new ArrayList<>();
        results = this.getSuccessors();
        for(Node n : results){
            if(n.getId()==id_other){
                return true;
            }
        }
        return false;
    }

    public int inDegree(){
        int count = 0;
        for(List<Edge> edges : this.getGraph().adjEdList.values()){
            if(edges != null){
                for(Edge e : edges){
                    if(e.to()==this){
                        count++;
                    }
                }
                
            }
        }
        return count;
    }

    public int outDegree(){
        return getOutEdges().size();
    }

    public int degree(){
        int in = this.inDegree();
        int out = this.outDegree();
        return in+out;
    }
    public List<Edge> getOutEdges(){
        return graph.getOutEdges(this);
    }
    public List<Edge> getInEdges(){
        List<Edge> results = new ArrayList<>();
        for(List<Edge> edges : this.getGraph().adjEdList.values()){
            if(edges != null){
                for(Edge e : edges){
                    if(e.to()==this){
                        results.add(e);
                    }
                }
                
            }
        }
        return results;
    }

    public List<Edge> getIncidentEdges(){
        List<Edge> in_list = this.getInEdges();
        List<Edge> out_list = this.getOutEdges();
        List<Edge> results = new ArrayList<>();
        for(Edge e : in_list) {
            results.add(e);
        }
        for(Edge e2 : out_list) {
            results.add(e2);
        }
        return results;
    }
    public List<Edge> getEdgesTo(Node u){
        List<Edge> out_list = this.getOutEdges();
        List<Edge> results = new ArrayList<>();
        for(Edge e : out_list) {
            if(e.to()==u){
                results.add(e);
            }
        }
        return results;
    }

}
