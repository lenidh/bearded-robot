package interrupts;

public class Idt extends STRUCT {
	@SJC(count = 48)
	public IdtEntry[] entries;
}
