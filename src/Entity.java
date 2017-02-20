import java.awt.*;

public abstract class Entity {

  protected Point position;
  protected int radius;
  protected double energy;
  protected final int maxRadius = 10;

  public Point getPosition() {
    return  position;
  }

  public int getRadius() {
    return radius;
  }

  public double getEnergy() {
    return energy;
  }

  public boolean shouldBeRemoved() {
    return energy <= 0;
  }

  public void update() {}

  public void render(Graphics g) {}

  public double reduceEnergy(double amount) {
    double energyLoss = Math.min(amount, energy);
    energy -= energyLoss;
    return energyLoss;
  }

}
