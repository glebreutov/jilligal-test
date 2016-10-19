package fundamental;

/**
 * Created by glebreutov on 12.10.16.
 */
public interface ValueObject{

    EXPOSED_TYPE type();

    long long_value(CharSequence name);
    void long_value(CharSequence name, long val);

    double double_value(CharSequence name);
    void double_value(CharSequence name, double val);

    CharSequence string_value(CharSequence name);
    void string_value(CharSequence name, CharSequence value);

    ValueObject get(CharSequence name);
    void set(CharSequence name, ValueObject value);

    //-------------------------------------------

    long long_value(int idx);
    void long_value(int idx, long val);

    double double_value(int idx);
    void double_value(int idx, double val);

    CharSequence string_value(int i);
    void string_value(int i, CharSequence value);

    ValueObject get(int idx);
    void set(int idx, ValueObject value);

    //size
    int size();

    ValueObject keys();


    ValueObject add_property(String venues, EXPOSED_TYPE map);

    PooledValueObject withPool(ValueObjectPool pool);

    void remove_property(String s);

    boolean property_exists(String s);
}
