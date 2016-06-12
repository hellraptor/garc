package robot;

import com.jme3.bullet.control.VehicleControl;
import com.jme3.scene.Node;
import map.BinaryMapManager;
import map.fragments.BinaryMapFragment;
import sensors.Lidar;


public class Robot implements Controllable {

    public static final int MASS = 400;
    public static final int SCANING_RADIOUS = 175;
    public static final int LIDAR_X_POS = 0;
    public static final float LIDAR_Y_POS = 1.2f;
    public static final float lIDAR_Z_POS = 2.45f;
    public static final int AXIS_SAMPLES = 16;
    private Lidar lidar;

    private VehicleControl vehicle;

    private Node vehicleNode;

    public void setVehicleNode(Node vehicleNode) {
        this.vehicleNode = vehicleNode;
    }

    public void setVehicle(VehicleControl vehicle) {
        this.vehicle = vehicle;
    }

    public Lidar getLidar() {
        return lidar;
    }

    public VehicleControl getVehicle() {
        return vehicle;
    }

    public BinaryMapManager getMapManager() {
        return mapManager;
    }

    public Node getVehicleNode() {
        return vehicleNode;
    }

    public void setTarget(Target target) {
        this.taget = target;
        getMapManager().getMap().addToMapByCoordinates(target.getPosition(), new BinaryMapFragment(true, true));

    }

    public void setLidar(Lidar lidar) {
        this.lidar = lidar;
        vehicleNode.attachChild(lidar.getLidarGeometry());
    }

    BinaryMapManager mapManager = new BinaryMapManager(0.5f);

    private Target taget;

    public Robot() {
    }

    public void updateMap() {
        mapManager.updateMapWithLidarData(getLidar().getLastMeasure());
        mapManager.getMap().addToMapByCoordinates(vehicleNode.getWorldTranslation(), new BinaryMapFragment(true, false));
    }

    public void start() {

    }

    public void stop() {

    }
}
