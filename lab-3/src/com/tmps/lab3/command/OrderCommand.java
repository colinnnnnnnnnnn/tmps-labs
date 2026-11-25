package com.tmps.lab3.command;

public interface OrderCommand {
    void execute();
    void undo();
    String getDescription();
}
