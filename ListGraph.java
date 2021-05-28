import java.io.Serializable;
import java.util.*;
public class ListGraph <T> implements Graph<T>, Serializable {
    private Map<T, Set<Edge>> nodes = new HashMap<>();

    @Override
    public void add(T node) {
        HashSet<Edge> edgeSet = new HashSet<>();
        nodes.putIfAbsent(node, edgeSet);
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {
        boolean duplicate = false;
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Fel vid connect");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Fel vikt");
        }

        for (Edge e : nodes.get(node1)) {
            if (e.getDestination() == node2) {
                duplicate = true;
                //throw new IllegalStateException();
            }
        }
        for (Edge e : nodes.get(node2)) {
            if (e.getDestination() == node1) {
                duplicate = true;
                //throw new IllegalStateException();
            }
        }
        if (!duplicate) {
            Edge e1 = new Edge(node2, name, weight);
            Edge e2 = new Edge(node1, name, weight);
            nodes.get(node1).add(e1);
            nodes.get(node2).add(e2);

        }
        //förbindelse från båda hållen
    }

    @Override
    public void remove(T node) {
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Fel vid borttagning");
        }
        for (Edge e : nodes.get(node)) {
            for (Edge edge : nodes.get(e.getDestination())) {
                if (edge.getDestination() == node) {
                    nodes.get(e.getDestination()).remove(edge);
                    break;
                }
            }
        }
        nodes.remove(node);
    }

    @Override
    public void disconnect(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Noden finns ej");
        }
        //Lägg till IllegalStateException när getEdgeBetween() är fixad
        Edge e = getEdgeBetween(node1, node2);
        if (e == null) {
            throw new IllegalStateException("Edges finns ej");
        }
        for (Edge edge : nodes.get(node1)) {
            if (edge.getDestination() == node2) {
                nodes.get(node1).remove(edge);
                break;
            }
        }
        for (Edge edge : nodes.get(node2)) {
            if (edge.getDestination() == node1) {
                nodes.get(node2).remove(edge);
                break;
            }
        }
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int newWeight) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Noden finns ej");
        }
        if (newWeight < 0) {
            throw new IllegalArgumentException("Fel vikt");
        }
        for (Edge e : nodes.get(node1)) {
            if (e.getDestination() == node2) {
                e.setWeight(newWeight);
            }
        }
        for (Edge e : nodes.get(node2)) {
            if (e.getDestination() == node1) {
                e.setWeight(newWeight);
            }
        }
    }

    @Override
    public Set getNodes() {
        return nodes.keySet();
    }

    @Override
    public Set getEdgesFrom(T node) {
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Fel vid getEdgesFrom");
        }
        return nodes.get(node);
    }

    @Override
    public Edge getEdgeBetween(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Fel vid getEdgeBetween");
        }
        for (Edge e : nodes.get(node1)) {
            if (e.getDestination() == node2) {
                return e;
            }
        }
        return null;
    }

    public String toString() {
        String string = "";
        for (T node : nodes.keySet()) {
            string += node + ": ";
            for (Edge e : nodes.get(node)) {
                string += e.toString();
                string += "\n";
            }
        }
        return string;
    }

    /*@Override
    public void depthFirstSearch(T node, Set<T> visited) {
        visited.add(node);
        for (Edge e : nodes.get(node)) {
            if (!visited.contains(e.getDestination())) {
                depthFirstSearch((T) e.getDestination(), visited);
            }
            //depthFirstSearch är en genomgång av alla noder i grafen
        }
    }*/
    @Override
    public boolean pathExists(T nodeFrom, T nodeTo) {
        //ska returnera true om det finns en väg mellan from och to
        if (!nodes.containsKey(nodeFrom) || !nodes.containsKey(nodeTo)) {
            return false;
        }
        Set<T> visited = new HashSet<>();
        LinkedList<T> queue = new LinkedList<>();
        Map<T, T> via = new HashMap<>();
        visited.add(nodeFrom);
        queue.add(nodeFrom);
        while (!queue.isEmpty()) {
            T node = queue.pollFirst();
            for (Edge e : nodes.get(node)) {
                T toNode = (T) e.getDestination();
                if (!visited.contains(toNode)) {
                    queue.add(toNode);
                    visited.add(toNode);
                    via.put(toNode, node);
                }
                if (visited.contains(nodeTo)) {
                    return true;
                }

            }
        }


        return false;
    }

    @Override
    public List<Edge<T>> getPath(T nodeFrom, T nodeTo) {
        if (!nodes.containsKey(nodeFrom) || !nodes.containsKey(nodeTo)) {
            throw new NoSuchElementException("Noden finns ej");
        }
        boolean pathOk = pathExists(nodeFrom, nodeTo);

        if (!pathOk) {
            return null;
        }
        Set<T> visited = new HashSet<>();
        LinkedList<T> queue = new LinkedList<>();
        Map<T, T> via = new HashMap<>();
        visited.add(nodeFrom);
        queue.add(nodeFrom);
        while (!queue.isEmpty()) {
            T node = queue.pollFirst();
            for (Edge e : nodes.get(node)) {
                T toNode = (T) e.getDestination();
                if (!visited.contains(toNode)) {
                    queue.add(toNode);
                    visited.add(toNode);
                    via.put(toNode, node);
                }
            }
        }
        ArrayList<Edge<T>> path = new ArrayList<>();
        T where = nodeTo;
        while (!where.equals(nodeFrom)) {
            T node = via.get(where);
            Edge e = getEdgeBetween(node, where);
            path.add(e);
            where = node;
        }
        Collections.reverse(path);
        return path;
    }
}