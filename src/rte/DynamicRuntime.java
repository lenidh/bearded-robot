package rte;

@SuppressWarnings({"UnusedDeclaration", "SpellCheckingInspection"})
public class DynamicRuntime {

	private static int newInstanceOffset;

	public static void init() {
		newInstanceOffset = MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4);
	}

	public static Object newInstance(int scalarSize, int relocEntries, SClassDesc type) {
		Object lastObj = type;
		while (lastObj._r_next != null) {
			lastObj = lastObj._r_next;
		}

		int objSize = scalarSize + relocEntries * 4;
		while (objSize % 4 == 0) objSize++;

		for(int i = newInstanceOffset; i < (objSize / 4); i++) {
			MAGIC.wMem32(i, 0);
		}
		Object obj = MAGIC.cast2Obj(newInstanceOffset + relocEntries * 4);

		MAGIC.assign(obj._r_scalarSize, scalarSize);
		MAGIC.assign(obj._r_relocEntries, relocEntries);
		MAGIC.assign(obj._r_type, type);

		//noinspection ConstantConditions
		MAGIC.assign(lastObj._r_next, obj);

		newInstanceOffset += objSize;

		return obj;
	}

	public static SArray newArray(int length, int arrDim, int entrySize, int stdType,
	                              Object unitType) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static void newMultArray(SArray[] parent, int curLevel, int destLevel, int length,
	                                int arrDim, int entrySize, int stdType, Object unitType) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static boolean isInstance(Object o, SClassDesc dest, boolean asCast) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static boolean isArray(SArray o, int stdType, Object unitType, int arrDim,
	                              boolean asCast) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static void checkArrayStore(Object dest, SArray newEntry) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}
}
