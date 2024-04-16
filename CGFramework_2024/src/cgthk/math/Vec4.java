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



public final class Vec4
{
	public float x;
	public float y;
	public float z;
	public float w;
	
	
	public Vec4( float x, float y, float z, float w )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	
	/**
	 * Creates a new vector containing [0, 0, 0, 1]
	 */
	public Vec4()
	{
		this( 0.0f, 0.0f, 0.0f, 1.0f );
	}
	

	/**
	 * Creates a new vector containing [value, value, value, value]
	 */
	public Vec4( float value )
	{
		this( value, value, value, value );
	}
	
	
	/**
	 * Creates a new vector containing [vec.x, vec.y, 0, 0]
	 */
	public Vec4( Vec2 vec )
	{
		this( vec.x, vec.y, 0.0f, 0.0f );
	}
	

	/**
	 * Creates a new vector containing [vec.x, vec.y, z, w]
	 */
	public Vec4( Vec2 vec, float z, float w )
	{
		this( vec.x, vec.y, z, w );
	}
	

	/**
	 * Creates a new vector containing [vec.x, vec.y, vec.z, 0]
	 */
	public Vec4( Vec3 vec )
	{
		this( vec.x, vec.y, vec.z, 0.0f );
	}
	
	
	/**
	 * Creates a new vector containing [vec.x, vec.y, vec.z, w]
	 */
	public Vec4( Vec3 vec, float w )
	{
		this( vec.x, vec.y, vec.z, w );
	}
	
	
	public Vec4( Vec4 vec )
	{
		this( vec.x, vec.y, vec.z, vec.w );
	}
	

	/**
	 * Creates a new vector containing [1, 0, 0, 0]
	 */
	public static Vec4 xAxis()
	{
		return new Vec4( 1.0f, 0.0f, 0.0f, 0.0f );
	}
	

	/**
	 * Creates a new vector containing [0, 1, 0, 0]
	 */
	public static Vec4 yAxis()
	{
		return new Vec4( 0.0f, 1.0f, 0.0f, 0.0f );
	}
	

	/**
	 * Creates a new vector containing [0, 0, 1, 0]
	 */
	public static Vec4 zAxis()
	{
		return new Vec4( 0.0f, 0.0f, 1.0f, 0.0f );
	}
	
	
	public Vec4 add( Vec4 vec )
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		this.w += vec.w;
		
