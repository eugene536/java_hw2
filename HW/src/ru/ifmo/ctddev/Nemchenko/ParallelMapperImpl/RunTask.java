package ru.ifmo.ctddev.Nemchenko.ParallelMapperImpl;

/**
 * Created by eugene on 2015/04/16.
 */
class RunTask implements Runnable {
    private MyBlockingQueue tasks;
    private boolean interrupted;

    public RunTask(MyBlockingQueue tasks) {
        this.tasks = tasks;
    }

    @Override
    public synchronized void run() {
        while (!interrupted) {
            RunFunction task = tasks.getTask();
            if (task == null) {
                return;
            }
            task.run();
            if (task.isInterrupted()) {
                interrupted = true;
            }
        }
    }

    public synchronized boolean isInterrupted() {
        return interrupted;
    }
}
