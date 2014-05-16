package memory;

import bios.BIOS;
import video.Printer;

/**
 * Diese Klasse stellt verschiedene statische Methoden zur Speicherverwaltung
 * bereit.
 * <p>
 * Intern wird der freie Speicher mithilfe von EmptyObjects verwaltet, welche
 * in einer "nach unten wachsenden" Liste angeordnet sind. D. h. Das erste
 * EmptyObject hat die höchste Adresse im Speicher, das letzt die niedrigste.
 * </p>
 */
public final class Memory {

	/**
	 * Basisadresse der IDT.
	 */
	public static final int IDT_BASE_ADDRESS = 0x7E00;

	/**
	 * Göße der IDT in Bytes.
	 */
	public static final int IDT_SIZE = 48;

	/**
	 * Basisadresse des Puffers, zum Auslesen der Speicherbereiche.
	 */
	public static final int MEMORY_MAP_BUFFER_BASE_ADDRESS = 0x7E80;

	/**
	 * Göße des Puffers, zum Auslesen der Speicherbereiche, in Bytes.
	 */
	public static final int MEMORY_MAP_BUFFER_SIZE = 20;

	private static final int PAGE_DIRECTORY_SIZE = 0x1000;

	private static final int PAGE_TABLES_SIZE = 0x400000;

	/**
	 * Referenz auf das {@link EmptyObject} mit der höchsten Adresse.
	 */
	static EmptyObject lastEmptyObject = null;

	private static boolean isInitialized = false;

	/**
	 * Initialisiert die {@link Memory}-Klasse. Zustand und Verhalten
	 * der Klasse, vor dem Aufruf dieser Methode, ist nicht definiert.
	 * Mehrmalige Aufrufe haben keine Auswirkung.
	 */
	public static void init() {
		// Stelle sicher, dass nur ein einziges Mal initialisiert wird.
		if (isInitialized) {
			return;
		}

		initVirtualMemory();

		MemoryMapBuffer buffer = (MemoryMapBuffer)MAGIC.cast2Struct(MEMORY_MAP_BUFFER_BASE_ADDRESS);

		// Ermittle die, laut BIOS vorhandenen, Speicherbereiche ab.
		BIOS.regs.EBX = 0;
		do {
			BIOS.regs.EAX = 0xE820;
			BIOS.regs.EDX = 0x534D4150;
			BIOS.regs.ECX = 20;
			BIOS.regs.ES = 0x0;
			BIOS.regs.EDI = MEMORY_MAP_BUFFER_BASE_ADDRESS;

			BIOS.rint(0x15);

			// Stelle alle freien Speicherbereiche oberhalb 1MB unter
			// Speicherverwaltung.
			if (buffer.base + buffer.length > getMinManagedAddress() && buffer.type == 1) { // type == 1 => frei

				// Unterstütze nur Adressen, die mit 32 Bit adressiert werden können.
				if(buffer.base <= Integer.MAX_VALUE
						&& buffer.base + buffer.length <= Integer.MAX_VALUE) {

					addSegment((int)buffer.base, (int)buffer.length);
				}
			}
		} while (BIOS.regs.EBX != 0 && (BIOS.regs.FLAGS & BIOS.F_CARRY) == 0);

		isInitialized = true;
	}

