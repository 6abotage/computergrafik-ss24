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



public final class Vec3
{
	public float x;
	public float y;
	public float z;
	
	
	public Vec3( float x, float y, float z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/**
	 * Creates a new vector containing [0, 0, 0]
	 */
	public Vec3()
	{
		this( 0.0f, 0.0f, 0.0f );
	}
	
	
	/**
	 * Creates a new vector containing [value, value, value]
	 */
	public Vec3( float value )
	{
		this( value, value, value );
	}
	
	
	/**
	 * Creates a new vector containing [vec.x, vec.y, 0]
	 */
	public Vec3( Vec2 vec )
	{
		this( vec.x, vec.y, 0.0f );
	}
	
	
	/**
	 * Creates a new vector containing [vec.x, vec.y, z]
	 */
	public Vec3( Vec2 vec, float z )
	{
		this( vec.x, vec.y, z );
	}
	
	
	public Vec3( Vec3 vec )
	{
		this( vec.x, vec.y, vec.z );
	}
	
	
	/**
	 * Creates a new vector containing [vec.x, vec.y, vec.z]
	 */
	public Vec3( Vec4 vec )
	{
		this( vec.x, vec.y, vec.z );
	}
	
	
	/**
	 * Creates a new vector containing [1, 0, 0]
	 */
	public static Vec3 xAxis()
	{
		return new Vec3( 1.0f, 0.0f, 0.0f );
	}
	

	/**
	 * Creates a new vector containing [0, 1, 0]
	 */
	public static Vec3 yAxis()
	{
		return new Vec3( 0.0f, 1.0f, 0.0f );
	}
	

	/**
	 * Creates a new vector containing [0, 0, 1]
	 */
	public static Vec3 zAxis()
	{
		return new Vec3( 0.0f, 0.0f, 1.0f );
	}
	
	
	public Vec3 add( Vec3 vec )
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		
		return this;
	}
	
	
	public static Vec3 add( Vec3 left, Vec3 right )
	{
		return new Vec3( left.x + right.x,
						 left.y + right.y,
						 left.z + right.z );
	}
	
	
	public Vec3 sub( Vec3 right )
	{
		this.x -= right.x;
		this.y -= right.y;
		this.z -= right.z;
		
		return this;
	}
	
	
	public static Vec3 sub( Vec3 left, Vec3 right )
	{
		return new Vec3( left.x - right.x,
						 left.y - right.y,
						 left.z - right.z );
	}
	
	
	public Vec3 mul( float value )
	{
		this.x *= value;
		this.y *= value;
		this.z *= value;
		
		return this;
	}
	
	
	public static Vec3 mul( Vec3 vec, float value )
	{
		return new Vec3( vec.x * value,
						 vec.y * value,
						 vec.z * value );
	}
	
	
	/**
	 * Does component wise multiplication.</br>
	 * result = [x1*x2, y1*y2, z1*z2]
	 */
	public Vec3 mul( Vec3 vec )
	{
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		
		return this;
	}
	
	
	/**
	 * Does component wise multiplication.</br>
	 * result = [x1*x2, y1*y2, z1*z2, w1*w2]
	 */
	public static Vec3 mul( Vec3 left, Vec3 right )
	{
		return new Vec3( left.x * right.x,
						 left.y * right.y,
						 left.z * right.z );
	}
	
	

