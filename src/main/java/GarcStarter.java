import builders.RobotBuilder;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import com.jme3.ui.Picture;
import jme3test.bullet.PhysicsTestHelper;
import map.RobotPosition;
import map.fragments.BinaryMapFragment;
import robot.Robot;
import robot.Target;


/**
 * Created by svyatoslav_yakovlev on 5/12/2016.
 */
public class GarcStarter extends SimpleApplication implements ActionListener {

    public static final int JUMP_Y_FORCE = 3000;
    public static final int SCREEN_MARGIN = 500;
    public static final int SCREEN_CELL_SIZE = 10;
    public static final float WHEEL_ROTATION_INKREMENT = .5f;
    private BulletAppState bulletAppState;

    private RobotBuilder robotBuilder;

    private final float accelerationForce = 1000.0f;
    private final float brakeForce = 100.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    private Vector3f jumpForce = new Vector3f(0, JUMP_Y_FORCE, 0);
    private Robot robot;
    private Geometry mark;
    private Node collidables;

    public static void main(String[] args) {
        GarcStarter app = new GarcStarter();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initCrossHairs();
        initMark();
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        collidables = new Node("Collidables");
        setupKeys();
        PhysicsTestHelper.createPhysicsTestWorld(collidables, assetManager, bulletAppState.getPhysicsSpace());
        robotBuilder = new RobotBuilder(getAssetManager(), bulletAppState.getPhysicsSpace(), rootNode);
        robot = robotBuilder.buildRobot(collidables);
        rootNode.attachChild(new Node("Rays"));
        rootNode.attachChild(collidables);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // cam.lookAt(robot.getVehicle().getPhysicsLocation(), Vector3f.UNIT_Y);
        drawRayLines();
        robot.updateMap();
        drawMap();
    }

    private void drawMap() {
        guiNode.detachChildNamed("mapViewNode");
        Node mapViewNode = new Node("mapViewNode");
        robot.getMapManager().getMap().getMapData().forEach((position, binaryMapFragment)
                -> mapViewNode.attachChild(createCellView(position, binaryMapFragment))
        );
        guiNode.attachChild(mapViewNode);
    }

    private Spatial createCellView(RobotPosition position, BinaryMapFragment binaryMapFragment) {
        Picture pictureOfCell = new Picture("Picture1");
        int size = SCREEN_CELL_SIZE;
        int marging = SCREEN_MARGIN;
        pictureOfCell.move(0, 0, -1);
        pictureOfCell.setPosition(marging + size * position.getX(), marging + size * position.getZ());
        pictureOfCell.setWidth(size);
        pictureOfCell.setHeight(size);
        pictureOfCell.setImage(assetManager, binaryMapFragment.isTarget() ? "target.png" : "cell.png", false);
        return pictureOfCell;
    }

    private void drawRayLines() {
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

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("StartMesure", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Stop", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
        inputManager.addListener(this, "StartMesure");
        inputManager.addListener(this, "Shoot");
        inputManager.addListener(this, "Stop");
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            robot.getVehicle().accelerate(1, accelerationValue * 2);
            robot.getVehicle().accelerate(2, -accelerationValue * 2);
        }
        if (binding.equals("Rights")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            robot.getVehicle().accelerate(2, accelerationValue * 2);
            robot.getVehicle().accelerate(1, -accelerationValue * 2);
        }
        if (binding.equals("Ups")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            robot.getVehicle().accelerate(accelerationValue);
        } else if (binding.equals("Downs")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            robot.getVehicle().accelerate(-accelerationValue);
        } else if (binding.equals("Stop")) {
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

        } else if (binding.equals("Shoot") && !value) {
            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(cam.getLocation(), cam.getDirection());
            // 3. Collect intersections between Ray and Shootables in results list.
            collidables.collideWith(ray, results);
            // 4. Print the results
            System.out.println("----- Collisions? " + results.size() + "-----");
            for (int i = 0; i < results.size(); i++) {
                // For each hit, we know distance, impact point, name of geometry.
                float dist = results.getCollision(i).getDistance();
                Vector3f pt = results.getCollision(i).getContactPoint();
                String hit = results.getCollision(i).getGeometry().getName();
                System.out.println("* Collision #" + i);
                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
            }
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                // Let's interact - we mark the hit with a red dot.
                mark.setLocalTranslation(closest.getContactPoint());
                rootNode.attachChild(mark);
                robot.setTarget(new Target(closest.getContactPoint()));

            } else {
                // No hits? Then remove the red mark.
                rootNode.detachChild(mark);
            }
        }
    }


    protected void initMark() {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    @Override
    public void stop() {
        robot.getLidar().interrupt();//// TODO: 5/27/2016
        super.stop();
    }

    /**
     * A centred plus sign to help the operator aim.
     */
    protected void initCrossHairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

}