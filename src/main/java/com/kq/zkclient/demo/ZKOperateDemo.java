package com.kq.zkclient.demo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooKeeper;

import com.kq.Config;

public class ZKOperateDemo extends Config implements Watcher {

	private static final CountDownLatch cdl = new CountDownLatch(1);

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		ZooKeeper zk = new ZooKeeper(ZK_SERVERS, 5000, new ZKOperateDemo());
		cdl.await();
		
		Stat stat = zk.exists(ROOT_NODE, null);
		
		System.out.println("stat="+stat);
		
		if(stat==null) {
			String path = zk.create(ROOT_NODE, "root".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("Success create path: " + path);
		}

		String path1 = zk.create(ROOT_NODE+"/zk-test-", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Success create path: " + path1);
		String path2 = zk.create(ROOT_NODE+"/zk-test-", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create path: " + path2);
		
		Thread.sleep(10000);
		System.out.println("---------------------------------");
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			cdl.countDown();
		}
	}

}
