package m1graphs2026;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UndirectedGraph extends Graph {

    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(int... sa) {
        super(sa);
    }

    @Override
    public void addEdge(Node from, Node to) {
        super.addEdge(from, to);
        if (!from.equals(to)) {
            Node realTo = getNode(to.getId());
            Node realFrom = getNode(from.getId());
            adjEdList.get(realTo).add(new Edge(realTo, realFrom));
        }
    }

    @Override
    public void addEdge(Node from, Node to, int weight) {
        super.addEdge(from, to, weight);
        if (!from.equals(to)) {
            Node realTo = getNode(to.getId());
            Node realFrom = getNode(from.getId());
            adjEdList.get(realTo).add(new Edge(realTo, realFrom, weight));
        }
    }

    @Override
    public boolean removeEdge(Node from, Node to) {
        boolean r1 = super.removeEdge(from, to);
        boolean r2 = super.removeEdge(to, from);
        return r1 || r2;
    }

    @Override
    public boolean removeEdge(Node from, Node to, Integer weight) {
        boolean r1 = super.removeEdge(from, to, weight);
        boolean r2 = super.removeEdge(to, from, weight);
        return r1 || r2;
    }

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

    @Override
    public List<Edge> getIncidentEdges(Node n) {
        return getOutEdges(n);
    }

    @Override
    public List<Edge> getInEdges(Node n) {
        return getOutEdges(n);
    }

    @Override
    public int inDegree(Node n) {
        return getOutEdges(n).size();
    }

    @Override
    public int outDegree(Node n) {
        return getOutEdges(n).size();
    }

    @Override
    public int degree(Node n) {
        return getOutEdges(n).size();
    }

    @Override
    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("graph G {\n");

        List<Node> nodes = getAllNodes();
        Collections.sort(nodes);

        Set<String> processedEdges = new HashSet<>();

        for (Node u : nodes) {
            List<Edge> edges = getOutEdges(u);
            Collections.sort(edges);

            for (Edge e : edges) {
                int id1 = e.from().getId();
                int id2 = e.to().getId();
                
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

    public static UndirectedGraph fromDotFile(String filename) {
        return Graph.fromDotFile(filename, "gv", UndirectedGraph.class);
    }

    public static UndirectedGraph fromDotFile(String filename, String extension) {
        return Graph.fromDotFile(filename, extension, UndirectedGraph.class);
    }

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