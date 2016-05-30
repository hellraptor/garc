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

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public class Lidar extends Thread implements Sensor<CollisionResults> {

    private final Material mat;
    CollisionResults lastMeasure;

    Geometry lidarGeometry;

    Node collidables;

    boolean isOn;
    private Geometry gray;
    public Vector3f add;//// TODO: 5/28/2016 refactor public fields
    public Vector3f add1;

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    float mesurementFrequency = 10;

    private float scaningRadious = 270;

    public Node getCollidables() {
        return collidables;
    }

    public void setCollidables(Node collidables) {
        this.collidables = collidables;
    }


    public CollisionResults getLastMeasure() {
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


    public CollisionResults makeMeasure() {
        lastMeasure = new CollisionResults();
        gray = null;

        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.

        Vector3f worldTranslation = lidarGeometry.getWorldTranslation();

        add = worldTranslation.add(0f, 0f, 0.2f);
        add1 = worldTranslation.add(getLidarGeometry().getWorldRotation().mult(new Vector3f(15f, 0, 0)));
        Ray ray = new Ray(add, add1);

        System.out.println("* lidarGeometry.getWorldTranslation" + lidarGeometry.getWorldTranslation());

        // 3. Collect intersections between Ray and Shootables in results list.
// TODO: 5/27/2016  correct raycasting

        collidables.collideWith(ray, results);

        CollisionResult closest = results.getClosestCollision();
        // 4. Print results.
        if (closest == null) return lastMeasure;
        // For each hit, we know distance, impact point, name of geometry.
        float dist = closest.getDistance();
        if (dist > 15) {
            return lastMeasure;
        }
        lastMeasure.addCollision(closest);
        Vector3f pt = closest.getContactPoint();
        String hit = closest.getGeometry().getName();
        System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
        return lastMeasure;
    }

    @Override
    public void run() {
        try {
            while (isOn) {
                makeMeasure();// TODO: 5/27/2016 thread should stops when app is stoped
                Thread.currentThread().sleep((long) (1000 / mesurementFrequency));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
