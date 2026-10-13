package m1graphs2026;
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
    public int compareTo(Edge other){
        if(this.from.getId()==other.from.getId()){
            if(this.to.getId()==other.to.getId()){
                return Integer.compare(this.weight, other.weight);
            }
            else {
                return Integer.compare(this.to.getId(), other.to.getId());
            }
        }
        else {
            return Integer.compare(this.from.getId(), other.from.getId());
        }
    }

    public Node from(){return this.from;}
    public Node to(){return this.to;}
    public Edge getSymmetric(){return new Edge(this.to,this.from,this.weight);}
    public boolean isSelfLoop(){return this.from==this.to;}

    public boolean isMultiEdge(){
        int count = 0;
        for(Node n : this.from.getGraph().adjEdList.keySet()){
            if(n==this.from){
                count++;
            }
        }
        return (count>=2);
    
    }
    public boolean isWeighted(){return this.weight!=null;}
    public Integer getWeight(){return this.weight;}
    
}
