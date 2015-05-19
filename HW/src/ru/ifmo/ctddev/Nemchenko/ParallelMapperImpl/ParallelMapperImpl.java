package ru.ifmo.ctddev.Nemchenko.ParallelMapperImpl;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

/**
 * Created by eugene on 2015/04/14.
 */
public class ParallelMapperImpl implements ParallelMapper {
    private final List<Thread> workingThreads;
    private final List<RunTask> runTasks;
    private final int cntThreads;
    private final MyBlockingQueue tasks;
    private boolean closed = false;

    /**
     * Create implementation of {@link ParallelMapper}.
     *
     * @param threads count of {@link java.lang.Thread} which will be created and run for work {@link #map(Function, List)}
     */
    public ParallelMapperImpl(int threads) {
        this.cntThreads = threads;
        workingThreads = new ArrayList<>();
        runTasks = new ArrayList<>();
        tasks = new MyBlockingQueue();
        for (int i = 0; i < threads; ++i) {
            runTasks.add(new RunTask(tasks));
            workingThreads.add(new Thread(runTasks.get(i)));
            workingThreads.get(i).start();
        }
    }

    /**
     * Generates list which contains elements received after
     * apply the {@code function} to each element from {@code list}.
     * <p>
     * Use threads {@link java.lang.Thread}, which {@link #ParallelMapperImpl(int)} created and run.
     * Finished immediately when {@link #close()} invok.
     * </p>
     *
     * @param function will be applied to each element from {@code list}
     * @param list     elements which will be passed to {@code function} for generate result list
     * @param <T>      type of elements in {@code list}
     * @param <R>      type of elements in returned list
     * @return list which contains elements received after apply the {@code function} to each element from {@code list}.
     * @throws InterruptedException when {@link #map(Function, List)} was invoked after {@link #close()}
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> function, List<? extends T> list) throws InterruptedException {
        if (closed) {
            throw new InterruptedException("ParallelMapper had already been closed");
        }
        int locCntThreads = Math.min(cntThreads, list.size());
        final RunFunction[] runFunctions = new RunFunction[locCntThreads];
        List<Object> retList = new ArrayList<>(list);
        for (int i = 0; i < locCntThreads; ++i) {
            runFunctions[i] = new RunFunction(function, getSubList(i, retList));
            tasks.addNewTask(runFunctions[i]);
        }

        for (RunFunction runFunction : runFunctions) {
            synchronized (runFunction) {
                while (!runFunction.isFinished()) {
                    if (closed) {
                        break;
                    }
                    runFunction.wait();
                }
            }
        }

        @SuppressWarnings("unchecked")
        List<R> result = (List<R>) retList;

        return result;
    }

    /**
     * Close all working threads created by {@link #ParallelMapperImpl(int)}
     *
     * @throws InterruptedException when {@link #map(Function, List)} has not finished work
     */
    @Override
    public void close() throws InterruptedException {
        closed = true;

        for (Thread thread : workingThreads) {
            thread.interrupt();
        }

        for (RunTask task : runTasks) {
            synchronized (task) {
                if (task.isInterrupted()) {
                    throw new InterruptedException("function map has not finished properly");
                }
            }
        }
    }

    private List<Object> getSubList(int i, List<Object> list) {
        int locCntThreads = Math.min(cntThreads, list.size());
        int lenInterval = list.size() / locCntThreads;
        int l = lenInterval * i;
        int r = lenInterval * (i + 1);
        if (locCntThreads - 1 == i) {
            r = list.size();
        }
        return list.subList(l, r);
    }
}
