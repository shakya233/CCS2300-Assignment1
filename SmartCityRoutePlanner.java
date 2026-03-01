import java.util.*;

/**
 * ============================================================
 *  Module 1: Smart City Route Planner
 *  Data Structures used:
 *    - BST  : stores Location objects (by name, alphabetical)
 *    - Graph: adjacency-list representation of roads
 *    - Stack: DFS traversal
 *    - Queue: BFS traversal
 * ============================================================
 */
public class SmartCityRoutePlanner {

    // =========================================================
    // 1.  BST NODE  – stores location data before graph mapping
    // =========================================================
    static class BSTNode {
        String name;
        String type;          // e.g. Hospital, Park, Station …
        BSTNode left, right;

        BSTNode(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    // =========================================================
    // 2.  BST  (ordered by location name)
    // =========================================================
    static class LocationBST {
        BSTNode root;

        void insert(String name, String type) {
            root = insertRec(root, name, type);
        }

        private BSTNode insertRec(BSTNode node, String name, String type) {
            if (node == null) return new BSTNode(name, type);
            int cmp = name.compareToIgnoreCase(node.name);
            if (cmp < 0)       node.left  = insertRec(node.left,  name, type);
            else if (cmp > 0)  node.right = insertRec(node.right, name, type);
            else               node.type  = type;   // update type if exists
            return node;
        }

        boolean contains(String name) {
            return findRec(root, name) != null;
        }

        BSTNode find(String name) { return findRec(root, name); }

        private BSTNode findRec(BSTNode node, String name) {
            if (node == null) return null;
            int cmp = name.compareToIgnoreCase(node.name);
            if (cmp == 0)     return node;
            if (cmp < 0)      return findRec(node.left,  name);
            return findRec(node.right, name);
        }

        BSTNode delete(String name) {
            root = deleteRec(root, name);
            return root;
        }

        private BSTNode deleteRec(BSTNode node, String name) {
            if (node == null) return null;
            int cmp = name.compareToIgnoreCase(node.name);
            if (cmp < 0) {
                node.left  = deleteRec(node.left,  name);
            } else if (cmp > 0) {
                node.right = deleteRec(node.right, name);
            } else {
                if (node.left == null)  return node.right;
                if (node.right == null) return node.left;
                // successor
                BSTNode succ = node.right;
                while (succ.left != null) succ = succ.left;
                node.name = succ.name;
                node.type = succ.type;
                node.right = deleteRec(node.right, succ.name);
            }
            return node;
        }

        // In-order → sorted list
        List<BSTNode> inOrder() {
            List<BSTNode> list = new ArrayList<>();
            inOrderRec(root, list);
            return list;
        }
        private void inOrderRec(BSTNode node, List<BSTNode> list) {
            if (node == null) return;
            inOrderRec(node.left, list);
            list.add(node);
            inOrderRec(node.right, list);
        }
    }

    // =========================================================
    // 3.  GRAPH  (undirected, weighted adjacency list)
    // =========================================================
    static class Edge {
        String to;
        int    distance;   // km / units

        Edge(String to, int distance) {
            this.to       = to;
            this.distance = distance;
        }
    }

    static class CityGraph {
        // adjacency list
        Map<String, List<Edge>> adj = new LinkedHashMap<>();

        boolean addVertex(String name) {
            if (adj.containsKey(name)) return false;
            adj.put(name, new ArrayList<>());
            return true;
        }

        boolean removeVertex(String name) {
            if (!adj.containsKey(name)) return false;
            adj.remove(name);
            // remove all edges pointing to this vertex
            for (List<Edge> edges : adj.values())
                edges.removeIf(e -> e.to.equals(name));
            return true;
        }

        boolean addEdge(String from, String to, int dist) {
            if (!adj.containsKey(from) || !adj.containsKey(to)) return false;
            // prevent duplicate
            for (Edge e : adj.get(from)) if (e.to.equals(to)) return false;
            adj.get(from).add(new Edge(to,   dist));
            adj.get(to)  .add(new Edge(from, dist));
            return true;
        }

        boolean removeEdge(String from, String to) {
            if (!adj.containsKey(from) || !adj.containsKey(to)) return false;
            boolean r1 = adj.get(from).removeIf(e -> e.to.equals(to));
            boolean r2 = adj.get(to)  .removeIf(e -> e.to.equals(from));
            return r1 || r2;
        }

        boolean hasVertex(String name) { return adj.containsKey(name); }

        void displayAll() {
            if (adj.isEmpty()) { System.out.println("  (Graph is empty)"); return; }
            for (Map.Entry<String, List<Edge>> entry : adj.entrySet()) {
                System.out.printf("  %-20s -> ", entry.getKey());
                if (entry.getValue().isEmpty()) {
                    System.out.println("(no connections)");
                } else {
                    StringJoiner sj = new StringJoiner(", ");
                    for (Edge e : entry.getValue())
                        sj.add(e.to + " [" + e.distance + " km]");
                    System.out.println(sj);
                }
            }
        }

        // --------------------------------------------------
        // BFS from source  (uses Queue)
        // --------------------------------------------------
        void bfs(String start) {
            if (!adj.containsKey(start)) {
                System.out.println("  Location not found.");
                return;
            }
            Queue<String> queue   = new LinkedList<>();
            Set<String>   visited = new LinkedHashSet<>();
            queue.add(start);
            visited.add(start);
            System.out.print("  BFS traversal: ");
            while (!queue.isEmpty()) {
                String cur = queue.poll();
                System.out.print(cur + " ");
                for (Edge e : adj.get(cur)) {
                    if (!visited.contains(e.to)) {
                        visited.add(e.to);
                        queue.add(e.to);
                    }
                }
            }
            System.out.println();
        }

        // --------------------------------------------------
        // DFS from source  (uses Stack)
        // --------------------------------------------------
        void dfs(String start) {
            if (!adj.containsKey(start)) {
                System.out.println("  Location not found.");
                return;
            }
            Deque<String> stack   = new ArrayDeque<>();
            Set<String>   visited = new LinkedHashSet<>();
            stack.push(start);
            System.out.print("  DFS traversal: ");
            while (!stack.isEmpty()) {
                String cur = stack.pop();
                if (visited.contains(cur)) continue;
                visited.add(cur);
                System.out.print(cur + " ");
                List<Edge> neighbours = new ArrayList<>(adj.get(cur));
                // push in reverse so left-most is processed first
                Collections.reverse(neighbours);
                for (Edge e : neighbours)
                    if (!visited.contains(e.to))
                        stack.push(e.to);
            }
            System.out.println();
        }

        // --------------------------------------------------
        // Dijkstra's shortest path  (bonus – commonly useful)
        // --------------------------------------------------
        void shortestPath(String src, String dst) {
            if (!adj.containsKey(src) || !adj.containsKey(dst)) {
                System.out.println("  One or both locations not found.");
                return;
            }
            Map<String, Integer> dist  = new HashMap<>();
            Map<String, String>  prev  = new HashMap<>();
            PriorityQueue<String> pq   = new PriorityQueue<>(
                Comparator.comparingInt(v -> dist.getOrDefault(v, Integer.MAX_VALUE)));

            for (String v : adj.keySet()) dist.put(v, Integer.MAX_VALUE);
            dist.put(src, 0);
            pq.add(src);

            while (!pq.isEmpty()) {
                String u = pq.poll();
                if (u.equals(dst)) break;
                for (Edge e : adj.get(u)) {
                    int nd = dist.get(u) + e.distance;
                    if (nd < dist.get(e.to)) {
                        dist.put(e.to, nd);
                        prev.put(e.to, u);
                        pq.add(e.to);
                    }
                }
            }

            if (dist.get(dst) == Integer.MAX_VALUE) {
                System.out.println("  No path found between " + src + " and " + dst);
                return;
            }

            // reconstruct path using a Stack
            Deque<String> path = new ArrayDeque<>();
            for (String at = dst; at != null; at = prev.get(at)) path.push(at);
            System.out.print("  Shortest path: ");
            System.out.println(String.join(" -> ", path)
                + "  |  Total distance: " + dist.get(dst) + " km");
        }
    }

    // =========================================================
    // 4.  HELPER – print separator
    // =========================================================
    static void sep() { System.out.println("--------------------------------------------------"); }

    static void printMenu() {
        sep();
        System.out.println("       SMART CITY ROUTE PLANNER - MAIN MENU");
        sep();
        System.out.println("  --- Location Management ---");
        System.out.println("  1. Add Location");
        System.out.println("  2. Remove Location");
        System.out.println("  3. List All Locations (BST in-order)");
        System.out.println("  4. Search Location");
        System.out.println("  --- Road / Edge Management ---");
        System.out.println("  5. Add Road (Edge)");
        System.out.println("  6. Remove Road (Edge)");
        System.out.println("  7. Display All Connections");
        System.out.println("  --- Traversal & Routes ---");
        System.out.println("  8. BFS Traversal (Queue-based)");
        System.out.println("  9. DFS Traversal (Stack-based)");
        System.out.println(" 10. Find Shortest Path (Dijkstra)");
        System.out.println("  --- System ---");
        System.out.println("  0. Exit");
        sep();
        System.out.print("  Enter choice: ");
    }

    // =========================================================
    // 5.  MAIN
    // =========================================================
    public static void main(String[] args) {
        Scanner sc   = new Scanner(System.in);
        LocationBST  bst   = new LocationBST();
        CityGraph    graph = new CityGraph();

        // ---- Pre-load sample data ----
        String[][] sampleLocations = {
            {"City Hall",      "Government"},
            {"Central Park",   "Park"},
            {"Main Hospital",  "Healthcare"},
            {"North Station",  "Transport"},
            {"South Station",  "Transport"},
            {"Tech Hub",       "Business"},
            {"University",     "Education"},
            {"Airport",        "Transport"}
        };
        int[][] sampleRoads = {
            // indices into sampleLocations
            {0,1,3},{0,2,5},{1,3,4},{1,5,6},
            {2,4,7},{3,6,8},{4,7,10},{5,6,3},{6,7,12}
        };

        System.out.println("\n  Loading sample city data...");
        for (String[] loc : sampleLocations) {
            bst.insert(loc[0], loc[1]);
            graph.addVertex(loc[0]);
        }
        for (int[] road : sampleRoads) {
            graph.addEdge(
                sampleLocations[road[0]][0],
                sampleLocations[road[1]][0],
                road[2]);
        }
        System.out.println("  Sample data loaded. " + sampleLocations.length
            + " locations, " + sampleRoads.length + " roads.\n");

        // ---- Menu loop ----
        while (true) {
            printMenu();
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("  [!] Please enter a numeric choice.");
                continue;
            }
            int choice = Integer.parseInt(input);

            switch (choice) {

                // --- 1. Add Location ---
                case 1: {
                    System.out.print("  Location name : ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) { System.out.println("  [!] Name cannot be empty."); break; }
                    System.out.print("  Location type : ");
                    String type = sc.nextLine().trim();
                    if (bst.contains(name)) {
                        System.out.println("  [!] Location '" + name + "' already exists.");
                    } else {
                        bst.insert(name, type);
                        graph.addVertex(name);
                        System.out.println("  [✓] Location '" + name + "' added.");
                    }
                    break;
                }

                // --- 2. Remove Location ---
                case 2: {
                    System.out.print("  Location name to remove: ");
                    String name = sc.nextLine().trim();
                    if (!bst.contains(name)) {
                        System.out.println("  [!] Location not found.");
                    } else {
                        bst.delete(name);
                        graph.removeVertex(name);
                        System.out.println("  [✓] Location '" + name + "' and its roads removed.");
                    }
                    break;
                }

                // --- 3. List Locations ---
                case 3: {
                    List<BSTNode> nodes = bst.inOrder();
                    if (nodes.isEmpty()) { System.out.println("  No locations stored."); break; }
                    System.out.println("\n  Locations (alphabetical order):");
                    System.out.printf("  %-25s %-20s%n", "Name", "Type");
                    System.out.println("  " + "-".repeat(45));
                    for (BSTNode n : nodes)
                        System.out.printf("  %-25s %-20s%n", n.name, n.type);
                    break;
                }

                // --- 4. Search Location ---
                case 4: {
                    System.out.print("  Search location name: ");
                    String name = sc.nextLine().trim();
                    BSTNode found = bst.find(name);
                    if (found == null) {
                        System.out.println("  [!] Location not found.");
                    } else {
                        System.out.println("  [✓] Found: " + found.name + " | Type: " + found.type);
                        List<Edge> edges = graph.adj.get(found.name);
                        System.out.print("  Connected to: ");
                        if (edges == null || edges.isEmpty()) {
                            System.out.println("(none)");
                        } else {
                            StringJoiner sj = new StringJoiner(", ");
                            for (Edge e : edges) sj.add(e.to + " [" + e.distance + " km]");
                            System.out.println(sj);
                        }
                    }
                    break;
                }

                // --- 5. Add Road ---
                case 5: {
                    System.out.print("  From location : ");
                    String from = sc.nextLine().trim();
                    System.out.print("  To   location : ");
                    String to   = sc.nextLine().trim();

                    if (!graph.hasVertex(from)) { System.out.println("  [!] '" + from + "' not in graph."); break; }
                    if (!graph.hasVertex(to))   { System.out.println("  [!] '" + to   + "' not in graph."); break; }
                    if (from.equalsIgnoreCase(to)) { System.out.println("  [!] Cannot connect a location to itself."); break; }

                    System.out.print("  Distance (km) : ");
                    String distStr = sc.nextLine().trim();
                    if (!distStr.matches("\\d+") || distStr.equals("0")) {
                        System.out.println("  [!] Distance must be a positive integer.");
                        break;
                    }
                    int dist = Integer.parseInt(distStr);
                    if (graph.addEdge(from, to, dist))
                        System.out.println("  [✓] Road added: " + from + " <-> " + to + " [" + dist + " km]");
                    else
                        System.out.println("  [!] Road already exists between these locations.");
                    break;
                }

                // --- 6. Remove Road ---
                case 6: {
                    System.out.print("  From location : ");
                    String from = sc.nextLine().trim();
                    System.out.print("  To   location : ");
                    String to   = sc.nextLine().trim();
                    if (graph.removeEdge(from, to))
                        System.out.println("  [✓] Road removed: " + from + " <-> " + to);
                    else
                        System.out.println("  [!] Road not found.");
                    break;
                }

                // --- 7. Display All ---
                case 7: {
                    System.out.println("\n  === All City Connections ===");
                    graph.displayAll();
                    break;
                }

                // --- 8. BFS ---
                case 8: {
                    System.out.print("  Start location for BFS: ");
                    String start = sc.nextLine().trim();
                    graph.bfs(start);
                    break;
                }

                // --- 9. DFS ---
                case 9: {
                    System.out.print("  Start location for DFS: ");
                    String start = sc.nextLine().trim();
                    graph.dfs(start);
                    break;
                }

                // --- 10. Shortest Path ---
                case 10: {
                    System.out.print("  Source      : ");
                    String src = sc.nextLine().trim();
                    System.out.print("  Destination : ");
                    String dst = sc.nextLine().trim();
                    graph.shortestPath(src, dst);
                    break;
                }

                // --- 0. Exit ---
                case 0: {
                    System.out.println("\n  Goodbye! Stay connected, smart city citizen.\n");
                    sc.close();
                    return;
                }

                default:
                    System.out.println("  [!] Invalid option. Choose 0-10.");
            }
        }
    }
}
