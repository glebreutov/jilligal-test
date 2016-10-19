package fundamental;

/**
 * Created by glebreutov on 13.10.16.
 */
public interface ValueObjectPool extends AutoCloseable{

    ValueObject pool();

    void put(ValueObject vo);
}
