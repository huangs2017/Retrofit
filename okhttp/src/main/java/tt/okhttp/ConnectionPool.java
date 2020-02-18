package tt.okhttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import test.Log;

public class ConnectionPool {

    private final Deque<SocketConnect> socketConnectDeque = new ArrayDeque<>();
    private boolean cleanupRunning;
    long keepAliveDuration;

    public ConnectionPool() {
        keepAliveDuration = TimeUnit.MINUTES.toMillis(1); // 每个连接存活1分钟
    }

    public SocketConnect get(String host, int port) {
        Iterator<SocketConnect> iterator = socketConnectDeque.iterator();
        while (iterator.hasNext()) {
            SocketConnect socketConnect = iterator.next();
            // 连接是否可复用（相同的host、port）
            if (socketConnect.isSameAddress(host, port)) {
                iterator.remove(); // 从连接池 移出使用的SocketConnect
                return socketConnect;
            }
        }
        return null;
    }

    public void put(SocketConnect socketConnect) {
        //执行检测清理
        if (!cleanupRunning) {
            cleanupRunning = true;
            executor.execute(cleanupRunnable);
        }
        socketConnectDeque.add(socketConnect);
    }





    // 线程池，用来检测闲置socket并对其进行清理
    static ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "OkHttpClient ConnectionPool");
            thread.setDaemon(true); // Daemon守护进程
            return thread;
        }
    };

    static Executor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), threadFactory);



    Runnable cleanupRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                long waitTimes = cleanup(System.currentTimeMillis());
                if (waitTimes == -1) {
                    return;
                }
                if (waitTimes > 0) {
                    synchronized (ConnectionPool.this) {
                        try {
                            //调用某个对象的wait()方法能让当前线程阻塞，并且当前线程必须拥有此对象的锁
                            ConnectionPool.this.wait(waitTimes);
                        } catch (InterruptedException ignored) {

                        }
                    }
                }
            }
        }
    };


    /**
     * 检查需要移除的连接返回下次检查时间
     */
    private long cleanup(long now) {
        long longestIdleDuration = -1;
        synchronized (this) {
            for (Iterator<SocketConnect> i = socketConnectDeque.iterator(); i.hasNext(); ) {
                SocketConnect socketConnect = i.next();
                // 获得闲置时间 多长时间没使用这个了
                long idleDuration = now - socketConnect.lastUsetime;
                //如果闲置时间超过允许
                if (idleDuration > keepAliveDuration) {
                    socketConnect.close();       // 重点====================
                    i.remove();                  // 重点====================
                    Log.e("移出连接池");
                    continue;
                }
                // 获得最大闲置时间
                if (longestIdleDuration < idleDuration) {
                    longestIdleDuration = idleDuration;
                }
            }
            //下次检查时间
            if (longestIdleDuration >= 0) {
                return keepAliveDuration - longestIdleDuration;
            } else {
                // 连接池没有连接，可以退出
                cleanupRunning = false;
                return longestIdleDuration;
            }
        }
    }

}