package com.kq.curator.nodecache;

import com.kq.Config;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * A Node Cache is used to watch a ZNode. Whenever the data is modified or the ZNode is deleted, the Node Cache will change its state to contain the current data (or null if ZNode was deleted)
 * 监听节点
 * 看监听节点内容有没有变
 * @author kongqi
 *
 * example:
 * create /cache-node cc  (会通知  Node changed: /cache-node, value: cc)
 * delete /cache-node (会通知 cache.getCurrentData()是null)
 * create -e /cache-node/one ab （不会通知）
 * set /cache-node ac  （会通知  Node changed: /cache-node, value: ac）
 *
 * 也就是有值变更的时候，会通知 （新增和改变值的时候）
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
		NodeCacheListener listener = ()-> { //nodeChanged

			if (cache.getCurrentData() != null){ //启动时候，就会打印1次
				System.out.println("1. Node changed : " + cache.getCurrentData().getPath() + ", value: "
						+ new String(cache.getCurrentData().getData()));
			} else {
				// 删除
				System.out.println("2. Node changed: CurrentData=" + cache.getCurrentData());
			}

		};
		cache.getListenable().addListener(listener);
	}

}
