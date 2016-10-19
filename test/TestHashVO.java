import fundamental.EXPOSED_TYPE;
import fundamental.ValueObject;
import impl.jilligal.StructureFactory;
import org.junit.Test;

import java.util.NoSuchElementException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Created by glebreutov on 18.10.16.
 */
public class TestHashVO {

    private ValueObject getFilledObject() {
        ValueObject hash = StructureFactory.local().get_collection(EXPOSED_TYPE.MAP, 100);

        for (int i = 0; i < 10; i++) {
            hash.long_value(""+i, i);
        }
        return hash;
    }

    @Test
    public void test_get_set(){
        ValueObject hash = getFilledObject();

        for (int i = 0; i < 10; i++) {
            long l = hash.long_value("" + i);
            assertEquals(l, i);
        }

        assertEquals(hash.size(), 10);
    }



    @Test
    public void test_iter(){
        ValueObject filledObject = getFilledObject();

        fail("implement iter");
    }

    @Test
    public void test_remove(){
        ValueObject hash = getFilledObject();

        hash.remove_property("8");
        assertEquals(hash.size(), 9);

        assertFalse(hash.property_exists("8"));


        for (int i = 0; i < 10; i++) {
            if(i != 8){
                long l = hash.long_value("" + i);
                assertEquals(l, i);
            }

        }

        try {
            hash.long_value("8");
            fail("method should throw exception");
        }catch (NoSuchElementException e){

        }

    }

    @Test
    public void test_type_collisions(){
        ValueObject filledObject = getFilledObject();
        filledObject.get("8");

    }
}
