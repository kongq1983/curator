package com.kq.curator.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.kq.Config;

/**
 * 删除节点测试
 * 
 * @author kongqi
 *
 */
public class DeleteNodeDemo extends Config {
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-client/c1";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(ZK_SERVERS).sessionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "test".getBytes());
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath(path);

		System.out.println("stat=" + stat);

		//删除叶子节点(包括子节点，不包括父节点)
		client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
	}
	
}
