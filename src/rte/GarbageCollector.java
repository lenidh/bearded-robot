package rte;

import interrupts.Interrupts;
import memory.Memory;
import scheduling.Task;

public class GarbageCollector extends Task {

	private int imageTop;

	private Object firstImageObject = null;

	public GarbageCollector() {
		this.imageTop = MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4);
		this.firstImageObject = MAGIC.cast2Obj(MAGIC.rMem32(MAGIC.imageBase + 16));
	}

	@Override
	protected void onSchedule() {
		if(DynamicRuntime.firstDynamicObject != null) {
			Interrupts.disableIRQs();
			// Alle dynamisch erstellten Objekte markieren.
			Object obj = DynamicRuntime.firstDynamicObject;
			while (obj != null) {
				MAGIC.assign(obj._selected, 1);
				obj = obj._r_next;
			}

			// Entferne Markierung von Objekten, die vom Root-Set erreichbar
			// sind.
			Object imageObject = this.firstImageObject;
			while (imageObject != null && MAGIC.cast2Ref(imageObject) < this.imageTop) {
				followRelocEntries(imageObject);
				imageObject = imageObject._r_next;
			}

			// Gebe weiterhin markierte Objekte frei.
			Object prev = null;
			Object now = DynamicRuntime.firstDynamicObject;
			while (now != null) {
				//noinspection ConstantConditions
				if(now._selected != 0 && prev != null) {
					MAGIC.assign(prev._r_next, now._r_next);
					Object tmp = now;
					now = now._r_next;
					Memory.free(tmp);
				} else {
					prev = now;
					now = now._r_next;
				}
			}

			// Versuche EmptyObjects zusammenzulegen.
			Memory.join();

			Interrupts.enableIRQs();
		}
	}

	private void followRelocEntries(Object obj) {
		int ref = MAGIC.cast2Ref(obj);
		// Gehe der Reihe nach alle RelocEntries durch. _r_type und _r_next
		// werden übersprungen.
		for(int i = 3; i <= obj._r_relocEntries; i++) {
			int targetAddr = MAGIC.rMem32(ref - 4 * i);
			Object target = MAGIC.cast2Obj(targetAddr);
			//noinspection ConstantConditions
			if(targetAddr >= this.imageTop && target._selected != 0) {
				MAGIC.assign(target._selected, 0);
				followRelocEntries(target);
			}
		}
	}

}
