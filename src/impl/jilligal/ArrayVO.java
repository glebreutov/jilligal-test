package impl.jilligal;

import fundamental.ValueObject;
import impl.DefaultVO;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ArrayOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ObjectOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.model.pool.ObjectPoolReferenceType;
import tr.com.serkanozal.jillegal.offheap.pool.impl.ComplexTypeArrayOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.ExtendableObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapService;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapServiceFactory;

/**
 * Created by glebreutov on 17.10.16.
 */
public abstract class ArrayVO extends DefaultVO {


    private ComplexTypeArrayOffHeapPool<Object, Object[]> arrayWrapper;

    private ExtendableObjectOffHeapPool<LongHolder> longPool;
    private ExtendableObjectOffHeapPool<DoubleHolder> doublePool;
    public void init(int element_count){
        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();

        arrayWrapper = offHeapService.createOffHeapPool(
                new ArrayOffHeapPoolCreateParameterBuilder<Object>().
                        type(Object.class).
                        length(element_count).
                        initializeElements(false).
                        build());


        longPool = offHeapService.createOffHeapPool(
                new ObjectOffHeapPoolCreateParameterBuilder<LongHolder>().
                        type(LongHolder.class).
                        //objectCount(desired_amount_of_elements).
                        referenceType(ObjectPoolReferenceType.LAZY_REFERENCED).
                        build());

        doublePool = offHeapService.createOffHeapPool(
                new ObjectOffHeapPoolCreateParameterBuilder<DoubleHolder>().
                        type(DoubleHolder.class).
                        //objectCount(desired_amount_of_elements).
                                referenceType(ObjectPoolReferenceType.LAZY_REFERENCED).
                        build());
    }

    private <T> T get_instance(Class<T> clazz){
        if (clazz.equals(LongHolder.class)){
            return (T)longPool.get();
        }else if(clazz.equals(DoubleHolder.class)){
            return (T)doublePool.get();
        }
        return null;
    }


    private void check_type(Object obj, Class clazz){
        if (!obj.getClass().equals(clazz)){
            throw new RuntimeException("Wrong datatype. Expected "+clazz.getName()
                    +" but get "+obj.getClass().getName());
        }
    }

    public <T> T abstract_get(Class<T> clazz, int idx){
        Object at = arrayWrapper.getAt(idx);
    //    if(at != null)
            //check_type(at, clazz);
        return (T) at;
    }

    public <T> T abstract_put_holder(Class<T> clazz, int idx){
        T at = abstract_get(clazz, idx);
        if(at == null){
            at = get_instance(clazz);
            arrayWrapper.setAt(at, idx);
        }
        return at;
    }

    @Override
    public long long_value(int idx) {
        return abstract_get(LongHolder.class, idx).get();
    }


    @Override
    public void long_value(int idx, long val) {
        abstract_put_holder(LongHolder.class, idx).set(val);
    }


    @Override
    public double double_value(int idx) {
        return abstract_get(DoubleHolder.class, idx).get();
    }

    @Override
    public void double_value(int idx, double val) {
        abstract_put_holder(DoubleHolder.class, idx).set(val);
    }

    @Override
    public CharSequence string_value(int i) {
        return abstract_get(CharSequence.class, i);
    }

    private void abstract_set_obj(int i, Class clazz, Object value){
        Object at = arrayWrapper.getAt(i);
        if(at != null){
            //check_type(at, clazz);
            if (at.getClass().equals(LongHolder.class)){
                longPool.free((LongHolder) at);
            }else if(at.getClass().equals(DoubleHolder.class)){
                doublePool.free((DoubleHolder) at);
            }
        }
        arrayWrapper.setAt(value, i);
    }

    @Override
    public void string_value(int i, CharSequence value) {
        abstract_set_obj(i, CharSequence.class, value);
    }

    @Override
    public ValueObject get(int idx) {
        return abstract_get(ValueObject.class, idx);
    }

    @Override
    public void set(int idx, ValueObject value) {
        abstract_set_obj(idx, ValueObject.class, value);


    }

    @Override
    public int size() {
        return arrayWrapper.getLength();
    }
}
