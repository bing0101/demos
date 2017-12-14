package com.bing.design.patterns.command.aCommand;

/**
 * Created by bing on 2017/12/14.
 */
public class Minister {
    private Command command;

    Minister(Command command) {
        this.command = command;
    }

    public void action() {
        command.execute();
    }
}
