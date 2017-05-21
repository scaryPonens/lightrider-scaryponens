package com.scaryponens.monads;

/**
 * Created by Reuben on 5/20/2017.
 */
public class Tuple1<T1> {
    public final T1 _1;


    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    public static <T1> Tuple1<T1> of(T1 t1) {
        return new Tuple1(t1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple1)) return false;

        Tuple1<?> tuple1 = (Tuple1<?>) o;

        return _1 != null ? _1.equals(tuple1._1) : tuple1._1 == null;
    }

    @Override
    public int hashCode() {
        return _1 != null ? _1.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tuple1{");
        sb.append("_1=").append(_1);
        sb.append('}');
        return sb.toString();
    }
}
