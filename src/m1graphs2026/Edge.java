    package m1graphs2026;

import java.util.Objects;

public class Edge implements Comparable<Edge>{
        private final Node from;
        private final Node to;
        private final Integer weight;

        public Edge(Node from, Node to){
            this.from = from;
            this.to = to;
            this.weight = null;
        }

        public Edge(Node from, Node to, Integer weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public Edge(int id_from, int id_to, Graph graph){
            this.from = graph.getNode(id_from);
            this.to = graph.getNode(id_to);
            this.weight = null;
        }

        public Edge(int id_from, int id_to, Graph graph, Integer weight){
            this.from = graph.getNode(id_from);
            this.to = graph.getNode(id_to);
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to) &&
                Objects.equals(weight, edge.weight);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to, weight);
        }

        @Override
        public int compareTo(Edge other) {
            if (this.from.getId() != other.from.getId()) {
                return Integer.compare(this.from.getId(), other.from.getId());
            }
            if (this.to.getId() != other.to.getId()) {
                return Integer.compare(this.to.getId(), other.to.getId());
            }
            
            // Gestion sécurisée des poids null
            if (this.weight == null && other.weight == null) return 0;
            if (this.weight == null) return -1;
            if (other.weight == null) return 1;
            
            return Integer.compare(this.weight, other.weight);
        }

        public Node from(){return this.from;}
        public Node to(){return this.to;}
        public Edge getSymmetric(){return new Edge(this.to,this.from,this.weight);}
        public boolean isSelfLoop(){return this.from==this.to;}

        public boolean isMultiEdge() {
            if (from == null || from.getGraph() == null) return false;
            return from.getGraph().isMultiEdge(this);
        }
        public boolean isWeighted(){return this.weight!=null;}
        public Integer getWeight(){return this.weight;}
        
    }
