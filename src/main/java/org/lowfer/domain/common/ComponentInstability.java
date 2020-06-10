package org.lowfer.domain.common;

public final class ComponentInstability {

    private final SoftwareComponent component;
    private final long fanIn;
    private final long fanOut;

    public ComponentInstability(SoftwareComponent component, long fanIn, long fanOut) {
        this.component = component;
        this.fanIn = fanIn;
        this.fanOut = fanOut;
    }

    public SoftwareComponent getComponent() {
        return component;
    }

    public long getFanIn() {
        return fanIn;
    }

    public long getFanOut() {
        return fanOut;
    }

    public long getAbsoluteInstability() {
        return fanOut - fanIn;
    }

    public double getDoubleValue() {
        return (double) fanOut / (double) (fanIn + fanOut);
    }

    public double getDoubleValue(int places) {
        return round(getDoubleValue(), places);
    }

    public String getFormattedValue(int places) {
        return Double.toString(getDoubleValue(places));
    }

    public boolean greaterThan(ComponentInstability other) {
        return this.getDoubleValue() > other.getDoubleValue();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public String toString() {
        return "I=" + getFormattedValue(2);
    }
}
