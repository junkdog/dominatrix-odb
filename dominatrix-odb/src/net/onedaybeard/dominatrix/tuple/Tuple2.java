package net.onedaybeard.dominatrix.tuple;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode @ToString
public class Tuple2<A, B>
{
	public final A a;
	public final B b;

	Tuple2(A a, B b)
	{
		this.a = a;
		this.b = b;
	}
}
