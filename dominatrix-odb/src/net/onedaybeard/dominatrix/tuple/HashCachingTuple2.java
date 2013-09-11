package net.onedaybeard.dominatrix.tuple;

import lombok.ToString;

@ToString
public class HashCachingTuple2<A, B> extends Tuple2<A,B>
{

	private int hashCode = Integer.MIN_VALUE;

	HashCachingTuple2(A a, B b)
	{
		super(a, b);
	}

	public int hashCode()
	{
		if (hashCode == Integer.MIN_VALUE)
			hashCode = super.hashCode();

		return hashCode;
	}
}
