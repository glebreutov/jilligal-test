import impl.jilligal.HashVO;
import impl.jilligal.HashVONode;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ObjectOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.StringOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.model.pool.ObjectPoolReferenceType;
import tr.com.serkanozal.jillegal.offheap.pool.StringOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.EagerReferencedObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.LazyReferencedObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapService;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapServiceFactory;


/**
 * Created by glebreutov on 14.10.16.
 */
public class TestApp{

    private static final int STRING_COUNT = 1000;
    private static final int ESTIMATED_STRING_LENGTH = 40;

    public static void main4(String[] args) {
        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();

        LazyReferencedObjectOffHeapPool<HashVONode> lazyReferencedSequentialObjectPool =
                offHeapService.createOffHeapPool(
                        new ObjectOffHeapPoolCreateParameterBuilder<HashVONode>().
                                type(HashVONode.class).
                                objectCount(1000).
                                referenceType(ObjectPoolReferenceType.LAZY_REFERENCED).
                                build());

        HashVONode hashVONode = lazyReferencedSequentialObjectPool.get();
        //hashVONode.lv = 2323;
        //hashVONode.dv = 2323.1;

    }



    private static StringOffHeapPool strPool() {

        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();

        StringOffHeapPool stringPool =
                offHeapService.createOffHeapPool(
                        new StringOffHeapPoolCreateParameterBuilder().
                                estimatedStringCount(STRING_COUNT).
                                estimatedStringLength(ESTIMATED_STRING_LENGTH).
                                build());

        return stringPool;

    }

    public static HashVO get_hash_map(){

        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();

        EagerReferencedObjectOffHeapPool<HashVO> map_pool =
                offHeapService.createOffHeapPool(
                        new ObjectOffHeapPoolCreateParameterBuilder<HashVO>().
                                type(HashVO.class).
                                objectCount(10).
                                referenceType(ObjectPoolReferenceType.EAGER_REFERENCED).
                                build());

        HashVO hashVO = map_pool.get();
        hashVO.init(10000);

        return hashVO;
    }

    public static void main(String[] args) {
        //System.setProperty("mysafe.enableSafeMemoryManagementMode", "true");
        //System.setProperty("mysafe.enableSafeMemoryAccessMode", "true");

        //MySafe.youAreMine();


        HashVO hashVO = get_hash_map();
        String test = strPool().get("test");
        String testd = strPool().get("testd");

        hashVO.long_value(test, 777);
        hashVO.long_value(testd, 555);

        System.err.println(hashVO.long_value(test));
        System.err.println(hashVO.long_value(testd));
        System.err.println(hashVO.size());
    }
}
