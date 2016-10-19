package samples;

import fundamental.EXPOSED_TYPE;
import fundamental.ValueObject;
import impl.jilligal.ArrayVO;
import impl.jilligal.LongHolder;
import impl.jilligal.StructureFactory;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ExtendableStringOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.ObjectOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.builder.pool.StringOffHeapPoolCreateParameterBuilder;
import tr.com.serkanozal.jillegal.offheap.domain.model.pool.ObjectPoolReferenceType;
import tr.com.serkanozal.jillegal.offheap.pool.DeeplyForkableStringOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.EagerReferencedObjectOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.pool.impl.ExtendableStringOffHeapPool;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapService;
import tr.com.serkanozal.jillegal.offheap.service.OffHeapServiceFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glebreutov on 18.10.16.
 */
public class OneMillionObjects {

    private static final int STRING_COUNT = (int)10E6;
    private static final int ESTIMATED_STRING_LENGTH = 8;



    public static void writeOneMillion(ValueObject hashVO, String [] names){
        for (int i = 0; i < 10E6; i++) {


                hashVO.long_value(names[i], i);

            if(i == 10E5){
                //System.gc();
            }
        }
    }


    public static long readRandomValues(ValueObject hashVO, String [] names){
        long counter = 0;
        for (int i = 0; i < 100000; i++) {
            int id = (int)(Math.random()*10E6);
            counter += hashVO.long_value(names[id]);
        }
        return counter;
    }

    public static long readRandomValues(Map<String, LongHolder> map, String [] names){
        long counter = 0;
        for (int i = 0; i < 100000; i++) {
            int id = (int)(Math.random()*10E6);
            LongHolder o = map.get(names[id]);
            counter += o.get();
        }
        return counter;
    }

    public static void writeOneMillionOnHeap(Map<String, LongHolder> map, String [] names){
        for (int i = 0; i < 10E6; i++) {


            map.put(names[i], new LongHolder(i));
            if(i == 10E5){
                System.gc();
            }
        }
    }
    public static void main(String[] args) {


        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();


//        EagerReferencedObjectOffHeapPool<HashVO> map_pool =
//                offHeapService.createOffHeapPool(
//                        new ObjectOffHeapPoolCreateParameterBuilder<HashVO>().
//                                type(HashVO.class).
//                                objectCount(10).
//                                referenceType(ObjectPoolReferenceType.EAGER_REFERENCED).
//                                build());

        DeeplyForkableStringOffHeapPool stringPool =
                offHeapService.createOffHeapPool(
                        new StringOffHeapPoolCreateParameterBuilder().
                                estimatedStringCount(STRING_COUNT).
                                estimatedStringLength(ESTIMATED_STRING_LENGTH).
                                build());

        ExtendableStringOffHeapPool extendableStringPool =
                offHeapService.createOffHeapPool(
                        new ExtendableStringOffHeapPoolCreateParameterBuilder().
                                forkableStringOffHeapPool(stringPool).
                                build());

        //HashVO hashVO = new HashVO();
        //hashVO.init((int)10E6);



        String [] names = new String[10000000];
        for (int i = 0; i < 10E6; i++) {
            if(i % 2 ==0){
                names[i] = extendableStringPool.get("a" + i);
            }else{
                names[i] = extendableStringPool.get("b" + i);
            }
        }

        ValueObject hashVO = StructureFactory.local().get_collection(EXPOSED_TYPE.MAP, 10000000);

        Map<String, LongHolder> map = new HashMap<>();
        long start = System.currentTimeMillis();
        writeOneMillion(hashVO, names);
        System.err.println("One million inserts to HashVO " +(System.currentTimeMillis() - start));
        System.gc();

        start = System.currentTimeMillis();
        writeOneMillionOnHeap(map, names);
        System.err.println("One million inserts to HashMap " +(System.currentTimeMillis() - start));
        System.gc();

        start = System.currentTimeMillis();
        readRandomValues(hashVO, names);
        System.err.println("100K reads from HashVO " +(System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        readRandomValues(map, names);
        System.err.println("100K reads from HashMap " +(System.currentTimeMillis() - start));
        //hashVO.destruct();
        //writeOneMillion(hashVO, names);

        //testList();
    }

    public static void testList(){

        OffHeapService offHeapService = OffHeapServiceFactory.getOffHeapService();


        EagerReferencedObjectOffHeapPool<ArrayVO> map_pool =
                offHeapService.createOffHeapPool(
                        new ObjectOffHeapPoolCreateParameterBuilder<ArrayVO>().
                                type(ArrayVO.class).
                                objectCount((int)10E6).
                                referenceType(ObjectPoolReferenceType.EAGER_REFERENCED).
                                build());


        ArrayVO arrayVO = map_pool.get();
        arrayVO.init(500000);



        for (int i = 0; i < 500000; i++) {
            arrayVO.set(i, map_pool.get());
            if(i == 10E5){
                System.gc();
            }
        }


        for (int i = 0; i < 500000; i++) {
            arrayVO.set(i, map_pool.get());
            if(i == 10E5){
                System.gc();
            }
        }

        map_pool.free(arrayVO);
    }
}
