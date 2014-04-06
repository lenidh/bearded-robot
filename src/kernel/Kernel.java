package kernel;

import interrupts.Interrupts;
import interrupts.Pic;
import rte.DynamicRuntime;
import video.Printer;

@SuppressWarnings("UnusedDeclaration")
public class Kernel {

	@SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
	public static void main() {
		DynamicRuntime.init();
		Pic.init();

		Printer.fillScreen(Printer.BLACK);

		long tmp=(((long)0x300000)<<16)|(long)(256*8);
		MAGIC.inline(0x0F, 0x01, 0x5D); MAGIC.inlineOffset(1, tmp); // lidt [ebp-0x08/tmp]

		int addr = MAGIC.rMem32(MAGIC.cast2Ref(MAGIC.clssDesc("Interrupts")) + MAGIC.mthdOff("Interrupts", "IHandler") + MAGIC.getCodeOff());
		MAGIC.wMem32(0x300000 + 0 * 8, (0x08 << 16)|(addr&0xFFFF));
		MAGIC.wMem32(0x300000 + 0 * 8 + 4, (addr & 0xFFFF0000)|0x8E00);

		int a = 1;
		int b = 0;
		int c = a / b;
		//MAGIC.inline(0xCC);

		while (true) ;
	}
}