		return this;
	}
	
	
	public static Vec4 add( Vec4 left, Vec4 right )
	{
		return new Vec4( left.x + right.x,
						 left.y + right.y,
						 left.z + right.z,
						 left.w + right.w );
	}
	
	
	public Vec4 sub( Vec4 right )
	{
		this.x -= right.x;
		this.y -= right.y;
		this.z -= right.z;
		this.w -= right.w;
		
		return this;
	}
	
	
	public static Vec4 sub( Vec4 left, Vec4 right )
	{
		return new Vec4( left.x - right.x,
						 left.y - right.y,
						 left.z - right.z,
						 left.w - right.w );
	}
	
	
	public Vec4 mul( float value )
	{
		this.x *= value;
		this.y *= value;
		this.z *= value;
		this.w *= value;
		
		return this;
	}
	
	
	public static Vec4 mul( Vec4 vec, float value )
	{
		return new Vec4( vec.x * value,
						 vec.y * value,
						 vec.z * value,
						 vec.w * value );
	}
	
	
	/**
	 * Does component wise multiplication.</br>
	 * result = [x1*x2, y1*y2, z1*z2, w1*w2]
	 */
	public Vec4 mul( Vec4 vec )
	{
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		this.w *= vec.w;
		
		return this;
	}
	

	/**
	 * Does component wise multiplication.</br>
	 * result = [x1*x2, y1*y2, z1*z2, w1*w2]
	 */
	public static Vec4 mul( Vec4 left, Vec4 right )
	{
		return new Vec4( left.x * right.x,
						 left.y * right.y,
						 left.z * right.z,
						 left.w * right.w );
	}
	
	/*
	public Vec4 transform( Mat3 mat )
	{
		//TODO: implement
		return new Vec4();
	}
	
	
	public static Vec4 transform( Vec4 vec, Mat3 mat )
	{
		//TODO: implement
		return new Vec4();
	}
	*/
	
	/**
	 * transforms the vector v by a given matrix M</br>
	 * v = M * v
	 */
	public Vec4 transform( Mat4 mat )
	{
		float x = this.x;
		float y = this.y;
		float z = this.z;
		float w = this.w;

		this.x = mat.m00 * x + mat.m01 * y + mat.m02 * z + mat.m03 * w;
		this.y = mat.m10 * x + mat.m11 * y + mat.m12 * z + mat.m13 * w;
		this.z = mat.m20 * x + mat.m21 * y + mat.m22 * z + mat.m23 * w;
		this.w = mat.m30 * x + mat.m31 * y + mat.m32 * z + mat.m33 * w;
		
		return this;
	}
	

	
	/**
	 * transforms a vector v by a given matrix M</br>
	 * v' = M * v
	 */
	public static Vec4 transform( Vec4 vec, Mat4 mat )
	{
		return new Vec4( mat.m00 * vec.x + mat.m01 * vec.y + mat.m02 * vec.z + mat.m03 * vec.w,
						 mat.m10 * vec.x + mat.m11 * vec.y + mat.m12 * vec.z + mat.m13 * vec.w,
						 mat.m20 * vec.x + mat.m21 * vec.y + mat.m22 * vec.z + mat.m23 * vec.w,
						 mat.m30 * vec.x + mat.m31 * vec.y + mat.m32 * vec.z + mat.m33 * vec.w );
	}
	
	
	public static float dot( Vec4 left, Vec4 right )
	{
		return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
	}

	
	public static float length( Vec4 vec )
	{
		return (float) Math.sqrt( dot(vec, vec) );
	}
	
	
	public Vec4 normalize()
	{
		float lengthInv = 1.0f / length( this );
		
		this.x *= lengthInv;
		this.y *= lengthInv;
		this.z *= lengthInv;
		this.w *= lengthInv;
		
		return this;
	}
	
	
	public static Vec4 normalize( Vec4 vec )
	{
		Vec4 result = new Vec4( vec );
		
		return result.normalize();
	}
	
	
	/**
	 * Computes the cross product by only using the xyz-components of the given vectors.
	 */
	public static Vec4 cross( Vec4 left, Vec4 right )
	{
		return new Vec4( left.y * right.z - left.z * right.y,
						 left.z * right.x - left.x * right.z,
						 left.x * right.y - left.y * right.x,
						 0.0f );
	}
	
	
	/**
	 * Returns a vector containing the component-wise minima</br></br>
	 * v1 = [0, 5, 1,  1]</br>
	 * v2 = [3, 0, 2, -2]</br></br>
	 * v1.min( v2 ) = [0, 0, 1, -2]
	 */
	public Vec4 min( Vec4 vec )
	{
		this.x = this.x < vec.x ? this.x : vec.x;
		this.y = this.y < vec.y ? this.y : vec.y;
		this.z = this.z < vec.z ? this.z : vec.z;
		this.w = this.w < vec.w ? this.w : vec.w;
		
		return this;
	}
	
	
	/**
	 * Returns a new vector containing the component-wise minima</br></br>
	 * v1 = [0, 5, 1,  1]</br>
	 * v2 = [3, 0, 2, -2]</br></br>
	 * min( v1, v2 ) = [0, 0, 1, -2]
	 */
	public static Vec4 min( Vec4 left, Vec4 right )
	{
		return new Vec4( left.x < right.x ? left.x : right.x,
						 left.y < right.y ? left.y : right.y,
						 left.z < right.z ? left.z : right.z ,
						 left.w < right.w ? left.w : right.w );
	}
	
	
	/**
	 * Returns a vector containing the component-wise maxima</br></br>
	 * v1 = [0, 5, 1,  1]</br>
	 * v2 = [3, 0, 2, -2]</br></br>
	 * v1.max( v2 ) = [3, 5, 2, 1]
	 */
	public Vec4 max( Vec4 vec )
	{
		this.x = this.x > vec.x ? this.x : vec.x;
		this.y = this.y > vec.y ? this.y : vec.y;
		this.z = this.z > vec.z ? this.z : vec.z;
		this.w = this.w > vec.w ? this.w : vec.w;
		
		return this;
	}
	
	
	/**
	 * Returns a new vector containing the component-wise maxima</br>
	 * v1 = [0, 5, 1,  1]</br>
	 * v2 = [3, 0, 2, -2]</br></br>
	 * max( v1, v2 ) = [3, 5, 2, 1]
	 */
	public static Vec4 max( Vec4 left, Vec4 right )
	{
		return new Vec4( left.x > right.x ? left.x : right.x,
				 		 left.y > right.y ? left.y : right.y,
						 left.z > right.z ? left.z : right.z,
						 left.w > right.w ? left.w : right.w );
	}
	
	
	/**
	 * Clamps each component to the given min/max values.
	 */
	public Vec4 clamp( Vec4 min, Vec4 max )
	{
		this.x = MathUtil.clamp( this.x, min.x, max.x );
		this.y = MathUtil.clamp( this.y, min.y, max.y );
		this.z = MathUtil.clamp( this.z, min.z, max.z );
		this.w = MathUtil.clamp( this.w, min.w, max.w );
		
		return this;
	}
	
	
	/**
	 * Returns a new vector, with each component clamped to the given min/max values.
	 */
	public static Vec4 clamp( Vec4 vec, Vec4 min, Vec4 max )
	{
		return new Vec4( MathUtil.clamp( vec.x, min.x, max.x ),
						 MathUtil.clamp( vec.y, min.y, max.y ),
						 MathUtil.clamp( vec.z, min.z, max.z ),
						 MathUtil.clamp( vec.w, min.w, max.w ) );
	}
	
	
	/**
	 * Discards the sign of each component.
	 */
	public Vec4 abs()
	{
		this.x = Math.abs( this.x );
		this.y = Math.abs( this.y );
		this.z = Math.abs( this.z );
		this.w = Math.abs( this.w );
		
		return this;
	}
	
	
	/**
	 * Returns a new vector, with each component containing the absolute values
	 */
	public static Vec4 abs( Vec4 vec )
	{
		return new Vec4( Math.abs(vec.x), 
						 Math.abs(vec.y), 
						 Math.abs(vec.z), 
						 Math.abs(vec.w) );
	}
	
	
	/**
	 * Linear, component-wise interpolation between the two vectors.
	 * @Returns alpha*this + (1-alpha) * vec
	 */
	public Vec4 mix( Vec4 vec, float alpha )
	{
		float alphaDiff = 1.0f - alpha;

		this.x = alpha * vec.x + alphaDiff * this.x;
		this.y = alpha * vec.y + alphaDiff * this.y;
		this.z = alpha * vec.z + alphaDiff * this.z;
		this.w = alpha * vec.w + alphaDiff * this.w;
		
		return this;
	}
	
	
	/**
	 * Linear, component-wise interpolation between the two vectors.
	 * @Returns alpha*left + (1-alpha) * right
	 */
	public static Vec4 mix( Vec4 left, Vec4 right, float alpha )
	{
		Vec4 result = new Vec4( left );
		
		return result.mix( right, alpha );
	}
	
	
	/**
	 * Component-wise linear interpolation between the two vectors.
	 * @Returns alpha*this + (1-alpha) * vec
	 */
	public Vec4 mix( Vec4 vec, Vec4 alpha )
	{
		this.x = alpha.x * vec.x + (1.0f - alpha.x) * this.x;
		this.y = alpha.y * vec.y + (1.0f - alpha.y) * this.y;
		this.z = alpha.z * vec.z + (1.0f - alpha.z) * this.z;
		this.w = alpha.w * vec.w + (1.0f - alpha.w) * this.w;
		
		return this;
	}
	
	
	/**
	 * Component-wise linear interpolation between the two vectors.
	 * @Returns alpha*left + (1-alpha) * right
	 */
	public static Vec4 mix( Vec4 left, Vec4 right, Vec4 alpha )
	{
		Vec4 result = new Vec4( left );
		
		return result.mix( right, alpha );
	}
	
	
	public String toString()
	{
		return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
	}
	
	
	public static void runTest()
	{
		Vec4 first  = new Vec4( 1.0f, 0.0f,  0.0f, 0.0f );
		Vec4 second = new Vec4( 0.0f, 0.0f, -1.0f, 0.0f );
		
		System.out.println();
		System.out.println( "first:  " + first );
		System.out.println( "second: " + second );
		System.out.println( "first + second: " + add(first, second) );
		System.out.println( "second - first: " + sub(second, first) );
		System.out.println( "max: " + max(first, second) );
		System.out.println( "min: " + min(first, second) );
		System.out.println( "length(second):   " + length(second) );
		System.out.println( "cross(first, second): " + cross(first, second) );
		System.out.println( "abs(first): " + abs(first) );
		System.out.println( "dot(first, second): " + dot(first, second) );
	}
}
