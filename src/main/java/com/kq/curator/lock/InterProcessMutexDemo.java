package com.kq.curator.lock;

import com.kq.Config;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * 锁例子
 * Shared Reentrant Lock
 * Fully distributed locks that are globally synchronous, meaning at any snapshot in time no two clients think they hold the same lock.
 * @author kq
 * @date 2020-08-26 18:07
 * @since 2020-0630
 * example:
 * ls /dev/lock/mutex
 */
public class InterProcessMutexDemo {

    public static void main(String[] args) throws Exception{
        String path = "/lock/mutex";

        CuratorFramework client = Config.getNameSpaceClient(Config.NAMESPACE);

        Runnable runnable = () ->{

            InterProcessMutex mutex = new InterProcessMutex(client,path);
            int time = 5;
            int times = 0;

            try {

                boolean accruire = false;

                while (!(accruire=mutex.acquire(time, TimeUnit.SECONDS)) && times<5) {
                    times++;
                }

                if(accruire){
                    System.out.println(Thread.currentThread().getName()+",成功获取锁!");
                    TimeUnit.SECONDS.sleep(10);
                    mutex.release();
                    System.out.println(Thread.currentThread().getName()+",成功释放锁!");
                }else {
                    System.out.println(Thread.currentThread().getName()+",未获取锁!");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        };

        Thread[] threads = new Thread[10];

        for(int i=0;i<threads.length;i++) {
            threads[i] = new Thread(runnable,"thread-"+i);

            threads[i].start();

        }


        TimeUnit.SECONDS.sleep(600);


    }

}
