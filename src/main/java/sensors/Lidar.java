package sensors;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public class Lidar extends Thread implements Sensor<CopyOnWriteArrayList<CollisionResult>> {

    public static final float distanceOfMesures = 15f;
    private final Material mat;
    CopyOnWriteArrayList<CollisionResult> lastMeasure = new CopyOnWriteArrayList<>();


    Geometry lidarGeometry;

    float angleIncrement = 3f;

    Node collidables;

    boolean isOn;

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    float mesurementFrequency = 5;

    private float scaningRadious = 175;

    public Node getCollidables() {
        return collidables;
    }

    public void setCollidables(Node collidables) {
        this.collidables = collidables;
    }


    @Override
    public synchronized CopyOnWriteArrayList<CollisionResult> getLastMeasure() {
        return lastMeasure;
    }

    public Geometry getLidarGeometry() {
        return lidarGeometry;
    }

    public float getScaningRadious() {
        return scaningRadious;
    }

    public Lidar(float scaningRadious, Material mat) {
        this.scaningRadious = scaningRadious;
        Box b = new Box(0.1f, 0.1f, 0.1f);
        lidarGeometry = new Geometry("Lidar", b);
        mat.setColor("Color", ColorRGBA.Blue);
        this.mat = mat;
        lidarGeometry.setMaterial(this.mat);
    }


    public CopyOnWriteArrayList<CollisionResult> makeMeasure() {
        CollisionResults results = new CollisionResults();
        Vector3f worldTranslation = lidarGeometry.getWorldTranslation();
        CopyOnWriteArrayList<CollisionResult> lastMeasure = new CopyOnWriteArrayList<>();
        float angle = 2.5f;
        while (angle < scaningRadious) {
            Ray ray = new Ray(worldTranslation.add(0f, 0f, 0f),
                    getLidarGeometry().getWorldRotation()
                            .mult(new Vector3f(-distanceOfMesures * (float) Math.cos(Math.toRadians(angle)), 0,
                                    distanceOfMesures * (float) Math.sin(Math.toRadians(angle)))).mult(new Vector3f(1, 0, 1)));
            collidables.collideWith(ray, results);

            CollisionResult result = getDetection(results);
            if (result != null) {
                lastMeasure.add(result);

               System.out.println("Ray origin: " + worldTranslation);
                System.out.println("You shot " + result.getGeometry().getName()
                        + " at " + result.getContactPoint() + ", "
                        + result.getDistance() + " wu away. angle: " +angle);


            }
            angle += angleIncrement;
        }

        this.lastMeasure = lastMeasure;
        return lastMeasure;
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
            while (isOn) {

                makeMeasure();
                System.out.println("sssssss");
                Thread.currentThread().sleep((long) (1000 / mesurementFrequency));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("!Q!!!!!!!!!!!"+e.getMessage());
        }
    }
}