	private static int getPageDirectoryBaseAddress() {
		return (MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4) + 4095) & ~0xFFF;
	}

	private static int getPageTablesBaseAddress() {
		return getPageDirectoryBaseAddress() + PAGE_DIRECTORY_SIZE;
	}

	private static int getMinManagedAddress() {
		return (getPageTablesBaseAddress() + PAGE_TABLES_SIZE + 3) & ~3;
	}

	/**
	 * Fügt den angegebenen Speicherbereich, dem {@link Memory}, als
	 * zu verwaltenden, freien Speicherbereich hinzu.
	 *
	 * @param base Basisadresse des zu verwaltenden, freien Speicherbereich.
	 * @param length Länge des zu verwaltenden, freien Speicherbereich.
	 */
	private static void addSegment(int base, int length) {
		// Falls das Image nicht im Bereich liegt, verwalte den gesamten Bereich
		// mit einem EmptyObject, sonst verwalte die Abschnitte vor und nach dem
		// Image jeweils mit einem eigenen Empty Object.
		if(getMinManagedAddress() <= base) {
			addEmptyObject(base, length);
		} else {
			if(getMinManagedAddress() + EmptyObject.getMinimumSize() < base + length) {
				addEmptyObject(getMinManagedAddress(), length - (getMinManagedAddress() - base));
			}
		}
	}

	/**
	 * Fügt der Frei-Speicher-Liste ein neues {@link EmptyObject} hinzu,
	 * das den angegebenen Speicherbereich abdeckt.
	 * <p>
	 * Ein {@link EmptyObject} wird nur dann erzeugt, wenn dessen Länge
	 * größer als {@link EmptyObject#getMinimumSize()} ist.
	 * </p>
	 *
	 * @param base Basisadresse des Speicherbereichs, in den das
	 * {@link EmptyObject} gelegt wird.
	 * @param length Länge des Speicherbereichs, in den das
	 * {@link EmptyObject} gelegt wird.
	 */
	private static void addEmptyObject(int base, int length) {
		if(length >= EmptyObject.getMinimumSize()) {
			// Erstelle EmptyObject
			Object eo = MAGIC.cast2Obj(base + MAGIC.getInstRelocEntries("EmptyObject") * 4);
			MAGIC.assign(eo._r_relocEntries, MAGIC.getInstRelocEntries("EmptyObject"));
			MAGIC.assign(eo._r_scalarSize, length - eo._r_relocEntries * 4);
			MAGIC.assign(eo._r_type, MAGIC.clssDesc("EmptyObject"));

			// Hänge EmptyObject in die Liste ein.
			if(lastEmptyObject != null && MAGIC.cast2Ref(lastEmptyObject) > base) {
				EmptyObject previousEmptyObject = lastEmptyObject;
				while (previousEmptyObject._r_next != null && MAGIC.cast2Ref(previousEmptyObject._r_next) > base) {
					previousEmptyObject = (EmptyObject)previousEmptyObject._r_next;
				}

				MAGIC.assign(eo._r_next, previousEmptyObject._r_next);
				MAGIC.assign(previousEmptyObject._r_next, (Object)eo);
			} else {
				MAGIC.assign(eo._r_next, (Object)lastEmptyObject);
				lastEmptyObject = (EmptyObject)eo;
			}
		}
	}

	/**
	 * Allozieren ein Objekt in der angegebenen Größe.
	 *
	 * @param relocEntries Anzahl der Referenzattribute.
	 * @param scalarSize Anzahl der Attribute mit Basisdatentyp.
	 * @return Ein alloziertes Objekt.
	 */
	public static Object allocate(int relocEntries, int scalarSize) {
		EmptyObject eo = lastEmptyObject;
		int objMemorySize = (relocEntries * 4 + scalarSize + 3) & ~3;

		// Durchsuche die EmptyObject-Liste
		while (eo != null) {
			// Das erste EmptyObject mit passender Größe wird verwendet.
			if(eo.getSize() >= objMemorySize + EmptyObject.getMinimumSize()) {
				// EmptyObject verkürzen
				MAGIC.assign(eo._r_scalarSize, eo._r_scalarSize - objMemorySize);

				// neues Objekt erstellen
				int eoRef = MAGIC.cast2Ref(eo);
				int objBase = eoRef + eo._r_scalarSize;

				// Initialisiere Speicher mit 0.
				for (int i = objBase; i < objBase + objMemorySize; i+=4) {
					MAGIC.wMem32(i, 0);
				}

				return MAGIC.cast2Obj(objBase + relocEntries * 4);
			}
			eo = (EmptyObject)eo._r_next;
			// TODO: Speichervergabe, kann auch erfolgen, wenn die Größe eines EmptyObject exakt "size" entspricht.
		}
		return null;
	}

	/**
	 * Gibt den, von einem Objekt belegten, Speicher frei.
	 *
	 * @param obj Objekt dessen Speicherbereich freigegeben werden soll.
	 */
	public static void free(Object obj) {
		int size = obj._r_scalarSize + obj._r_relocEntries * 4;
		int addr = MAGIC.cast2Ref(obj) - obj._r_relocEntries * 4;
		addEmptyObject(addr, size);
	}

	/**
	 * Führt nebeneinander liegende EmptyObjects zu einem einzelnen EmptyObject
	 * zusammen.
	 */
	public static void join() {
		EmptyObject previousEmptyObject = null;
		EmptyObject eo = lastEmptyObject;
		while (eo != null && eo._r_next != null) {
			// Prüfe, ob eo._r_next und eo aneinander grenzen.
			if(MAGIC.cast2Ref(eo._r_next) + eo._r_next._r_scalarSize + eo._r_relocEntries * 4
					== MAGIC.cast2Ref(eo)) {

				// Erweitere eo._r_next um die Größe von eo.
				MAGIC.assign(eo._r_next._r_scalarSize, eo._r_next._r_scalarSize + eo._r_scalarSize
						+ eo._r_relocEntries * 4);

				// Verkettung korrigieren
				eo = (EmptyObject)eo._r_next;
				if(previousEmptyObject == null) {
					lastEmptyObject = eo;
				} else {
					MAGIC.assign(previousEmptyObject._r_next, (Object)eo);
				}
			} else {
				eo = (EmptyObject)eo._r_next;
			}
		}
	}

	public static void initVirtualMemory() {
		createDirectory();
		createTables();

		setCR3(getPageDirectoryBaseAddress());
		enableVirtualMemory();
	}

	private static void createDirectory() {
		for(int i = 0; i < 1024; i++) {
			int tableAddr = getPageTablesBaseAddress() + 4096 * i;
			MAGIC.wMem32(getPageDirectoryBaseAddress() + 4 * i, tableAddr | 3);
		}
	}

	private static void createTables() {
		// Erste Page
		MAGIC.wMem32(getPageTablesBaseAddress(), 0);

		for(int i = 1; i < 1024 * 1024 - 1; i++) {
			int pageAddr = 4096 * i;
			MAGIC.wMem32(getPageTablesBaseAddress() + 4 * i, pageAddr | 3);
		}

		// Letzte Page
		int pageAddr = 4096 * (1024 * 1024 - 1);
		MAGIC.wMem32(getPageTablesBaseAddress() + 4 * (1024 * 1024 - 1), pageAddr);
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
