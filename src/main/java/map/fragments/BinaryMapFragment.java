package map.fragments;

import com.jme3.math.Vector3f;

/**
 * Created by svyatoslav_yakovlev on 6/2/2016.
 */
public class BinaryMapFragment extends SimpleMapFragment {

    private boolean target;
    private boolean presented;

    public BinaryMapFragment(boolean presented, boolean target) {
        this.target = target;
        this.presented = true;
    }

    private Vector3f position;// TODO: 6/2/2016 should be used only like debug information

    public BinaryMapFragment(boolean presented) {
        this(presented, false);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public boolean isPresented() {
        return presented;
    }

    public void setPresented(boolean presented) {
        this.presented = presented;
    }

    public boolean isTarget() {
        return target;
    }
}
