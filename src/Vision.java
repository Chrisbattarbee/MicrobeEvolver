import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Vision {

    public static final double VISION_RADIUS = 100;


    //Returns true if a vector intersects an Entity
    private static boolean intersectVectorEntity(Point origin, Point dir, Entity entity) {
        return Point.intersectVectorCircle(origin, dir, entity.getPosition(), entity.radius);
    }

    //Returns true if a vector intersects at least one entity
    private static boolean intersectVectorEntities(Point origin, Point dir, List<Entity> entities) {
        for(Entity entity : entities) {
            if (intersectVectorEntity(origin, dir, entity)) {
                return true;
            }
        }

        return false;
    }

    //Returns the list of entities on the way of microbe and at a certain distance
    public static List<Entity> getOnPath(Microbe microbe, double distance) {
        List<Entity> closeEntities = microbe.getTerrain().getEntitiesInRadius(microbe.getPosition(), distance);

        return closeEntities.stream()
                            .filter(x -> intersectVectorEntity(microbe.getPosition(), microbe.getDirection(), x)
                                            && x != microbe)
                            .collect(Collectors.toList());
    }

    //Returns a vision given a terrain and a microbe
    public static List<Double> getVision(Microbe microbe) {

        List<Double> vision = new ArrayList<>();

        List<Entity> closeMicrobeEntities = microbe.getTerrain().getEntitiesInRadius(microbe.getPosition(), VISION_RADIUS);

        closeMicrobeEntities =
                closeMicrobeEntities.stream()
                                    .filter(x -> x != microbe)
                                    .collect(Collectors.toList());

        Point tempDir = microbe.getViewDirection().rotate(-((Microbe.viewDivisions + 1) * microbe.getFieldOfVision()));

        for (int i = 0; i < 2 * Microbe.viewDivisions + 1; i++) {
            tempDir = tempDir.rotate(microbe.getFieldOfVision());
            vision.add(new Double(intersectVectorEntities(microbe.getPosition(), tempDir, closeMicrobeEntities) ? 1 : 0));
        }

        return vision;
    }
}
