package com.kq.curator.latch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.kq.Config;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kq
 * @date 2020-08-26 15:37
 * @since 2020-0630
 */
public class LeaderLatchDemo {

    public static final String QUORUM_PATH = "/latch";

    public static final AtomicInteger ato = new AtomicInteger(0);

    public static void main(String[] args) throws Exception{

        Runnable runnable = ()-> {
// 1.Connect to zk
//            CuratorFramework client = CuratorFrameworkFactory.newClient(Config.ZK_SERVERS, new RetryNTimes(10, 5000));
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(Config.ZK_SERVERS)
                    .retryPolicy(retryPolicy)
                    .sessionTimeoutMs(60000)
                    .connectionTimeoutMs(3000)
                    .namespace("dev")
                    .build();

            client.start();

//            client.usingNamespace("dev");

            LeaderLatch leaderLatch = new LeaderLatch(client, QUORUM_PATH, Thread.currentThread().getName());
            leaderLatch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    System.out.println(Thread.currentThread()+",成为Master");

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread()+",释放锁");

                    try {
                        leaderLatch.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void notLeader() {
                    System.out.println(Thread.currentThread()+",Currently run as slave");
                }
            });

            try {
                leaderLatch.start();
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
