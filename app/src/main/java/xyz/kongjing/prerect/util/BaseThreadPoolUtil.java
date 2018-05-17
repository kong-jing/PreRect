package xyz.kongjing.prerect.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 公共的线程池
 */
public class BaseThreadPoolUtil {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1; // 线程池的大小最好设置成为CUP核数+1
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1; // 设置线程池的最大线程数
    private static final int KEEP_ALIVE = 1; // 设置线程的存活时间

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(); // 超出线程池容量的线程都会放到这个队列中存放

    private static final Executor sExcutor
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, Executors.defaultThreadFactory());


    public static final void execute(Runnable runnable) {
        sExcutor.execute(runnable);
    }

}