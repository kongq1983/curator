package com.kq.curator.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.utils.ZKPaths;

import com.kq.Config;


/**
 * 	TreeCache
	特点：
	（1）永久监听指定节点下的节点的变化 
	（2）可以监听到指定节点下所有节点的变化，比如说指定节点”/example”, 在下面添加”node1”可以监听到，但是添加”node1/n1”也能被监听到 
	（3）可以监听到的事件：节点创建、节点数据的变化、节点删除等
	
	使用方式：
	（1）创建curatorframework的client 
	（2）添加TreeCache 
	（3）启动client 和 TreeCache 
	（4）注册监听器
	
 * 	@author kongqi
 *
 */
public class TreeCacheDemo extends Config {

	public static void main(String[] args) throws Exception {
		// 节点监听
		CuratorFramework client = getClient();

//		final NodeCache cache = new NodeCache(client, CACHE_NODE);
		TreeCache cache = new TreeCache(client, CACHE_NODE);
		cache.start();
		
		addListener(cache);

		Thread.sleep(Integer.MAX_VALUE);
	}

	private static void addListener(final TreeCache cache) {
		TreeCacheListener listener = new TreeCacheListener() {

			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				switch (event.getType()) {
				case NODE_ADDED: {
					System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(event.getData().getPath())
							+ ", value: " + new String(event.getData().getData()));
					break;
				}
				case NODE_UPDATED: {
					System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath())
							+ ", value: " + new String(event.getData().getData()));
					break;
				}
				case NODE_REMOVED: {
					System.out.println("TreeNode removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				}
				default:
					System.out.println("Other event: " + event.getType().name());
				}
			}

		};

		cache.getListenable().addListener(listener);
	}

	
	protected static void printHelp() {
        System.out.println("An example of using PathChildrenCache. This example is driven by entering commands at the prompt:\n");
        System.out.println("set <name> <value>: Adds or updates a node with the given name");
        System.out.println("remove <name>: Deletes the node with the given name");
        System.out.println("list: List the nodes/values in the cache");
        System.out.println("quit: Quit the example");
        System.out.println();
    }

}
