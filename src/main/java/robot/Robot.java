package robot;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import sensors.Lidar;
import sensors.Sensor;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public class Robot implements Manageble{

    private Sensor lidar;
    private Geometry robotGeometry;
    private RigidBodyControl rigidBodyControl;
    private float mass;

    public Geometry getGeometry(){
        return robotGeometry;
    }

    public Sensor getLidar() {
        return lidar;
    }


    public RigidBodyControl getRigidBodyControl() {
        return rigidBodyControl;
    }

    public float getMass() {
        return mass;
    }


    public Robot(float mass){
        this.mass=mass;
    }


    public void initialise(Material mat){
        lidar=new Lidar();
        initialiseGeometry(mat);
        initialiseRigidBody();

    }

    private void initialiseGeometry(Material mat) {
        Box box = new Box(1,1,2);
        robotGeometry = new Geometry("Box", box);
        robotGeometry.setLocalTranslation(new Vector3f(1,3,1));

        robotGeometry.setMaterial(mat);
    }

    private void initialiseRigidBody() {
        rigidBodyControl = new RigidBodyControl(mass);
        /** Add physical brick to physics space. */
        robotGeometry.addControl(rigidBodyControl);
    }

    public void start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setTarget(Target target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
