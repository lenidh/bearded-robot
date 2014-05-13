package rte;

/**
 * Diese Klasse stellt Klassendeskriptoren dar, welche werwendet werden können,
 * um zur Laufzeit Metainformationen zu Objekten zu erhalten.
 */
@SuppressWarnings("all")
public class SClassDesc {

	/**
	 * Der Klassendeskriptor der Klasse, von der die Klasse dieses
	 * Klassendeskriptors abgeleitet ist.
	 */
	public SClassDesc parent;

	/**
	 * Die Liste der Interfacedeskriptoren der Interfaces, die von der Klasse
	 * dieses Klassendeskriptors implementiert werden.
	 */
	public SIntfMap implementations;

	/**
	 * Die nächste Unit im selben Package.
	 */
	public SClassDesc nextUnit;

	/**
	 * Der einfache Name der Unit.
	 */
	public String name;

	/**
	 * Das Package, in dem die Klasse enthalten ist.
	 */
	public SPackage pack;

	/**
	 * Die erste Methode der Unit.
	 */
	public SMthdBlock mthds;

	/**
	 * Die Modifier der Unit.
	 */
	public int modifier;
}
