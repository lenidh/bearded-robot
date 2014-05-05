package rte;

import bios.BIOS;
import bios.MemoryMapBuffer;
import kernel.Kernel;
import video.Printer;

public class MemoryManager {

	private static boolean isInitialized = false;

	private static Object emptyMemoryList = null;

	static void init() {
		if(isInitialized) {
			return;
		}

		int minOffset = MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4);
		MemoryMapBuffer buffer = (MemoryMapBuffer)MAGIC.cast2Struct(Kernel.MEMORY_MAP_BUFFER_BASE);

		BIOS.regs.EBX = 0;
		do {
			BIOS.regs.EAX = 0xE820;
			BIOS.regs.EDX = 0x534D4150;
			BIOS.regs.ECX = 20;
			BIOS.regs.ES = 0x0;
			BIOS.regs.EDI = Kernel.MEMORY_MAP_BUFFER_BASE;

			BIOS.rint(0x15);

			if(buffer.base >= 100000 && buffer.type == 1
					&& buffer.base + buffer.length >= minOffset) {

				int base = (int)buffer.base;
				int length = (int)buffer.length;
				if(base < minOffset) {
					length = length - (minOffset - base);
					base = minOffset;
				}

				Object nextObj = MAGIC.cast2Obj(base);
				MAGIC.assign(nextObj._r_relocEntries, 0);
				MAGIC.assign(nextObj._r_scalarSize, (int)length);

				if(emptyMemoryList == null) {
					emptyMemoryList = nextObj;
				} else {
					Object lastObj = emptyMemoryList;
					while (lastObj._r_next != null) {
						lastObj = lastObj._r_next;
					}
					MAGIC.assign(nextObj._r_next, lastObj);
				}
			}
		} while (BIOS.regs.EBX != 0 && (BIOS.regs.FLAGS & BIOS.F_CARRY) == 0);

		isInitialized = true;
	}

	static int allocate(int size) {
		Object nextObj = emptyMemoryList;
		while (nextObj != null) {
			if(nextObj._r_scalarSize > size) {
				MAGIC.assign(nextObj._r_scalarSize, nextObj._r_scalarSize - size);
				int base = MAGIC.cast2Ref(nextObj);
				return base + nextObj._r_scalarSize;
			}
		}
		return 0;
	}

}
