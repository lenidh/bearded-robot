package scheduling;

import container.RingBuffer;

public class Scheduler {

	private RingBuffer tasks = new RingBuffer();

	public void start() {
		while (true) {
			if(tasks.size() > 0) {
				Task task = (Task)tasks.front();
				if(task.isStopped) {
					tasks.pop();
				} else {
					task.schedule();
					tasks.next();
				}
			}
		}
	}

	public void addTask(Task task) {
		addTask(task, false);
	}

	public void addTask(Task task, boolean sticky) {
		task.isSticky = sticky;
		tasks.push(task);
		task.start();
	}
}
