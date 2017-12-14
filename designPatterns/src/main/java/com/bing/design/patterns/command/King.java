package com.bing.design.patterns.command;

/**
 * Created by bing on 2017/12/14.
 * 场景：国王 让 大臣  建房子
 * 首先得有工人才能建房子
 * 大臣自己不会自己建房子，他会找工人去建房子
 * 国王下达建房子命令给大臣，大臣找来工人建房子
 */
public class King {
    //1. 在国王的权利范围内，让大臣建房子
    public static void main(String[] args) {
        //2. 首先得有一个工人
        Worker worker = new Worker();

        //3. 创建一个建房子的命令，这个命令需要有实际执行者
        Command buildHouseCommand = new BuildHouseCommand(worker);

        //4. 建房子的大臣，国王把命令给大臣
        Minister minister = new Minister(buildHouseCommand);

        //5. 大臣执行命令，其实是由公司实际操作
        minister.build();
    }
}
