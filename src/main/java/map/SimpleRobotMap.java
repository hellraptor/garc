package map;

import com.jme3.math.Vector3f;
import map.fragments.SimpleMapFragment;

import java.util.Map;


public abstract class SimpleRobotMap<E, V extends SimpleMapFragment, S extends Map<E, V>> {

    protected S map;

    public S getMapData() {
        return map;
    }

    public void addToMap(E position, V fragment) {
        map.put(position, fragment);
    }

    public void deleteFromMap(E position) {
        map.remove(position);
    }

    public abstract void addToMapByCoordinates(Vector3f position, V fragment);

}
