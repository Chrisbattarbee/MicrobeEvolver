import static java.lang.Math.PI;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Microbe extends Entity{

  public static final int DIVISION_THRESHOLD = 150;
  public static final int STARTING_ENERGY = 100;
  public static final int DEFAULT_RADIUS = 10;
  public static final double BASE_ENERGY_LOSS = 0.2;
  public static final double MOVE_CONST = 10;
  public static final double MAX_SPEED = 10;
  public static final int viewDivisions = 45;
  private static final Random generator = new Random();

  //half the field of vision of the microbe with respect to the viewDirection
  private double fieldOfVision = PI / 36;

  //Represents the direction the microbe faces in radians, with 0 rad being E
  private Point direction;

  private double speed;

  private Terrain terrain;
  private Point viewDirection;
  private NeuralNetwork nn;
  private int age;
  private Color color;

  private int totalEnergy = 0;

  private Microbe child;

  public Microbe(Terrain terrain, int radius, Point position, Point viewDirection, double fieldOfVision, Point direction, Color color) {
      this.terrain = terrain;
      this.position = position;
      this.nn = new NeuralNetwork(viewDivisions);
      this.age = 0;
      this.radius = radius;
      this.direction = direction;
      this.energy = STARTING_ENERGY;
      this.viewDirection = viewDirection;
      this.fieldOfVision = fieldOfVision;
      this.color = color;
  }

  public Microbe(Terrain terrain, int radius, Point position, NeuralNetwork nn, Color color) {
    this.terrain = terrain;
    this.position = position;
    this.nn = nn;
    this.age = 0;
    this.radius = radius;
    double randomAngle = generator.nextDouble() * 2 * Math.PI;
    this.direction = new Point(Math.sin(randomAngle), Math.cos(randomAngle));
    this.viewDirection = this.direction;
    this.energy = STARTING_ENERGY;
    this.speed = generator.nextDouble() * MAX_SPEED;
    this.color = color;

  }

  public Microbe(Terrain terrain, int radius, Point position, Color color) {
      this(terrain, radius, position, new NeuralNetwork(viewDivisions), color);
  }

  public Microbe(Terrain terrain, int radius, Color color) {
    this(terrain, radius, new Point (generator.nextInt(terrain.getWidth()),
        generator.nextInt(terrain.getHeight())), new NeuralNetwork(viewDivisions), color);
  }

  public Microbe(Terrain terrain, Color color) {
    this(terrain, DEFAULT_RADIUS, new Point (generator.nextInt(terrain.getWidth()),
        generator.nextInt(terrain.getHeight())), new NeuralNetwork(viewDivisions), color);
    double randomAngle = generator.nextDouble() * 2 * Math.PI;
    this.direction = new Point(Math.sin(randomAngle), Math.cos(randomAngle));
    this.viewDirection = this.direction;

  }

  public double getFieldOfVision() {
    return fieldOfVision;
  }

  public Point getDirection() {
    return direction;
  }

  public Microbe getChild() {
    return child;
  }

  public void setChild(Microbe child) {
    this.child = child;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public Point getViewDirection() {
    return viewDirection;
  }

  public boolean isAlive() {
    return energy > 0;
  }

  public void eat(Entity other) {

    if (other instanceof Food) {
      double amount = other.reduceEnergy(other.getEnergy());
      energy += amount;
      totalEnergy += amount;
    }

  }

  public void divide() {
    if (energy >= DIVISION_THRESHOLD) {
      NeuralNetwork newNetwork = GeneticAlgorithms.reproduceWithModifications(nn);
      energy -= STARTING_ENERGY * 1.1;
      double prevX = position.getX();
      double prevY = position.getY();
      double x;
      double y;
      Point newPos;

      // Find a place for the microbe not too close to the parent and that is not empty
      do {
        x = Math.min(terrain.getWidth() - 2, (int) Math.max(0, (Math.random() * 100) + prevX - 50));
        y = Math.min(terrain.getHeight() - 2, (int) Math.max(0, (Math.random() * 100) + prevY - 50));
        newPos = new Point(x, y);
      }while (terrain.isCellEmpty(terrain.getCellPosition(newPos)));

      // Allocate the microbe to the child reference to be pulled by the game manager later
      child = new Microbe(terrain, radius, newPos, newNetwork, this.color);
    }
  }

  @Override
  public void update() {
    List<Double> input = Vision.getVision(this);
    nn.feedForward(input);
    double moveDist = nn.getDistance() * MOVE_CONST;
    double rotationAngle = (nn.getDirection() * 2 - 1) * (Math.PI / 8);
    direction = direction.rotate(rotationAngle);
    viewDirection = direction;

    List<Entity> foodOnPath = Vision.getOnPath(this, moveDist);

    eatOnPath(foodOnPath);
    decreaseEnergy();

    while (energy > DIVISION_THRESHOLD) {
      divide();
    }

    this.position = this.getPosition().add(direction);
    if (position.getX() > terrain.getWidth()) {
      position = new Point(terrain.getWidth() - 1, position.getY());
      direction = new Point(-1 * direction.getX(), direction.getY());
    }
    if (position.getX() < 0) {
      position = new Point(0, position.getY());
      direction = new Point(-1 * direction.getX(), direction.getY());
    }
    if (position.getY() > terrain.getHeight()) {
      position = new Point(position.getX(), terrain.getHeight() - 1);
      direction = new Point(direction.getX(), -1 * direction.getY());
    }
    if (position.getY() < 0) {
      position = new Point(position.getX(), 0);
      direction = new Point(direction.getX(), -1 * direction.getY());
    }

  }

  @Override
  public void render(Graphics g) {
    g.setColor(color);
    g.fillOval((int)this.getPosition().getX(), (int)this.getPosition().getY(), this.getRadius(), this.getRadius());
  }

  public void render(Graphics g, Color color) {
    g.setColor(color);
    g.fillOval((int)this.getPosition().getX(), (int)this.getPosition().getY(), this.getRadius(), this.getRadius());
  }

  private void decreaseEnergy() {
    energy -=  BASE_ENERGY_LOSS;
  }

  private void eatOnPath(List<Entity> entities) {
    for(Entity e : entities) {
      eat(e);
    }
  }

  public double getTotalEnergy() {
    return totalEnergy;
  }

}
