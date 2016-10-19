package impl.jilligal;

import fundamental.EXPOSED_TYPE;
import fundamental.ValueObject;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ArrayOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ObjectOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.model.pool.ObjectPoolReferenceType;
import tr.com.serkanozal.jillegal.offheap.pool.impl.EagerReferencedObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.ExtendableObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapService;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapServiceFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glebreutov on 17.10.16.
 */
public class StructureFactory {


    //Todo: add real tread localness
    private static StructureFactory structureFactory = new StructureFactory();
    public static StructureFactory local(){
        return structureFactory;
    }

    private OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();
    private Map<Class, ExtendableObjectOffHeapPool> pools = new HashMap<>();

    private EagerReferencedObjectOffHeapPool<HashVO> hash_pool;

    private StructureFactory(){
        hash_pool =
                offHeapService.createOffHeapPool(
                        new ObjectOffHeapPoolCreateParameterBuilder<HashVO>().
                                type(HashVO.class).
                                objectCount(10).
                                referenceType(ObjectPoolReferenceType.EAGER_REFERENCED).
                                build());

    }

    public ValueObject get_collection(EXPOSED_TYPE type, int desired_element_count){
        switch (type){
            case MAP:
                HashVO hashVO = hash_pool.get();
                //hashVO.init(desired_element_count);
                hashVO.setArrayWrapper(offHeapService.createOffHeapPool(
                        new ArrayOffHeapPoolCreateParameterBuilder<HashVONode>().
                                type(HashVONode.class).
                                length(desired_element_count).
                                initializeElements(false).
                                build()));

//        arrayWrapper = structureFactory.get_wrapped_array(HashVONode.class, desired_amount_of_elements);
//        nodePool = structureFactory.get_typed_pool(HashVONode.class);


//        nodePool = offHeapService.createOffHeapPool(
//                        new DefaultExtendableObjectOffHeapPoolCreateParameterBuilder<HashVONode>().
//                                elementType(HashVONode.class).
//                                build());

                hashVO.setNodePool(offHeapService.createOffHeapPool(
                        new ObjectOffHeapPoolCreateParameterBuilder<HashVONode>().
                                type(HashVONode.class).
                                objectCount(desired_element_count).
                                referenceType(ObjectPoolReferenceType.EAGER_REFERENCED).
                                build()));
                return hashVO;
            default:
                throw new RuntimeException("Type is not implemented");

        }
    }


}
