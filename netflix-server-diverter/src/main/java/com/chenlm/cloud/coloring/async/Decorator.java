package com.chenlm.cloud.coloring.async;

public interface Decorator<T> {
    T decorate(T t);
}
