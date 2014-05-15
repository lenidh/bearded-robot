package memory;

import video.Printer;

public class VirtualMemory {

	/**
	 * Basisadresse des PageDirectory
	 */
	private static final int pageDirectoryBase = 0x300000;

	/**
	 * Basisadresse der ersten PageTable
	 */
	private static final int pageTablesBase = pageDirectoryBase + 0x1000;

	public static void init() {
		createDirectory();
		createTables();

		setCR3(pageDirectoryBase);
		enableVirtualMemory();
	}

	private static void createDirectory() {
		for(int i = 0; i < 1024; i++) {
			int tableAddr = pageTablesBase + 4096 * i;
			MAGIC.wMem32(pageDirectoryBase + 4 * i, tableAddr | 3);
		}
	}

	private static void createTables() {
		// Erste Page
		MAGIC.wMem32(pageTablesBase, 0);

		for(int i = 1; i < 1024 * 1024 - 1; i++) {
			int pageAddr = 4096 * i;
			MAGIC.wMem32(pageTablesBase + 4 * i, pageAddr | 3);
		}

		// Letzte Page
		int pageAddr = 4096 * (1024 * 1024 - 1);
		MAGIC.wMem32(pageTablesBase + 4 * (1024 * 1024 - 1), pageAddr);
	}

	private static void setCR3(int addr) {
		MAGIC.inline(0x8B, 0x45); MAGIC.inlineOffset(1, addr); //mov eax,[ebp+8]
		MAGIC.inline(0x0F, 0x22, 0xD8); //mov cr3,eax
	}

	private static void enableVirtualMemory() {
		MAGIC.inline(0x0F, 0x20, 0xC0); //mov eax,cr0
		MAGIC.inline(0x0D, 0x00, 0x00, 0x01, 0x80); //or eax,0x80010000
		MAGIC.inline(0x0F, 0x22, 0xC0); //mov cr0,eax
	}

	public static int getCR2() {
		int cr2=0;
		MAGIC.inline(0x0F, 0x20, 0xD0); //mov e/rax,cr2
		MAGIC.inline(0x89, 0x45); MAGIC.inlineOffset(1, cr2); //mov [ebp-4],eax
		return cr2;
	}
}
