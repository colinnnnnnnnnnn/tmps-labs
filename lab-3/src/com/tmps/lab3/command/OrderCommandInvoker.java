package com.tmps.lab3.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class OrderCommandInvoker {
    private final Deque<OrderCommand> undoHistory = new ArrayDeque<>();
    private final Deque<OrderCommand> redoHistory = new ArrayDeque<>();
    private final List<String> executionLog = new ArrayList<>();

    public void executeCommand(OrderCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        command.execute();
        undoHistory.push(command);
        redoHistory.clear();
        executionLog.add("EXECUTED: " + command.getDescription());
    }

    public boolean undo() {
        if (undoHistory.isEmpty()) {
            return false;
        }
        OrderCommand command = undoHistory.pop();
        command.undo();
        redoHistory.push(command);
        executionLog.add("UNDONE: " + command.getDescription());
        return true;
    }

    public boolean redo() {
        if (redoHistory.isEmpty()) {
            return false;
        }
        OrderCommand command = redoHistory.pop();
        command.execute();
        undoHistory.push(command);
        executionLog.add("REDONE: " + command.getDescription());
        return true;
    }

    public boolean canUndo() {
        return !undoHistory.isEmpty();
    }

    public boolean canRedo() {
        return !redoHistory.isEmpty();
    }

    public List<OrderCommand> getUndoHistory() {
        return Collections.unmodifiableList(new ArrayList<>(undoHistory));
    }

    public List<String> getExecutionLog() {
        return Collections.unmodifiableList(executionLog);
    }
}
