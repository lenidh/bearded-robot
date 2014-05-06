package scheduling;

public abstract class Task {

	boolean isStopped = false;

	boolean isSticky = false;

	void schedule() {
		isStopped = false;
		onSchedule();
	}

	public final void start() {
		isStopped = false;
		onStart();
	}

	public final void stop() {
		if(!this.isSticky) {
			onStop();
			isStopped = true;
		}
	}

	protected abstract void onSchedule();

	protected void onStart() {
	}

	protected void onStop() {
	}
}
