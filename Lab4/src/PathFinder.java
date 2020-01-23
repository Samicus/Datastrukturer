
import org.w3c.dom.Node;

import javax.xml.crypto.NodeSetData;
import java.util.*;

import java.util.stream.Collectors;


public class PathFinder<V> {

    private DirectedGraph<V> graph;
    private long startTimeMillis;


    public PathFinder(DirectedGraph<V> graph) {
        this.graph = graph;
    }


    public class Result<V> {
        public final boolean success;
        public final V start;
        public final V goal;
        public final double cost;
        public final List<V> path;
        public final int visitedNodes;
        public final double elapsedTime;

        public Result(boolean success, V start, V goal, double cost, List<V> path, int visitedNodes) {
            this.success = success;
            this.start = start;
            this.goal = goal;
            this.cost = cost;
            this.path = path;
            this.visitedNodes = visitedNodes;
            this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        }

        public String toString() {
            String s = "";
            s += String.format("Visited nodes: %d\n", visitedNodes);
            s += String.format("Elapsed time: %.1f seconds\n", elapsedTime);
            if (success) {
                s += String.format("Total cost from %s -> %s: %s\n", start, goal, cost);
                s += "Path: " + path.stream().map(x -> x.toString()).collect(Collectors.joining(" -> "));
            } else {
                s += String.format("No path found from %s", start);
            }
            return s;
        }
    }


    public Result<V> search(String algorithm, V start, V goal) {
        startTimeMillis = System.currentTimeMillis();
        switch (algorithm) {
        case "random":   return searchRandom(start, goal);
        case "dijkstra": return searchDijkstra(start, goal);
        case "astar":    return searchAstar(start, goal);
        }
        throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
    }


    public Result<V> searchRandom(V start, V goal) {
        int visitedNodes = 0;
        LinkedList<V> path = new LinkedList<>();
        double cost = 0.0;
        Random random = new Random();

        V current = start;
        path.add(current);
        while (current != null) {
            visitedNodes++;
            if (current.equals(goal)) {
                return new Result<>(true, start, current, cost, path, visitedNodes);
            }

            List<DirectedEdge<V>> neighbours = graph.outgoingEdges(start);
            if (neighbours == null || neighbours.size() == 0) {
                break;
            } else {
                DirectedEdge<V> edge = neighbours.get(random.nextInt(neighbours.size()));
                cost += edge.weight();
                current = edge.to();
                path.add(current);
            }
        }
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }


    public Result<V> searchDijkstra(V start, V goal) {
        int visitedNodes = 0;
        V v;
        V w;
        double newdist;

        Map<V, DirectedEdge<V>> edgeTo = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();

        Comparator<V> comparator = new Comparator<V>() {
            @Override
            public int compare(V distTo1, V distTo2) {
                return (distTo.get(distTo1).compareTo(distTo.get(distTo2)));

            }
        };

        Queue<V> queue = new PriorityQueue<V>(comparator);
        Set<V> visited = new HashSet<V>();
        queue.add(start);
        distTo.put(start, 0.0);

        while (!queue.isEmpty()) {
            v = queue.poll();
            visitedNodes++;
            if (!visited.contains(v)) {
                visited.add(v);
                if (v.equals(goal)) {
                    //success: calculate the path and return
                        List<V> shortestPath = reverseList(edgeTo,v);
                    return new Result<>(true, start, goal, distTo.get(v), shortestPath, visitedNodes);

                }
            }
            for (DirectedEdge<V> e : graph.outgoingEdges(v)) {
                w = e.to();
                newdist = distTo.get(v) + e.weight();

                if (distTo.get(w) == null || distTo.get(w) > newdist) {
                    distTo.put(w, newdist);
                    edgeTo.put(w, e);
                    queue.add(w);
                }
            }

        }

        return new Result<>(false, start, null, -1, null, visitedNodes);
    }

 private List<V> reverseList(Map<V ,DirectedEdge<V>> edge,V v){
        DirectedEdge<V> e2 = edge.get(v);
         V v2 = e2.from();
         List<V>  path;
        if (v2 == null){return null;}
        else {
         path = reverseList(edge,v2);
     }
        return path;
 }
    public Result<V> searchAstar(V start, V goal) {
        int visitedNodes = 0;
        /********************
         * TODO: Task 3
         ********************/
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }

}
