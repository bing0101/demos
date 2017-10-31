package com.bing.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class ZkClientTest {
    private ZkClient zkClient;

    @Before
    public void init() {
        String serverstring = "101.132.104.145:2181,101.132.104.145:2182,101.132.104.145:2183";
        zkClient = new ZkClient(serverstring);
        System.out.println("zk session established!");
    }

    @Test
    public void create() {
//        zkClient.createPersistent("/testZkClient/test1", "testZkClient");
        zkClient.createPersistent("/testParent/test1/test2", true);
    }

    @Test
    public void del() {
//        boolean result = zkClient.delete("/testParent/test1/test2");
        boolean result = zkClient.deleteRecursive("/testParent");
        System.out.println(result);
    }

    @Test
    public void getChildren() {
        List<String> children = zkClient.getChildren("/testParent");
        System.out.println(children);
    }

    @Test
    public void readData() {
        String data = zkClient.readData("/test");
        System.out.println(data);
    }

    @Test
    public void writeData() {
        zkClient.writeData("/test", "test");
    }

    @Test
    public void exists() {
        boolean exists = zkClient.exists("/test1");
        System.out.println(exists);
    }
}
