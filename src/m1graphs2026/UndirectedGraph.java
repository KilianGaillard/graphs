package m1graphs2026;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an undirected graph.
 *
 * <p>An undirected graph is stored as a directed {@link Graph} in which every
 * edge {u, v} with {@code u != v} is represented by two symmetric edges: one
 * from {@code u} to {@code v} and one from {@code v} to {@code u}. A self-loop
 * is stored only once. The methods inherited from {@link Graph} work on this
 * symmetric representation, and the methods overridden here adapt the
 * behaviour (edge insertion and removal, edge count, degrees, DOT export,
 * etc.) so that each undirected edge is seen as a single edge.</p>
 */
public class UndirectedGraph extends Graph {

    /**
     * Builds an empty undirected graph (no node, no edge).
     */
    public UndirectedGraph() {
        super();
    }

    /**
     * Builds an undirected graph from a successor array. The array lists, for
     * each node {@code 1, 2, ..., n} in turn, the identifiers of its
     * successors, each list being terminated by {@code 0}. Since the graph is
     * undirected, each listed edge is also stored in the opposite direction.
     *
     * @param sa the successor array, which must end with {@code 0} and contain
     *           no negative value
     * @throws IllegalArgumentException if the array is {@code null}, does not
     *                                  end with {@code 0} or contains a
     *                                  negative value
     * @see Graph#Graph(int...)
     */
    public UndirectedGraph(int... sa) {
        super(sa);
    }

    /**
     * Adds an unweighted undirected edge between two nodes. The edge is added
     * from {@code from} to {@code to} and, if the two nodes are different, the
     * symmetric edge from {@code to} to {@code from} is added as well. The
     * nodes are added to the graph if they are not already present.
     *
     * @param from one end of the edge
     * @param to   the other end of the edge
     */
    @Override
    public void addEdge(Node from, Node to) {
        super.addEdge(from, to);
        if (!from.equals(to)) {
            Node realTo = getNode(to.getId());
            Node realFrom = getNode(from.getId());
            adjEdList.get(realTo).add(new Edge(realTo, realFrom));
        }
    }

    /**
     * Adds a weighted undirected edge between two nodes. The edge is added
     * from {@code from} to {@code to} and, if the two nodes are different, the
     * symmetric edge from {@code to} to {@code from} (with the same weight) is
     * added as well. The nodes are added to the graph if they are not already
     * present.
     *
     * @param from   one end of the edge
     * @param to     the other end of the edge
     * @param weight the weight of the edge
     */
    @Override
    public void addEdge(Node from, Node to, int weight) {
        super.addEdge(from, to, weight);
        if (!from.equals(to)) {
            Node realTo = getNode(to.getId());
            Node realFrom = getNode(from.getId());
            adjEdList.get(realTo).add(new Edge(realTo, realFrom, weight));
        }
    }

    /**
     * Removes an undirected edge between two nodes, whatever its weight. One
     * edge is removed from {@code from} to {@code to} and one edge is removed
     * from {@code to} to {@code from}, if they exist. If the nodes are
     * parallel-connected, only one occurrence per direction is removed.
     *
     * @param from one end of the edge
     * @param to   the other end of the edge
     * @return {@code true} if at least one of the two directed edges was found
     *         and removed; {@code false} otherwise
     */
    @Override
    public boolean removeEdge(Node from, Node to) {
        boolean r1 = super.removeEdge(from, to);
        boolean r2 = super.removeEdge(to, from);
        return r1 || r2;
    }

    /**
     * Removes an undirected edge with a given weight between two nodes. One
     * edge with this weight is removed from {@code from} to {@code to} and one
     * edge with this weight is removed from {@code to} to {@code from}, if they
     * exist.
     *
     * @param from   one end of the edge
     * @param to     the other end of the edge
     * @param weight the weight of the edge to remove ({@code null} to remove an
     *               unweighted edge)
     * @return {@code true} if at least one of the two directed edges was found
     *         and removed; {@code false} otherwise
     */
    @Override
    public boolean removeEdge(Node from, Node to, Integer weight) {
        boolean r1 = super.removeEdge(from, to, weight);
        boolean r2 = super.removeEdge(to, from, weight);
        return r1 || r2;
    }

