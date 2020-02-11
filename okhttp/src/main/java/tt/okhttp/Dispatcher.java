package tt.okhttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

    private int maxRequests = 64;       // 最大请求数
    private int maxRequestsPerHost = 2; // 同一个host的最大请求数

    public Dispatcher() {

    }

    private final Deque<Call.Task> runningTasks = new ArrayDeque<>(); // 执行队列
    private final Deque<Call.Task> readyTasks = new ArrayDeque<>();   // 等待队列


    // 线程池
    // SynchronousQueue：此队列中不缓存任何一个任务
    static final Executor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    public void enqueue(Call.Task task) {
        if (runningTasks.size() < maxRequests && runningAsyncCallsForHost(task) < maxRequestsPerHost) {
            runningTasks.add(task);
            executor.execute(task); // 重点==================
        } else {
            readyTasks.add(task);
        }
    }

    // 同一host的 同时请求数
    private int runningAsyncCallsForHost(Call.Task task) {
        int result = 0;
        for (Call.Task task2 : runningTasks) {
            if (task2.host().equals(task.host())) {
                result++;
            }
        }
        return result;
    }

    // Call.AsyncCall执行完成
    public void finished(Call.Task task) {
        synchronized (this) {
            runningTasks.remove(task); // 重点==================请求结束，从执行队列移出
            promoteCalls();
        }
    }

    // 判断是否执行等待队列中的请求
    private void promoteCalls() {
        if (runningTasks.size() >= maxRequests || readyTasks.isEmpty()) {
            return;
        }

        for (Iterator<Call.Task> i = readyTasks.iterator(); i.hasNext(); ) {
            Call.Task task = i.next();
            //到达同一host的同时请求上限
            if (runningAsyncCallsForHost(task) < maxRequestsPerHost) {
                i.remove();
                runningTasks.add(task);
                executor.execute(task); // 重点==================
            }
            if (runningTasks.size() >= maxRequests) { // 到达同时请求上限
                return;
            }
        }
    }


}
