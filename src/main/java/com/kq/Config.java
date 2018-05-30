package com.kq;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Config {

	public static final String ZK_SERVERS = "192.168.6.233:2001";

	public static final String CACHE_NODE = "/king";
	public static final String ROOT_NODE = "/zktest";

	/**
	 * 默认启动了
	 * @return
	 */
	public static CuratorFramework getClient() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(ZK_SERVERS)
				.retryPolicy(retryPolicy).sessionTimeoutMs(6000).connectionTimeoutMs(3000).build();
		
//		.namespace("demo")
		client.start();
		return client;
	}

	/**
	 * 判断节点是否存在
	 * @param zk
	 * @param path
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	protected static boolean exists(ZooKeeper zk,String path) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, null);
		if(stat!=null) {
			return true;
		}
		
		return false;
	}
}
