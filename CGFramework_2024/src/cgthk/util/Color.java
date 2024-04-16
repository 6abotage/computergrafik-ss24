/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */
package cgthk.util;


import cgthk.math.Vec3;



public class Color
{
	public static Vec3 red()
	{
		return new Vec3( 1.0f, 0.0f, 0.0f );
	}
	
	
	public static Vec3 green()
	{
		return new Vec3( 0.0f, 1.0f, 0.0f );
	}
	
	
	public static Vec3 blue()
	{
		return new Vec3( 0.0f, 0.0f, 1.0f );
	}
	
	
	public static Vec3 lightBlue()
	{
		return new Vec3( 0.25f, 0.5f, 1.0f );
	}
	
	
	public static Vec3 cyan()
	{
		return new Vec3( 0.0f, 1.0f, 1.0f );
	}
	
	
	public static Vec3 magenta()
	{
		return new Vec3( 1.0f, 0.0f, 1.0f );
	}
	
	
	public static Vec3 yellow()
	{
		return new Vec3( 1.0f, 1.0f, 0.0f );
	}
	
	
	public static Vec3 orange()
	{
		return new Vec3( 1.0f, 0.5f, 0.0f );
	}
	
	
	public static Vec3 black()
	{
		return new Vec3( 0.0f );
	}
	
	
	public static Vec3 grey()
	{
		return new Vec3( 0.5f );
	}
	
	
	public static Vec3 white()
	{
		return new Vec3( 1.0f );
	}
}
