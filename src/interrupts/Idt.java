package interrupts;

public class Idt extends STRUCT {
	@SJC(count = 256)
	public IdtEntry[] entries;
}
