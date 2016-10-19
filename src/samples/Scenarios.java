package samples;

import fundamental.EXPOSED_TYPE;
import fundamental.ValueObject;
import fundamental.ValueObjectBuilder;

/**
 * Created by glebreutov on 12.10.16.
 */
public class Scenarios {

    public static void main(String[] args) {

    }


    public void making_struct_on_a_go(){
        ValueObject universe = ValueObjectBuilder.newObject(EXPOSED_TYPE.MAP, 6000).create();

        String [] symbols = {"AAPL", "IBM", "MSFT"};
        String [] venues = {"ARCA", "NYSE", "NASDAQ"};

        for(String sym : symbols){
            ValueObject newSym = ValueObjectBuilder.newObject(EXPOSED_TYPE.TUPLE).create();
            newSym.add_property("book", EXPOSED_TYPE.ARRAY);
            ValueObject venues_map = newSym.add_property("venues", EXPOSED_TYPE.MAP);
            for(String ven: venues){
                ValueObject venue = venues_map.add_property(ven, EXPOSED_TYPE.TUPLE);
                venue.add_property("position", EXPOSED_TYPE.LONG);
            }

            universe.set(sym, newSym);
        }

        working_with_tree_struct(universe);
    }

    //more efficient memory managenent
    public void making_struct_with_builder(){

        //no need to specify init size, bcoz it's calculates automatically
        ValueObjectBuilder symbolBuilder = ValueObjectBuilder.newObject(EXPOSED_TYPE.TUPLE)
                .with_property("mid_price", EXPOSED_TYPE.LONG)
                .with_property("book", EXPOSED_TYPE.ARRAY)
                .with_property("venues", EXPOSED_TYPE.MAP);

        ValueObjectBuilder book_builder = ValueObjectBuilder.newObject(EXPOSED_TYPE.ARRAY, 20);

        ValueObjectBuilder venueBuilder = ValueObjectBuilder.newObject(EXPOSED_TYPE.TUPLE)
                .with_property("position", EXPOSED_TYPE.LONG);

        String [] symbols = {"AAPL", "IBM", "MSFT"};
        String [] venues = {"ARCA", "NYSE", "NASDAQ"};

        int init_map_size = 6000;
        ValueObject universe = ValueObjectBuilder.newObject(EXPOSED_TYPE.MAP, init_map_size).create();

        for (String symbol: symbols) {
            ValueObject symStruct = symbolBuilder.create();
            symStruct.set("book", book_builder.create());
            for (String ven: venues){
                symStruct.set(ven, venueBuilder.create());
            }
            universe.set(symbol, symStruct);
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
