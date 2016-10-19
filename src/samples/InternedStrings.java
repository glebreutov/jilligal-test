package samples;

import fundamental.*;

/**
 * Created by glebreutov on 13.10.16.
 */
public class InternedStrings {

    public void string_pool(InternedStringPool is){
        int aapl = is.register("AAPL");
        int ibm = is.register("IBM");
        int msft = is.register("MSFT");

        int arca = is.register("ARCA");
        int nyse = is.register("NYSE");
        int nasdaq = is.register("NASDAQ");

        is.register("AAPL", "IBM", "MSFT");

        String [] symbols = {"AAPL", "IBM", "MSFT"};
        String [] venues = {"ARCA", "NYSE", "NASDAQ"};

        is.get(aapl);
        aapl = is.get("AAPL");
    }

    public void internal_string_as_value_object(){
        ValueObject valueObject = ValueObjectBuilder.newObject(EXPOSED_TYPE.MAP, 6000).create();
        int i=0;
        //register
        valueObject.string_value(++i, "AAPL");
        valueObject.string_value(++i, "MSFT");
        valueObject.string_value(++i, "IBM");

        //get str by id
        CharSequence aapl = valueObject.string_value(1);
        //get id by str
        valueObject.long_value("AAPL");

    }

    InternedStringPool is_sym;
    InternedStringPool is_ven;
    InternedStringPool is_fields;
    ValueObjectPool vopool;



    //more efficient memory managenent
    public void making_struct_with_builder(){

        //no need to specify init size, bcoz it's calculates automatically
        ValueObjectBuilder symbolBuilder = ValueObjectBuilder.newObject(EXPOSED_TYPE.TUPLE)
                .withInternedStringPool(is_fields, true)
                .withValueObjectPool(vopool)
                .with_id_property(is_fields.get("mid_price"), EXPOSED_TYPE.LONG)
                .with_id_property(is_fields.get("book"), EXPOSED_TYPE.ARRAY)
                .with_id_property(is_fields.get("venues"), EXPOSED_TYPE.MAP);

        ValueObjectBuilder book_builder = ValueObjectBuilder.newObject(EXPOSED_TYPE.ARRAY, 20)
                .withInternedStringPool(is_sym, true)
                .withValueObjectPool(vopool);

        ValueObjectBuilder venueBuilder = ValueObjectBuilder.newObject(EXPOSED_TYPE.TUPLE)
                .withInternedStringPool(is_fields, true)
                .withValueObjectPool(vopool)
                .with_id_property(is_fields.get("position"), EXPOSED_TYPE.LONG);



        int init_map_size = 6000;
        ValueObject universe = ValueObjectBuilder.newObject(EXPOSED_TYPE.MAP, init_map_size)
                .withInternedStringPool(is_sym, false)
                .create();


        InternedStringPool.InternedStringIter symIter = is_sym.iter();
        InternedStringPool.InternedStringIter venIter = is_ven.iter();

        while (symIter.hasNext()){
            ValueObject symStruct = symbolBuilder.create();
            symStruct.set(is_fields.get("book"), book_builder.create());

            venIter.reset();

            while (venIter.hasNext()){
                symStruct.set(venIter.strid(), venueBuilder.create());
            }

            universe.set(symIter.strid(), symStruct);
        }


        working_with_tree_struct(universe);
    }

    public void working_with_tree_struct(ValueObject obj){
        ValueObject aapl = obj.get("AAPL");
        aapl.double_value("mid_price", 560.00);

        ValueObject aapl_book = aapl.get("book");
        for (int i = 0; i < aapl_book.size(); i++) {
            System.out.println(aapl_book.double_value("price"));
            System.out.println(aapl_book.long_value("size"));
        }

        ValueObject venue_keys = aapl.get("venues").keys();

        for (int i = 0; i < venue_keys.size(); i++) {
            CharSequence seq = venue_keys.string_value(i);

            long position = aapl.get("venues").get(seq).long_value("position");
            System.out.println(position);
        }

    }
}
