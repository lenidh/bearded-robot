package dbg;

import interrupts.InterruptHandler;
import video.Printer;

class BreakpointExceptionHandler extends InterruptHandler {

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

	private static final int fg = Printer.WHITE;

	private static final int bg = Printer.GRAY;

	@Override
	public void onInterrupt(int number, Integer errorCode) {
		int ebp=0;
		MAGIC.inline(0x89, 0x6D); MAGIC.inlineOffset(1, ebp); //mov [ebp+xx],ebp

		// Hole den EBP der ISR (alter EBP == EBP von isr3)
		int isrEbp = MAGIC.rMem32(ebp);
		// Hole die Registerwerte, die beim Auslösen der Exception aktuell waren.
		PushAValues regs = (PushAValues)MAGIC.cast2Struct(isrEbp+4);

		int line = 0;
		Printer.fillScreen(bg);
		printLine(line++, "EAX: ", regs.eax);
		printLine(line++, "ECX: ", regs.ecx);
		printLine(line++, "EDX: ", regs.edx);
		printLine(line++, "EBX: ", regs.ebx);
		printLine(line++, "ESP: ", regs.esp);
		printLine(line++, "EBP: ", regs.ebp);
		printLine(line++, "ESI: ", regs.esi);
		printLine(line++, "EDI: ", regs.edi);
		printLine(line++, "EIP: ", MAGIC.rMem32(isrEbp+36));

		while (true);
	}

	private void printLine(int line, String label, int value) {
		Printer.directPrintString(label, 0, line, fg, bg);
		Printer.directPrintInt(value, 16, 8, label.length(), line, fg, bg);
	}
}
