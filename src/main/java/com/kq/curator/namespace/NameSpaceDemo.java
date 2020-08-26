package com.kq.curator.namespace;

import com.kq.Config;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2020-08-26 16:29
 * @since 2020-0630
 */
public class NameSpaceDemo {


    public static void main(String[] args) throws Exception{
        String namespace = "dev";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(Config.ZK_SERVERS)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(3000)
                .namespace(namespace)  //这里  会建第一层/dev
                .build();
        client.start();

        String node = "/zktest2";

        String data1 = "hello";
        print("create", "/"+namespace+node, data1);
        client.create().creatingParentsIfNeeded().forPath(node, data1.getBytes());

        TimeUnit.SECONDS.sleep(120);

    }

    private static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds) {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }

}
