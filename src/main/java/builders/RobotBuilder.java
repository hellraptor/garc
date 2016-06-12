package builders;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import robot.Robot;
import sensors.Lidar;

import static robot.Robot.*;

public class RobotBuilder {

    private AssetManager assetManager;
    private PhysicsSpace physicsSpace;
    private Node rootNode;

    public RobotBuilder(AssetManager assetManager, PhysicsSpace physicsSpace, Node rootNode) {
        this.assetManager = assetManager;
        this.physicsSpace = physicsSpace;
        this.rootNode = rootNode;
    }

    public Robot buildRobot(Node collidables) {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);
        Robot robot = new Robot();

        /**
         * the following code is setting of suspension
         */

        //create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
        //this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(1.2f, 0.5f, 2.4f));
        compoundShape.addChildShape(box, new Vector3f(0, 1.1f, 0));
        //create vehicle node


        robot.setVehicleNode(new Node("vehicleNode"));
        robot.setVehicle(new VehicleControl(compoundShape, MASS));
        robot.getVehicleNode().addControl(robot.getVehicle());

        //setting suspension values for wheels, this can be a bit tricky
        //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
        float stiffness = 60.0f;//200=f1 car
        float compValue = .3f; //(should be lower than damp)
        float dampValue = .4f;
        robot.getVehicle().setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        robot.getVehicle().setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        robot.getVehicle().setSuspensionStiffness(stiffness);
        robot.getVehicle().setMaxSuspensionForce(10000.0f);

        //Create four wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
        float radius = 0.5f;
        float restLength = 0.3f;
        float yOff = 0.5f;
        float xOff = 1f;
        float zOff = 2f;

        Cylinder wheelMesh = new Cylinder(AXIS_SAMPLES, AXIS_SAMPLES, radius, radius * 0.6f, true);

        Material wheelMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wheelMaterial.getAdditionalRenderState().setWireframe(true);
        wheelMaterial.setColor("Color", ColorRGBA.Green);

        Node node2 = createWheal(wheelMaterial, wheelMesh, 0);
        Node node3 = createWheal(wheelMaterial, wheelMesh, 1);
        Node node4 = createWheal(wheelMaterial, wheelMesh, 2);

        robot.getVehicle().addWheel(node2, new Vector3f(xOff / 2 - restLength * 1.5f, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);
        robot.getVehicle().addWheel(node3, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);
        robot.getVehicle().addWheel(node4, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        robot.getVehicleNode().attachChild(node2);
        robot.getVehicleNode().attachChild(node3);
        robot.getVehicleNode().attachChild(node4);

        rootNode.attachChild(robot.getVehicleNode());
        physicsSpace.add(robot.getVehicle());

        robot.setLidar(buildLidar(collidables, assetManager));
        robot.getLidar().start();
        return robot;
    }

    private Lidar buildLidar(Node collidables, AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Black);

        Lidar lidar = new Lidar(SCANING_RADIOUS);
        Box b = new Box(0.1f, 0.1f, 0.1f);
        lidar.setLidarGeometry(new Geometry("Lidar", b));
        mat.setColor("Color", ColorRGBA.Blue);
        lidar.getLidarGeometry().setMaterial(mat);
        /**
         * setting of the lidar position
         */
        lidar.getLidarGeometry().move(LIDAR_X_POS, LIDAR_Y_POS, lIDAR_Z_POS);
        lidar.setCollisionCollection(collidables);
        return lidar;
    }

    private Node createWheal(Material mat, Cylinder wheelMesh, int wheelNumber) {
        Node node = new Node("wheel_" + wheelNumber + "_node");
        Geometry whealGeometry = new Geometry("wheel_" + wheelNumber, wheelMesh);
        node.attachChild(whealGeometry);
        whealGeometry.rotate(0, FastMath.HALF_PI, 0);
        whealGeometry.setMaterial(mat);
        return node;
    }

}
