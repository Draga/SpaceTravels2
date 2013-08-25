package com.draga.SpaceTravels2.utility;

/**
 * Created with IntelliJ IDEA.
 * User: Draga86
 * Date: 25/08/13
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class Utility {
	public static short tagCategoryBit(EntityTags tag) {
		return (short) Math.pow(2, tag.ordinal());
	}
}
