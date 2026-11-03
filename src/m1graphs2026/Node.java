package m1graphs2026;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a node (vertex) of a {@link Graph}.
 *
 * <p>Each node is characterised by a unique integer identifier, by the graph
 * it belongs to, and optionally by a name. The identifier and the graph cannot
 * change once the node is built; the name is optional and may be {@code null}.</p>
 *
 * <p>Two nodes are equal if and only if they have the same identifier. Nodes
 * are ordered by increasing identifier.</p>
 *
 * <p>Most methods of this class are convenient shortcuts that query the graph
 * the node belongs to.</p>
 */
public class Node implements Comparable<Node> {

    /** The unique identifier of the node. */
    private final int id;

    /** The optional name of the node ({@code null} if the node has no name). */
    private String name;

    /** The graph in which the node is present. */
    private final Graph graph;

    /**
     * Builds a node without a name.
     *
     * @param id    the unique identifier of the node
     * @param graph the graph the node belongs to
     */
    public Node(int id, Graph graph) {
        this.id = id;
        this.graph = graph;
    }

    /**
     * Builds a node with a name.
     *
     * @param id    the unique identifier of the node
     * @param name  the optional name of the node
     * @param graph the graph the node belongs to
     */
    public Node(int id, String name, Graph graph) {
        this.id = id;
        this.name = name;
        this.graph = graph;
    }

    /**
     * Tests whether this node is equal to another object. Two nodes are equal
     * if they are the same object or if they have the same identifier (the name
     * and the graph are not taken into account).
     *
     * @param o the object to compare this node with
     * @return {@code true} if {@code o} is the same object as this node, or a
     *         {@code Node} with the same identifier; {@code false} otherwise
     *         (including when {@code o} is {@code null} or is not a {@code Node})
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node other = (Node) o;
        return this.id == other.id;
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}, based only
     * on the identifier of the node.
     *
     * @return the hash code of this node
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compares the identifier of this node with the identifier of another node.
     *
     * @param node_parameter the node to compare this node with (must not be
     *                       {@code null})
     * @return a negative integer if the identifier of this node is smaller than
     *         the one of {@code node_parameter}, zero if they are equal, and a
     *         positive integer if it is greater
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node node_parameter) {
        return Integer.compare(this.id, node_parameter.id);
    }

    /**
     * Getter for the identifier of this node.
     *
     * @return the identifier of the node
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the name of this node.
     *
     * @return the name of the node, or {@code null} if the node has no name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the graph this node belongs to.
     *
     * @return the graph in which the node is present
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Computes the successors of this node, i.e. the targets of its outgoing
     * edges. Each successor appears only once, even if several edges lead to it
     * (see {@link #getSuccessorsMulti()} to keep the duplicates). The order of
     * the outgoing edges in the graph is preserved.
     *
     * @return a new list containing the distinct successors of the node
     *         (empty if the node has no outgoing edge)
     */
    public List<Node> getSuccessors() {
        Set<Node> result = new LinkedHashSet<>();
        for (Edge e : getOutEdges()) {
            result.add(e.to());
        }
        return new ArrayList<>(result);
    }

    /**
     * Computes the successors of this node with multiplicity: a node reached by
     * {@code k} parallel outgoing edges appears {@code k} times in the result.
     * The order of the outgoing edges in the graph is preserved.
     *
     * @return a new list containing one successor per outgoing edge (empty if
     *         the node has no outgoing edge)
     */
    public List<Node> getSuccessorsMulti() {
        List<Node> result = new ArrayList<>();
        for (Edge e : getOutEdges()) {
            result.add(e.to());
        }
        return result;
    }

