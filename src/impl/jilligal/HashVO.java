package impl.jilligal;

import fundamental.ValueObject;
import impl.DefaultVO;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ArrayOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ObjectOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.model.pool.ObjectPoolReferenceType;
import tr.com.serkanozal.jillegal.offheap.pool.impl.ComplexTypeArrayOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.EagerReferencedObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapService;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapServiceFactory;

import java.util.NoSuchElementException;

/**
 * Created by glebreutov on 12.10.16.
 */

public class HashVO extends DefaultVO {

    private int node_count = 0;
    private final int hash_coef = 2;
    private ComplexTypeArrayOffHeapPool<HashVONode, HashVONode[]> arrayWrapper;
    private EagerReferencedObjectOffHeapPool<HashVONode> nodePool;


    public void setArrayWrapper(ComplexTypeArrayOffHeapPool<HashVONode, HashVONode[]> arrayWrapper) {
        this.arrayWrapper = arrayWrapper;
    }

    public void setNodePool(EagerReferencedObjectOffHeapPool<HashVONode> nodePool) {
        this.nodePool = nodePool;
    }

    public ComplexTypeArrayOffHeapPool<HashVONode, HashVONode[]> getArrayWrapper() {
        return arrayWrapper;
    }


    public void init(int desired_amount_of_elements){
        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();

        setArrayWrapper(offHeapService.createOffHeapPool(
                new ArrayOffHeapPoolCreateParameterBuilder<HashVONode>().
                        type(HashVONode.class).
                        length(hash_coef * desired_amount_of_elements).
                        initializeElements(false).
                        build()));

//        arrayWrapper = structureFactory.get_wrapped_array(HashVONode.class, desired_amount_of_elements);
//        nodePool = structureFactory.get_typed_pool(HashVONode.class);


//        nodePool = offHeapService.createOffHeapPool(
//                        new DefaultExtendableObjectOffHeapPoolCreateParameterBuilder<HashVONode>().
//                                elementType(HashVONode.class).
//                                build());

        setNodePool(offHeapService.createOffHeapPool(
                new ObjectOffHeapPoolCreateParameterBuilder<HashVONode>().
                        type(HashVONode.class).
                        objectCount(desired_amount_of_elements).
                        referenceType(ObjectPoolReferenceType.EAGER_REFERENCED).
                        build()));
    }


    private HashVONode getVoarrayElt(int i){
        return getArrayWrapper().getAt(i);
    }

    private int hash_code(Object key){
        int h;
        h = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        return Math.abs(h) % getArrayWrapper().getLength();
    }

    private HashVONode get_node(CharSequence key){
        int i = hash_code(key);
        HashVONode o = getVoarrayElt(i);
        if(o == null){
            throw new NoSuchElementException("No property with name" + key);
        }
        while (!o.keyEquals(key)){
            o = o.getNext();
        }
        return o;
    }

    private HashVONode find_or_create_node(CharSequence key){
        //System.err.println("find_or_create_node");
        int i = hash_code(key);
        HashVONode o = getVoarrayElt(i);
        HashVONode po = null;


        while (o != null && !o.keyEquals(key)){
            po = o;
            o = o.getNext();
        }
        if(o == null){
            node_count++;
            o = nodePool.get();
            //System.err.println(key);
            o.setStr(key);
            if(po != null){
                po.setNext(o);
            }else {
                getArrayWrapper().setAt(o, i);
                //getVoarrayElt().setAt()
            }
        }
        return o;
    }

    @Override
    public void remove_property(String s) {
        int i = hash_code(s);
        HashVONode o = getVoarrayElt(i);
        if(o == null) return;

        HashVONode po = null;

        while (o != null && !o.keyEquals(s)){
            po = o;
            o = o.getNext();
        }

        if(po == null){
            getArrayWrapper().setAt(o.getNext(), i);
            nodePool.free(o);
        }else {
            po.setNext(o.getNext());
            nodePool.free(o);
        }
        node_count--;
    }

    @Override
    public boolean property_exists(String s) {
        int i = hash_code(s);
        HashVONode o = getVoarrayElt(i);
        if(o == null){
            return false;
        }
        while (!o.keyEquals(s)){
            o = o.getNext();
        }

        return o != null;
    }

    @Override
    public long long_value(CharSequence name) {
        return get_node(name).getLv();
    }

    @Override
    public void long_value(CharSequence name, long val) {
        find_or_create_node(name).setLv(val);
    }

    @Override
    public double double_value(CharSequence name) {
        return get_node(name).getDv();
    }

    @Override
    public void double_value(CharSequence name, double val) {
        find_or_create_node(name).setDv(val);
    }

    @Override
    public CharSequence string_value(CharSequence name) {
        return get_node(name).getStr();
    }

    @Override
    public void string_value(CharSequence name, CharSequence value) {
        find_or_create_node(name).setStr(value);
    }

    @Override
    public ValueObject get(CharSequence name) {
        return get_node(name).getObj();
    }

    @Override
    public void set(CharSequence name, ValueObject value) {
        find_or_create_node(name).setObj(value);
    }

    @Override
    public int size() {
        return node_count;
    }



}
