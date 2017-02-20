import org.junit.Test;

import java.awt.*;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class UnitTests {

  @Test
  public void TerrainGetEntitiesInRadiusTest() {
    Terrain terrain = new Terrain(100, 100, 10);

    terrain.add(new Microbe(terrain, 0, new Point(15, 15), Color.cyan));
    terrain.add(new Microbe(terrain, 0, new Point(0, 0), Color.cyan));
    terrain.add(new Food(new Point(0, 0)));

    terrain.add(new Microbe(terrain, 0, new Point(50, 50), Color.cyan));
    terrain.add(new Microbe(terrain, 0, new Point(60, 50), Color.cyan));
    terrain.add(new Microbe(terrain, 0, new Point(50, 60), Color.cyan));

    /* Only for a visualization purposes

    for (int y = 0; y < terrain.getGridHeight(); y++) {
      for (int x = 0; x < terrain.getGridWidth(); x++) {
        System.out.print(terrain.getEntitiesAtCell(new Point(x, y)).size() + ", ");
      }
      System.out.println();
    }
    */

    assertEquals(1, terrain.getEntitiesInRadius(Microbe.class, new Point(15, 15), 10).size());
    assertEquals(1, terrain.getEntitiesInRadius(Microbe.class, new Point(15, 15), 14).size());
    assertEquals(2, terrain.getEntitiesInRadius(Microbe.class, new Point(15, 15), 25).size());
    assertEquals(3, terrain.getEntitiesInRadius(new Point(15, 15), 25).size());
    assertEquals(1, terrain.getEntitiesInRadius(new Point(50, 50), 5).size());
    assertEquals(0, terrain.getEntitiesInRadius(new Point(50, 50), 0).size());
    assertEquals(0, terrain.getEntitiesInRadius(new Point(50, 50), -10).size());
    assertEquals(6, terrain.getEntitiesInRadius(new Point(50, 50), 100000).size());
  }

  @Test
  public void getEntitiesTerrain() {

    Terrain terrain = new Terrain(100, 100, 10);
    Random random = new Random();


    for (int i = 0; i < 500; i++) {
      terrain.add(new Microbe(terrain, 10, new Point(random.nextInt(100), random.nextInt(100)), Color.cyan));
    }

    assertEquals(500, terrain.getEntitiesInRadius(Microbe.class, new Point(50, 50), 10000).size());
  }

}
