package rte;

final class EmptyObject {
	// ACHTUNG: Diese Klasse darf keine eigenen Attribute definieren, um den
	// frei werdenden Speicher beliebiger Objekte verwalten zu können.

	static int getMinimumSize() {
		return MAGIC.getInstRelocEntries("EmptyObject") * 4
				+ MAGIC.getInstScalarSize("EmptyObject");
	}

	// Verhindert, dass EmptyObject-Objekte dynamisch erzeugt werden.
	private EmptyObject() { }
}
