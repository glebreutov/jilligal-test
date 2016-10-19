package impl.jilligal;

import fundamental.ValueObject;
import tr.com.serkanozal.jillegal.config.annotation.JillegalAware;

/**
 * Created by glebreutov on 14.10.16.
 */

@JillegalAware
public class HashVONode {
    private ValueObject obj;

    public void setStr(CharSequence str) {
        this.str = str;
    }

    private CharSequence str;

    private HashVONode next;
    private long lv;
    private double dv;

    public boolean keyEquals(CharSequence name) {
        return str.equals(name);
    }

    public ValueObject getObj() {
        return obj;
    }

    public void setObj(ValueObject obj) {
        this.obj = obj;
    }

    public CharSequence getStr() {
        return str;
    }

    public HashVONode getNext() {
        return next;
    }

    public void setNext(HashVONode next) {
        this.next = next;
    }

    public long getLv() {
        return lv;
    }

    public void setLv(long lv) {
        this.lv = lv;
    }

    public double getDv() {
        return dv;
    }

    public void setDv(double dv) {
        this.dv = dv;
    }
}
