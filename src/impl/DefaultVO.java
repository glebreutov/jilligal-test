package impl;

import fundamental.EXPOSED_TYPE;
import fundamental.PooledValueObject;
import fundamental.ValueObject;
import fundamental.ValueObjectPool;

/**
 * Created by glebreutov on 12.10.16.
 */
public abstract class DefaultVO implements ValueObject {

    private EXPOSED_TYPE type;

//    public DefaultVO(EXPOSED_TYPE type) {
//        this.type = type;
//    }

    @Override
    public EXPOSED_TYPE type() {
        return type;
    }

    @Override
    public long long_value(CharSequence name) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void long_value(CharSequence name, long val) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public double double_value(CharSequence name) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void double_value(CharSequence name, double val) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public CharSequence string_value(CharSequence name) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void string_value(CharSequence name, CharSequence value) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public ValueObject get(CharSequence name) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void set(CharSequence name, ValueObject value) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public long long_value(int idx) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void long_value(int idx, long val) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public double double_value(int idx) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void double_value(int idx, double val) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public CharSequence string_value(int i) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void string_value(int i, CharSequence value) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public ValueObject get(int idx) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public void set(int idx, ValueObject value) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public int size() {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public ValueObject keys() {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public ValueObject add_property(String venues, EXPOSED_TYPE map) {
        throw new RuntimeException("unsupported method");
    }

    @Override
    public PooledValueObject withPool(ValueObjectPool pool) {
        throw new RuntimeException("unsupported method");
    }
}
