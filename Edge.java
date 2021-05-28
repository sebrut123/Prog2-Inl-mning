public class Edge <T> {
    private int weight;
    private T node;
    private String name;

    public Edge(T node,String name, int weight){
        this.node=node;
        this.weight=weight;
        this.name=name;
    }

    /*
    public int hashCode(){
        return node.hashCode();
    }

    public boolean equals(Object other){
            if(node == other){
                return true;
            }
                return false;
    }*/
    public T getDestination(){
        return node;
    }
    public int getWeight(){
        return weight;
    }
    public void setWeight(int newWeight){
        this.weight=newWeight;
    }
    public String getName(){
        return name;
    }
    public String toString(){
        return "till " + node + " med " + name + " tar " + weight;

    }
}