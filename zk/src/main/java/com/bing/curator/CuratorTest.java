package com.bing.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bing on 2017/10/27.
 */
public class CuratorTest {
    private CuratorFramework client;

    @Before
    public void init() {
//        String serverstring = "101.132.104.145:2181,101.132.104.145:2182,101.132.104.145:2183";
        String serverstring = "localhost:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        client = CuratorFrameworkFactory.newClient(serverstring, retryPolicy);
        client = CuratorFrameworkFactory.builder()
                .connectString(serverstring)
                .retryPolicy(retryPolicy)
                .namespace("curator")
                .build();
        client.start();
    }

    @Test
    public void create() throws Exception {
//        String ret = client.create().forPath("/test", "test".getBytes());
//        System.out.println(ret);
        client.create().creatingParentsIfNeeded().forPath("/test");
    }

    @Test
    public void delete() throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/test");
    }

    @Test
    public void getData() throws Exception {
        client.setData().forPath("/test", "test".getBytes());
        byte[] ret = client.getData().forPath("/test");
        System.out.println(new String(ret));
    }

    @Test
    public void asyncCreate() throws Exception {
        client.create().creatingParentsIfNeeded().inBackground((client, event) -> {
            System.out.println(event.getResultCode() + " " + event.getType());
        }).forPath("/testAsync");

    }

    @Test
    public void asyncThreadCreate() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        client.create().creatingParentsIfNeeded().inBackground((client, event) -> System.out.println(event.getResultCode() + " " + event.getType()), executorService).forPath("/testAsync");

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void nodeCache() throws Exception {
        final NodeCache nodeCache = new NodeCache(client, "/test11");
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> System.out.println("node changed, new data: " + new String(nodeCache.getCurrentData().getData())));

        client.create().forPath("/test11", "test".getBytes());
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void pathChildrenCache() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, "/test", false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("child added, " + event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("child updated, " + event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("child removed, " + event.getData().getPath());
                    break;
                default:
                    break;
            }
        });

        client.create().forPath("/test/testChild");
        Thread.sleep(1000);
        client.setData().forPath("/test/testChild", "testChild1".getBytes());
        Thread.sleep(1000);
        client.delete().forPath("/test/testChild");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void chooseMaster() throws InterruptedException {
        int count = 0;
        String masterPath = "/master";
        LeaderSelector selector = new LeaderSelector(client, masterPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("choosed master" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println("release master");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(60000);
        selector.close();
    }

    @Test
    public void chooseMaster2() throws Exception {
        String masterPath = "/master2";
        LeaderLatch leaderLatch = new LeaderLatch(client, masterPath);
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println("is leader");
            }

            @Override
            public void notLeader() {
                System.out.println("not leader");
            }
        });
        leaderLatch.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void interProcessMutex() throws InterruptedException {
        String lockPath = "/lock";
        final InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    lock.acquire();

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
                    System.out.println(sdf.format(new Date()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch.countDown();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void distributedAtomicInteger() throws Exception {
        String distributedAtomicIntegerPath = "/distributedAtomicInteger";
        DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(client, distributedAtomicIntegerPath, new RetryNTimes(3, 1000));
        AtomicValue<Integer> rc = distributedAtomicInteger.add(8);
        System.out.println(rc.succeeded());
    }

    @Test
    public void distributedBarrier() throws Exception {
        String distributedBarrierPath = "/barrier";
        DistributedBarrier controlBarrier = new DistributedBarrier(client, distributedBarrierPath);
        controlBarrier.setBarrier();
        System.out.println("set barrier " + System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    DistributedBarrier barrier = new DistributedBarrier(client, distributedBarrierPath);

                    System.out.println("wait on barrier " + System.currentTimeMillis());
                    barrier.waitOnBarrier();
                    System.out.println(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        controlBarrier.removeBarrier();
        Thread.sleep(1000);
    }

    @Test
    public void distributedDoubleBarrier() throws InterruptedException {
        String distributedDoubleBarrierPath = "/barrier/double";
        for (int i = 0; i < 10; i++) {
            final int j = i;
            new Thread(() -> {
                DistributedDoubleBarrier distributedDoubleBarrier = new DistributedDoubleBarrier(client, distributedDoubleBarrierPath, 10);
                try {
                    System.out.println(Thread.currentThread().getName() + " enter!");
                    distributedDoubleBarrier.enter();

                    System.out.println("do something...");

                    distributedDoubleBarrier.leave();
                    System.out.println(Thread.currentThread().getName() + " leave!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void zkPaths() throws Exception {
        String namespace = "/zkpaths/namespace";
        String parentPath = "/zkpaths/parent";
        ZooKeeper zookeeper = client.getZookeeperClient().getZooKeeper();

        System.out.println(ZKPaths.fixForNamespace(namespace, "/test"));
        System.out.println(ZKPaths.makePath(parentPath, "/child"));
        System.out.println(ZKPaths.getNodeFromPath("/test/test1"));
        ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode("/test/test1");
        System.out.println(pathAndNode.getPath());
        System.out.println(pathAndNode.getNode());

        ZKPaths.mkdirs(zookeeper, "/test/test1/test2");
        ZKPaths.mkdirs(zookeeper, "/test/test1/test3");
        System.out.println(ZKPaths.getSortedChildren(zookeeper, "/test/test1"));

        ZKPaths.deleteChildren(zookeeper, "/test", true);
    }

}
