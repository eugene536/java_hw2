package ru.ifmo.ctddev.Nemchenko.ParallelMapperImpl;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by eugene on 2015/04/16.
 */
class MyBlockingQueue {
    private final Queue<RunFunction> tasks;

    public MyBlockingQueue() {
        tasks = new LinkedList<>();
    }

    public synchronized void addNewTask(RunFunction task) {
        tasks.add(task);
        notifyAll();
    }

    public synchronized RunFunction getTask() {
        while (tasks.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        return tasks.remove();
    }
}
