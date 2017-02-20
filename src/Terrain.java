import java.util.ArrayList;
import java.util.List;

public class Terrain {
  private int width;
  private int height;


  // 2D array of Lists to store the organisms in the cells of the grid
  private List<Entity>[][] grid;

  // Width and height of every cell in the grid
  private int cellWidth;
  private int cellHeight;

  // Number of cells for width and height
  private int gridWidth;
  private int gridHeight;

  public Terrain(int width, int height) {
    this(width, height, width / 10, height / 10);
  }

  public Terrain(int width, int height, int cellSize) {
    this(width, height, cellSize, cellSize);
  }

  public Terrain(int width, int height, int cellWidth, int cellHeight) {
    this.width = width;
    this.height = height;

    this.cellWidth = cellWidth;
    this.cellHeight = cellHeight;

    this.gridWidth = width / cellWidth;
    this.gridHeight = height / cellHeight;

    this.grid = new ArrayList[gridHeight][gridWidth];

    this.clearGrid();
  }

  public void add(Entity e) {
    if (!isInMapBounds(e.getPosition())) {
      return;
    }

    Point cellPosition = getCellPosition(e);
    grid[(int) cellPosition.getY()][(int) cellPosition.getX()].add(e);
  }

  public void clearGrid() {
    for (int y = 0; y < gridHeight; y++) {
      for (int x = 0; x < gridWidth; x++) {
        this.grid[y][x] = new ArrayList<>();
      }
    }
  }

  public boolean isInMapBounds(Point p) {
    return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
  }

  public List<Entity> getEntitiesAtCell(Point p) {
    return grid[(int)p.getY()][(int)p.getX()];
  }

  public List<Entity> getEntitiesInRadius(Point p, double radius) {
    return getEntitiesInRadius(Entity.class, p, radius);
  }

  public List<Entity> getEntitiesInRadius(Class<?> cls, Point p, double radius) {
    if (!isInMapBounds(p) || radius <= 0) {
      return new ArrayList<>();
    }

    Point cellCoord = getCellPosition(p);
    List<Entity> result = new ArrayList<>();

    int radius_width = (int)(Math.ceil(Math.min(radius, width) / cellWidth));
    int radius_height = (int)(Math.ceil(Math.min(radius, height) / cellHeight));

    for (int y = -radius_height; y <= radius_height; y++) {
      for (int x = -radius_width; x <= radius_width; x++) {

        int px = (int)cellCoord.getX() + x;
        int py = (int)cellCoord.getY() + y;
        px = Math.min(px, gridWidth - 1);
        px = Math.max(px, 0);
        py = Math.min(py, gridHeight - 1);
        py = Math.max(py, 0);

        for (Entity m : grid[py][px]) {
          if (m.getClass() != cls && m.getClass().getSuperclass() != cls) {
            continue;
          }
          if (m.getPosition().dist(p) <= radius) {
            if (!result.contains(m)) {
              result.add(m);
            }
          }
        }
      }
    }

    return result;
  }

  public Point getCellPosition(Point p) {
    return new Point(p.getX() / cellWidth, p.getY() / cellHeight);
  }

  public Point getCellPosition(Entity m) {
    return new Point(m.getPosition().getX() / cellWidth, m.getPosition().getY() / cellHeight);
  }

  public boolean isCellEmpty(Point p) {
    return grid[(int) p.getY()][(int) p.getX()].isEmpty();
  }

  public int getCellWidth() {
    return cellWidth;
  }

  public int getCellHeight() {
    return cellHeight;
  }

  public int getGridWidth() {
    return gridWidth;
  }

  public int getGridHeight() {
    return gridHeight;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

}
