package map;

import com.jme3.math.Vector3f;
import map.fragments.BinaryMapFragment;

import java.util.concurrent.ConcurrentHashMap;


public class TrickyBinaryMap extends SimpleRobotMap<MapPosition, BinaryMapFragment,
        ConcurrentHashMap<MapPosition, BinaryMapFragment>> {

    private float cellSize;

    private TrickyBinaryMap() {
        map = new ConcurrentHashMap<>();
    }

    public float getCellSize() {
        return cellSize;
    }

    @Override
    public void addToMapByCoordinates(Vector3f position, BinaryMapFragment fragment) {
        addToMap(new MapPosition(calculateCellAxisPosition(position.getX()),
                calculateCellAxisPosition(position.getZ())), fragment);
    }

    public TrickyBinaryMap(float cellSize) {
        this();
        this.cellSize = cellSize;
    }

    public Integer calculateCellAxisPosition(float positionByAxis) {
        return Math.round(positionByAxis / cellSize);
    }

    public int getSize() {
        return map.size();
    }


    public void addToMapFakeVertices(MapPosition mapPosition, BinaryMapFragment binaryMapFragment) {
        if (map.get(mapPosition) == null) {
            addToMap(mapPosition, binaryMapFragment);
        }
    }
}
