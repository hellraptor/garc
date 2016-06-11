package robot;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public interface Controllable {

    void start();

    void stop();

    void setTarget(Target target);
}
