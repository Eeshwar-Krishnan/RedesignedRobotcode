package Utils;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble extends Number {

    private AtomicLong bits;

    public AtomicDouble() {
        this(0f);
    }

    public AtomicDouble(double initialValue) {
        bits = new AtomicLong(Double.doubleToLongBits(initialValue));
    }

    public final boolean compareAndSet(float expect, float update) {
        return bits.compareAndSet(Double.doubleToLongBits(expect),
                                  Double.doubleToLongBits(update));
    }

    public final void set(double newValue) {
        bits.set(Double.doubleToLongBits(newValue));
    }

    public final double get() {
        return Double.longBitsToDouble(bits.get());
    }

    public float floatValue() {
        return ((float)get());
    }

    public final double getAndSet(double newValue) {
        return Double.longBitsToDouble(bits.getAndSet(Double.doubleToLongBits(newValue)));
    }

    public final boolean weakCompareAndSet(double expect, double update) {
        return bits.weakCompareAndSet(Double.doubleToLongBits(expect),
                                      Double.doubleToLongBits(update));
    }

    public double doubleValue() { return (double) floatValue(); }
    public int intValue()       { return (int) get();           }
    public long longValue()     { return (long) get();          }

}