    /**
     * Computes the number of undirected edges of the graph. Each non-loop edge
     * is stored twice in the underlying structure but is counted once here,
     * whereas each self-loop (stored once) is counted once.
     *
     * @return the number of undirected edges of the graph
     */
    @Override
    public int nbEdges() {
        int totalEdges = super.nbEdges();
        int selfLoops = 0;
        for (Edge e : getAllEdges()) {
            if (e.isSelfLoop()) {
                selfLoops++;
            }
        }
        return (totalEdges - selfLoops) / 2 + selfLoops;
    }

    /**
     * Gets the edges incident to a node. In an undirected graph, these are
     * exactly the edges stored as outgoing from the node.
     *
     * @param n the node whose incident edges are requested
     * @return a new list of the edges incident to {@code n} (empty if the node
     *         is not in the graph)
     */
    @Override
    public List<Edge> getIncidentEdges(Node n) {
        return getOutEdges(n);
    }

    /**
     * Gets the edges entering a node. In an undirected graph, they coincide
     * with the edges leaving it.
     *
     * @param n the node whose edges are requested
     * @return a new list of the edges incident to {@code n} (empty if the node
     *         is not in the graph)
     */
    @Override
    public List<Edge> getInEdges(Node n) {
        return getOutEdges(n);
    }

    /**
     * Computes the in-degree of a node. In an undirected graph it is equal to
     * the degree of the node.
     *
     * @param n the node whose in-degree is requested
     * @return the number of edges incident to {@code n}
     */
    @Override
    public int inDegree(Node n) {
        return getOutEdges(n).size();
    }

    /**
     * Computes the out-degree of a node. In an undirected graph it is equal to
     * the degree of the node.
     *
     * @param n the node whose out-degree is requested
     * @return the number of edges incident to {@code n}
     */
    @Override
    public int outDegree(Node n) {
        return getOutEdges(n).size();
    }

    /**
     * Computes the degree of a node, i.e. the number of edges incident to it.
     * A self-loop is stored once and is therefore counted once.
     *
     * @param n the node whose degree is requested
     * @return the number of edges incident to {@code n}
     */
    @Override
    public int degree(Node n) {
        return getOutEdges(n).size();
    }

    /**
     * Exports the graph as a String in the DOT language, as an undirected
     * graph ({@code graph G { ... }} with {@code --} connectors). Nodes and
     * edges are sorted to guarantee a reproducible output. Each undirected
     * edge is written only once (even though it is stored in both directions),
     * while self-loops are always written. A weighted edge is written with a
     * {@code label} and a {@code len} attribute equal to its weight.
     *
     * @return the DOT representation of the graph
     */
    @Override
    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("graph G {\n");

        List<Node> nodes = getAllNodes();
        Collections.sort(nodes);

        // Keys of the undirected edges which have already been written
        Set<String> processedEdges = new HashSet<>();

