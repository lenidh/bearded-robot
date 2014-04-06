package interrupts;

import video.Printer;
import video.VideoMemory;

public class Interrupts {

	//private static final Idt idt = (Idt)MAGIC.cast2Struct(0x200000);

	private static int baseAddress = 0x300000;

	private static int tableLimit = 8 * 256;

	public static void init() {
		long tmp=(((long)baseAddress)<<16)|(long)tableLimit;
		MAGIC.inline(0x0F, 0x01, 0x5D); MAGIC.inlineOffset(1, tmp); // lidt [ebp-0x08/tmp]

		int addr = MAGIC.rMem32(MAGIC.cast2Ref(MAGIC.clssDesc("Interrupts")) + MAGIC.mthdOff("Interrupts", "IHandler") + MAGIC.getCodeOff());
		MAGIC.wMem32(baseAddress + 3 * 8, (0x08 << 16)|(addr&0xFFFF));
		MAGIC.wMem32(baseAddress + 3 * 8 + 4, (addr & 0xFFFF0000)|0x8E00);

		//setHandler(3, addr);
	}

	/*private static void setHandler(int n, int handlerAddress) {
		idt.entries[n].offsetLo = (short)(handlerAddress & 0xFFFF);
		idt.entries[n].selector = 0x8;
		idt.entries[n].zero = 0;
		idt.entries[n].flags = (byte)0x8E;
		idt.entries[n].offsetHi = (short)((handlerAddress >> 16) & 0xFFFF);
	}*/

	private Interrupts() {
	}

	public static void enable() {
		MAGIC.inline(0xFB);
	}

	public static void disable() {
		MAGIC.inline(0xFA);
	}

	@SJC.Interrupt
	public static void IHandler() {
		Printer.fillScreen(Printer.RED);
		while (true);
	}

}
