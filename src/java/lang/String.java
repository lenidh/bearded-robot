package java.lang;

@SuppressWarnings("all")
public class String {
	private char[] value;
	private int count;
	@SJC.Inline
	public int length() {
		return count;
	}
	@SJC.Inline
	public char charAt(int i) {
		return value[i];
	}
}
