package map;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector3f;
import map.fragments.BinaryMapFragment;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by svyatoslav_yakovlev on 6/2/2016.
 */
public class BinaryMapManager extends MapManager<TrickyBinaryMap> {
    // private static final Logger logger = LoggerFactory.getLogger(BinaryMapManager.class);

    private Vector3f robotPosition;

    public void updateMapWithLidarData(CopyOnWriteArrayList<CollisionResult> pointCloud) {
        TrickyBinaryMap map = getMap();
        pointCloud.forEach(point -> map.addToMapByCoordinates(point.getContactPoint(), new BinaryMapFragment(true)));
    }

    public BinaryMapManager(float cellSize) {
        setMap(new TrickyBinaryMap(cellSize));
    }

    public void updateRobotPosition(final Vector3f physicsLocation) {
        if (robotPosition != null) {
            Integer x = getMap().calculateCellAxisPosition(robotPosition.getX());
            Integer z = getMap().calculateCellAxisPosition(robotPosition.getZ());
            getMap().getMapData().remove(new RobotPosition(x, z));
        }
        robotPosition = physicsLocation;
        getMap().addToMapByCoordinates(physicsLocation, new BinaryMapFragment(true));
    }
}
