package karakters.managers;

import karakters.GameEngine;
import karakters.GameObject;
import karakters.entities.blocks.Wall;
import karakters.entities.enemies.TriangleShooter;
import karakters.enums.ENTITY_TYPE;
import karakters.entities.blocks.Block;
import karakters.entities.enemies.JackedUpEnemyWithAIAndShit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class WorldManager implements KeyListener {
    public float amplitude = 32f; // 32f
    public float frequency = 16f; // 16f
    public float spacing = 3.15f, time;
    public boolean animate = true, pixel = true, fill = true;
    public boolean animateX = false, animateY = true;
    List<GameObject> e, ce, pe, we;
    EntityManager em;

    // blokes
    Random r = new Random();
    Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    Color cb = wallColor(c);
    public BufferedImage brick = AssetManager.brickMaskCopy(AssetManager.blocks[8], c);
    public BufferedImage brickWall = AssetManager.brickMaskCopy(AssetManager.blocks[8], cb);

    public WorldManager(EntityManager em, GameEngine game) {
        this.em = em;
        this.e = em.objects; this.ce = em.culledObjects; this.pe = em.particleObjects; we = em.wallObjects;
        game.addKeyListener(this);
//        jsonTest();
        levelDataGenerator();
    }

    public void tick() {
//        controller.doxx();
        if (!StateManager.DEV_FREEZE_WORLD) time += 0.001; // 0.001
    }

    public void render(Graphics g) {
//        controller.paint(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // adapted from AssetManager darkerColor
    public Color wallColor(Color rgb) {
        int a = rgb.getAlpha();
        int r = Math.max(0, rgb.getRed()  -75);
        int g = Math.max(0, rgb.getGreen()-75);
        int b = Math.max(0, rgb.getBlue() -75);
        return new Color(r, g, b, a);
    }

    // world parser
    public void levelDataGenerator() {
        int[][] tileData0 = generateRandomTileData(width, height, totalPlatforms, enemyCount);
        spawnEverything(tileData0, tileSize, 0, 0, Color.RED);
        //png2MazeParser(tileData0);

        /*int[][] tileData1 = generateRandomTileData(width, height, totalPlatforms, enemyCount);
        spawnEverything(tileData1, tileSize, -width*tileSize/2, -height*tileSize, Color.GREEN);

        int[][] tileData2 = generateRandomTileData(width, height, totalPlatforms, enemyCount);
        spawnEverything(tileData2, tileSize, width*tileSize/2, -height*tileSize, Color.BLUE);*/
    }

    private static int[][] generateRandomTileData(int width, int height, int totalPlatforms, int totalEnemies) {
        int[][] tileData = new int[height][width];
        Random random = new Random();

        // Generate grounded platforms
        int minPlatformLength = 5;
        int maxPlatformLength = 15;
        int platformHeight = 3;

        // THIN PLATFORMS
        int groundedPlatforms = (int)(totalPlatforms * .5f);
        for (int i = 0; i < groundedPlatforms; i++) {
            int platformStart = random.nextInt(width - maxPlatformLength);
            int platformEnd = platformStart + minPlatformLength + random.nextInt(maxPlatformLength - minPlatformLength + 1);

            int platformY = random.nextInt(height);
            for (int x = platformStart; x < platformEnd && platformY < height; x++) {
                try {
                    tileData[platformY][x] = 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        // Generate floating platforms (THICC)
        int floatingPlatforms = (int)(totalPlatforms * .5f);
        for (int i = 0; i < floatingPlatforms; i++) {
            int platformWidth = minPlatformLength + random.nextInt(maxPlatformLength - minPlatformLength + 1);
            int platformX = random.nextInt(width - platformWidth);
            int platformY = random.nextInt(height);

            for (int y = platformY; y < platformY + platformHeight && y < height; y++) {
                for (int x = platformX; x < platformX + platformWidth; x++) {
//                    System.out.println("x: "+x+",y: "+y);
                    try {
                        tileData[y][x] = 1;
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }

        // Add solid blocks around the edges
        for (int x = 0; x < width; x++) {
            tileData[0][x] = 1;              // Bottom row (top on screen)
            tileData[1][x] = 1;              // Bottom row (top on screen)
            tileData[height - 1][x] = 1;     // Top row (bottom on screen)
            tileData[height - 2][x] = 1;     // Top row (bottom on screen)
        }
        for (int y = 0; y < height; y++) {
            tileData[y][0] = 1;              // Leftmost column
            tileData[y][1] = 1;              // Leftmost column
            tileData[y][width - 1] = 1;      // Rightmost column
            tileData[y][width - 2] = 1;      // Rightmost column
        }

        // spawn enemies???
        int index = 0;
        while (index < totalEnemies) {
            int getX = random.nextInt(width-1), getY = random.nextInt(height-1);
            if (tileData[getY][getX] == 0) {
                tileData[getY][getX] = 2;
                index++;
            }
        }

        // try spawning some portals
        int count = 1000; index = 0;
        while (index < count) {
            int getX1 = random.nextInt(width-1), getY1 = random.nextInt(height-1);
            int getX2 = random.nextInt(width-1), getY2 = random.nextInt(height-1);
            if (tileData[getY1][getX1] == 0 && tileData[getY2][getX2] == 0) {
                tileData[getY1][getX1] = tileData[getY2][getX2] = 3;
                index++;
            }
        }

        return tileData;
    }

    private static int[][] convertJSONArrayTo2DArray(JSONArray jsonArray) throws JSONException {
        int[][] result = new int[jsonArray.length()][((JSONArray) jsonArray.get(0)).length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray row = jsonArray.getJSONArray(i);
            for (int j = 0; j < row.length(); j++) {
                result[i][j] = row.getInt(j);
            }
        }
        return result;
    }

    public void jsonTest() {
        String jsonFilePath = "res/data.json";

        try {
            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int[][] data = convertJSONArrayTo2DArray(jsonObject.getJSONArray("data"));

                System.out.println("Tile " + id + ": " + name);
                System.out.println("Data:");

                spawnEverything(data, 32, 0, 0, Color.RED);
                png2MazeParser(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this is where the hell begins
    ArrayList<int[][]> generatedTowers = new ArrayList<>();

    public class RoomNode {
        public int[][] tileData;
        public String question;
        public String[] choices; // 2
        public AnimeNode[] anime;// 1
        public RoomNode[] rooms; // 1
        public RoomNode(String question, String[] choices, RoomNode[] rooms, AnimeNode[] anime, int[][] tileData) {
            this.question = question;
            this.choices = choices;
            this.tileData = tileData;
            this.anime = anime;
            this.rooms = rooms;
        }
    }

    public class AnimeNode {
        public String title;
        public int[][] tileData;
        public AnimeNode(String title, int[][] customTileData) {
            this.title = title;
            this.tileData = customTileData;
        }
    }

    // this must be in SpawnManager
    void spawnEverything(int[][] data, int tileSize, int offsetX, int offsetY, Color blockColor) {
        Random r = new Random();
        int[] t1 = null;
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                int x1 = x * tileSize + offsetX;
                int y1 = y * tileSize - (data.length * tileSize - tileSize) + offsetY;
                we.add(new Wall(x1, y1, tileSize, tileSize, Color.GRAY, this));
                // e.add(new Block(x1, y1, tileSize, tileSize, blockColor, this));
                // TODO: block optimization, e.g. only the outer layer not the inner
                /*if (data[y][x] == 0)
                    we.add(new Wall(x1, y1, tileSize, tileSize, Color.BLUE, this));*/
                if (data[y][x] == 1) {
                    e.add(new Block(x1, y1, tileSize, tileSize, blockColor, this));
                }
                if (data[y][x] == 2) {
                    e.add(r.nextBoolean() ?
                            new TriangleShooter(x1, y1, 64, 64, em) :
                            new JackedUpEnemyWithAIAndShit(x1, y1, 32, 64, em));
                }
                // fun: portals
                /*if (data[y][x] == 3) {
                    if (t1 == null)
                        t1 = new int[]{x1, y1};
                    else {
                        // weird way to sync
                        Portal one = new Portal(t1[0], t1[1], 128, 128, em, Color.BLUE);
                        Portal two = new Portal(x1, y1, 128, 128, em, Color.RED);
                        one.setEndPortal(two);
                        two.setEndPortal(one);
                        e.add(one);
                        e.add(two);
                        t1 = null;
                    }
                }*/
                // System.out.print(data[y][x] + " ");
            }
            // System.out.println();
        }
    }

    void towerGenreGenerator() {
        // design: generate anime first, then run a very sophisticated tree algorithm that maps the rec flowchart

    }

    // my own A*
    int width = 128, height = 512, totalPlatforms = 1000, tileSize = 32, enemyCount = 1000;
    int NODE_SIZE = tileSize, WIDTH = width*NODE_SIZE, HEIGHT = height*NODE_SIZE, yOffset = -(height * NODE_SIZE - NODE_SIZE);
    Controller controller = new Controller();

    // not actual png reader, it's just a function responsible for placing walls for pathfinding
    public void png2MazeParser(int[][] tileData) {
        int width = tileData[tileData.length-1].length;
        int height = tileData.length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tileData[y][x] == 1) {
                    int mouseX = x * NODE_SIZE;
                    int mouseY = y * NODE_SIZE;
                    int xOver = mouseX % NODE_SIZE;
                    int yOver = mouseY % NODE_SIZE;
                    //create walls and add to wall list
                    Node tmpWall = new Node(mouseX - xOver, mouseY - yOver);
                    controller.path.addWall(new Point(tmpWall.getX(), tmpWall.getY()));
                }
            }
        }
    }

    public class Node {
        private int x;
        // varaibles used for x
        private int y;
        private double f, g, h;
        private Node parent;

        /**
         * The node constructor that creates the initial positions of the nodes.
         */
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Setter Functions to set the cost calculations, parents of the node, and
         * more.
         */
        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void setXY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setF(double f) {
            this.f = f;
        }

        public void setG(double g) {
            this.g = g;
        }

        public void setH(double h) {
            this.h = h;
        }

        /**
         * Various getter functions to get the positions of the node, get the
         * heuristic/cost calculations, and the parent of this node.
         */

        public Node getParent() {
            return parent;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getG() {
            return g;
        }

        public double getF() {
            return f;
        }

        public double getH() {
            return h;
        }

        /**
         * Extra methods that is used to compare Nodes and print the location
         * of the nodes.
         */

        @Override
        public String toString() {
            return "F Cost: " + f + " G Cost: " + g + " H Cost: " + h + "\n";
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) {
                return false;
            }

            Node tmp = (Node) obj;
            if(this.x == tmp.getX() && this.y == tmp.getY()) {
                return true;
            }

            return false;
        }
    }

    public class PathFinder {
        private Controller control;
        private Node start, end;
        private boolean deleteWalls, complete, isPause, run, isDijkstra;

        //data structures for A* pathfinding
        private PriorityQueue<Node> open;

        //list closed poitns and of all walls on the grid
        private Set<Point> closed, wall;

        // final path leading to the list
        private ArrayList<Node> finalPath;

        /**
         * inner class used for Dijkstra's Algorithm
         */
        class DFSFinder {

            //list for finalPath creation and keeping track of walls
            private ArrayList<Node> finalPath;

            //data structures for DFS search
            private Stack<Node> open;

            private Node start, end;

            private Set<Point> wall, closed;

            private Controller control;

            public DFSFinder(Controller control) {
                this.control = control;

                finalPath = new ArrayList<Node>();
                wall = new HashSet<Point>();
                open = new Stack<Node>();
                closed = new HashSet<Point>();
            }
        }

        /**
         * inner class used for comaparing Nodes
         */

        class NodeComparator implements Comparator<Node> {
            public int compare(Node xCoord, Node yCoord) {
                if(xCoord.getF() > yCoord.getF()) {
                    return 1;

                }else if(xCoord.getF() < yCoord.getF()) {
                    return -1;

                }else{

                    if(xCoord.getG() > yCoord.getG()) {
                        return 1;

                    }else if(xCoord.getG() < yCoord.getG()) {
                        return -1;

                    }

                }
                return 0;

            }
        }

        public PathFinder(Controller control) {
            this.control = control;

            run = false;
            isPause = true;

            finalPath = new ArrayList<Node>();
            wall = new HashSet<Point>();
            open = new PriorityQueue<Node>(new NodeComparator());
            closed = new HashSet<Point>();
        }

        /**
         * Checks to see if the list of walls contains a certain node.
         */
        public boolean isWall(Point point) {
            return wall.contains(point);
        }

        /**
         * Contains method to see if nodes are in the closed list.
         */
        public boolean closedContains(Point point) {
            return closed.contains(point);
        }

        public boolean closedRemove(Point point) {
            return closed.remove(point);
        }

        /**
         * Contains method to see if nodes are in the open list.
         */
        public boolean openContains(Node n) {
            return open.contains(n);
        }

        public boolean openRemove(Node n) {
            return open.remove(n);
        }

        public Node openFind(Node n) {
            for(Node x : open) {
                if(x.equals(n)) {
                    return x;
                }
            }

            return null;
        }

        /**
         * Adds a wall to the wall list if a wall at the same location is not
         * already present.
         */
        public boolean addWall(Point point) {
            return wall.add(point);
        }

        /**
         * Removes a wall node from the list of walls.
         */
        public boolean removeWall(Point point) {
            return wall.remove(point);
        }

        public void deleteWalls(boolean check) {
            deleteWalls = check;
        }

        public void setisDijkstra(boolean check) {
            isDijkstra = check;
        }

        public void reset() {
            run = false;
            isPause = true;
            complete = false;

            if(deleteWalls) {
                wall.clear();
                deleteWalls = false;
            }

            closed.clear();
            open.clear();
            finalPath.clear();
        }

        /**
         * Various getter methods to get the various lists containing the nodes.
         */
        public Set<Point> getWall() {
            return wall;
        }

        public PriorityQueue<Node> getOpen() {
            return open;
        }

        public Set<Point> getClosed() {
            return closed;
        }

        public ArrayList<Node> getFinal() {
            return finalPath;
        }

        /**
         * Various setter methods to set the various lists containing the nodes.
         */
        public void setStart(Node start) {
            this.start = new Node(start.getX(), start.getY());
            open.add(this.start);
        }

        public void setEnd(Node end) {
            this.end = new Node(end.getX(), end.getY());
        }

        public void setisPause(boolean isPause) {
            this.isPause = isPause;
        }

        public void setisRun(boolean run) {
            this.run = run;
        }

        public boolean isRun() {
            return run;
        }

        public boolean isPause() {
            return isPause;
        }

        public boolean isComplete() {
            return complete;
        }

        /**
         * Constructs the final path from start to end node. Only called once a
         * valid path is found.
         */
        public void constructPath() {
            Node current = end;
            while(!(current.getParent().equals(start))) {
                finalPath.add(0, current.getParent());
                current = current.getParent();
            }

            finalPath.add(0, current);
        }

        /**
         * Method finds the cost associated with moving from the current node to
         * the neighbor node. Uses the formula for the distance between two points.
         */
        public double gCostMovement(Node parent, Node neighbor) {
            //distance from point to point in a grid
            int xCoord = neighbor.getX() - parent.getX();
            int yCoord = neighbor.getY() - parent.getY();

            return (int) (Math.sqrt(Math.pow(xCoord, 2) + Math.pow(yCoord, 2)));
        }

        /**
         * Method finds the heuristic cost from the neighbor node to the end node.
         * From the Stanford page: "Here we compute the number of steps you take if
         * you can’t take a diagonal, then subtract the steps you save by using the
         * diagonal. There are min(dx, dy) diagonal steps, and each one costs D2 but
         * saves you 2⨉D non-diagonal steps."
         *
         * The heuristic used is octile distance where the cost of an orthogonal move
         * is one and the cost of a diagonal is sqrt(2).
         */
        public double hCostMovement(Node neighbor) {
            int hXCost = Math.abs(end.getX() - neighbor.getX());
            int hYCost = Math.abs(end.getY() - neighbor.getY());
            double hCost = hXCost + hYCost;

            if(control.isOctile()) {
                if(hXCost > hYCost) {
                    hCost = ((hXCost - hYCost) + Math.sqrt(2) * hYCost);
                } else {
                    hCost = ((hYCost - hXCost) + Math.sqrt(2) * hXCost);
                }
            }

            return hCost;
        }

        /**
         * A* pathfinding algorithm. Tries to explore the fewest number of nodes to
         * reach the end node. Self corrects the path to the end node using the
         * heuristic cost function h.
         */
        public void aStarPath() {
            //get node with lowest F cost off PQ
            Node current = open.poll();

            //if no min node, then no path
            if(current == null) {
                System.out.println("No path");
                run = false;
                isPause = true;
                return;
            }

            //if min node is the end, then stop algorithm and build final path
            if(!isDijkstra && current.equals(end)) {
                end.setParent(current.getParent());
                run = false;
                isPause = true;
                complete = true;
                constructPath();
                System.out.println("Total Cost of Path: " + end.getParent().getG());
                System.out.println("Size of Open: " + open.size());
                System.out.println("Size of Closed: " + closed.size());
                System.out.println("Size of Path: " + finalPath.size() + "\n");
                return;
            }

            closed.add(new Point(current.getX(), current.getY()));

            //calculate costs for the 8 possible adjacent nodes to current
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {

                    //skip the current node we are exploring
                    if(i == 1 && j == 1) {
                        continue;
                    }

                    int xCoord = (current.getX() - NODE_SIZE) + (NODE_SIZE * i);
                    int yCoord = (current.getY() - NODE_SIZE) + (NODE_SIZE * j);
                    Node neighbor = new Node(xCoord, yCoord);

                    //for Dijkstra, once we encounter end node we have the shortest path
                    if(isDijkstra && neighbor.equals(end)) {
                        end.setParent(current);
                        run = false;
                        isPause = true;
                        complete = true;
                        constructPath();
                        System.out.println("Total Cost of Path: " + end.getParent().getG());
                        System.out.println("Size of Open: " + open.size());
                        System.out.println("Size of Closed: " + closed.size());
                        System.out.println("Size of Path: " + finalPath.size() + "\n");
                        return;
                    }

                    //checks if node is within canvas boundary
                    if(xCoord < 0 || yCoord < 0 || xCoord >= WIDTH || yCoord >=
                            HEIGHT) {
                        continue;
                    }

                    //checks to see if the neighbor node is a wall, in the open/closed list
                    if(isWall(new Point(neighbor.getX(), neighbor.getY()))) {
                        continue;
                    }

                    int wallJumpX = current.getX() + (xCoord - current.getX());
                    int wallJumpY = current.getY() + (yCoord - current.getY());

                    //checks for border in adjacent pos, does not allow for a diagonal
                    //jump across a border
                    if(isWall(new Point(wallJumpX, current.getY())) || isWall(new
                            Point(current.getX(), wallJumpY)) && ((j == 0 | j == 2) && i != 1)) {
                        continue;
                    }

                    //calculate f, g, and h costs for this node
                    double gCost = current.getG() + gCostMovement(current, neighbor);
                    double hCost = hCostMovement(neighbor);
                    double fCost = gCost + hCost;

                    boolean inOpen = openContains(neighbor);
                    boolean inClosed = closedContains(new Point(neighbor.getX(),
                            neighbor.getY()));
                    Node found = openFind(neighbor);

                    //if inOpen and inClosed cases just in case, should not happen
                    //if node in open and we found lower gCost, no need to search neighbor
                    if(inOpen && (gCost < found.getG())) {
                        openRemove(found);
                        neighbor.setG(gCost);
                        neighbor.setF(gCost + found.getH());
                        neighbor.setParent(current);
                        open.add(neighbor);
                        continue;
                    }

                    //if neighbor in closed and found lower gCost, visit again
                    if(inClosed && (gCost < neighbor.getG())) {
                        System.out.println("HEYCLOSED");
                        continue;
                    }

                    //if neighbor not visited, then add to open list
                    if(!inOpen && !inClosed) {

                        if(isDijkstra) {
                            neighbor.setG(gCost);
                            neighbor.setF(gCost);
                        } else {
                            neighbor.setG(gCost);
                            neighbor.setH(hCost);
                            neighbor.setF(fCost);
                        }

                        neighbor.setParent(current);

                        open.add(neighbor);
                    }
                }
            }
        }
    }

    public class Controller {
        public PathFinder path;
        public Node start, end;

        private boolean isOctile;
        private boolean isManhattan;

        public Controller(){
            isOctile = true;
            isManhattan = false;
            path = new PathFinder(this);
        }

        public void doxx() {
            // must be snapped

            // for json
            /*start = new Node(NODE_SIZE, 14*NODE_SIZE);
            end = new Node(14*NODE_SIZE, 14*NODE_SIZE);*/

            start = new Node(NODE_SIZE, NODE_SIZE);
            end = new Node(14*NODE_SIZE, 14*NODE_SIZE);

            for (GameObject o : em.objects) {
                if (o.type == ENTITY_TYPE.Player)
                    end = new Node((int)o.getX() / NODE_SIZE * NODE_SIZE, ((int)o.getY() - yOffset) / NODE_SIZE * NODE_SIZE);
            }

            if (path.isComplete() || !path.isRun()) {
                path.reset();
            }

            if (!path.isComplete() && !path.isRun() && start != null && end != null && start.x != end.x && start.y != end.y) {
                path.setisRun(true);
                path.setStart(start);
            }
            path.setEnd(end);
            path.aStarPath();
        }

        public void paint(Graphics g) {
            int n = NODE_SIZE - 2;
            //Draw grid for pathfind
            g.setColor(Color.lightGray);
            for(int j = 0; j < HEIGHT; j += NODE_SIZE) {
                for(int i = 0; i < WIDTH; i += NODE_SIZE) {
                    if (CameraManager.screen.intersects(new Rectangle(i, j + yOffset, NODE_SIZE, NODE_SIZE))) {
                        g.setColor(new Color(40, 42, 54));
                        g.fillRect(i, j + yOffset, NODE_SIZE, NODE_SIZE);
                        g.setColor(Color.black);
                        g.drawRect(i, j + yOffset, NODE_SIZE, NODE_SIZE);
                    }
                }
            }

            //draw the wall nodes
            Set<Point> wallList = path.getWall();
            g.setColor(new Color(68, 71, 90));
            for(Point pt : wallList) {
                int xCoord = (int) pt.getX();
                int yCoord = (int) pt.getY();

                int x = xCoord + 1, y = yCoord + 1 + yOffset;
                if (CameraManager.screen.intersects(new Rectangle(x, y, n, n)))
                    g.fillRect(x, y, n, n);
            }

            //draw closed list
            Set<Point> closedList = path.getClosed();
            g.setColor(new Color(253, 90, 90));
            for(Point pt : closedList) {
                int xCoord = (int) pt.getX();
                int yCoord = (int) pt.getY();
                int x = xCoord + 1, y = yCoord + 1 + yOffset;
                if (CameraManager.screen.intersects(new Rectangle(x, y, n, n)))
                    g.fillRect(x, y, n, n);
            }

            //draw open list
            PriorityQueue<Node> openList = path.getOpen();
            g.setColor(new Color(80, 250, 123));
            for(Node e : openList) {
                int x = e.getX() + 1, y = e.getY() + 1 + yOffset;;
                if (CameraManager.screen.intersects(new Rectangle(x, y, n, n)))
                    g.fillRect(x, y, n, n);
            }

            //draw final path
            ArrayList<Node> finalPath = path.getFinal();
            g.setColor(new Color(189, 147, 249));
            for(int i = 0; i < finalPath.size(); i++) {
                int x = finalPath.get(i).getX() + 1, y = finalPath.get(i).getY() + 1 + yOffset;;
                if (CameraManager.screen.intersects(new Rectangle(x, y, n, n)))
                    g.fillRect(x, y, n, n);
            }

            //draw the start node
            if(start != null) {
                g.setColor(new Color(139, 233, 253));
                g.fillRect(start.getX() + 1, start.getY() + 1 + yOffset, n, n);
            }

            //draw the end node
            if(end != null) {
                g.setColor(new Color(255, 121, 198));
                g.fillRect(end.getX() + 1, end.getY() + 1 + yOffset, n, n);
            }
        }

        // ???
        public boolean isOctile() {
            return isOctile;
        }
    }
}
