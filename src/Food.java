import java.awt.*;
import java.util.Random;

public class Food extends Entity {

  private final int energyMax = Microbe.STARTING_ENERGY / 4;

  public Food(Point point) {
    this.position = point;
    init();
  }

  public Food(Terrain terrain) {
    Random random = new Random();
    this.position = new Point(random.nextDouble() * terrain.getWidth(),
                              random.nextDouble() * terrain.getHeight());
    init();
  }

  public void init() {
    this.energy = new Random().nextInt(energyMax);
    this.radius = (int) Math.max(Math.sqrt(energy), maxRadius);
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.GREEN);
    g.fillOval((int) this.getPosition().getX(), (int) this.getPosition().getY(),
                this.getRadius(), this.getRadius());
  }

}
