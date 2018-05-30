package com.kq.zkclient.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.kq.Config;

class ChildrenCallback implements AsyncCallback.Children2Callback {
	@Override
	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		System.out.println(
				"Child: " + rc + ", path: " + path + ", ctx: " + ctx + ", children: " + children + ", stat: " + stat);
	}
}

public class ZKChildrenAsyncDemo extends Config implements Watcher {
	private static final CountDownLatch cdl = new CountDownLatch(1);
	private static ZooKeeper zk = null;

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		zk = new ZooKeeper(ZK_SERVERS, 5000, new ZKChildrenAsyncDemo());
		cdl.await();

		if(!exists(zk, ROOT_NODE+"/zk-test")) {
			zk.create(ROOT_NODE+"/zk-test", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		zk.create(ROOT_NODE+"/zk-test/c1", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

		zk.getChildren(ROOT_NODE+"/zk-test", true, new ChildrenCallback(), "ok");

		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState())
			if (EventType.None == event.getType() && null == event.getPath()) {
				cdl.countDown();
			} else if (event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("Child: " + zk.getChildren(event.getPath(), true));
				} catch (Exception e) {
				}
			}
	}
}

