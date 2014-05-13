package rte;

import bios.BIOS;
import bios.MemoryMapBuffer;
import kernel.Kernel;
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
public class MemoryManager {

	private static final int MIN_MANAGED_ADDRESS = 0x100000; // 1MB

	/**
	 * Referenz auf das {@link rte.EmptyObject} mit der höchsten Adresse.
	 */
	static EmptyObject lastEmptyObject = null;

	private static boolean isInitialized = false;

	/**
	 * Initialisiert die {@link rte.MemoryManager}-Klasse. Zustand und Verhalten
	 * der Klasse, vor dem Aufruf dieser Methode, ist nicht definiert.
	 * Mehrmalige Aufrufe haben keine Auswirkung.
	 */
	static void init() {
		// Stelle sicher, dass nur ein einziges Mal initialisiert wird.
		if (isInitialized) {
			return;
		}

		MemoryMapBuffer buffer = (MemoryMapBuffer)MAGIC.cast2Struct(Kernel.MEMORY_MAP_BUFFER_BASE);

		// Ermittle die, laut BIOS vorhandenen, Speicherbereiche ab.
		BIOS.regs.EBX = 0;
		do {
			BIOS.regs.EAX = 0xE820;
			BIOS.regs.EDX = 0x534D4150;
			BIOS.regs.ECX = 20;
			BIOS.regs.ES = 0x0;
			BIOS.regs.EDI = Kernel.MEMORY_MAP_BUFFER_BASE;

			BIOS.rint(0x15);

			// Stelle alle freien Speicherbereiche oberhalb 1MB unter
			// Speicherverwaltung.
			if (buffer.base >= MIN_MANAGED_ADDRESS && buffer.type == 1) { // type == 1 => frei

				// Unterstütze nur Adressen, die mit 32 Bit adressiert werden können.
				if(buffer.base <= Integer.MAX_VALUE
						&& buffer.base + buffer.length <= Integer.MAX_VALUE) {

					addSegment((int)buffer.base, (int)buffer.length);
				}
			}
		} while (BIOS.regs.EBX != 0 && (BIOS.regs.FLAGS & BIOS.F_CARRY) == 0);

		isInitialized = true;
	}

	/**
	 * Fügt den angegebenen Speicherbereich, dem {@link rte.MemoryManager}, als
	 * zu verwaltenden, freien Speicherbereich hinzu.
	 *
	 * @param base Basisadresse des zu verwaltenden, freien Speicherbereich.
	 * @param length Länge des zu verwaltenden, freien Speicherbereich.
	 */
	private static void addSegment(int base, int length) {
		int imageTop = MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4);

		// Falls das Image nicht im Bereich liegt, verwalte den gesamten Bereich
		// mit einem EmptyObject, sonst verwalte die Abschnitte vor und nach dem
		// Image jeweils mit einem eigenen Empty Object.
		if(imageTop <= base || MAGIC.imageBase >= base + length) {
			addEmptyObject(base, length);
		} else {
			if(MAGIC.imageBase > base) {
				addEmptyObject(base, length - (MAGIC.imageBase - base));
			}
			if(imageTop < base + length) {
				addEmptyObject(imageTop, (base + length) - imageTop);
			}
		}
	}

	/**
	 * Fügt der Frei-Speicher-Liste ein neues {@link rte.EmptyObject} hinzu,
	 * das den angegebenen Speicherbereich abdeckt.
	 * <p>
	 * Ein {@link rte.EmptyObject} wird nur dann erzeugt, wenn dessen Länge
	 * größer als {@link EmptyObject#getMinimumSize()} ist.
	 * </p>
	 *
	 * @param base Basisadresse des Speicherbereichs, in den das
	 * {@link rte.EmptyObject} gelegt wird.
	 * @param length Länge des Speicherbereichs, in den das
	 * {@link rte.EmptyObject} gelegt wird.
	 */
	private static void addEmptyObject(int base, int length) {
		if(length > EmptyObject.getMinimumSize()) {
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
	 * Teilt einem aufrufenden Programm einen freien Speicherbereich zu.
	 * <p>
	 * Der Inhalt des Speicherbereichs, wird von dieser Methode nicht
	 * initialisiert.
	 * </p>
	 *
	 * @param size Größe des Speichers, der zugeteilt werden soll.
	 * @return Basisadresse des zugeteilten Speicherbereichs oder 0, falls kein
	 * Speicher mehr zur verfügung steht.
	 */
	static int allocate(int size) {
		EmptyObject eo = lastEmptyObject;
		// Durchsuche die EmptyObject-Liste
		while (eo != null) {
			// Das erste EmptyObject mit passender Größe wird verwendet.
			if(eo._r_scalarSize >= size + EmptyObject.getMinimumSize()) {
				MAGIC.assign(eo._r_scalarSize, eo._r_scalarSize - size);
				int eoBase = MAGIC.cast2Ref(eo);
				return eoBase + eo._r_scalarSize;
			}
			eo = (EmptyObject)eo._r_next;
			// TODO: Speichervergabe, kann auch erfolgen, wenn die Größe eines EmptyObject exakt "size" entspricht.
		}
		return 0;
	}

	/**
	 * Gibt den, von einem Objekt belegten, Speicher frei.
	 *
	 * @param obj Objekt dessen Speicherbereich freigegeben werden soll.
	 */
	static void free(Object obj) {
		int size = obj._r_scalarSize + obj._r_relocEntries * 4;
		int addr = MAGIC.cast2Ref(obj) - obj._r_relocEntries * 4;
		addEmptyObject(addr, size);
	}

	/**
	 * Führt nebeneinander liegende EmptyObjects zu einem einzelnen EmptyObject
	 * zusammen.
	 */
	static void join() {
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

}
