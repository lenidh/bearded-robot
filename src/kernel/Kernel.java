package kernel;

import rte.DynamicRuntime;
import video.Printer;

@SuppressWarnings("UnusedDeclaration")
public class Kernel {

	@SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
	public static void main() {
		DynamicRuntime.init();

		Printer.fillScreen(Printer.BLACK);
		Printer p = new Printer();
		p.setCursor(35, 0);
		p.print("Guten Tag!");
		p.setCursor(35, 1);
		p.print("==========");
		p.println();
		p.println();
		p.print("Dieses Printer-Objekt liegt an Adresse: ");
		p.setColor(Printer.RED, Printer.BLACK);
		p.printHex(MAGIC.cast2Ref(p));
		p.print(" (");
		p.print(MAGIC.cast2Ref(p));
		p.print(')');

		while (true) ;
	}
}
