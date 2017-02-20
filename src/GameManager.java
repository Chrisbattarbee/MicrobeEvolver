import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

  private List<Entity> entities;
  private Terrain terrain;

  private final double foodThreshold = 0.70;
  private int ticks;

  private boolean render = true;

  private Microbe best;

  public GameManager(int initialNumMicrobes, int initialAmountOfFood, int width, int height) {

    terrain = new Terrain(width, height);

    entities = new ArrayList<>();

    for (int x = 0; x < initialNumMicrobes; x ++) {
      Random random = new Random();
      Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
      entities.add(new Microbe(terrain, color));
    }

    for (int x = 0; x < initialAmountOfFood; x ++) {
      entities.add(new Food(terrain));
    }
  }

  private void addFood(List<Entity> entities) {
    if (new Random().nextDouble() > foodThreshold) {
      entities.add(new Food(terrain));
    }
  }

  public void tick(Graphics g) {

    terrain.clearGrid();

    // Clean up all entities
    List<Entity> entitiesToRemove = new ArrayList<>();

    for (Entity entity : entities) {
      if (entity.shouldBeRemoved()) {
        if (best == entity) {
          best = null;
        }
        entitiesToRemove.add(entity);
      }
    }

    entities.removeAll(entitiesToRemove);

    addFood(entities);

    for (Entity entity : entities) {
      terrain.add(entity);
    }

    // Update all entities
    List<Entity> entitiesToAdd = new ArrayList<>();

    for (Entity entity : entities) {
      entity.update();
      if(entity instanceof Microbe) {
        Microbe mic = (Microbe) entity;

        // Update the best microbe
        if (best == null) {
          best = mic;
        } else if (mic.getTotalEnergy() > best.getTotalEnergy()) {
          best = mic;
        }

        // See if we need to spawn any more microbes
        if(mic.getChild() != null) {
          entitiesToAdd.add(mic.getChild());
        }

        mic.setChild(null);
      }
    }

    entities.addAll(entitiesToAdd);

    if (render) {
      for (Entity e : entities) {
        // Display the best microbe as a red circle
        if (e.equals(best)) {
          ((Microbe) e).render(g, Color.RED);
        } else {
          e.render(g);
        }
      }
    }
    ticks ++;
  }

  public Microbe getBest() {
    return best;
  }

  public boolean getRender() {
    return render;
  }

  public void setRender(boolean b) {
    this.render = b;
  }

  public int getTicks() {
    return ticks;
  }

}
