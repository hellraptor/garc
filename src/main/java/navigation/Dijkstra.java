package navigation;


import map.MapPosition;
import map.TrickyBinaryMap;
import map.fragments.BinaryMapFragment;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Dijkstra {

    TrickyBinaryMap binaryMap;
    MapPosition robotPosition;

    ConcurrentHashMap<MapPosition, BinaryMapFragment> graph;


    public static void main(String[] args) {
        new Dijkstra().buildVisibilityGraph(null);
    }

    TrickyBinaryMap buildTempMap(TrickyBinaryMap binaryMap) {
        TrickyBinaryMap tempMap = new TrickyBinaryMap(binaryMap.getCellSize());


        binaryMap.getMapData().forEach((position, binaryMapFragment) -> {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                for (int z = position.getZ() - 1; z <= position.getZ() + 1; z++) {
                    if (!position.equals(robotPosition)) {
                        tempMap.addToMapFakeVertices(new MapPosition(x, z), new BinaryMapFragment(false));
                    }
                }
            }
        });
        return tempMap;
    }


    public final void buildVisibilityGraph(final TrickyBinaryMap binaryMap) {

        HashMap<MapPosition, TreeMap<Integer, MapPosition>> visibilityGraph = new HashMap<>();


    }


}
