package com.kolosya.zavodsimulator.threadpool;

import com.kolosya.zavodsimulator.Shutdownable;
import com.kolosya.zavodsimulator.factory.Debug;

import java.util.Queue;
import java.util.concurrent.*;

public class ThreadPool implements Shutdownable {
    private final int threadsCount;
    private final PoolThread[] threads;
    private final Queue<Runnable> tasks;
    private boolean useTheSameTask = false;
    private Runnable defaultTask;
    private long sleepTime;

    public ThreadPool(int threadsCount, long sleepTime) {
        this.tasks = new ConcurrentLinkedQueue<>();
        this.threadsCount = threadsCount;
        this.threads = new PoolThread[this.threadsCount];
        for (int i = 0; i < threadsCount; ++i) {
            threads[i] = new PoolThread();
            threads[i].start();
        }
        this.sleepTime = sleepTime;
    }

    public int getThreadID(Thread thread) {
        for (int i = 0; i < threadsCount; ++i) {
            if (threads[i].equals(thread)) {
                return i;
            }
        }
        return -1;
    }

    public void waitForTasks() {
        synchronized (tasks) {
            try {
                tasks.wait();
            } catch (InterruptedException ignored) {
                //TODO: implement me
            }
        }
    }

    public void setUseTheSameTask(boolean useTheSameTask) {
        this.useTheSameTask = useTheSameTask;
    }

    public void setDefaultTask(Runnable defaultTask) {
        this.defaultTask = defaultTask;
    }

    public int tasksCount() {
        return tasks.size();
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public void execute(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
        }
        synchronized (threads) {
            threads.notify();
        }
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void shutdown() {
        for (int i = 0; i < threadsCount; ++i) {
            threads[i].shutdown();
        }
        synchronized (tasks) {
            tasks.clear();
        }
    }

    class PoolThread extends Thread {
        private boolean isRunning = true;

        public void shutdown() {
            isRunning = false;
            interrupt();
        }

        @Override
        public void run() {
            isRunning = true;

            while (isRunning) {
                if (tasks.isEmpty() && !useTheSameTask) {
                    synchronized (threads) {
                        try {
                            threads.wait();
                        } catch (InterruptedException ignored) {
                            //Debug.getInstance().log(String.format("Thread %d is free", getThreadID(this)));
                        }
                    }
                }

                Runnable task = null;
                synchronized (this) {
                    task = useTheSameTask ? defaultTask : tasks.poll();
                }
                if (task != null) {
                    if (!useTheSameTask) {
                        synchronized (tasks) {
                            tasks.notify();
                        }
                    }
                    task.run();
                }

                if (!isRunning) {
                    break;
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {
                    //Debug.getInstance().log(String.format("Thread %d is free", getThreadID(this)));
                }
            }
        }
    }
}
