
public class Point {


  //Tells whether a certain vector intersects a circle
  public static boolean intersectVectorCircle(Point origin, Point dir, Point centre,
      double radius) {
    return (centre.distLine(origin, origin.add(dir)) <= radius) &&
        (centre.substract(origin).dotProd(dir) >= 0);
  }

  private final double x;
  private final double y;

  public Point() {
    this(0, 0);
  }

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double length() {
    return Math.sqrt(getX() * getX() + getY() * getY());
  }

  public double dist(Point other) {
    return Math.sqrt(Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2));
  }

  public double distLine(Point a, Point b) {
    double normalLength = Math.sqrt(
        (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a
            .getY()));
    return Math.abs(
        (getX() - a.getX()) * (b.getY() - a.getY()) - (getY() - a.getY()) * (b.getX() - a.getX()))
        / normalLength;
  }

  public Point rotate(double angle) {
    return new Point(x * Math.cos(angle) - y * Math.sin(angle),
        x * Math.sin(angle) + y * Math.cos(angle));
  }

  public Point add(Point other) {
    return new Point(x + other.getX(), y + other.getY());
  }

  public Point substract(Point other) {
    return new Point(x - other.getX(), y - other.getY());
  }

  public double dotProd(Point other) {
    return getX() * other.getX() + getY() * other.getY();
  }

  public Point multiply(double scalar) {
    return new Point(this.x * scalar, y * scalar);
  }

  public Point divide(double scalar) {
    if (scalar == 0) {
      throw new IllegalArgumentException("Can not divide points x and y by 0!");
    }


    return new Point(this.x / scalar, y / scalar);
  }

  @Override
  public String toString() {
    return "x: " + getX() + "y: " + getY();
  }
}
