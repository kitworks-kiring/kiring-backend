package io.dodn.springboot.matzip.domain.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class LikeTaskQueue {
    private final BlockingQueue<LikeTask> queue = new LinkedBlockingQueue<>();
    public void addTask(LikeTask task) { queue.add(task); }
    public List<LikeTask> pollTask() {
        List<LikeTask> drainedTasks = new ArrayList<>();
        queue.drainTo(drainedTasks);
        return drainedTasks;
    }


    public int size() { return queue.size(); }

}
