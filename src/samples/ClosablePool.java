package samples;

import fundamental.PooledValueObject;
import fundamental.ValueObject;
import fundamental.ValueObjectPool;

/**
 * Created by glebreutov on 13.10.16.
 */
public class ClosablePool {

    ValueObjectPool pool;
    public void working_with_tree_struct(ValueObject obj){
        try (PooledValueObject pooled = obj.withPool(pool)){
            ValueObject valueObject = pooled.get("AAPL").get("NASDAQ").get("bid_book");
            for (int i = 0; i < valueObject.size(); i++) {
                System.out.println(i+ " : "+ valueObject.get(i).long_value("price"));
            }
        }
    }
}
