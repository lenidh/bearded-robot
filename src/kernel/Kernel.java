package kernel;

import apps.BusyCrocodile;
import apps.Menu;
import apps.tests.KeyboardTest;
import bios.BIOS;
import dbg.Bluescreen;
import dbg.Debugging;
import memory.VirtualMemory;
import rte.GarbageCollector;
import scheduling.Scheduler;
import interrupts.Interrupts;
import keyboard.Keyboard;
import rte.DynamicRuntime;
import timer.Timer;
import video.Printer;

@SuppressWarnings("UnusedDeclaration")
public class Kernel {

	/**
	 * Basisadresse der IDT.
	 */
	public static final int IDT_BASE = 0x7E00;

	/**
	 * Basisadresse des Puffers zum Auslesen der Speicherbereiche.
	 */
	public static final int MEMORY_MAP_BUFFER_BASE = 0x7E80;


	public static Scheduler scheduler;

	@SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
	public static void main() {
		Printer.fillScreen(Printer.BLACK);
		VirtualMemory.init();
		DynamicRuntime.init();
		Interrupts.init();
		Timer.init();
		Debugging.init();

		Interrupts.enableIRQs();

		scheduler = new Scheduler();
		scheduler.addTask(new BusyCrocodile(), true);
		scheduler.addTask(Keyboard.initstance(), true);
		scheduler.addTask(new Menu(), true);
		//scheduler.addTask(new KeyboardTest());
		scheduler.addTask(new GarbageCollector(), true);
		scheduler.start();
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

		// HACK: 'Halte an'; Effekt tritt bei zu leistungsfähigen Rechnern nicht
		// auf.
		// TODO: Timer basiertes Warten
		for(int i = 0; i < 1000000000; i++) { }

		// Zurück in den Textmodus wechseln
		BIOS.regs.EAX = 0x0003;
		BIOS.rint(0x10);

		Printer p = new Printer();
		p.println("Ende");
	}
}
