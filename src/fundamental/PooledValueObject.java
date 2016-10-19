package fundamental;

/**
 * Created by glebreutov on 13.10.16.
 */
public interface PooledValueObject extends ValueObject, AutoCloseable {


    void close();
}
