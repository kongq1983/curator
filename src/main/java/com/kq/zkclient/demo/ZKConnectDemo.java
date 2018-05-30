package com.kq.zkclient.demo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import com.kq.Config;

public class ZKConnectDemo extends Config implements Watcher{
	
	private static final CountDownLatch cdl = new CountDownLatch(1);

	public static void main(String[] args) throws IOException {
		ZooKeeper zk = new ZooKeeper(ZK_SERVERS, 5000, new ZKConnectDemo());
		System.out.println(zk.getState());

		try {
			cdl.await();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ZK Session established.");
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			cdl.countDown();
		}
	}

}
