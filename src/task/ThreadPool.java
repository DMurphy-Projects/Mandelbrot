package task;

import java.util.LinkedList;

public class ThreadPool {
    private final int mThreadCount = Runtime.getRuntime().availableProcessors();
    private final WorkerThread[] mThreads;
    private final LinkedList<Runnable> queue;

    private int mItemsToProcess = 0;

    private static ThreadPool mInstance;

    private ThreadPool() {
	queue = new LinkedList<Runnable>();
	mThreads = new WorkerThread[mThreadCount];

	for (int i = 0; i < mThreadCount; i++) {
	    mThreads[i] = new WorkerThread();
	    mThreads[i].start();
	}
    }

    public static ThreadPool getInstance() {
	if (mInstance == null) {
	    mInstance = new ThreadPool();
	}
	return mInstance;
    }

    public void execute(Runnable r) {
	synchronized (queue) {
	    increment();
	    queue.addLast(r);
	    queue.notify();
	}
    }

    private synchronized void increment() {
	mItemsToProcess++;
    }

    private synchronized void decrement() {
	mItemsToProcess--;
    }

    public void waitUntilDone() {
	while (mItemsToProcess != 0) {
	    try {
		Thread.sleep(1);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    private class WorkerThread extends Thread {
	public void run() {
	    Runnable r;

	    while (true) {
		synchronized (queue) {
		    while (queue.isEmpty()) {
			try {
			    queue.wait();
			} catch (InterruptedException ignored) {
			}
		    }

		    r = queue.removeFirst();
		}
		try {
		    r.run();
		} catch (RuntimeException e) {
		    e.printStackTrace();
		} finally {
		    decrement();
		}
	    }
	}
    }
}
