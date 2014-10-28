package net.onedaybeard.dominatrix.tuple;


public class Tuple4<A, B, C, D>
{
	public final A a;
	public final B b;
	public final C c;
	public final D d;

	Tuple4(A a, B b, C c, D d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Tuple4 other = (Tuple4)obj;
		if (a == null) {
			if (other.a != null) return false;
		}
		else if (!a.equals(other.a)) return false;
		if (b == null) {
			if (other.b != null) return false;
		}
		else if (!b.equals(other.b)) return false;
		if (c == null) {
			if (other.c != null) return false;
		}
		else if (!c.equals(other.c)) return false;
		if (d == null) {
			if (other.d != null) return false;
		}
		else if (!d.equals(other.d)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tuple4 [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
	}
}
