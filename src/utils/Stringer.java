package utils;

public class Stringer {
    public final StringBuilder inner;

    public Stringer() {
        inner = new StringBuilder();
    }

    public String concat(Object... args) {
        for(Object arg : args) {
            inner.append(arg);
        }

        String val = inner.toString();
        inner.setLength(0);
        return val;
    }
}
