package scheduling;

import container.RingBuffer;

public class Scheduler {

	private RingBuffer tasks = new RingBuffer();

	public void run() {
		while (true) {
			if(tasks.size() > 0) {
				Task task = (Task)tasks.front();
				if(task.stopped) {
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
		task.sticky = sticky;
		tasks.push(task);
		task.start();
	}
}
