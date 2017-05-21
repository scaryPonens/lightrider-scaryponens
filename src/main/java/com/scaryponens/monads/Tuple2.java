package com.scaryponens.monads;

/**
 * Created by Reuben on 5/20/2017.
 */
public class Tuple2<T1,T2> extends Tuple1<T1> {

    public final T2 _2;

    public Tuple2(T1 t1, T2 t2) {
        super(t1);
        this._2 = t2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple2)) return false;
        if (!super.equals(o)) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        return _2.equals(tuple2._2);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + _2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tuple2{");
        sb.append("_1=").append(super._1);
        sb.append("_2=").append(_2);
        sb.append('}');
        return sb.toString();
    }
}
