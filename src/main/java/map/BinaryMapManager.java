package map;

import com.jme3.collision.CollisionResult;
import map.fragments.BinaryMapFragment;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by svyatoslav_yakovlev on 6/2/2016.
 */
public class BinaryMapManager extends MapManager<TrickyBinaryMap> {
   // private static final Logger logger = LoggerFactory.getLogger(BinaryMapManager.class);

    public void updateMapWithLidarData(CopyOnWriteArrayList<CollisionResult> pointCloud) {
        TrickyBinaryMap map = getMap();
        pointCloud.forEach(point -> map.addToMapByCoordinates(point.getContactPoint(), new BinaryMapFragment(true)));
        System.out.println("new map size is " + map.getSize());
    }

    public BinaryMapManager(float cellSize) {
        setMap(new TrickyBinaryMap(cellSize));
    }

}
