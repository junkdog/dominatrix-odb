package net.onedaybeard.dominatrix.tuple;

public final class Tuple
{
	private Tuple() {}

	public static <A, B>Tuple2<A,B> create(A a, B b)
	{
		return new Tuple2<A,B>(a, b);
	}

	public static <A, B>Tuple2<A,B> createHashCached(A a, B b)
	{
		return new HashCachingTuple2<A,B>(a, b);
	}

	public static <A, B, C>Tuple3<A,B,C> create(A a, B b, C c)
	{
		return new Tuple3<A,B,C>(a, b, c);
	}

	public static <A, B, C>Tuple3<A,B,C> createHashCached(A a, B b, C c)
	{
		return new HashCachingTuple3<A,B,C>(a, b, c);
	}

	public static <A, B, C, D>Tuple4<A,B,C,D> create(A a, B b, C c, D d)
	{
		return new Tuple4<A,B,C,D>(a, b, c, d);
	}

	public static <A, B, C, D>Tuple4<A,B,C,D> createHashCached(A a, B b, C c, D d)
	{
		return new HashCachingTuple4<A,B,C,D>(a, b, c, d);
	}
}
