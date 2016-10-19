package impl.jilligal;

/**
 * Created by glebreutov on 13.10.16.
 */
public class DoubleHolder {

    private Double val;

    public DoubleHolder(double val) {
        this.val = val;
    }

    public double get(){
        return val;
    }

    public void set(double l){
        this.val = l;
    }

    public int hashCode() {
        return Double.hashCode(val);
    }
}
