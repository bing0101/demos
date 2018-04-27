package com.bing;

import com.bing.spi.TestInterface;

import java.util.ServiceLoader;

/**
 * Created by bing on 2018/4/16.
 */
public class Main {
    public static void main(String[] args) {
        ServiceLoader<TestInterface> loaders = ServiceLoader.load(TestInterface.class);
        for (TestInterface test : loaders) {
            test.test();
        }

    }
}
