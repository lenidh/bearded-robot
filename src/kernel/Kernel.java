package kernel;

import bios.BIOS;
import interrupts.Interrupts;
import rte.DynamicRuntime;
import video.Printer;

@SuppressWarnings("UnusedDeclaration")
public class Kernel {

	@SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
	public static void main() {
		Printer.fillScreen(Printer.BLACK);
		DynamicRuntime.init();
		Interrupts.init();

		MAGIC.wIOs8(0x43, (byte)0x36);
		MAGIC.wIOs8(0x40, (byte)(1193&0xFF));
		MAGIC.wIOs8(0x40, (byte)((1193>>8)&0xFF));

		TimerHandler timerHandler = new TimerHandler();
		Interrupts.HANDLERS[32] = timerHandler;
		Interrupts.enableIRQs();

		//testMode13h();

		while (true) ;
	}

	// Phase 3b
	public static void testMode13h() {
		// Wechsle in den Grafikmodus.
		BIOS.regs.EAX = 0x0013;
		BIOS.rint(0x10);

		// Zeichne ein Haus im dunklen.
		for(int y = 0; y < 200; y++) {
			for(int x = 0; x < 320; x++) {
				if(y < 100) {
					if(x > 200 && x < 250 && y > 30 && y < 100) {
						if((x > 210 && x < 220 && y > 40 && y < 50) ||
								(x > 230 && x < 240 && y > 40 && y < 50) ||
								(x > 210 && x < 220 && y > 60 && y < 70) ||
								(x > 230 && x < 240 && y > 60 && y < 70) ||
								(x > 230 && x < 240 && y > 80 && y < 90)) {
							MAGIC.wMem8(0xA0000 + x + 320 * y, (byte)0x2C);
						} else if(x > 210 && x < 220 && y > 80 && y < 100){
							MAGIC.wMem8(0xA0000 + x + 320 * y, (byte)0xb8);
						} else {
							MAGIC.wMem8(0xA0000 + x + 320 * y, (byte)0x13);
						}
					} else {
						MAGIC.wMem8(0xA0000 + x + 320 * y, (byte)0x01);
					}
				} else {
					MAGIC.wMem8(0xA0000 + x + 320 * y, (byte)0xbf);
				}
			}
		}

		// HACK: 'Halte an'; Interrupts funktionieren noch nicht; Effekt tritt
		// bei zu leistungsfähigen Rechnern nicht auf..
		for(int i = 0; i < 1000000000; i++) { }

		// Zurück in den Textmodus wechseln
		BIOS.regs.EAX = 0x0003;
		BIOS.rint(0x10);

		Printer p = new Printer();
		p.println("Ende");
	}
}
