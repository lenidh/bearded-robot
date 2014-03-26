package kernel;

import rte.DynamicRuntime;
import video.Printer;

@SuppressWarnings("UnusedDeclaration")
public class Kernel {

	@SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
	public static void main() {
		DynamicRuntime.init();

		Printer printer = new Printer();
		printer.setColor(0, 15);
		printer.clear();
		printer.println("Hallo");
		printer.println("Welt");

		while (true) ;
	}

	/**
	 * Löscht alle Zeichen auf dem Bildschirm.
	 */

}
