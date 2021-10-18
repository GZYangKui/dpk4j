package cn.navclub.xtm.core.function;

@FunctionalInterface
public interface FIConsumer<X,Y,Z> {

    void accept(X x, Y y, Z z);
}
