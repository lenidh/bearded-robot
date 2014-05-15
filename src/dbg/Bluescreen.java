package dbg;

import interrupts.Interrupts;
import memory.VirtualMemory;
import rte.SClassDesc;
import rte.SMthdBlock;
import rte.SPackage;
import video.Printer;

public class Bluescreen {

	private static final int fg = Printer.WHITE;

	private static final int bg = Printer.GRAY;

	static void show() {
		show(null);
	}

	static void show(Integer arg) {
		Interrupts.disableIRQs();

		Printer.fillScreen(bg);

		int ebp=0;
		MAGIC.inline(0x89, 0x6D); MAGIC.inlineOffset(1, ebp); //mov [ebp+xx],ebp
		ebp = MAGIC.rMem32(ebp); // Diese Methode ausschließen
		ebp = MAGIC.rMem32(ebp); // Interrupt-Handler ausschließen

		printRegisters(ebp);
		printStackTrace(ebp, arg);
		if(arg != null) printPageFaultInfo(arg.intValue());

		while (true);
	}

	private static void printRegisters(int ebp) {
		PushAValues regs = (PushAValues)MAGIC.cast2Struct(ebp+4);

		Printer.directPrintString("Registers:", 0, 0, fg, bg);
		Printer.directPrintString("EAX=", 0, 1, fg, bg);
		Printer.directPrintInt(regs.eax, 16, 8, 4, 1, fg, bg);
		Printer.directPrintString("EBX=", 0, 2, fg, bg);
		Printer.directPrintInt(regs.ebx, 16, 8, 4, 2, fg, bg);
		Printer.directPrintString("ECX=", 0, 3, fg, bg);
		Printer.directPrintInt(regs.ecx, 16, 8, 4, 3, fg, bg);
		Printer.directPrintString("EDX=", 0, 4, fg, bg);
		Printer.directPrintInt(regs.edx, 16, 8, 4, 4, fg, bg);
		Printer.directPrintString("EBP=", 0, 5, fg, bg);
		Printer.directPrintInt(regs.ebp, 16, 8, 4, 5, fg, bg);
		Printer.directPrintString("ESP=", 0, 6, fg, bg);
		Printer.directPrintInt(regs.esp, 16, 8, 4, 6, fg, bg);
		Printer.directPrintString("ESI=", 0, 7, fg, bg);
		Printer.directPrintInt(regs.esi, 16, 8, 4, 7, fg, bg);
		Printer.directPrintString("EDI=", 0, 8, fg, bg);
		Printer.directPrintInt(regs.edi, 16, 8, 4, 8, fg, bg);
	}

	private static void printStackTrace(int ebp, Integer arg) {
		Printer.directPrintString("Stack Trace:", 0, 10, fg, bg);

		int line = 11;
		int eip = 0;
		SMthdBlock mthdBlock = null;
		do {
			if(MAGIC.rMem32(ebp) == MAGIC.rMem32(ebp + 12)) {
				eip = MAGIC.rMem32(ebp + 36);
			} else {
				eip = MAGIC.rMem32(ebp + 4);
			}
			ebp = MAGIC.rMem32(ebp);
			mthdBlock = EipToMthdBlock(eip);
			printMthdBlock(mthdBlock, 0, line++);
		} while (mthdBlock != null && !"main()".equals(mthdBlock.namePar) && !"Kernel".equals(mthdBlock.owner.name));
	}

	private static void printPageFaultInfo(int arg) {
		Printer.directPrintString("Page Fault:", 20, 0, fg, bg);
		if(arg == 1) {
			Printer.directPrintString("Not Present", 20, 1, fg, bg);
		} else {
			Printer.directPrintString("Not Writable", 20, 1, fg, bg);
		}
		int cr2 = VirtualMemory.getCR2();
		int addr = cr2 & 0xFFFFF000;
		Printer.directPrintString("Address=", 20, 2, fg, bg);
		Printer.directPrintInt(addr, 16, 8, 20, 2, fg, bg);
	}

	private static void printMthdBlock(SMthdBlock block, int x, int y) {
		if(block == null) {
			Printer.directPrintString("unknown", x, y, fg, bg);
		} else {
			Printer.directPrintString(block.namePar, x, y, fg, bg);
		}
	}

	private static SMthdBlock EipToMthdBlock(int eip) {
		return searchMthdBlockInPackage(SPackage.root, eip);
	}

	private static SMthdBlock searchMthdBlockInPackage(SPackage pkg, int eip) {
		SMthdBlock block = null;

		SClassDesc unit = pkg.units;
		while (block == null && unit != null) {
			block = searchMthdBlockInUnit(unit, eip);
			unit = unit.nextUnit;
		}

		if(block == null && pkg.nextPack != null) {
			block = searchMthdBlockInPackage(pkg.nextPack, eip);
		}
		if(block == null && pkg.subPacks != null) {
			block = searchMthdBlockInPackage(pkg.subPacks, eip);
		}

		return block;
	}

	private static SMthdBlock searchMthdBlockInUnit(SClassDesc unit, int eip) {
		SMthdBlock block = unit.mthds;
		while (block != null) {
			int blockBase = MAGIC.cast2Ref(block) + MAGIC.getCodeOff();
			if(eip >= blockBase && eip < blockBase + block._r_scalarSize - MAGIC.getCodeOff()) {
				break;
			}
			block = block.nextMthd;
		}

		return block;
	}

	private static class PushAValues extends STRUCT {
		int edi;
		int esi;
		int ebp;
		int esp;
		int ebx;
		int edx;
		int ecx;
		int eax;
	}
}
