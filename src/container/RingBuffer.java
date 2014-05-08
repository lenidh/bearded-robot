package container;

/**
 * Stellt eine Ringpuffer bereit.
 */
public class RingBuffer {

	/**
	 * Einzelnes Pufferelement.
	 */
	private static class Element {
		public Element next;
		public Object value;
	}

	/**
	 * Die Kapazität des Puffers.
	 */
	private int capacity = Integer.MAX_VALUE;

	/**
	 * Die Anzahl der Elemente im Puffer.
	 */
	private int size = 0;

	private Element in = null;

	private Element out = null;

	public RingBuffer() {
	}

	public RingBuffer(int capacity) {
		this.capacity = capacity;
	}

	public int capacity() {
		return this.capacity;
	}

	public int size() {
		return this.size;
	}

	/**
	 * Fügt dem Puffer ein Element hinzu.
	 *
	 * @param value Ein Objekt, das im Puffer gespeichert werden soll.
	 */
	public void push(Object value) {
		if(this.size < this.capacity) {
			// Falls die Kapazität nicht ausgeschöpft ist, wird ein neues
			// Element erzeugt.
			Element newElement = new Element();
			newElement.value = value;

			if(this.size <= 0) {
				// Falls der Puffer leer ist ( => in == out == null), werden
				// sowohl in als auch out auf das neuen Element gesetzt und das
				// neue Element mit sich selbst verkettet.
				newElement.next = newElement;
				this.in = this.out = newElement;
			} else {
				newElement.next = this.in.next;
				this.in.next = newElement;
				this.in = newElement;
			}

			size++;
		} else {
			// Falls die Kapazität ausgeschöpft ist, wird der Wert des ältesten
			// Elements ersetzt.
			this.in = this.out;
			this.out = this.out.next;
			this.in.value = value;
		}
	}

	/**
	 * Löscht das erste Element.
	 */
	public void pop() {
		if(this.size > 0) {
			this.in.next = this.out.next;
			this.out = this.out.next;
			this.size--;
		}
	}

	/**
	 * Liefert das erste Element.
	 *
	 * @return
	 */
	public Object front() {
		if(this.size > 0) {
			return this.out.value;
		}
		return null;
	}

	/**
	 * Element überspringen.
	 */
	public void next() {
		if(this.size > 0) {
			this.in = this.out;
			this.out = this.out.next;
		}
	}
}
