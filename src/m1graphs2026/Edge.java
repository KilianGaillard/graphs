package m1graphs2026;

import java.util.Objects;

/**
 * Represents an edge (or arc) of a {@link Graph}.
 *
 * <p>An edge is defined by its source node ({@code from}), its target node
 * ({@code to}) and an optional integer weight. When the weight is
 * {@code null}, the edge is considered unweighted.</p>
 *
 * <p>Instances are immutable: all fields are final and no setter is provided.</p>
 *
 * <p>Two edges are equal if they have the same source, the same target and the
 * same weight. Edges are ordered by source identifier, then by target
 * identifier, then by weight (an unweighted edge comes before a weighted one).</p>
 */
public class Edge implements Comparable<Edge> {

    /** The source node of the edge. */
    private final Node from;

    /** The target node of the edge. */
    private final Node to;

    /** The weight of the edge, or {@code null} if the edge is unweighted. */
    private final Integer weight;

    /**
     * Builds an unweighted edge between two nodes.
     *
     * @param from the source node of the edge
     * @param to   the target node of the edge
     */
    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.weight = null;
    }

    /**
     * Builds a weighted edge between two nodes.
     *
     * @param from   the source node of the edge
     * @param to     the target node of the edge
     * @param weight the weight of the edge ({@code null} for an unweighted edge)
     */
    public Edge(Node from, Node to, Integer weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Builds an unweighted edge from the identifiers of its two end nodes.
     * The nodes are looked up in the given graph with {@link Graph#getNode(int)}.
     * If an identifier does not match any node of the graph, the corresponding
     * end of the edge is {@code null}.
     *
     * @param id_from the identifier of the source node
     * @param id_to   the identifier of the target node
     * @param graph   the graph in which the two nodes are searched
     */
    public Edge(int id_from, int id_to, Graph graph) {
        this.from = graph.getNode(id_from);
        this.to = graph.getNode(id_to);
        this.weight = null;
    }

    /**
     * Builds a weighted edge from the identifiers of its two end nodes.
     * The nodes are looked up in the given graph with {@link Graph#getNode(int)}.
     * If an identifier does not match any node of the graph, the corresponding
     * end of the edge is {@code null}.
     *
     * @param id_from the identifier of the source node
     * @param id_to   the identifier of the target node
     * @param graph   the graph in which the two nodes are searched
     * @param weight  the weight of the edge ({@code null} for an unweighted edge)
     */
    public Edge(int id_from, int id_to, Graph graph, Integer weight) {
        this.from = graph.getNode(id_from);
        this.to = graph.getNode(id_to);
        this.weight = weight;
    }

    /**
     * Tests whether this edge is equal to another object.
     * Two edges are equal if the other object is also an {@code Edge} and if
     * their source nodes, their target nodes and their weights are all equal
     * (a {@code null} weight is only equal to another {@code null} weight).
     *
     * @param o the object to compare this edge with
     * @return {@code true} if {@code o} is an edge with the same source, target
     *         and weight as this edge; {@code false} otherwise (including when
     *         {@code o} is {@code null} or is not an {@code Edge})
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(from, edge.from) &&
            Objects.equals(to, edge.to) &&
            Objects.equals(weight, edge.weight);
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}, based on
     * the source node, the target node and the weight.
     *
     * @return the hash code of this edge
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to, weight);
    }

    /**
     * Compares this edge with another one. The comparison is done successively
     * on: the identifier of the source node, the identifier of the target node,
     * and finally the weight. An unweighted edge ({@code null} weight) is
     * considered smaller than any weighted edge, and two unweighted edges are
     * considered equal on this criterion.
     *
     * @param other the edge to compare this edge with (must not be {@code null})
     * @return a negative integer, zero or a positive integer if this edge is
     *         respectively smaller than, equal to or greater than {@code other}
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Edge other) {
        if (this.from.getId() != other.from.getId()) {
            return Integer.compare(this.from.getId(), other.from.getId());
        }
        if (this.to.getId() != other.to.getId()) {
            return Integer.compare(this.to.getId(), other.to.getId());
        }

        // Null-safe handling of the weights
        if (this.weight == null && other.weight == null) return 0;
        if (this.weight == null) return -1;
        if (other.weight == null) return 1;

        return Integer.compare(this.weight, other.weight);
    }

    /**
     * Getter for the source node of this edge.
     *
     * @return the source node of the edge
     */
    public Node from() {
        return this.from;
    }

    /**
     * Getter for the target node of this edge.
     *
     * @return the target node of the edge
     */
    public Node to() {
        return this.to;
    }

    /**
     * Builds the symmetric edge of this edge, i.e. the edge going in the
     * opposite direction with the same weight. This edge is not modified.
     *
     * @return a new edge whose source is the target of this edge, whose target
     *         is the source of this edge, and which has the same weight
     */
    public Edge getSymmetric() {
        return new Edge(this.to, this.from, this.weight);
    }

    /**
     * Tests whether this edge is a self-loop, i.e. an edge whose source and
     * target are the same node (compared by reference).
     *
     * @return {@code true} if the source and the target are the same node
     *         object; {@code false} otherwise
     */
    public boolean isSelfLoop() {
        return this.from == this.to;
    }

    /**
     * Tests whether this edge is a multi-edge, i.e. whether its source node
     * is linked to its target node by at least two edges in the graph owning
     * the source node. The test is delegated to {@link Graph#isMultiEdge(Edge)}.
     *
     * @return {@code true} if at least two edges go from the source to the
     *         target in the graph; {@code false} otherwise, or if the source
     *         node or its graph is {@code null}
     */
    public boolean isMultiEdge() {
        if (from == null || from.getGraph() == null) return false;
        return from.getGraph().isMultiEdge(this);
    }

    /**
     * Tests whether this edge carries a weight.
     *
     * @return {@code true} if the weight is not {@code null}; {@code false}
     *         otherwise
     */
    public boolean isWeighted() {
        return this.weight != null;
    }

    /**
     * Getter for the weight of this edge.
     *
     * @return the weight of the edge, or {@code null} if the edge is unweighted
     */
    public Integer getWeight() {
        return this.weight;
    }

}