package com.kq.curator.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import com.kq.Config;

/**
 * 监听子节点
 * 监听自己点的新增、修改、删除
 * @author kongqi
 *
 * example:
 * create /mychild   (这个不会监听到)
 * create -e /mychild/one 1
 * create -e /mychild/two 2
 * create -e /mychild/three 3
 *
 *
 */
public class ChildNodeListenerDemo extends Config {

	public static void main(String[] args) throws Exception {

		// 1.Connect to zk
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_SERVERS, new RetryNTimes(10, 5000));
		client.start();
//		CuratorFramework client = getClient();
		
		
		final PathChildrenCache cache = new PathChildrenCache(client, CHILD_CACHE_NODE, true);
		cache.start();

		cache.getListenable().addListener(new PathChildrenCacheListener() {

			@Override
			public void childEvent(CuratorFramework curator, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED:
					System.out.println("ChildNodeListenerDemo add:" + event.getData());
					break;
				case CHILD_UPDATED:
					System.out.println("ChildNodeListenerDemo update:" + event.getData());
					break;
				case CHILD_REMOVED:
					System.out.println("ChildNodeListenerDemo remove:" + event.getData());
					break;
				default:
					System.out.println("ChildNodeListenerDemo default:" + event.getData());
					break;
				}
			}
		});
		
//		Thread.sleep(2000);
//		
//		String path = CACHE_NODE+"/c1";
//        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
//        Thread.sleep(1000); // 此处需留意，如果没有现成睡眠则无法触发监听事件
//        client.delete().forPath(path);
		
		System.out.println("-------------------------------------");

		Thread.sleep(Integer.MAX_VALUE);
		
//		PathChildrenCache不会对二级子节点进行监听，只会对子节点进行监听。
//		看上面的实例会发现在创建子节点和删除子节点两个操作中间使用了线程睡眠，
//		否则无法接收到监听事件，这也是在使用过程中需要留意的一点。
	}

}
