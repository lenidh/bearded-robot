package rte;

/**
 * Diese Klasse stellt Metainformationen zu Methoden bereit, welche zur Laufzeit
 * verwendet werden können.
 */
@SuppressWarnings("all")
public class SMthdBlock {

	/**
	 * Der einfache Methodenname mit den vollqualifizierten Parmetertypen in
	 * runden Klammern.
	 */
	public String namePar;

	/**
	 * Die Klasse, zu der diese Methode gehört.
	 */
	public SClassDesc owner;

	/**
	 * Die nächste Methode der selben Unit.
	 */
	public SMthdBlock nextMthd;

	/**
	 * Die Modifier der Methode.
	 */
	public int modifier;

	/**
	 * Codebytezeilennummernzuordnung
	 */
	public int[] lineInCodeOffset;
}
