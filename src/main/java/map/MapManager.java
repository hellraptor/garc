package map;

/**
 * Created by svyatoslav_yakovlev on 6/2/2016.
 */
public abstract class MapManager<M extends SimpleRobotMap> {
    private M map;

    public M getMap() {
        return map;
    }

    public void setMap(M map) {
        this.map = map;
    }

}
