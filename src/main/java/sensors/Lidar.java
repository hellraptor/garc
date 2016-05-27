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

    CollisionResults lastMeasure;

    Geometry lidarGeometry;

    Node collidables;

    boolean isOn;

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
        lidarGeometry.setMaterial(mat);

    }


    public CollisionResults makeMeasure() {
        lastMeasure = null;

        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(lidarGeometry.getWorldTranslation().add(0.2f,0.2f,0.2f), Vector3f.UNIT_X.mult(150f));
        // 3. Collect intersections between Ray and Shootables in results list.
// TODO: 5/27/2016  correct raycasting

         collidables.collideWith(ray, results);
        // 4. Print results.
        System.out.println("----- Collisions? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String hit = results.getCollision(i).getGeometry().getName();
            System.out.println("* Collision #" + i);
            System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
        }

        CollisionResult closest = results.getClosestCollision();

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
