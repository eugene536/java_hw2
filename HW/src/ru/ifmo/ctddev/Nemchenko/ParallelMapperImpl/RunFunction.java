package ru.ifmo.ctddev.Nemchenko.ParallelMapperImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by eugene on 2015/04/14.
 */
class RunFunction implements Runnable {
    private Function<Object, Object> fun;
    private List<Object> list;
    private Boolean finished = false;
    private Boolean interrupted = false;

    public <T> RunFunction(Function<? super T, ?> function, List<Object> list) {
        this.list = list;
        this.fun = (Object o) -> {
            @SuppressWarnings("unchecked")
            Object result = function.apply((T) o);
            return result;
        };
    }

    @Override
    public synchronized  void run() {
        for (int i = 0; i < list.size(); i++) {
            if (Thread.interrupted()) {
                interrupted = true;
                return;
            }
            list.set(i, fun.apply(list.get(i)));
        }
        finished = true;
        notify();
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized boolean isInterrupted() {
        return interrupted;
    }

}
