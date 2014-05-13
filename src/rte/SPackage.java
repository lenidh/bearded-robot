package rte;

/**
 * Diese Klasse stellt Metadaten für Java-Packages bereit, die zur Laufzeit
 * verwendet werden können.
 */
@SuppressWarnings("ALL")
public class SPackage {

	/**
	 * Das Wurzel- bzw. default-Package;
	 */
	public static SPackage root;

	/**
	 * Einfacher Name des Packages.
	 */
	public String name;

	/**
	 * Das Package, in dem dieses Package enthalten ist.
	 */
	public SPackage outer;

	/**
	 * Das erste Package, das in diesem Package enthalten ist.
	 */
	public SPackage subPacks;

	/**
	 * Das nächste Package, im selben Package wie dieses Package.
	 */
	public SPackage nextPack;

	/**
	 * Die erste Unit, die in diesem Package enthalten ist.
	 */
	public SClassDesc units;
}
