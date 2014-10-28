package net.onedaybeard.dominatrix.tuple;

public class HashCachingTuple4<A, B, C, D> extends Tuple4<A,B,C,D>
{

	private int hashCode = Integer.MIN_VALUE;

	HashCachingTuple4(A a, B b, C c, D d)
	{
		super(a, b, c, d);
	}

	public int hashCode()
	{
		if (hashCode == Integer.MIN_VALUE)
			hashCode = super.hashCode();

		return hashCode;
	}
}
