package robot;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import sensors.Lidar;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public class Robot implements Manageble {

    private Lidar lidar;

    private VehicleControl vehicle;
    private Node vehicleNode;

    public Lidar getLidar() {
        return lidar;
    }

    public VehicleControl getVehicle() {
        return vehicle;
    }

    public Node getVehicleNode() {
        return vehicleNode;
    }

    public Robot(Material mat) {
        initialise(mat);
        initialiseLidar(mat);
    }


    private void initialise(Material mat) {

        //create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
        //this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(1.2f, 0.5f, 2.4f));
        compoundShape.addChildShape(box, new Vector3f(0, 1.1f, 0));

        //create vehicle node

        vehicleNode = new Node("vehicleNode");
        vehicle = new VehicleControl(compoundShape, 400);
        vehicleNode.addControl(vehicle);

        //setting suspension values for wheels, this can be a bit tricky
        //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f;
        vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionStiffness(stiffness);
        vehicle.setMaxSuspensionForce(10000.0f);

        //Create four wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
        float radius = 0.5f;
        float restLength = 0.3f;
        float yOff = 0.5f;
        float xOff = 1f;
        float zOff = 2f;

        addWheals(mat, wheelDirection, wheelAxle, radius, restLength, yOff, xOff, zOff);


    }

    private void initialiseLidar(Material mat) {
        lidar = new Lidar(175, mat);
        lidar.getLidarGeometry().move(0, 1.2f, 2.45f);
        vehicleNode.attachChild(lidar.getLidarGeometry());
        lidar.setOn(true);
        lidar.start();
    }

    private void addWheals(Material mat, Vector3f wheelDirection, Vector3f wheelAxle, float radius,
                           float restLength, float yOff, float xOff, float zOff) {
        Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);

        Node node2 = new Node("wheel 1 node");
        Geometry wheels2 = new Geometry("wheel 1", wheelMesh);
        node2.attachChild(wheels2);
        wheels2.rotate(0, FastMath.HALF_PI, 0);
        wheels2.setMaterial(mat);
        vehicle.addWheel(node2, new Vector3f(xOff / 2 - restLength * 1.5f, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node3 = new Node("wheel 2 node");
        Geometry wheels3 = new Geometry("wheel 2", wheelMesh);
        node3.attachChild(wheels3);
        wheels3.rotate(0, FastMath.HALF_PI, 0);
        wheels3.setMaterial(mat);
        vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        Node node4 = new Node("wheel 3 node");
        Geometry wheels4 = new Geometry("wheel 3", wheelMesh);
        node4.attachChild(wheels4);
        wheels4.rotate(0, FastMath.HALF_PI, 0);
        wheels4.setMaterial(mat);
        vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        vehicleNode.attachChild(node2);
        vehicleNode.attachChild(node3);
        vehicleNode.attachChild(node4);
    }


    public void start() {

    }

    public void stop() {

    }

    public void setTarget(Target target) {

    }
}
