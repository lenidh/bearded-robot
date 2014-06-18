package scheduling;

import container.RingBuffer;

public class Scheduler {

	private RingBuffer tasks = new RingBuffer();

	private Task currentTask = null;

	private static int cpEbp = 0;

	private static int cpEsp = 0;

	private static int cpEdi = 0;

	private static int cpEsi = 0;

	public void start() {
		MAGIC.inline(0x89, 0x2D); MAGIC.inlineOffset(4, cpEbp); //mov [addr(v1)],ebp
		MAGIC.inline(0x89, 0x25); MAGIC.inlineOffset(4, cpEsp); //mov [addr(v1)],esp
		MAGIC.inline(0x89, 0x35); MAGIC.inlineOffset(4, cpEsi); //mov [addr(v1)],esi
		MAGIC.inline(0x89, 0x3D); MAGIC.inlineOffset(4, cpEdi); //mov [addr(v1)],edi

		run();
	}

	private void run() {
		while (true) {
			if(tasks.size() > 0) {
				this.currentTask = (Task)tasks.front();
				if(currentTask.stopped) {
					tasks.pop();
				} else {
					currentTask.schedule();
					tasks.next();
				}
				this.currentTask = null;
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

	public void reset(int isrEbp) {
		MAGIC.wMem32(isrEbp, this.cpEbp);
		MAGIC.wMem32(isrEbp + 4, this.cpEdi);
		MAGIC.wMem32(isrEbp + 8, this.cpEsi);
		MAGIC.wMem32(isrEbp + 12, this.cpEbp);
		MAGIC.wMem32(isrEbp + 16, this.cpEsp);

		int newEip = MAGIC.rMem32(MAGIC.cast2Ref(MAGIC.clssDesc("Scheduler"))
				+ MAGIC.mthdOff("Scheduler", "run")) + MAGIC.getCodeOff();
		MAGIC.wMem32(isrEbp + 36, newEip);
	}

	public void stopCurrent() {
		if(this.currentTask != null) {
			this.currentTask.stop();
		}
	}
}
