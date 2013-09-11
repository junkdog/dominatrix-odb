package net.onedaybeard.dominatrix.tuple;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode @ToString
public class Tuple3<A, B, C>
{
	public final A a;
	public final B b;
	public final C c;

	Tuple3(A a, B b, C c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
}
