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
 * Enumeration representing the color state of a node during a graph traversal.
 */
enum NodeColor {
    /** The node has not been discovered yet. */
    WHITE,
    /** The node has been discovered but its exploration is not finished. */
    GRAY,
    /** The node and all its successors have been completely explored. */
    BLACK
}

/**
 * Enumeration representing the classification of an edge during an enriched
 * Depth-First Search (DFS) traversal.
 */
enum EdgeVisitType {
    /** Tree edge of the DFS forest. */
    TREE,
    /** Back edge leading to an ancestor in the DFS tree. */
    BACKWARD,
    /** Forward edge leading to a non-child descendant in the DFS tree. */
    FORWARD,
    /** Cross edge leading to a node already completed or in another tree. */
    CROSS
}

/**
 * Encapsulates the traversal metadata of a node during an enriched
 * Depth-First Search (DFS): its color, its predecessor in the search forest,
 * its discovery timestamp and its finish timestamp.
 */
class NodeVisitInfo {

    /** The current color of the node. */
    private NodeColor color;

    /** The parent of the node in the DFS forest ({@code null} for a root). */
    private Node predecessor;

    /** The timestamp at which the node was discovered ({@code null} if unvisited). */
    private Integer discoveryTime;

    /** The timestamp at which the exploration of the node ended ({@code null} if not finished). */
    private Integer finishTime;

    /**
     * Builds a new visit information with the given attributes.
     *
     * @param color         the current color of the node (WHITE, GRAY or BLACK)
     * @param predecessor   the parent node in the DFS forest, or {@code null}
     *                      if the node is a root or is unvisited
     * @param discoveryTime the timestamp at which the node was first
     *                      discovered, or {@code null} if unvisited
     * @param finishTime    the timestamp at which the exploration of the node
     *                      was completed, or {@code null} if not finished
     */
    public NodeVisitInfo(NodeColor color, Node predecessor, Integer discoveryTime, Integer finishTime) {
        this.color = color;
        this.predecessor = predecessor;
        this.discoveryTime = discoveryTime;
        this.finishTime = finishTime;
    }

    /**
     * Getter for the color of the node.
     *
     * @return the current color of the node
     */
    public NodeColor getColor() {
        return color;
    }

    /**
     * Setter for the color of the node.
     *
     * @param color the new color of the node
     */
    public void setColor(NodeColor color) {
        this.color = color;
    }

    /**
     * Getter for the predecessor of the node in the DFS forest.
     *
     * @return the predecessor node, or {@code null} if there is none
     */
    public Node getPredecessor() {
        return predecessor;
    }

    /**
     * Setter for the predecessor of the node in the DFS forest.
     *
     * @param predecessor the new parent node (may be {@code null})
     */
    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    /**
     * Getter for the discovery timestamp of the node.
     *
     * @return the discovery timestamp, or {@code null} if the node is unvisited
     */
    public Integer getDiscoveryTime() {
        return discoveryTime;
    }

    /**
     * Setter for the discovery timestamp of the node.
     *
     * @param discoveryTime the time at which the node was discovered
     */
    public void setDiscoveryTime(Integer discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    /**
     * Getter for the finish timestamp of the node.
     *
     * @return the finish timestamp, or {@code null} if the exploration of the
     *         node is not complete
     */
    public Integer getFinishTime() {
        return finishTime;
    }

    /**
     * Setter for the finish timestamp of the node.
     *
     * @param finishTime the time at which the exploration of the node was completed
     */
    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }
}

/**
 * Represents a directed multigraph, stored as an adjacency list of edges.
 *
 * <p>The graph associates each of its nodes with the list of its outgoing
 * edges. Self-loops and parallel (multiple) edges are allowed, and edges may
 * optionally be weighted. Nodes are identified by their integer identifier
 * (two nodes with the same identifier are considered to be the same node).</p>
 *
 * <p>Whenever a traversal or an export is performed, nodes and edges are
 * sorted by increasing identifiers so that the results are reproducible.</p>
 *
 * <p>The class {@link UndirectedGraph} extends this class to represent
 * undirected graphs.</p>
 */
public class Graph {

    /** The adjacency structure: each node is mapped to the list of its outgoing edges. */
    Map<Node, List<Edge>> adjEdList;

    /**
     * The default constructor, which builds an empty graph (no node, no edge).
     */
    public Graph() {
        this.adjEdList = new HashMap<>();
    }

    /**
     * Builds a graph from a successor array. The array lists, for each node
     * {@code 1, 2, ..., n} in turn, the identifiers of its successors, and the
     * list of each node is terminated by {@code 0}. For instance,
     * {@code {2, 3, 0, 3, 0, 0}} describes a graph with three nodes and the
     * edges 1 to 2, 1 to 3 and 2 to 3. The number of nodes is the number of
     * {@code 0} in the array. An empty array gives an empty graph.
     *
     * @param sa the successor array: it must end with {@code 0} and must not
     *           contain any negative value
     * @throws IllegalArgumentException if {@code sa} is {@code null}, if it is
     *                                  not empty and does not end with
     *                                  {@code 0}, or if it contains a negative
     *                                  value
     */
    public Graph(int... sa) {
        this();
        if (sa == null) {
            throw new IllegalArgumentException("Successor array is null");
        }
        if (sa.length > 0 && sa[sa.length - 1] != 0) {
            throw new IllegalArgumentException("Successor array must end with 0");
        }

        int nbNodes = 0;
        for (int v : sa) {
            if (v < 0) throw new IllegalArgumentException("Negative value: " + v);
            if (v == 0) nbNodes++;
        }

        for (int i = 1; i <= nbNodes; i++) {
            addNode(i);
        }

        int current = 1;
        for (int v : sa) {
            if (v == 0) {
                current++;
            } else {
                addEdge(current, v);
            }
        }
    }

