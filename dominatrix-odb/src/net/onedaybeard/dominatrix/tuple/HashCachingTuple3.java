package net.onedaybeard.dominatrix.tuple;


public class HashCachingTuple3<A, B, C> extends Tuple3<A,B,C>
{

	private int hashCode = Integer.MIN_VALUE;

	HashCachingTuple3(A a, B b, C c)
	{
		super(a, b, c);
	}

	public int hashCode()
	{
		if (hashCode == Integer.MIN_VALUE)
			hashCode = super.hashCode();

		return hashCode;
	}
}
