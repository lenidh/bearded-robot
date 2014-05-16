package memory;

final class EmptyObject {
	// ACHTUNG: Diese Klasse darf keine eigenen Attribute definieren, um den
	// frei werdenden Speicher beliebiger Objekte verwalten zu können.

	public static int getMinimumSize() {
		return (MAGIC.getInstRelocEntries("EmptyObject") * 4
				+ MAGIC.getInstScalarSize("EmptyObject") + 3) & ~3;
	}

	public int getSize() {
		return (this._r_relocEntries * 4 + this._r_scalarSize + 3) & ~3;
	}

	// Verhindert, dass EmptyObject-Objekte dynamisch erzeugt werden.
	private EmptyObject() { }
}
