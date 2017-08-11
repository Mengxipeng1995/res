package com.cmp.res.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.TaskExecutor;

public class AsyncEvent extends SimpleApplicationEventMulticaster {
	private TaskExecutor taskExecutor = new TaskExecutor() {
		ExecutorService exeserv = Executors.newCachedThreadPool();
		public void execute(Runnable task) {
			exeserv.execute(task);
		}
	};

	protected TaskExecutor getTaskExecutor() {
		return this.taskExecutor;
	}
}