	/**
	 * transforms the vector v by a given matrix M</br>
	 * v = M * v
	 */
	public Vec3 transform( Mat3 mat )
	{
		float x = this.x;
		float y = this.y;
		float z = this.z;

		this.x = mat.m00 * x + mat.m01 * y + mat.m02 * z;
		this.y = mat.m10 * x + mat.m11 * y + mat.m12 * z;
		this.z = mat.m20 * x + mat.m21 * y + mat.m22 * z;
		
		return this;
	}
	
	
	/**
	 * transforms a vector v by a given matrix M</br>
	 * v' = M * v
	 */
	public static Vec3 transform( Vec3 vec, Mat3 mat )
	{
		return new Vec3( mat.m00 * vec.x + mat.m01 * vec.y + mat.m02 * vec.z,
						 mat.m10 * vec.x + mat.m11 * vec.y + mat.m12 * vec.z,
						 mat.m20 * vec.x + mat.m21 * vec.y + mat.m22 * vec.z );
	}
	
	
	/**
	 * transforms the vector v by a given matrix M</br>
	 * v = M * v
	 */
	public Vec3 transform( float w, Mat4 mat )
	{
		float x = this.x;
		float y = this.y;
		float z = this.z;

		this.x = mat.m00 * x + mat.m01 * y + mat.m02 * z + mat.m03 * w;
		this.y = mat.m10 * x + mat.m11 * y + mat.m12 * z + mat.m13 * w;
		this.z = mat.m20 * x + mat.m21 * y + mat.m22 * z + mat.m23 * w;
		
		return this;
	}
	
	
	/**
	 * transforms a vector v by a given matrix M</br>
	 * v' = M * v
	 */
	public static Vec3 transform( Vec3 vec, float w, Mat4 mat )
	{
		return new Vec3( mat.m00 * vec.x + mat.m01 * vec.y + mat.m02 * vec.z + mat.m03 * w,
						 mat.m10 * vec.x + mat.m11 * vec.y + mat.m12 * vec.z + mat.m13 * w,
						 mat.m20 * vec.x + mat.m21 * vec.y + mat.m22 * vec.z + mat.m23 * w );
	}
	
	
	public static float dot( Vec3 left, Vec3 right )
	{
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}
	
	
	public static float length( Vec3 vec )
	{
		return (float) Math.sqrt( dot(vec,vec) );
	}
	
	public static float distance( Vec3 left, Vec3 right ){
		return (float)Math.sqrt( (left.x-right.x)*(left.x-right.x)+(left.y-right.y)*(left.y-right.y)+(left.z-right.z)*(left.z-right.z) );
	}
	
	public Vec3 normalize()
	{
		float lengthInv = 1.0f / length( this );
		
		this.x *= lengthInv;
		this.y *= lengthInv;
		this.z *= lengthInv;
		
		return this;
	}
	
	
	
