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
import com.jme3.scene.shape.Line;
import jme3test.bullet.PhysicsTestHelper;
import robot.Robot;


/**
 * Created by svyatoslav_yakovlev on 5/12/2016.
 */
public class GarcStarter extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;

    private final float accelerationForce = 1000.0f;
    private final float brakeForce = 100.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    private Vector3f jumpForce = new Vector3f(0, 3000, 0);
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
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
    }

    public void buildRobot() {
        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);
        robot = new Robot(mat);
        attachRobotToScene(robot);
        attachRobotPhysicBodyToPhysicWorld(robot);
        robot.getLidar().setCollidables(collidables);// TODO: 5/26/2016 remove this setter and move addition of colidables to the robot builder

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
    }

    private synchronized void drawRayLines() {
        rootNode.detachChildNamed("Rays");

        Node rays = new Node("Rays");
        for (CollisionResult cl : robot.getLidar().getLastMeasure()) {
            //  if (robot.getLidar().getLastMeasure().size() <= 0) return;
            Line line = new Line(robot.getLidar().getLidarGeometry().getWorldTranslation(), cl.getContactPoint());
            Geometry gline = new Geometry("rayLine", line);
            Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat1.setColor("Color", ColorRGBA.Green);
            gline.setMaterial(mat1);
            rays.attachChild(gline);
        }
        rootNode.attachChild(rays);

    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += .5f;
            } else {
                steeringValue += -.5f;
            }
            robot.getVehicle().steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue += -.5f;
            } else {
                steeringValue += .5f;
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
            } else {
            }
        }
    }

    @Override
    public void stop() {
        robot.getLidar().setOn(false);//// TODO: 5/27/2016
        super.stop();
    }

}