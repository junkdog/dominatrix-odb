package net.onedaybeard.artemis;

import java.util.Comparator;

import com.artemis.Component;

public final class ComponentNameComparator implements Comparator<Component>
{
	@Override
	public int compare(Component o1, Component o2)
	{
		String s1 = o1.getClass().getSimpleName();
		String s2 = o2.getClass().getSimpleName();
		return s1.compareTo(s2);
	}
}