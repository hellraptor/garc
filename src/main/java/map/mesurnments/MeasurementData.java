package map.mesurnments;

/**
 * Created by svyatoslav_yakovlev on 6/2/2016.
 */
public abstract class MeasurementData<T> {
    private T mesurenments;

    public T getMesurenments() {
        return mesurenments;
    }

    public void setMesurenments(T mesurenments) {
        this.mesurenments = mesurenments;
    }
}
