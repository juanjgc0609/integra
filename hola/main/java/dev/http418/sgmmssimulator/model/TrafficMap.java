package dev.http418.sgmmssimulator.model;

import dev.http418.sgmmssimulator.model.enums.IncidentType;
import dev.http418.sgmmssimulator.model.enums.TileType;
import dev.http418.sgmmssimulator.model.enums.VehicleType;
import dev.http418.sgmmssimulator.model.interfaces.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

import static dev.http418.sgmmssimulator.model.Tile.TILE_SIZE;

public class TrafficMap implements Drawable {
    private final Tile[][] grid;
    private final int width;
    private final int height;


    public TrafficMap(ArrayList<ArrayList<String>> zones) {
        this.width = TILE_SIZE * zones.size();
        this.height = TILE_SIZE * zones.getFirst().size();
        grid = new Tile[zones.size()][zones.getFirst().size()];
        for (int x = 0; x < zones.size(); x++) {
            for (int y = 0; y < zones.getFirst().size(); y++) {
                TileType type = TileType.valueOf(zones.get(x).get(y));
                grid[x][y] = new Tile(x , y, type);
            }
        }
    }
    @Override
    public void draw(GraphicsContext gc, double offsetX, double offsetY) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        int startX = (int) (offsetX / TILE_SIZE);
        int endX = (int) ((offsetX + gc.getCanvas().getWidth()) / TILE_SIZE) + 1;
        int startY = (int) (offsetY / TILE_SIZE);
        int endY = (int) ((offsetY + gc.getCanvas().getHeight()) / TILE_SIZE) + 1;

        for (int x = startX; x < Math.min(endX, grid.length); x++) {
            for (int y = startY; y < Math.min(endY, grid[x].length); y++) {
                Tile tile = grid[x][y];
                double drawX = x * TILE_SIZE - offsetX;
                double drawY = y * TILE_SIZE - offsetY;
                tile.draw(gc, drawX, drawY);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public List<Tile> getNeighbors(int x, int y) {
        List<Tile> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (Math.abs(dx) + Math.abs(dy) == 1) {
                    int nx = x + dx;
                    int ny = y + dy;
                    if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[nx].length) {
                        neighbors.add(grid[nx][ny]);
                    }
                }
            }
        }
        return neighbors;
    }
    public List<Tile> getNeighbors(Tile tile) {
        return getNeighbors((int) tile.getPoint().getX(), (int) tile.getPoint().getY());
    }
    public Queue<Tile> dijkstra(Tile from, Tile destination) {
        Map<Tile, Double> dist = new HashMap<>();
        Map<Tile, Tile> prev = new HashMap<>();
        PriorityQueue<Tile> pq = new PriorityQueue<>(Comparator.comparing(dist::get));

        dist.put(from, 0.0);
        pq.add(from);

        while (!pq.isEmpty()) {
            Tile current = pq.poll();
            if (current.equals(destination)) break;

            for (Tile neighbor : getNeighbors(current)) {
                if (!neighbor.isTraversable()) continue;

                double newDist = dist.get(current) + (1.0 / neighbor.getSpeedModifier());
                if (newDist < dist.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    dist.put(neighbor, newDist);
                    prev.put(neighbor, current);
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        // Reconstruir camino
        LinkedList<Tile> path = new LinkedList<>();
        for (Tile at = destination; at != null; at = prev.get(at)) {
            path.addFirst(at);
        }

        return path.isEmpty() || !path.peek().equals(from) ? new LinkedList<>() : path;
    }

    public ArrayList<Tile> getSpawnableTiles(VehicleType type) {
        Set<Tile> spawnSet = new HashSet<>();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                Tile station = grid[x][y];
                if (station.getType() == type.getSpawnTileType()) {
                    for (Tile neighbor : getNeighbors(x, y)) {
                        if (neighbor.isTraversable()) {
                            spawnSet.add(neighbor);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(spawnSet);
    }

    public ArrayList<Tile> getSpawnableTiles(IncidentType type) {
        Set<Tile> spawnSet = new HashSet<>();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                Tile tile = grid[x][y];

                if (type.getValidSpawnTileTypes().contains(tile.getType())) {
                    if (type == IncidentType.ACCIDENT) {
                        // El accidente ocurre directamente sobre una vía
                        spawnSet.add(tile);
                    } else {
                        // El incidente ocurre en un edificio, pero se genera en un vecino accesible
                        for (Tile neighbor : getNeighbors(x, y)) {
                            if (neighbor.isTraversable()) {
                                spawnSet.add(neighbor);
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(spawnSet);
    }

    public Tile getTile(double x, double y) {
        int tileX = (int)Math.floor(x / TILE_SIZE);
        int tileY = (int)Math.floor(y / TILE_SIZE);
        if (tileX < 0 || tileX >= grid.length || tileY < 0 || tileY >= grid[0].length) {
            return null;
        }
        return grid[tileX][tileY];
    }


    public int getTileWidth() {
        return grid.length;
    }
    public int getTileHeight() {
        return grid[0].length;
    }
}
