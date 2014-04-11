package interrupts;

class Idt extends STRUCT {

	public static final int SIZE = 48;

	@SJC(count = SIZE)
	public IdtEntry[] entries;
}
