package fundamental;

/**
 * Created by glebreutov on 13.10.16.
 */
public interface InternedStringPool {

    CharSequence get(int id);

    //int register(CharSequence seq);

    int register(CharSequence... seq);

    int get(CharSequence str);

    //epi sync methods here

    InternedStringIter iter();

    interface InternedStringIter {

        boolean hasNext();

        void next();

        CharSequence str();

        int strid();

        void reset();
    }

}