    /**
     * Computes the number of nodes of the graph.
     *
     * @return the number of nodes
     */
    public int nbNodes() {
        return adjEdList.size();
    }

    /**
     * Tests whether the graph contains a node with the same identifier as the
     * given node (the node object itself may be a different instance).
     *
     * @param n the node to look for (must not be {@code null})
     * @return {@code true} if a node of the graph has the identifier of
     *         {@code n}; {@code false} otherwise
     */
    public boolean usesNode(Node n) {
        for (Node node : adjEdList.keySet()) {
            if (node.getId() == n.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether the graph contains a node with the given identifier.
     *
     * @param id the identifier to look for
     * @return {@code true} if a node of the graph has the identifier
     *         {@code id}; {@code false} otherwise
     */
    public boolean usesNode(int id) {
        for (Node node : adjEdList.keySet()) {
            if (node.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether the graph holds the given node, i.e. whether the node is a
     * key of the adjacency structure (nodes are compared with
     * {@link Node#equals(Object)}, hence by identifier).
     *
     * @param n the node to look for
     * @return {@code true} if the graph holds a node equal to {@code n};
     *         {@code false} otherwise
     */
    public boolean holdsNode(Node n) {
        return adjEdList.containsKey(n);
    }

    /**
     * Gets the node of the graph having the given identifier.
     *
     * @param id the identifier of the node
     * @return the node of the graph with this identifier, or {@code null} if
     *         there is none
     */
    public Node getNode(int id) {
        for (Node n : adjEdList.keySet()) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    /**
     * Adds a node to the graph. The node is added with an empty list of
     * outgoing edges. Nothing is done if a node equal to {@code n} (same
     * identifier) is already in the graph.
     *
     * @param n the node to add
     * @return {@code true} if the node was added; {@code false} if the graph
     *         already contained it
     */
    public boolean addNode(Node n) {
        if (adjEdList.containsKey(n)) return false;
        adjEdList.put(n, new ArrayList<>());
        return true;
    }

    /**
     * Adds to the graph a new node with the given identifier. Nothing is done
     * if the graph already contains a node with this identifier.
     *
     * @param id the identifier of the node to create
     * @return {@code true} if the node was created and added; {@code false} if
     *         the identifier was already used
     */
    public boolean addNode(int id) {
        if (usesNode(id)) {
            return false;
        }
        return addNode(new Node(id, this));
    }

    /**
     * Removes a node from the graph, together with all the edges leaving it and
     * all the edges entering it.
     *
     * @param n the node to remove
     * @return {@code false} if {@code n} is {@code null}; {@code true}
     *         otherwise (even if the node was not in the graph, in which case
     *         only the edges pointing to an equal node are removed)
     */
    public boolean removeNode(Node n) {

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

    /**
     * Removes from the graph the node with the given identifier, together with
     * all the edges leaving it and all the edges entering it.
     *
     * @param id the identifier of the node to remove
     * @return {@code true} if the node was found and removed; {@code false} if
     *         the graph has no node with this identifier
     */
    public boolean removeNode(int id) {
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

    /**
     * Gets all the nodes of the graph, in no particular order.
     *
     * @return a new list containing the nodes of the graph; modifying this list
     *         does not modify the graph
     */
    public List<Node> getAllNodes() {
        return new ArrayList<>(adjEdList.keySet());
    }

    /**
     * Computes the largest node identifier of the graph.
     *
     * @return the largest identifier among the nodes, or {@code -1} if the
     *         graph has no node
     */
    public int largestNodeId() {
        int max = -1;
        for (Node n : adjEdList.keySet()) {
            if (max < n.getId()) {
                max = n.getId();
            }
        }
        return max;
    }

    /**
     * Computes the smallest node identifier of the graph.
     *
     * @return the smallest identifier among the nodes, or
     *         {@link Integer#MAX_VALUE} if the graph has no node
     */
    public int smallestNodeId() {
        int min = Integer.MAX_VALUE;
        for (Node n : adjEdList.keySet()) {
            if (min > n.getId()) {
                min = n.getId();
            }
        }
        return min;
    }

    /**
     * Computes the successors of a node, i.e. the targets of its outgoing
     * edges. Each successor appears only once, even if several edges lead to
     * it (see {@link #getSuccessorsMulti(Node)} to keep the duplicates).
     *
     * @param n the node whose successors are requested
     * @return a new list containing the distinct successors of {@code n}
     *         (empty if {@code n} is not in the graph or has no outgoing edge)
     */
    public List<Node> getSuccessors(Node n) {
        Set<Node> result = new LinkedHashSet<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if (node.equals(n)) {
                List<Edge> edges = entry.getValue();
                for (Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return new ArrayList<>(result);

    }

    /**
     * Computes the successors of the node with the given identifier. Each
     * successor appears only once, even if several edges lead to it.
     *
     * @param id the identifier of the node whose successors are requested
     * @return a new list containing the distinct successors of the node (empty
     *         if the graph has no such node or if it has no outgoing edge)
     */
    public List<Node> getSuccessors(int id) {
        Set<Node> result = new LinkedHashSet<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if (node.getId() == id) {
                List<Edge> edges = entry.getValue();
                for (Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * Computes the successors of a node with multiplicity: a node reached by
     * {@code k} parallel edges from {@code n} appears {@code k} times in the
     * result.
     *
     * @param n the node whose successors are requested
     * @return a new list containing one successor per outgoing edge of
     *         {@code n} (empty if {@code n} is not in the graph or has no
     *         outgoing edge)
     */
    public List<Node> getSuccessorsMulti(Node n) {
        List<Node> result = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if (node.equals(n)) {
                List<Edge> edges = entry.getValue();
                for (Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return result;
    }

    /**
     * Computes the successors, with multiplicity, of the node with the given
     * identifier: a node reached by {@code k} parallel edges appears {@code k}
     * times in the result.
     *
     * @param id the identifier of the node whose successors are requested
     * @return a new list containing one successor per outgoing edge of the node
     *         (empty if the graph has no such node or if it has no outgoing edge)
     */
    public List<Node> getSuccessorsMulti(int id) {
        List<Node> result = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if (node.getId() == id) {
                List<Edge> edges = entry.getValue();
                for (Edge e : edges) {
                    result.add(e.to());
                }
            }
        }
        return result;
    }

    /**
     * Tests whether two nodes are adjacent, i.e. whether there is an edge from
     * {@code u} to {@code v} or an edge from {@code v} to {@code u}.
     *
     * @param u the first node
     * @param v the second node
     * @return {@code true} if an edge links the two nodes in at least one
     *         direction; {@code false} otherwise
     */
    public boolean adjacent(Node u, Node v) {
        return (existsEdge(u, v) || existsEdge(v, u));
    }

    /**
     * Computes the in-degree of a node, i.e. the number of edges entering it.
     *
     * @param n the node whose in-degree is requested
     * @return the number of edges whose target is {@code n}
     * @see Node#inDegree()
     */
    public int inDegree(Node n) {
        return n.inDegree();
    }

    /**
     * Computes the in-degree of the node with the given identifier.
     *
     * @param id the identifier of the node
     * @return the number of edges whose target is the node
     * @throws NullPointerException if the graph has no node with this identifier
     */
    public int inDegree(int id) {
        return inDegree(getNode(id));
    }

    /**
     * Computes the out-degree of a node, i.e. the number of edges leaving it.
     *
     * @param n the node whose out-degree is requested
     * @return the number of edges whose source is {@code n}
     * @see Node#outDegree()
     */
    public int outDegree(Node n) {
        return n.outDegree();
    }

    /**
     * Computes the out-degree of the node with the given identifier.
     *
     * @param id the identifier of the node
     * @return the number of edges whose source is the node
     * @throws NullPointerException if the graph has no node with this identifier
     */
    public int outDegree(int id) {
        return outDegree(getNode(id));
    }

    /**
     * Computes the degree of a node, i.e. the sum of its in-degree and of its
     * out-degree (a self-loop is therefore counted twice).
     *
     * @param n the node whose degree is requested
     * @return the in-degree plus the out-degree of {@code n}
     * @see Node#degree()
     */
    public int degree(Node n) {
        return n.degree();
    }

    /**
     * Computes the degree of the node with the given identifier, i.e. the sum
     * of its in-degree and of its out-degree.
     *
     * @param id the identifier of the node
     * @return the in-degree plus the out-degree of the node
     * @throws NullPointerException if the graph has no node with this identifier
     */
    public int degree(int id) {
        return degree(getNode(id));
    }

    /**
     * Computes the number of edges of the graph. Parallel edges and self-loops
     * are all counted.
     *
     * @return the total number of edges stored in the graph
     */
    public int nbEdges() {
        int count = 0;
        for (List<Edge> edges : adjEdList.values()) {
            for (Edge e : edges) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tests whether there is at least one edge going from {@code u} to
     * {@code v}. Only this direction is considered.
     *
     * @param u the source node
     * @param v the target node
     * @return {@code true} if such an edge exists; {@code false} otherwise
     *         (including when {@code u} or {@code v} is {@code null} or when
     *         {@code u} is not in the graph)
     */
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

    /**
     * Tests whether there is at least one edge going from the node with
     * identifier {@code id1} to the node with identifier {@code id2}.
     *
     * @param id1 the identifier of the source node
     * @param id2 the identifier of the target node
     * @return {@code true} if such an edge exists; {@code false} otherwise
     *         (including when one of the nodes is not in the graph)
     */
    public boolean existsEdge(int id1, int id2) {
        Node u = getNode(id1);
        Node v = getNode(id2);
        if (u == null || v == null) {
            return false;
        }
        return existsEdge(u, v);
    }

    /**
     * Tests whether the graph contains an edge with the same source and the
     * same target as the given edge (the weight is not taken into account).
     *
     * @param e the edge to look for
     * @return {@code true} if such an edge exists; {@code false} otherwise
     *         (including when {@code e} is {@code null})
     */
    public boolean existsEdge(Edge e) {
        if (e == null) {
            return false;
        }
        return existsEdge(e.from(), e.to());
    }

    /**
     * Tests whether the edges from {@code u} to {@code v} are multiple, i.e.
     * whether there are at least two edges going from {@code u} to {@code v}.
     *
     * @param u the source node
     * @param v the target node
     * @return {@code true} if at least two edges go from {@code u} to
     *         {@code v}; {@code false} otherwise (including when {@code u} or
     *         {@code v} is {@code null} or when {@code u} is not in the graph)
     */
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

    /**
     * Tests whether there are at least two edges going from the node with
     * identifier {@code id1} to the node with identifier {@code id2}.
     *
     * @param id1 the identifier of the source node
     * @param id2 the identifier of the target node
     * @return {@code true} if at least two such edges exist; {@code false}
     *         otherwise (including when one of the nodes is not in the graph)
     */
    public boolean isMultiEdge(int id1, int id2) {
        Node u = getNode(id1);
        Node v = getNode(id2);
        if (u == null || v == null) {
            return false;
        }
        return isMultiEdge(u, v);
    }

    /**
     * Tests whether the given edge is a multi-edge, i.e. whether there are at
     * least two edges (whatever their weights) with the same source and the
     * same target in the graph.
     *
     * @param e the edge to test
     * @return {@code true} if at least two edges go from the source to the
     *         target of {@code e}; {@code false} otherwise (including when
     *         {@code e} is {@code null})
     */
    public boolean isMultiEdge(Edge e) {
        if (e == null) {
            return false;
        }
        return isMultiEdge(e.from(), e.to());
    }

    /**
     * Adds an unweighted edge from {@code from} to {@code to}. The end nodes
     * are added to the graph if they are not already in it. The edge is
     * appended to the list of outgoing edges of {@code from}; parallel edges
     * are therefore allowed.
     *
     * @param from the source node of the new edge
     * @param to   the target node of the new edge
     */
    public void addEdge(Node from, Node to) {
        if (!usesNode(from)) {
            addNode(from);
        }
        if (!usesNode(to)) {
            addNode(to);
        }
        Edge e = new Edge(from, to);
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if (node.equals(from)) {
                this.adjEdList.get(node).add(e);
            }
        }

    }

    /**
     * Adds a weighted edge from {@code from} to {@code to}. The end nodes are
     * added to the graph if they are not already in it. The edge is appended to
     * the list of outgoing edges of {@code from}; parallel edges are therefore
     * allowed.
     *
     * @param from   the source node of the new edge
     * @param to     the target node of the new edge
     * @param weight the weight of the new edge
     */
    public void addEdge(Node from, Node to, int weight) {
        if (!usesNode(from)) {
            addNode(from);
        }
        if (!usesNode(to)) {
            addNode(to);
        }
        Edge e = new Edge(from, to, weight);
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            Node node = entry.getKey();
            if (node.equals(from)) {
                this.adjEdList.get(node).add(e);
            }
        }
    }

    /**
     * Adds an unweighted edge between the nodes with the given identifiers. The
     * nodes that do not exist yet are created and added to the graph.
     *
     * @param id1 the identifier of the source node
     * @param id2 the identifier of the target node
     */
    public void addEdge(int id1, int id2) {
        if (!usesNode(id1)) {
            Node from = new Node(id1, this);
            addNode(from);
            if (!usesNode(id2)) {
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(from, to);
            } else {
                addEdge(from, this.getNode(id2));
            }
        } else {
            if (!usesNode(id2)) {
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(getNode(id1), to);
            } else {
                addEdge(getNode(id1), getNode(id2));
            }

        }
    }

    /**
     * Adds a weighted edge between the nodes with the given identifiers. The
     * nodes that do not exist yet are created and added to the graph.
     *
     * @param id1    the identifier of the source node
     * @param id2    the identifier of the target node
     * @param weight the weight of the new edge
     */
    public void addEdge(int id1, int id2, int weight) {
        if (!usesNode(id1)) {
            Node from = new Node(id1, this);
            addNode(from);
            if (!usesNode(id2)) {
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(from, to, weight);
            } else {
                addEdge(from, this.getNode(id2), weight);
            }
        } else {
            if (!usesNode(id2)) {
                Node to = new Node(id2, this);
                addNode(to);
                addEdge(getNode(id1), to, weight);
            } else {
                addEdge(getNode(id1), getNode(id2), weight);
            }

        }
    }

    /**
     * Adds to the graph a copy of the given edge, with the same end nodes
     * (identified by their identifiers) and the same weight (if any). The nodes
     * that do not exist yet are created. Nothing is done if the edge is
     * {@code null} or if one of its end nodes is {@code null} or has the
     * identifier {@code 0} (which is reserved as a separator in successor
     * arrays).
     *
     * @param e the edge to add
     */
    public void addEdge(Edge e) {
        if (e == null) return;

        Node fromNode = e.from();
        Node toNode = e.to();

        int idFrom = (fromNode != null) ? fromNode.getId() : 0;
        int idTo = (toNode != null) ? toNode.getId() : 0;

        if (idFrom == 0 || idTo == 0) return; // Avoids creating an invalid node '0'

        if (e.isWeighted()) {
            addEdge(idFrom, idTo, e.getWeight());
        } else {
            addEdge(idFrom, idTo);
        }
    }

    /**
     * Adds to the graph an edge with the same end nodes as the given edge but
     * with the given weight, which replaces the weight of {@code e}. The nodes
     * that do not exist yet are created. Nothing is done if the edge is
     * {@code null} or if one of its end nodes is {@code null} or has the
     * identifier {@code 0}.
     *
     * @param e      the edge whose end nodes are used
     * @param weight the weight of the edge to add
     */
    public void addEdge(Edge e, int weight) {
        if (e == null) return;

        Node fromNode = e.from();
        Node toNode = e.to();

        int idFrom = (fromNode != null) ? fromNode.getId() : 0;
        int idTo = (toNode != null) ? toNode.getId() : 0;

        if (idFrom == 0 || idTo == 0) return;

        addEdge(idFrom, idTo, weight);
    }

    /**
     * Removes the first edge found from node {@code from} to node {@code to},
     * regardless of its weight.
     *
     * @param from the source node
     * @param to   the target node
     * @return {@code true} if an edge was found and removed, {@code false}
     *         otherwise
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
     * Removes the first edge from node {@code from} to node {@code to} that has
     * exactly the given weight.
     *
     * @param from   the source node
     * @param to     the target node
     * @param weight the weight of the edge to remove ({@code null} to remove an
     *               unweighted edge)
     * @return {@code true} if a matching edge was found and removed,
     *         {@code false} otherwise
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
     * Removes the first edge found between the nodes with the given
     * identifiers, regardless of its weight (overload of
     * {@link #removeEdge(Node, Node)} using node identifiers).
     *
     * @param id1 the identifier of the source node
     * @param id2 the identifier of the target node
     * @return {@code true} if an edge was found and removed, {@code false}
     *         otherwise (including when one of the nodes is not in the graph)
     */
    public boolean removeEdge(int id1, int id2) {
        Node u = getNode(id1);
        Node v = getNode(id2);
        if (u == null || v == null) return false;
        return removeEdge(u, v);
    }

    /**
     * Removes the first edge between the nodes with the given identifiers that
     * has exactly the given weight (overload of
     * {@link #removeEdge(Node, Node, Integer)} using node identifiers).
     *
     * @param id1    the identifier of the source node
     * @param id2    the identifier of the target node
     * @param weight the weight of the edge to remove ({@code null} to remove an
     *               unweighted edge)
     * @return {@code true} if a matching edge was found and removed,
     *         {@code false} otherwise (including when one of the nodes is not
     *         in the graph)
     */
    public boolean removeEdge(int id1, int id2, Integer weight) {
        Node u = getNode(id1);
        Node v = getNode(id2);
        if (u == null || v == null) return false;
        return removeEdge(u, v, weight);
    }

    /**
     * Removes an edge having the same source, target and weight as the given
     * edge (overload of {@link #removeEdge(Node, Node, Integer)} using an
     * {@code Edge} instance).
     *
     * @param e the edge to remove
     * @return {@code true} if a matching edge was found and removed,
     *         {@code false} otherwise (including when {@code e} is {@code null})
     */
    public boolean removeEdge(Edge e) {
        if (e == null) return false;
        return removeEdge(e.from(), e.to(), e.getWeight());
    }

    /**
     * Removes an edge having the same source and target as the given edge and
     * the given weight; the weight of {@code e} itself is ignored.
     *
     * @param e      the edge whose end nodes are used
     * @param weight the weight of the edge to remove ({@code null} to remove an
     *               unweighted edge)
     * @return {@code true} if a matching edge was found and removed,
     *         {@code false} otherwise (including when {@code e} is {@code null})
     */
    public boolean removeEdge(Edge e, Integer weight) {
        if (e == null) return false;
        return removeEdge(e.from(), e.to(), weight);
    }

    /**
     * Gets the outgoing edges of a node.
     *
     * @param n the node whose outgoing edges are requested
     * @return a new list containing the edges whose source is {@code n} (empty
     *         if the node is not in the graph); modifying this list does not
     *         modify the graph
     */
    public List<Edge> getOutEdges(Node n) {
        List<Edge> edges = adjEdList.get(n);
        return edges == null ? new ArrayList<>() : new ArrayList<>(edges);
    }

    /**
     * Gets the outgoing edges of the node with the given identifier.
     *
     * @param id the identifier of the node
     * @return a new list containing the edges whose source is the node (empty
     *         if the graph has no such node)
     */
    public List<Edge> getOutEdges(int id) {
        return getOutEdges(getNode(id));
    }

    /**
     * Gets the incoming edges of a node, i.e. all the edges of the graph whose
     * target is {@code n}.
     *
     * @param n the node whose incoming edges are requested
     * @return a new list containing the edges whose target is {@code n} (empty
     *         if there is none)
     */
    public List<Edge> getInEdges(Node n) {
        List<Edge> res = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            for (Edge e : entry.getValue()) {
                if (e.to().equals(n)) {
                    res.add(e);
                }
            }
        }
        return res;
    }

    /**
     * Gets the incoming edges of the node with the given identifier.
     *
     * @param id the identifier of the node
     * @return a new list containing the edges whose target is the node (empty
     *         if there is none or if the graph has no such node)
     */
    public List<Edge> getInEdges(int id) {
        return getInEdges(getNode(id));
    }

    /**
     * Gets the edges incident to a node: its outgoing edges followed by its
     * incoming edges. A self-loop therefore appears twice.
     *
     * @param n the node whose incident edges are requested
     * @return a new list containing the outgoing and then the incoming edges of
     *         {@code n}
     */
    public List<Edge> getIncidentEdges(Node n) {
        List<Edge> in = getInEdges(n);
        List<Edge> res = getOutEdges(n);
        for (Edge e : in) {
            res.add(e);
        }
        return res;
    }

    /**
     * Gets the edges incident to the node with the given identifier: its
     * outgoing edges followed by its incoming edges.
     *
     * @param id the identifier of the node
     * @return a new list containing the outgoing and then the incoming edges of
     *         the node
     */
    public List<Edge> getIncidentEdges(int id) {
        return getIncidentEdges(getNode(id));
    }

    /**
     * Gets all the edges going from {@code u} to {@code v}. Several edges are
     * returned if the graph contains parallel edges.
     *
     * @param u the source node
     * @param v the target node
     * @return a new list containing the edges from {@code u} to {@code v}
     *         (empty if there is none)
     */
    public List<Edge> getEdges(Node u, Node v) {
        List<Edge> out_u = getOutEdges(u);
        List<Edge> res = new ArrayList<>();
        for (Edge e : out_u) {
            if (e.to().equals(v)) {
                res.add(e);
            }
        }
        return res;
    }

    /**
     * Gets all the edges going from the node with identifier {@code id1} to the
     * node with identifier {@code id2}.
     *
     * @param id1 the identifier of the source node
     * @param id2 the identifier of the target node
     * @return a new list containing the edges between the two nodes (empty if
     *         there is none)
     */
    public List<Edge> getEdges(int id1, int id2) {
        return getEdges(getNode(id1), getNode(id2));
    }

    /**
     * Gets all the edges of the graph.
     *
     * @return a new list containing every edge of the graph (parallel edges and
     *         self-loops included)
     */
    public List<Edge> getAllEdges() {
        List<Edge> res = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjEdList.entrySet()) {
            for (Edge e : entry.getValue()) {
                res.add(e);
            }
        }
        return res;
    }

    /**
     * Converts the graph into its successor array representation. For each node
     * in increasing order of identifier, the identifiers of the targets of its
     * outgoing edges (sorted in increasing order, with repetitions for parallel
     * edges) are written, followed by a {@code 0}. Edge weights are not
     * represented.
     *
     * @return the successor array of the graph, of length
     *         {@code nbEdges + nbNodes}
     */
    public int[] toSuccessorArray() {
        int totalPhysicalEdges = getAllEdges().size();
        int[] res = new int[totalPhysicalEdges + nbNodes()];
        int insertion = 0;

        List<Node> nodes = getAllNodes();
        Collections.sort(nodes); // Reproducible output

        for (Node u : nodes) {
            List<Edge> edges = getOutEdges(u);
            Collections.sort(edges); // Reproducible output

            for (Edge e : edges) {
                res[insertion++] = e.to().getId();
            }
            res[insertion++] = 0;
        }
        return res;
    }

    /**
     * Converts the graph into its adjacency matrix. The cell
     * {@code matrix[i - 1][j - 1]} contains the number of edges going from node
     * {@code i} to node {@code j} (parallel edges are counted; weights are
     * ignored). The nodes are assumed to be numbered from {@code 1} to
     * {@code nbNodes()}.
     *
     * @return a square matrix of size {@code nbNodes()} holding the number of
     *         edges between each ordered pair of nodes
     * @throws ArrayIndexOutOfBoundsException if the identifiers of the nodes
     *                                        are not exactly {@code 1..nbNodes()}
     */
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

    /**
     * Builds the reverse graph: it has the same nodes as this graph and each
     * edge from {@code u} to {@code v} is replaced by an edge from {@code v} to
     * {@code u} with the same weight (if any). This graph is not modified.
     *
     * @return a new graph which is the reverse of this graph
     */
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

    /**
     * Computes the transitive closure of the graph with a Warshall-like
     * algorithm: the closure has the same nodes as this graph and contains all
     * its edges, plus an (unweighted) edge from {@code i} to {@code j} whenever
     * there is a path from {@code i} to {@code j}. This graph is not modified.
     *
     * @return a new graph which is the transitive closure of this graph
     */
    public Graph getTransitiveClosure() {
        Graph closure = this.copy();
        List<Node> nodes = closure.getAllNodes();

        for (Node k : nodes) {
            for (Node i : nodes) {
                for (Node j : nodes) {
                    // If a path exists from i to k AND from k to j
                    if (closure.existsEdge(i, k) && closure.existsEdge(k, j)) {
                        // And if there is no direct edge from i to j yet
                        if (!closure.existsEdge(i, j)) {
                            closure.addEdge(i, j);
                        }
                    }
                }
            }
        }

        return closure;
    }

    /**
     * Tests whether the graph is a multigraph, i.e. whether it contains at
     * least one multi-edge (two or more edges with the same source and target).
     *
     * @return {@code true} if the graph contains a multi-edge; {@code false}
     *         otherwise
     */
    public boolean isMultiGraph() {
        List<Edge> edges = getAllEdges();
        for (Edge e : edges) {
            if (isMultiEdge(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether the graph is simple, i.e. whether it has neither multi-edges
     * nor self-loops.
     *
     * @return {@code true} if the graph is simple; {@code false} otherwise
     */
    public boolean isSimpleGraph() {
        return !isMultiGraph() && !hasSelfLoops();
    }

    /**
     * Tests whether the graph contains at least one self-loop, i.e. an edge
     * whose source and target are the same node.
     *
     * @return {@code true} if the graph contains a self-loop; {@code false}
     *         otherwise
     */
    public boolean hasSelfLoops() {
        List<Edge> edges = getAllEdges();
        for (Edge e : edges) {
            if (e.from().equals(e.to())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a simple graph from this graph: same nodes, but all the self-loops
     * are removed and only the first edge found between two nodes is kept
     * (parallel edges are merged, keeping the weight of the first one). If this
     * graph is already simple, a plain copy is returned. This graph is not
     * modified.
     *
     * @return a new simple graph derived from this graph
     */
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

    /**
     * Builds a copy of this graph: new nodes (belonging to the copy) and new
     * edges with the same end identifiers and the same weights. The copy is
     * always a plain {@link Graph}, even when called on a subclass. Modifying
     * the copy does not modify this graph.
     *
     * @return a new graph with the same nodes and edges as this graph
     */
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
     * starting by default from the node with the lowest identifier.
     *
     * @return a list of nodes in the order they were first discovered during
     *         the traversal (empty if the graph has no node)
     * @see #getDFS(Node)
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
     * Performs a Depth-First Search (DFS) traversal of the graph starting from
     * a specified node. The outgoing edges of each node are explored in
     * increasing order. Once the exploration from {@code u} is finished, the
     * nodes that remain unvisited (other connected components) are used as new
     * starting points, in increasing order of identifier.
     * Those methods are inspired from the course of Graf of this year,
     * presented by Ms SONIGA.
     *
     * @param u the starting node for the traversal
     * @return a list of nodes in the order they were first discovered during
     *         the traversal, or an empty list if {@code u} is {@code null} or
     *         does not belong to the graph
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
     * Performs a Depth-First Search (DFS) traversal of the graph starting from
     * the node with the given identifier.
     *
     * @param id the identifier of the starting node
     * @return a list of nodes in the order they were first discovered during
     *         the traversal, or an empty list if the graph has no such node
     * @see #getDFS(Node)
     */
    public List<Node> getDFS(int id) {
        return getDFS(getNode(id));
    }

    /**
     * Auxiliary recursive method for visiting a node during a Depth-First
     * Search. The node is colored GRAY and added to the result, its WHITE
     * successors are recursively visited (outgoing edges taken in increasing
     * order), then the node is colored BLACK.
     *
     * @param u        the node currently being visited
     * @param colorMap a map tracking the color state of each node (updated by
     *                 the method)
     * @param result   the list collecting the nodes in discovery order
     *                 (updated by the method)
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
     * starting by default from the node with the lowest identifier.
     *
     * @return a list of nodes in the order they were visited during the
     *         traversal (empty if the graph has no node)
     * @see #getBFS(Node)
     */
    public List<Node> getBFS() {
        List<Node> nodes = getAllNodes();
        if (nodes.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort nodes by increasing identifier for output reproducibility
        Collections.sort(nodes);

        // Start the traversal from the node with the lowest identifier
        return getBFS(nodes.get(0));
    }

    /**
     * Performs a Breadth-First Search (BFS) traversal of the graph starting
     * from a specified node. The outgoing edges of each node are explored in
     * increasing order. Once the exploration from {@code u} is finished, the
     * nodes that remain unvisited (other connected components) are used as new
     * starting points, in increasing order of identifier.
     *
     * @param u the starting node for the traversal
     * @return a list of nodes in the order they were visited during the
     *         traversal, or an empty list if {@code u} is {@code null} or does
     *         not belong to the graph
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
        Collections.sort(allNodes); // Deterministic traversal order
        for (Node n : allNodes) {
            if (colorMap.get(n) == NodeColor.WHITE) {
                bfsVisit(n, colorMap, result);
            }
        }

        return result;
    }

    /**
     * Performs a Breadth-First Search (BFS) traversal of the graph starting
     * from the node with the given identifier.
     *
     * @param id the identifier of the starting node
     * @return a list of nodes in the order they were visited during the
     *         traversal, or an empty list if the graph has no such node
     * @see #getBFS(Node)
     */
    public List<Node> getBFS(int id) {
        return getBFS(getNode(id));
    }

    /**
     * Auxiliary method visiting, level by level and with a FIFO queue, all the
     * nodes reachable from a starting node. Discovered nodes are colored GRAY
     * when they are enqueued and BLACK once they are processed.
     *
     * @param start    the node from which to start the BFS iteration
     * @param colorMap a map tracking the color state of each node (updated by
     *                 the method)
     * @param result   the list collecting the nodes in visit order (updated by
     *                 the method)
     */
    private void bfsVisit(Node start, Map<Node, NodeColor> colorMap, List<Node> result) {
        Queue<Node> queue = new ArrayDeque<>();

        // Discover the starting node
        colorMap.put(start, NodeColor.GRAY);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node u = queue.poll();
            result.add(u);

            // Sort outgoing edges to guarantee a deterministic traversal order
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

    /** Global counter for the timestamps of the DFS with visit information. */
    private static int dfsTime = 0;

    /**
     * Performs an enriched Depth-First Search (DFS) traversal starting from the
     * node with the lowest identifier. The two maps are output parameters: they
     * are cleared, then filled with the traversal metrics (colors, timestamps,
     * predecessors, edge types).
     *
     * @param nodeVisit output parameter: map associating each node with its
     *                  visit metadata (color, predecessor, discovery and finish
     *                  times)
     * @param edgeVisit output parameter: map associating each traversed edge
     *                  with its DFS classification (TREE, BACKWARD, FORWARD or
     *                  CROSS)
     * @return a list of nodes in discovery order (empty if the graph has no node)
     * @see #getDFSWithVisitInfo(Node, Map, Map)
     */
    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        List<Node> nodes = getAllNodes();
        if (nodes.isEmpty()) {
            return new ArrayList<>();
        }
        Collections.sort(nodes); // Reproducibility
        return getDFSWithVisitInfo(nodes.get(0), nodeVisit, edgeVisit);
    }

    /**
     * Performs an enriched Depth-First Search (DFS) traversal starting from a
     * given node {@code u}, then from the remaining unvisited nodes in
     * increasing order of identifier. The two maps are output parameters: they
     * are cleared, then filled with the traversal metrics. Each node gets a
     * discovery time and a finish time taken from a counter incremented at each
     * discovery and each completion. An edge to a GRAY node is BACKWARD; an
     * edge to a BLACK node is FORWARD if the source was discovered before the
     * target and CROSS otherwise; an edge to a WHITE node is a TREE edge.
     *
     * <p>Note: the timestamp counter is a static field, so this method is not
     * thread-safe.</p>
     *
     * @param u         the starting node for the traversal
     * @param nodeVisit output parameter: map associating each node with its
     *                  visit metadata (color, predecessor, discovery and finish
     *                  times)
     * @param edgeVisit output parameter: map associating each traversed edge
     *                  with its DFS classification
     * @return a list of nodes in discovery order, or an empty list (and the
     *         maps left untouched) if {@code u} is {@code null} or not in the
     *         graph, or if one of the maps is {@code null}
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

        // 2. Visit the remaining unvisited nodes
        Collections.sort(allNodes);
        for (Node n : allNodes) {
            if (nodeVisit.get(n).getColor() == NodeColor.WHITE) {
                dfsVisitWithInfo(n, nodeVisit, edgeVisit, result);
            }
        }

        return result;
    }

    /**
     * Auxiliary recursive method for the enriched DFS traversal. It stamps the
     * discovery time of {@code u}, explores its outgoing edges in increasing
     * order while classifying each of them, then stamps the finish time of
     * {@code u}.
     *
     * @param u         the node currently being visited
     * @param nodeVisit output parameter: the visit metadata of the nodes
     *                  (updated by the method)
     * @param edgeVisit output parameter: the classification of the edges
     *                  (updated by the method)
     * @param result    output parameter: the list collecting the nodes in
     *                  discovery order (updated by the method)
     */
    private void dfsVisitWithInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit, List<Node> result) {
        dfsTime++;
        NodeVisitInfo uInfo = nodeVisit.get(u);
        uInfo.setColor(NodeColor.GRAY);
        uInfo.setDiscoveryTime(dfsTime);
        result.add(u);

        List<Edge> outEdges = getOutEdges(u);
        Collections.sort(outEdges);

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
    // Graph Import and Export (DOT Format)
    // ==========================================

    /**
     * Builds a graph from a file in the DOT language. The file
     * {@code filename.extension} is read line by line, and only the lines
     * describing an edge are taken into account, e.g. {@code 1 -> 2},
     * {@code 1 -- 2}, or {@code 1 -> 2 [label=5, len=5]} for a weighted edge
     * (the {@code len=} and {@code label=} attributes may appear in either
     * order). Every other line (header, braces, blank lines, etc.) is ignored.
     * Nodes are created when they are first met in an edge.
     *
     * @param <T>        the type of graph to build ({@link Graph} or a subclass)
     * @param filename   the path of the file, without its extension
     * @param extension  the extension of the file, without the dot
     * @param graphClass the class of the graph to instantiate; it must have a
     *                   public no-argument constructor
     * @return the graph read from the file, or {@code null} if the graph could
     *         not be instantiated or if the file could not be opened or read
     */
    public static <T extends Graph> T fromDotFile(String filename, String extension, Class<T> graphClass) {
        try {
            T graph = graphClass.getDeclaredConstructor().newInstance();
            String fullPath = filename + "." + extension;

            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fullPath))) {
                String line;
                // Regular expression matching the arrows '->' and '--', with an optional weight
                java.util.regex.Pattern edgePattern = java.util.regex.Pattern.compile(
                    "^\\s*(\\d+)\\s*(?:->|--)\\s*(\\d+)(?:\\s*\\[(?:label=(\\d+),\\s*len=\\d+|len=\\d+,\\s*label=(\\d+))\\])?\\s*;?\\s*$"
                );

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    java.util.regex.Matcher matcher = edgePattern.matcher(line);
                    if (matcher.find()) {
                        int fromId = Integer.parseInt(matcher.group(1));
                        int toId = Integer.parseInt(matcher.group(2));
                        String wStr = matcher.group(3) != null ? matcher.group(3) : matcher.group(4);

                        if (wStr != null) {
                            int weight = Integer.parseInt(wStr);
                            graph.addEdge(fromId, toId, weight);
                        } else {
                            graph.addEdge(fromId, toId);
                        }
                    }
                }
            }
            return graph;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Builds a directed graph from a DOT file with the default extension
     * {@code .gv}.
     *
     * @param filename the path of the file, without its extension
     * @return the graph read from {@code filename.gv}, or {@code null} if the
     *         file could not be read
     * @see #fromDotFile(String, String, Class)
     */
    public static Graph fromDotFile(String filename) {
        return fromDotFile(filename, "gv", Graph.class);
    }

    /**
     * Builds a directed graph from a DOT file with a custom extension.
     *
     * @param filename  the path of the file, without its extension
     * @param extension the extension of the file, without the dot
     * @return the graph read from {@code filename.extension}, or {@code null}
     *         if the file could not be read
     * @see #fromDotFile(String, String, Class)
     */
    public static Graph fromDotFile(String filename, String extension) {
        return fromDotFile(filename, extension, Graph.class);
    }

    /**
     * Exports the graph as a String using the DOT language, as a directed graph
     * ({@code digraph G { ... }}). Nodes and, for each node, outgoing edges are
     * sorted to guarantee a reproducible output; each edge is written on its own
     * line as {@code from -> to}, followed by {@code [label=w, len=w]} if the
     * edge is weighted.
     *
     * @return the DOT representation of the graph
     */
    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");

        List<Node> nodes = getAllNodes();
        Collections.sort(nodes); // Reproducibility

        for (Node u : nodes) {
            List<Edge> edges = getOutEdges(u);
            Collections.sort(edges); // Reproducibility

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
     * Exports the graph to a file in DOT syntax, with the default extension
     * {@code .gv}. The content is the one given by {@link #toDotString()}.
     *
     * @param fileName the path of the file to write, without its extension
     * @see #toDotFile(String, String)
     */
    public void toDotFile(String fileName) {
        toDotFile(fileName, "gv");
    }

    /**
     * Exports the graph to a file in DOT syntax, with a custom extension. The
     * file {@code fileName.extension} is created (or overwritten) and receives
     * the content given by {@link #toDotString()}. If the file cannot be
     * written, the error is printed on the standard error stream and nothing
     * else happens.
     *
     * @param fileName  the path of the file to write, without its extension
     * @param extension the extension of the file, without the dot
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