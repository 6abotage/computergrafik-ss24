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



public final class Vec2
{
	public float x;
	public float y;
	
	
	public Vec2( float x, float y )
	{
		this.x = x;
		this.y = y;
	}
	

	/**
	 * Creates a new vector containing [0, 0]
	 */
	public Vec2()
	{
		this( 0.0f, 0.0f );
	}
	

	/**
	 * Creates a new vector containing [value, value]
	 */
	public Vec2( float value )
	{
		this( value, value );
	}
	
	/**
	 * Creates a new vector containing [vec.x, vec.y]
	 */
	public Vec2( Vec2 vec )
	{
		this( vec.x, vec.y );
	}
	

	/**
	 * Creates a new vector containing [vec.x, vec.y]
	 */
	public Vec2( Vec3 vec )
	{
		this( vec.x, vec.y );
	}
	

	/**
	 * Creates a new vector containing [vec.x, vec.y]
	 */
	public Vec2( Vec4 vec )
	{
		this( vec.x, vec.y );
	}
	
	
	public Vec2 add( Vec2 right )
	{
		this.x += right.x;
		this.y += right.y;
		
		return this;
	}
	
	
	public static Vec2 add( Vec2 left, Vec2 right )
	{
		return new Vec2( left.x + right.x,
						 left.y + right.y );
	}
	
	
	public Vec2 sub( Vec2 right )
	{
		this.x -= right.x;
		this.y -= right.y;
		
		return this;
	}
	
	
	public static Vec2 sub( Vec2 left, Vec2 right )
	{
		return new Vec2( left.x - right.x,
						 left.y - right.y );
	}
	
	
	public Vec2 mul( float value )
	{
		this.x *= value;
		this.y *= value;
		
		return this;
	}
	
	
	public static Vec2 mul( Vec2 vec, float value )
	{
		return new Vec2( vec.x * value,
						 vec.y * value );
	}
	

	/**
	 * Does component wise multiplication.</br>
	 * result = [x1*x2, y1*y2]
	 */
	public Vec2 mul( Vec2 vec )
	{
		this.x *= vec.x;
		this.y *= vec.y;
		
		return this;
	}
	

	/**
	 * Does component wise multiplication.</br>
	 * result = [x1*x2, y1*y2]
	 */
	public static Vec2 mul( Vec2 left, Vec2 right )
	{
		return new Vec2( left.x * right.x,
						 left.y * right.y );
	}
	
	
	public static float dot( Vec2 left, Vec2 right )
	{
		return left.x * right.x + left.y * right.y;
	}

	
	public static float length( Vec2 vec )
	{
		return (float) Math.sqrt( dot(vec, vec) );
	}
	
	
	public Vec2 normalize()
	{
		float lengthInv = 1.0f / length( this );
		
		this.x *= lengthInv;
		this.y *= lengthInv;
		
		return this;
	}
	
	
	public static Vec2 normalize( Vec2 vec )
	{
		Vec2 result = new Vec2( vec );
		
		return result.normalize();
	}
	
	
	/**
	 * Returns a vector containing the component-wise minima</br></br>
	 * v1 = [0, 5]</br>
	 * v2 = [3, 0]</br></br>
	 * v1.min( v2 ) = [0, 0]
	 */
	public Vec2 min( Vec2 vec )
	{
		this.x = this.x < vec.x ? this.x : vec.x;
		this.y = this.y < vec.y ? this.y : vec.y;
		
		return this;
	}
	

	/**
	 * Returns a new vector containing the component-wise minima</br></br>
	 * v1 = [0, 5]</br>
	 * v2 = [3, 0]</br></br>
	 * min( v1, v2 ) = [0, 0]
	 */
	public static Vec2 min( Vec2 left, Vec2 right )
	{
		return new Vec2( left.x < right.x ? left.x : right.x,
						 left.y < right.y ? left.y : right.y );
	}
	

	/**
	 * Returns a vector containing the component-wise maxima</br></br>
	 * v1 = [0, 5]</br>
	 * v2 = [3, 0]</br></br>
	 * v1.max( v2 ) = [3, 5]
	 */
	public Vec2 max( Vec2 vec )
	{
		this.x = this.x > vec.x ? this.x : vec.x;
		this.y = this.y > vec.y ? this.y : vec.y;
		
		return this;
	}
	

	/**
	 * Returns a new vector containing the component-wise maxima</br>
	 * v1 = [0, 5]</br>
	 * v2 = [3, 0]</br></br>
	 * max( v1, v2 ) = [3, 5]
	 */
	public static Vec2 max( Vec2 left, Vec2 right )
	{
		return new Vec2( left.x > right.x ? left.x : right.x,
						 left.y > right.y ? left.y : right.y );
	}
	

	/**
	 * Clamps each component to the given min/max values.
	 */
	public Vec2 clamp( Vec2 min, Vec2 max )
	{
		this.x = MathUtil.clamp( this.x, min.x, max.x );
		this.y = MathUtil.clamp( this.y, min.y, max.y );
		
		return this;
	}
	

	/**
	 * Returns a new vector, with each component clamped to the given min/max values.
	 */
	public static Vec2 clamp( Vec2 vec, Vec2 min, Vec2 max )
	{
		return new Vec2( MathUtil.clamp( vec.x, min.x, max.x ),
						 MathUtil.clamp( vec.y, min.y, max.y ) );
	}
	

	/**
	 * Discards the sign of each component.
	 */
	public Vec2 abs()
	{
		this.x = Math.abs( this.x );
		this.y = Math.abs( this.y );
		
		return this;
	}
	

	/**
	 * Returns a new vector, with each component containing the absolute values
	 */
	public static Vec2 abs( Vec2 vec )
	{
		return new Vec2( Math.abs(vec.x), 
						 Math.abs(vec.y) );
	}
	

	/**
	 * Linear, component-wise interpolation between the two vectors.
	 * @Returns alpha*this + (1-alpha) * vec
	 */
	public Vec2 mix( Vec2 vec, float alpha )
	{
		float alphaDiff = 1.0f - alpha;

		this.x = alpha * vec.x + alphaDiff * this.x;
		this.y = alpha * vec.y + alphaDiff * this.y;
		
		return this;
	}
	

	/**
	 * Linear, component-wise interpolation between the two vectors.
	 * @Returns alpha*left + (1-alpha) * right
	 */
	public static Vec2 mix( Vec2 left, Vec2 right, float alpha )
	{
		Vec2 result = new Vec2( left );
		
		return result.mix( right, alpha );
	}
	

	/**
	 * Component-wise linear interpolation between the two vectors.
	 * @Returns alpha*this + (1-alpha) * vec
	 */
	public Vec2 mix( Vec2 vec, Vec2 alpha )
	{
		this.x = alpha.x * vec.x + (1.0f - alpha.x) * this.x;
		this.y = alpha.y * vec.y + (1.0f - alpha.y) * this.y;
		
		return this;
	}
	

	/**
	 * Component-wise linear interpolation between the two vectors.
	 * @Returns alpha*left + (1-alpha) * right
	 */
	public static Vec2 mix( Vec2 left, Vec2 right, Vec2 alpha )
	{
		Vec2 result = new Vec2( left );
		
		return result.mix( right, alpha );
	}
	
	
	public String toString()
	{
		return "[" + this.x + ", " + this.y + "]";
	}
	
	
	@Override
    public boolean equals( Object obj )
	{
        if( obj == this )
            return true;
        
        if( obj == null || obj.getClass() != this.getClass() )
            return false;

        Vec2 vec = (Vec2) obj;
        return this.x == vec.x
            && ( this.y == vec.y );
    }

}
