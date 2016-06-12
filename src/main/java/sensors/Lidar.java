package sensors;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public class Lidar extends Thread implements Sensor<CopyOnWriteArrayList<CollisionResult>> {

    private static final float distanceOfMesures = 15f;
    private static final float STARTING_ANGLE = 2.5f;
    private static final boolean debugMode = true;
    private float scanningRadius = 175;
    private float angleIncrement = 3f;
    private float measurementFrequency = 5;
    private boolean isOn;
    private Node collisionCollection;
    private CopyOnWriteArrayList<CollisionResult> lastMeasure = new CopyOnWriteArrayList<>();
    private Geometry lidarGeometry;

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public synchronized CopyOnWriteArrayList<CollisionResult> getLastMeasure() {
        return lastMeasure;
    }

    public Node getCollisionCollection() {
        return collisionCollection;
    }

    public void setCollisionCollection(Node collisionCollection) {
        this.collisionCollection = collisionCollection;
    }

    public Geometry getLidarGeometry() {
        return lidarGeometry;
    }

    public float getScanningRadius() {
        return scanningRadius;
    }

    public Lidar(float scanningRadius) {
        this.scanningRadius = scanningRadius;
    }

    public CopyOnWriteArrayList<CollisionResult> makeMeasure() {
        CollisionResults results = new CollisionResults();
        Vector3f worldTranslation = lidarGeometry.getWorldTranslation();
        CopyOnWriteArrayList<CollisionResult> lastMeasure = new CopyOnWriteArrayList<>();
        float angle = STARTING_ANGLE;
        while (angle < scanningRadius) {
            Ray ray = new Ray(worldTranslation, getLidarGeometry().getWorldRotation()
                    .mult(new Vector3f(-distanceOfMesures * (float) Math.cos(Math.toRadians(angle)), 0,
                            distanceOfMesures * (float) Math.sin(Math.toRadians(angle)))).mult(new Vector3f(1, 0, 1)));
            collisionCollection.collideWith(ray, results);

            CollisionResult result = getDetection(results);
            if (result != null) {
                lastMeasure.add(result);
                lidarDetectionLogining(worldTranslation, angle, result);
            }
            angle += angleIncrement;
        }

        this.lastMeasure = lastMeasure;
        return lastMeasure;
    }

    private void lidarDetectionLogining(Vector3f worldTranslation, float angle, CollisionResult result) {
        if (debugMode) {
            System.out.println("Ray origin: " + worldTranslation);
            System.out.println("You shot " + result.getGeometry().getName()
                    + " at " + result.getContactPoint() + ", "
                    + result.getDistance() + " wu away. angle: " + angle);
        }
    }

    private CollisionResult getDetection(final CollisionResults results) {
        for (CollisionResult collisionResult : results) {
            if (!isSelfDetection(collisionResult) && (collisionResult.getDistance() < distanceOfMesures)) {
                return collisionResult;
            }
        }
        return null;
    }

    private boolean isSelfDetection(CollisionResult collisionResult) {
        return collisionResult.getGeometry().getName().equals(lidarGeometry.getName());
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (isOn) {
                    makeMeasure();
                }
                Thread.currentThread().sleep((long) (1000 / measurementFrequency));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("!!!!!!!!!!!" + e.getMessage());
        }
    }

    public void setLidarGeometry(Geometry lidarGeometry) {
        this.lidarGeometry = lidarGeometry;
    }
}
