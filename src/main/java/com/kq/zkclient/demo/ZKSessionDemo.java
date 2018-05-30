package com.kq.zkclient.demo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import com.kq.Config;

public class ZKSessionDemo extends Config implements Watcher {
	
	private static final CountDownLatch cdl = new CountDownLatch(1);

	public static void main(String[] args) throws IOException, InterruptedException {
		ZooKeeper zk = new ZooKeeper(ZK_SERVERS, 5000, new ZKSessionDemo());
		cdl.await();
		long sessionId = zk.getSessionId();
		byte[] passwd = zk.getSessionPasswd();
		
		System.out.println("sessionId="+sessionId);
		System.out.println("passwd="+new String(passwd));

		zk = new ZooKeeper(ZK_SERVERS, 5000, new ZKSessionDemo(), 1l, "test".getBytes());
		zk = new ZooKeeper(ZK_SERVERS, 5000, new ZKSessionDemo(), sessionId, passwd);
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			cdl.countDown();
		}
	}

}
