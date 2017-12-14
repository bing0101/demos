package com.bing.design.patterns.command;

/**
 * Created by bing on 2017/12/14.
 */
public class BuildHouseCommand implements Command {
    private Worker worker;

    BuildHouseCommand(Worker worker) {
        this.worker = worker;
    }

    public void execute() {
        worker.build();
    }
}
