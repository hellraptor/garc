package sensors;

/**
 * Created by svyatoslav_yakovlev on 5/22/2016.
 */
public interface Sensor <E>{
    E makeMeasure();
    E getLastMeasure();
}
