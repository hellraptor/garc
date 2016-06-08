import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResult;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.jme3.ui.Picture;
import javafx.util.Pair;
import jme3test.bullet.PhysicsTestHelper;
import map.fragments.BinaryMapFragment;
import robot.Robot;


/**
 * Created by svyatoslav_yakovlev on 5/12/2016.
 */
public class GarcStarter extends SimpleApplication implements ActionListener {

    public static final int JUMP_Y_FORCE = 3000;
    public static final int SCREEN_MARGIN = 500;
    public static final float WHEEL_ROTATION_INKREMENT = .5f;
    public static final int SCREEN_CELL_SIZE = 10;
    private BulletAppState bulletAppState;

    private final float accelerationForce = 1000.0f;
    private final float brakeForce = 100.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    private Vector3f jumpForce = new Vector3f(0, JUMP_Y_FORCE, 0);
    private Robot robot;
    Node collidables;

    public static void main(String[] args) {
        GarcStarter app = new GarcStarter();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        collidables = new Node("Collidables");
        PhysicsTestHelper.createPhysicsTestWorld(collidables, assetManager, bulletAppState.getPhysicsSpace());
        setupKeys();
        buildRobot();
        rootNode.attachChild(new Node("Rays"));
        rootNode.attachChild(collidables);
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("StartMesure", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
        inputManager.addListener(this, "StartMesure");
    }

    public void buildRobot() {
        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);
        robot = new Robot(mat);
        attachRobotToScene(robot);
        attachRobotPhysicBodyToPhysicWorld(robot);
        robot.getLidar().setCollisionCollection(collidables);// TODO: 5/26/2016 remove this setter and move addition of colidables to the robot builder

    }

    private void attachRobotPhysicBodyToPhysicWorld(Robot robot) {
        getPhysicsSpace().add(robot.getVehicle());
    }

    private void attachRobotToScene(Robot robot) {
        rootNode.attachChild(robot.getVehicleNode());
    }

    @Override
    public void simpleUpdate(float tpf) {
        cam.lookAt(robot.getVehicle().getPhysicsLocation(), Vector3f.UNIT_Y);
        drawRayLines();
        robot.updateMap();
        drawMap();
    }

    private void drawMap() {
        //   guiNode.detachChildNamed("map");
        Node map = new Node("map");
        robot.getMapManager().getMap().getMapData().forEach((position, binaryMapFragment)
                -> map.attachChild(createCellView(position, binaryMapFragment))
        );
        guiNode.attachChild(map);
    }

    private Spatial createCellView(Pair<Integer, Integer> position, BinaryMapFragment binaryMapFragment) {
        Picture pictureOfCell = new Picture("Picture1");
        int size = SCREEN_CELL_SIZE;
        pictureOfCell.move(0, 0, -1);
        int marging = SCREEN_MARGIN;
        pictureOfCell.setPosition(marging + size * position.getKey(), marging + size * position.getValue());
        pictureOfCell.setWidth(size);
        pictureOfCell.setHeight(size);
        pictureOfCell.setImage(assetManager, "Interface/Logo/Monkey.jpg", false);
        return pictureOfCell;
    }

    private synchronized void drawRayLines() {
        rootNode.detachChildNamed("Rays");
        Node rays = new Node("Rays");
        for (CollisionResult cl : robot.getLidar().getLastMeasure()) {
            Line line = new Line(robot.getLidar().getLidarGeometry().getWorldTranslation(), cl.getContactPoint());
            Geometry gLine = new Geometry("rayLine", line);
            Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat1.setColor("Color", ColorRGBA.Green);
            gLine.setMaterial(mat1);
            rays.attachChild(gLine);
        }
        rootNode.attachChild(rays);
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += WHEEL_ROTATION_INKREMENT;
            } else {
                steeringValue += -WHEEL_ROTATION_INKREMENT;
            }
            robot.getVehicle().steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue += -WHEEL_ROTATION_INKREMENT;
            } else {
                steeringValue += WHEEL_ROTATION_INKREMENT;
            }
            robot.getVehicle().steer(steeringValue);
        } else if (binding.equals("Ups")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            robot.getVehicle().accelerate(accelerationValue);
        } else if (binding.equals("Downs")) {
            if (value) {
                robot.getVehicle().brake(brakeForce);
            } else {
                robot.getVehicle().brake(0f);
            }
        } else if (binding.equals("Space")) {
            if (value) {
                robot.getVehicle().applyImpulse(jumpForce, Vector3f.ZERO);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                robot.getVehicle().setPhysicsLocation(Vector3f.ZERO);
                robot.getVehicle().setPhysicsRotation(new Matrix3f());
                robot.getVehicle().setLinearVelocity(Vector3f.ZERO);
                robot.getVehicle().setAngularVelocity(Vector3f.ZERO);
                robot.getVehicle().resetSuspension();
            }
        } else if (binding.equals("StartMesure")) {
            if (value) {
                System.out.println("StartMesure");
                robot.getLidar().setOn(!robot.getLidar().isOn());
            }

        }
    }

    @Override
    public void stop() {
        robot.getLidar().interrupt();//// TODO: 5/27/2016
        super.stop();
    }

}