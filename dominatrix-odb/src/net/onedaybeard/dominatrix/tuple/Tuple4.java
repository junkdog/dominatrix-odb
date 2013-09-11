package net.onedaybeard.dominatrix.tuple;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode @ToString
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
}