    /**
     * Tests whether the given node is a successor of this node, i.e. whether
     * there is at least one edge going from this node to {@code u}. The test is
     * oriented: it does not consider the edges going from {@code u} to this
     * node. The nodes are compared by reference.
     *
     * @param u the node to look for among the successors of this node
     * @return {@code true} if {@code u} is a successor of this node;
     *         {@code false} otherwise
     */
    public boolean adjacent(Node u) {
        List<Node> results = new ArrayList<>();
        results = this.getSuccessors();
        for (Node n : results) {
            if (n == u) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether the node with the given identifier is a successor of this
     * node, i.e. whether there is at least one edge going from this node to it.
     * The test is oriented: it does not consider the edges going in the
     * opposite direction.
     *
     * @param id_other the identifier of the node to look for among the
     *                 successors of this node
     * @return {@code true} if a successor of this node has the identifier
     *         {@code id_other}; {@code false} otherwise
     */
    public boolean adjacent(int id_other) {
        List<Node> results = new ArrayList<>();
        results = this.getSuccessors();
        for (Node n : results) {
            if (n.getId() == id_other) {
                return true;
            }
        }
        return false;
    }

    /**
     * Computes the in-degree of this node, i.e. the number of edges of the
     * graph whose target is this node. A self-loop counts once, and parallel
     * edges are counted individually. The whole adjacency structure of the
     * graph is scanned.
     *
     * @return the number of incoming edges of the node
     */
    public int inDegree() {
        int count = 0;
        for (List<Edge> edges : this.getGraph().adjEdList.values()) {
            if (edges != null) {
                for (Edge e : edges) {
                    if (e.to() == this) {
                        count++;
                    }
                }

            }
        }
        return count;
    }

    /**
     * Computes the out-degree of this node, i.e. the number of edges of the
     * graph whose source is this node. A self-loop counts once, and parallel
     * edges are counted individually.
     *
     * @return the number of outgoing edges of the node
     */
    public int outDegree() {
        return getOutEdges().size();
    }

    /**
     * Computes the degree of this node as the sum of its in-degree and of its
     * out-degree. Consequently, a self-loop is counted twice.
     *
     * @return the in-degree plus the out-degree of the node
     * @see #inDegree()
     * @see #outDegree()
     */
    public int degree() {
        int in = this.inDegree();
        int out = this.outDegree();
        return in + out;
    }

    /**
     * Gets the outgoing edges of this node, i.e. the edges of the graph whose
     * source is this node.
     *
     * @return a new list containing the outgoing edges of the node (empty if
     *         there is none); modifying this list does not modify the graph
     */
    public List<Edge> getOutEdges() {
        return graph.getOutEdges(this);
    }

    /**
     * Gets the incoming edges of this node, i.e. the edges of the graph whose
     * target is this node. The whole adjacency structure of the graph is
     * scanned.
     *
     * @return a new list containing the incoming edges of the node (empty if
     *         there is none); modifying this list does not modify the graph
     */
    public List<Edge> getInEdges() {
        List<Edge> results = new ArrayList<>();
        for (List<Edge> edges : this.getGraph().adjEdList.values()) {
            if (edges != null) {
                for (Edge e : edges) {
                    if (e.to() == this) {
                        results.add(e);
                    }
                }

            }
        }
        return results;
    }

    /**
     * Gets the edges incident to this node, i.e. its incoming edges followed by
     * its outgoing edges. A self-loop therefore appears twice in the result.
     *
     * @return a new list containing the incoming edges and then the outgoing
     *         edges of the node
     * @see #getInEdges()
     * @see #getOutEdges()
     */
    public List<Edge> getIncidentEdges() {
        List<Edge> in_list = this.getInEdges();
        List<Edge> out_list = this.getOutEdges();
        List<Edge> results = new ArrayList<>();
        for (Edge e : in_list) {
            results.add(e);
        }
        for (Edge e2 : out_list) {
            results.add(e2);
        }
        return results;
    }

    /**
     * Gets all the edges going from this node to the given node. Several edges
     * are returned if the graph contains parallel edges. Only the direction
     * {@code this -> u} is considered, and the target is compared by reference.
     *
     * @param u the target node
     * @return a new list containing the edges whose source is this node and
     *         whose target is {@code u} (empty if there is none)
     */
    public List<Edge> getEdgesTo(Node u) {
        List<Edge> out_list = this.getOutEdges();
        List<Edge> results = new ArrayList<>();
        for (Edge e : out_list) {
            if (e.to() == u) {
                results.add(e);
            }
        }
        return results;
    }

    /**
     * Builds a textual representation of the node: {@code name(id)} if the node
     * has a name, or just the identifier otherwise.
     *
     * @return the string representation of the node
     */
    @Override
    public String toString() {
        return name != null ? name + "(" + id + ")" : String.valueOf(id);
    }

}