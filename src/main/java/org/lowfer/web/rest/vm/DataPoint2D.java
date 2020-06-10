package org.lowfer.web.rest.vm;

public final class DataPoint2D<X> {

    private final X x;
    private final long y;

    public DataPoint2D(X x, long y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }
}
