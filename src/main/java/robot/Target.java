package robot;

import com.jme3.math.Vector3f;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public class Target {
    private Vector3f position;

    public Target(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
