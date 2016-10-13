package com.xiaoli.library.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池操作工具类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class ThreadPoolUtils {

    private static ThreadPoolExecutor threadPoolExecutor;//线程池

    /**
     * 初始化线程池
     */
    public static void init(){
        if(threadPoolExecutor!=null)return;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        threadPoolExecutor = new ThreadPoolExecutor(1,3,5, TimeUnit.SECONDS,queue,new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 初始化线程池
     * param corePoolSize  线程池中所保存的核心线程数
     * param maximumPoolSize 线程池允许创建的最大线程数.当workQueue使用无界队列时（如：LinkedBlockingQueue），则此参数无效。
     * param keepAliveTime 当前线程池线程总数大于核心线程数时，终止多余的空闲线程的时间。
     * param unit  keepAliveTime参数的时间单位。
     * param workQueue 工作队列
     * param handler 拒绝策略
     */
    public static void init(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            RejectedExecutionHandler handler) {
        if(threadPoolExecutor==null)return;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,handler);
    }

    /**
     * 向线程池里面添加任务
     * param runnable
     */
    public static void add(Runnable runnable){
        if(threadPoolExecutor==null)return;
        threadPoolExecutor.execute(runnable);
    }

    /**
     * 关闭线程池
     */
    public  static void shutdown(){
        if(threadPoolExecutor==null)return;
        threadPoolExecutor.shutdown();
    }


    /**
     * 我自己的线程池管理
     */
    private static Map<String,List<Thread>>  myThreadPool = new HashMap<String,List<Thread>>();

    /**
     * 添加一个线程到线程池中
     * param activity
     * param thread
     */
    public static void addMyThread(Activity activity,Thread thread){
        String simpleName = activity.getClass().getSimpleName();
        List<Thread> list = new ArrayList<>();
        if(myThreadPool.containsKey(simpleName)){
            list = myThreadPool.get(simpleName);
        }
        list.add(thread);
        myThreadPool.put(simpleName,list);
    }

    /**
     * 销毁线程
     * param activity
     */
    public static void destoryMyThread(Activity activity){
        String simpleName = activity.getClass().getSimpleName();
        List<Thread> list = new ArrayList<>();
        List<Thread> del = new ArrayList<>();
        if(myThreadPool.containsKey(simpleName)){
            list = myThreadPool.get(simpleName);
        }
        if(list==null)list = new ArrayList<>();
        for (Thread thread:list) {
            if(thread.isAlive()){
                thread.interrupt();
            }else{
                del.add(thread);
            }
        }
        list.removeAll(del);
        myThreadPool.put(simpleName,list);
    }
}
