/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */
package cgthk.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;



public final class Mat3
{
	public float m00, m01, m02;
	public float m10, m11, m12;
	public float m20, m21, m22;
	
	
	public Mat3( float m00, float m01, float m02,
				 float m10, float m11, float m12,
				 float m20, float m21, float m22 )
	{
		this.m00 = m00; this.m01 = m01; this.m02 = m02;
		this.m10 = m10; this.m11 = m11; this.m12 = m12;
		this.m20 = m20; this.m21 = m21; this.m22 = m22;
	}
	

	/**
	 * Creates a new identity matrix.
	 */
	public Mat3()
	{
		this( 1.0f, 0.0f, 0.0f,
			  0.0f, 1.0f, 0.0f,
			  0.0f, 0.0f, 1.0f );
	}
	
	
	public Mat3( Mat3 mat )
	{
		this( mat.m00, mat.m01, mat.m02,
			  mat.m10, mat.m11, mat.m12,
			  mat.m20, mat.m21, mat.m22 );
	}
	
	
	public Mat3( Mat4 mat )
	{
		this( mat.m00, mat.m01, mat.m02,
			  mat.m10, mat.m11, mat.m12,
			  mat.m20, mat.m21, mat.m22 );
	}
	
	
	public Mat3( Vec3 xAxis, Vec3 yAxis, Vec3 zAxis )
	{
		this( xAxis.x, yAxis.x, zAxis.x,
			  xAxis.y, yAxis.y, zAxis.y,
			  xAxis.z, yAxis.z, zAxis.z );
	}
	
	
	public static Mat3 rotation( Vec3 axis, float angle )
	{
		float x2 = axis.x * axis.x;
		float xy = axis.x * axis.y;
		float xz = axis.x * axis.z;
		float y2 = axis.y * axis.y;
		float yz = axis.y * axis.z;
		float z2 = axis.z * axis.z;
		float sinA = (float) Math.sin( angle );
		float cosA = (float) Math.cos( angle );
		float cosDif = 1.0f - cosA;
		
		return new Mat3( cosA + x2 * cosDif,           xy * cosDif - axis.z * sinA,  xz * cosDif + axis.y * sinA,
						 xy * cosDif + axis.z * sinA,  cosA + y2 * cosDif,           yz * cosDif - axis.x * sinA,
						 xz * cosDif - axis.y * sinA,  yz * cosDif + axis.x * sinA,  cosA + z2 * cosDif           );
	}
	
	
	public Mat3 mul( Mat3 mat )
	{
		float tmp00 = this.m00,  tmp01 = this.m01,  tmp02 = this.m02;
		float tmp10 = this.m10,  tmp11 = this.m11,  tmp12 = this.m12;
		float tmp20 = this.m20,  tmp21 = this.m21,  tmp22 = this.m22;
		
		this.m00 = tmp00 * mat.m00 + tmp01 * mat.m10 + tmp02 * mat.m20;
		this.m01 = tmp00 * mat.m01 + tmp01 * mat.m11 + tmp02 * mat.m21;
		this.m02 = tmp00 * mat.m02 + tmp01 * mat.m12 + tmp02 * mat.m22;

		this.m10 = tmp10 * mat.m00 + tmp11 * mat.m10 + tmp12 * mat.m20;
		this.m11 = tmp10 * mat.m01 + tmp11 * mat.m11 + tmp12 * mat.m21;
		this.m12 = tmp10 * mat.m02 + tmp11 * mat.m12 + tmp12 * mat.m22;

		this.m20 = tmp20 * mat.m00 + tmp21 * mat.m10 + tmp22 * mat.m20;
		this.m21 = tmp20 * mat.m01 + tmp21 * mat.m11 + tmp22 * mat.m21;
		this.m22 = tmp20 * mat.m02 + tmp21 * mat.m12 + tmp22 * mat.m22;
		
		return this;
	}
	
	
	public static Mat3 mul( Mat3 left, Mat3 right )
	{
		Mat3 result = new Mat3( left );
		
		return result.mul( right );
	}
	
	
	public Mat3 transpose()
	{
		float tempVal = this.m01;
		this.m01 = this.m10;
		this.m10 = tempVal;

		tempVal  = this.m02;
		this.m02 = this.m20;
		this.m20 = tempVal;

		tempVal  = this.m12;
		this.m12 = this.m21;
		this.m21 = tempVal;
		
		return this;
	}
	
	
	public static Mat4 transpose( Mat4 mat )
	{
		Mat4 result = new Mat4( mat );
		
		return result.transpose();
	}
	
	
	public FloatBuffer toFloatBuffer()
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
		Mat3.storeInBuffer( this, buffer );
		buffer.flip();
		
		return buffer;
	}
	
	
	public FloatBuffer toFloatBuffer( FloatBuffer buffer )
	{
		assert buffer.capacity() == 9 : "FloatBuffer should have a capacity of 9 but has " + buffer.capacity();
		
		Mat3.storeInBuffer( this, buffer );
		buffer.flip();
		
		return buffer;
	}
	
	
	public String toString()
	{
		return "[\t" + this.m00 + "\t" + this.m01 + "\t" + this.m02 + "]\n" + 
			   "[\t" + this.m10 + "\t" + this.m11 + "\t" + this.m12 + "]\n" + 
			   "[\t" + this.m20 + "\t" + this.m21 + "\t" + this.m22 + "]\n";
	}
	
	
	private static void storeInBuffer( Mat3 mat, FloatBuffer buffer )
	{
		buffer.put( mat.m00 );
		buffer.put( mat.m10 );
		buffer.put( mat.m20 );
		buffer.put( mat.m01 );
		buffer.put( mat.m11 );
		buffer.put( mat.m21 );
		buffer.put( mat.m02 );
		buffer.put( mat.m12 );
		buffer.put( mat.m22 );
	}
	
	
	public static void runTest()
	{
		
	}
}
