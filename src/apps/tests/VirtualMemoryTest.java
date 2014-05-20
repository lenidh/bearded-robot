package apps.tests;

import scheduling.Task;

/**
 * Testanwendung, welche absichtliche einen PageFault erzeugt.
 */
public class VirtualMemoryTest extends Task {

	@Override
	protected void onSchedule() {
		// Schreibtest
		//MAGIC.wMem32(0, 0);

		// Lesetest
		Task task = null;
		int size = task._r_scalarSize;
	}
}
