package com.kq.curator.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听节点
 * 看监听节点内容有没有变
 * @author kongqi
 */
public class NodeCacheDemo extends Config {
	
	public static void main(String[] args) throws Exception {
		// 节点监听
		RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_SERVERS, 5000, 5000, retry);
		client.start();

		final NodeCache cache = new NodeCache(client, CACHE_NODE);
		cache.start();

		addListener(cache);
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	protected static void addListener(final NodeCache cache) {
		// a PathChildrenCacheListener is optional. Here, it's used just to log
		// changes
		NodeCacheListener listener = new NodeCacheListener() {

			@Override
			public void nodeChanged() throws Exception {
				if (cache.getCurrentData() != null)
					System.out.println("Node changed: " + cache.getCurrentData().getPath() + ", value: "
							+ new String(cache.getCurrentData().getData()));
			}
		};
		cache.getListenable().addListener(listener);
	}

}