	public static Vec3 normalize( Vec3 vec )
	{
		Vec3 result = new Vec3( vec );
		return result.normalize();
	}
	
	
	public static Vec3 cross( Vec3 left, Vec3 right )
	{
		return new Vec3( left.y * right.z - left.z * right.y,
						 left.z * right.x - left.x * right.z,
						 left.x * right.y - left.y * right.x );
	}
	
	
	/**
	 * Returns a vector containing the component-wise minima</br></br>
	 * v1 = [0, 5, 1]</br>
	 * v2 = [3, 0, 2]</br></br>
	 * v1.min( v2 ) = [0, 0, 1]
	 */
	public Vec3 min( Vec3 vec )
	{
		this.x = this.x < vec.x ? this.x : vec.x;
		this.y = this.y < vec.y ? this.y : vec.y;
		this.z = this.z < vec.z ? this.z : vec.z;
		
		return this;
	}
	
	
	/**
	 * Returns a new vector containing the component-wise minima</br></br>
	 * v1 = [0, 5, 1]</br>
	 * v2 = [3, 0, 2]</br></br>
	 * min( v1, v2 ) = [0, 0, 1]
	 */
	public static Vec3 min( Vec3 left, Vec3 right )
	{
		return new Vec3( left.x < right.x ? left.x : right.x,
						 left.y < right.y ? left.y : right.y,
						 left.z < right.z ? left.z : right.z );
	}
	
	
	/**
	 * Returns a vector containing the component-wise maxima</br></br>
	 * v1 = [0, 5, 1]</br>
	 * v2 = [3, 0, 2]</br></br>
	 * v1.max( v2 ) = [3, 5, 2]
	 */
	public Vec3 max( Vec3 vec )
	{
		this.x = this.x > vec.x ? this.x : vec.x;
		this.y = this.y > vec.y ? this.y : vec.y;
		this.z = this.z > vec.z ? this.z : vec.z;
		
		return this;
	}
	
	
	/**
	 * Returns a new vector containing the component-wise maxima</br>
	 * v1 = [0, 5, 1]</br>
	 * v2 = [3, 0, 2]</br></br>
	 * max( v1, v2 ) = [3, 5, 2]
	 */
	public static Vec3 max( Vec3 left, Vec3 right )
	{
		return new Vec3( left.x > right.x ? left.x : right.x,
						 left.y > right.y ? left.y : right.y,
						 left.z > right.z ? left.z : right.z );
	}
	
	
	/**
	 * Clamps each component to the given min/max values.
	 */
	public Vec3 clamp( Vec3 min, Vec3 max )
	{
		this.x = MathUtil.clamp( this.x, min.x, max.x );
		this.y = MathUtil.clamp( this.y, min.y, max.y );
		this.z = MathUtil.clamp( this.z, min.z, max.z );
		
		return this;
	}
	
	
	/**
	 * Returns a new vector, with each component clamped to the given min/max values.
	 */
	public static Vec3 clamp( Vec3 vec, Vec3 min, Vec3 max )
	{
		return new Vec3( MathUtil.clamp( vec.x, min.x, max.x ),
						 MathUtil.clamp( vec.y, min.y, max.y ),
						 MathUtil.clamp( vec.z, min.z, max.z ) );
	}
	
	
	/**
	 * Discards the sign of each component.
	 */
	public Vec3 abs()
	{
		this.x = Math.abs( this.x );
		this.y = Math.abs( this.y );
		this.z = Math.abs( this.z );
		
		return this;
	}
	
	
	/**
	 * Returns a new vector, with each component containing the absolute values
	 */
	public static Vec3 abs( Vec3 vec )
	{
		return new Vec3( Math.abs(vec.x), 
						 Math.abs(vec.y), 
						 Math.abs(vec.z) );
	}
	
	
	/**
	 * Linear, component-wise interpolation between the two vectors.
	 * @Returns alpha*this + (1-alpha) * vec
	 */
	public Vec3 mix( Vec3 vec, float alpha )
	{
		float alphaDiff = 1.0f - alpha;

		this.x = alpha * vec.x + alphaDiff * this.x;
		this.y = alpha * vec.y + alphaDiff * this.y;
		this.z = alpha * vec.z + alphaDiff * this.z;
		
		return this;
	}
	
	
	/**
	 * Linear, component-wise interpolation between the two vectors.
	 * @Returns alpha*left + (1-alpha) * right
	 */
	public static Vec3 mix( Vec3 left, Vec3 right, float alpha )
	{
		Vec3 result = new Vec3( left );
		
		return result.mix( right, alpha );
	}
	
	
	/**
	 * Component-wise linear interpolation between the two vectors.
	 * @Returns alpha*this + (1-alpha) * vec
	 */
	public Vec3 mix( Vec3 vec, Vec3 alpha )
	{
		this.x = alpha.x * vec.x + (1.0f - alpha.x) * this.x;
		this.y = alpha.y * vec.y + (1.0f - alpha.y) * this.y;
		this.z = alpha.z * vec.z + (1.0f - alpha.z) * this.z;
		
		return this;
	}
	
	
	/**
	 * Component-wise linear interpolation between the two vectors.
	 * @Returns alpha*left + (1-alpha) * right
	 */
	public static Vec3 mix( Vec3 left, Vec3 right, Vec3 alpha )
	{
		Vec3 result = new Vec3( left );
		
		return result.mix( right, alpha );
	}
	
		
	public String toString()
	{
		return "[" + this.x + ", " + this.y + ", " + this.z + "]";
	}
	
	
	@Override
    public boolean equals( Object obj )
	{
        if( obj == this )
            return true;
        
        if( obj == null || obj.getClass() != this.getClass() )
            return false;

        Vec3 vec = (Vec3) obj;
        return this.x == vec.x
            && ( this.y == vec.y )
            && ( this.z == vec.z );
    }
	
	
	public static void runTest()
	{
		Vec3 first  = new Vec3( 1.0f, 0.0f, 0.0f );
		Vec3 second = new Vec3( 0.0f,  0.0f, -1.0f );
		
		System.out.println();
		System.out.println( "first:  " + first );
		System.out.println( "second: " + second );
		System.out.println( "first + second: " + add(first, second) );
		System.out.println( "second - first: " + sub(second, first) );
		System.out.println( "max: " + max(first, second) );
		System.out.println( "min: " + min(first, second) );
		System.out.println( "cross(first, second): " + cross(first, second) );
		System.out.println( "abs(first): " + abs(first) );
		System.out.println( "dot(first, second): " + dot(first, second) );
	}
}
