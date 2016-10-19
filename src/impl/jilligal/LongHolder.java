package impl.jilligal;

/**
 * Created by glebreutov on 13.10.16.
 */
public class LongHolder {

    public LongHolder(long val) {
        this.val = val;
    }

    private long val;

    public long get(){
        return val;
    }

    public void set(long l){
        this.val = l;
    }

    public int hashCode() {
        return Long.hashCode(val);
    }
}
