package com.kq.curator.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author kq
 * @date 2020-08-26 17:13
 * @since 2020-0630
 */
public class ZKUtil {

    /**
     * 判断节点是否存在
     * @param zk
     * @param path
     * @throws InterruptedException
     * @throws KeeperException
     */
    public static boolean isExists(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, null);
        if(stat!=null) {
            return true;
        }

        return false;
    }

    /**
     * 判断节点是否存在
     * @param client
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean isExists(CuratorFramework client,String path) throws Exception{
        Stat stat =  client.checkExists().forPath(path);

        if(stat!=null) {
            return true;
        }

        return false;
    }

    public static void deletePath(CuratorFramework client,String path) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }



}
