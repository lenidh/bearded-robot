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
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String) {
			String other = (String)obj;
			if(this.count == other.count) {
				for(int i = 0; i < this.count; i++) {
					if(this.value[i] != other.value[i]) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}
}