        for (Node u : nodes) {
            List<Edge> edges = getOutEdges(u);
            Collections.sort(edges);

            for (Edge e : edges) {
                int id1 = e.from().getId();
                int id2 = e.to().getId();

                // The key does not depend on the direction in which the edge is read
                String key = id1 < id2 ? id1 + "--" + id2 : id2 + "--" + id1;
                if (e.isWeighted()) {
                    key += "[" + e.getWeight() + "]";
                }

                if (e.isSelfLoop() || !processedEdges.contains(key)) {
                    processedEdges.add(key);
                    sb.append("  ").append(id1).append(" -- ").append(id2);
                    if (e.isWeighted()) {
                        sb.append(" [label=").append(e.getWeight()).append(", len=").append(e.getWeight()).append("]");
                    }
                    sb.append("\n");
                }
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Builds a copy of this undirected graph: new nodes (belonging to the copy)
     * and new edges with the same end identifiers and the same weights. This
     * graph is not modified, and modifying the copy does not modify this graph.
     *
     * <p><b>Implementation choices:</b> the inherited {@link Graph#copy()}
     * cannot be used because it returns a plain directed {@link Graph}. The
     * copy cannot either be built by calling {@code addEdge} on every edge of
     * {@link #getAllEdges()}: each undirected edge is already stored in both
     * directions, and {@code addEdge} would add both directions again, which
     * would duplicate every edge. Each stored edge is therefore copied
     * as-is into the adjacency list of its (copied) source node, which
     * reproduces exactly the symmetric structure of this graph, including its
     * self-loops (stored once) and its parallel edges.</p>
     *
     * @return a new {@code UndirectedGraph} with the same nodes and the same
     *         edges (and weights) as this graph
     */
    @Override
    public UndirectedGraph copy() {
        UndirectedGraph copyGraph = new UndirectedGraph();
        for (Node n : getAllNodes()) {
            copyGraph.addNode(new Node(n.getId(), n.getName(), copyGraph));
        }
        for (Edge e : getAllEdges()) {
            Node newFrom = copyGraph.getNode(e.from().getId());
            Node newTo = copyGraph.getNode(e.to().getId());
            // Direct insertion: the symmetric edge is itself part of getAllEdges()
            copyGraph.adjEdList.get(newFrom).add(new Edge(newFrom, newTo, e.getWeight()));
        }
        return copyGraph;
    }

    /**
     * Builds the reverse of this graph. Reversing all the edges of an
     * undirected graph gives back the same edges, so the result has the same
     * nodes and the same edges as this graph. This graph is not modified.
     *
     * @return a new undirected graph containing the nodes and the edges of this
     *         graph
     */
    @Override
    public UndirectedGraph getReverse() {
        UndirectedGraph rev = new UndirectedGraph();
        for (Node n : getAllNodes()) rev.addNode(n.getId());
        for (Edge e : getAllEdges()) {
            if (e.isWeighted()) rev.addEdge(e.from().getId(), e.to().getId(), e.getWeight());
            else rev.addEdge(e.from().getId(), e.to().getId());
        }
        return rev;
    }

    /**
     * Builds an undirected graph from a DOT file with the default extension
     * {@code .gv}.
     *
     * @param filename the path of the file, without its extension
     * @return the undirected graph read from {@code filename.gv}, or
     *         {@code null} if the file could not be read or parsed
     * @see Graph#fromDotFile(String, String, Class)
     */
    public static UndirectedGraph fromDotFile(String filename) {
        return Graph.fromDotFile(filename, "gv", UndirectedGraph.class);
    }

    /**
     * Builds an undirected graph from a DOT file with a custom extension.
     *
     * @param filename  the path of the file, without its extension
     * @param extension the extension of the file, without the dot
     * @return the undirected graph read from {@code filename.extension}, or
     *         {@code null} if the file could not be read or parsed
     * @see Graph#fromDotFile(String, String, Class)
     */
    public static UndirectedGraph fromDotFile(String filename, String extension) {
        return Graph.fromDotFile(filename, extension, UndirectedGraph.class);
    }

    /**
     * Computes the transitive closure of this graph: an edge is present between
     * two nodes {@code u} and {@code v} if there is a path between them in this
     * graph. The closure is computed on the underlying symmetric structure
     * (see {@link Graph#getTransitiveClosure()}) and the result is converted
     * into an undirected graph. This graph is not modified.
     *
     * @return a new undirected graph with the same nodes as this graph, whose
     *         (unweighted) edges represent the reachability relation
     */
    @Override
    public UndirectedGraph getTransitiveClosure() {
        Graph closureOriented = super.getTransitiveClosure();
        UndirectedGraph res = new UndirectedGraph();
        for (Node n : closureOriented.getAllNodes()) {
            res.addNode(n.getId());
        }
        for (Edge e : closureOriented.getAllEdges()) {
            res.addEdge(e.from().getId(), e.to().getId());
        }
        return res;
    }
}