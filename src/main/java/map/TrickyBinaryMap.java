package map;

import com.jme3.math.Vector3f;
import javafx.util.Pair;
import map.fragments.BinaryMapFragment;


public class TrickyBinaryMap extends SimpleRobotMap<Pair<Integer, Integer>, BinaryMapFragment> {

    private float cellSize;

    @Override
    public void addToMapByCoordinates(Vector3f position, BinaryMapFragment fragment) {
        addToMap(new Pair<>(calculateCellAxisPosition(position.getX()),
                calculateCellAxisPosition(position.getY())), fragment);
    }

    public TrickyBinaryMap(float cellSize) {
        this.cellSize = cellSize;
    }

    private Integer calculateCellAxisPosition(float positionByAxis) {
        return (int) (positionByAxis / cellSize);
    }

    public int getSize() {
        return map.size();
    }


}
