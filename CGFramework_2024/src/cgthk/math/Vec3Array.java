package cgthk.math;

import static cgthk.math.MathUtil.clamp;

import java.nio.FloatBuffer;
import java.util.Arrays;
import org.lwjgl.BufferUtils;



public final class Vec3Array
{
	private float[] values;
	private int     length;
	
	
	public Vec3Array( int capacity )
	{
		this.length = 0;
		this.values = new float[capacity * 3];
	}
	
	
	public Vec3Array( Vec3Array vec3Array )
	{
		this.length = vec3Array.length;
		this.values = Arrays.copyOf( vec3Array.values, vec3Array.values.length );
	}
	
	
	public Vec3Array( float[] values )
	{
		this.length = 0;
		
		if( values == null )
		{
			return;
		}
		
		if( values.length % 3 != 0 )
		{
			System.err.println( "Array length is not a multiple of 3!" );
			return;
		}
		
		this.length = values.length / 3;
		this.values = Arrays.copyOf( values, values.length );
	}
	
	
	public Vec3Array( Vec3 vec, int count )
	{
		this.length = count;
		this.values = new float[count * 3];
		
		if( vec != null )
			this.set( vec );
	}
	
	
	public Vec3Array( Vec3[] vectors )
	{
		this.length = vectors.length;
		this.values = new float[this.length * 3];
		
		int index = 0;
		
		for( int i = 0; i < vectors.length; ++i )
		{
			index = i * 3;
			this.values[index    ] = vectors[i].x;
			this.values[index + 1] = vectors[i].y;
			this.values[index + 2] = vectors[i].z;
		}
	}
	
	
	public Vec3Array( Vec4[] vectors )
	{
		this.length = vectors.length;
		this.values = new float[this.length * 3];
		
		int index = 0;
		
		for( int i = 0; i < vectors.length; ++i )
		{
			index = i * 3;
			this.values[index    ] = vectors[i].x;
			this.values[index + 1] = vectors[i].y;
			this.values[index + 2] = vectors[i].z;
		}
	}
	
	
	public int length()
	{
		return this.length;
	}
	
	
	public void setLength( int newLength )
	{
		this.length = newLength;
	}
	
	
	public int capacity()
	{
		return this.values.length / 3;
	}
	
	
	public float[] data()
	{
		return this.values;
	}
	
	
	public void resize( int size )
	{
		this.values = Arrays.copyOf( this.values, size * 3 );
	}
	
	
	public void clear()
	{
		Arrays.fill( this.values, 0.0f );
		this.length = 0;
	}
	
	
	public void push_back( float x, float y, float z )
	{
		int index = this.length * 3;
		
		this.values[index    ] = x;
		this.values[index + 1] = y;
		this.values[index + 2] = z;
		
		this.length++;
	}
	
	
	public void push_back( Vec3 vec )
	{
		int index = this.length * 3;
		
		this.values[index    ] = vec.x;
		this.values[index + 1] = vec.y;
		this.values[index + 2] = vec.z;
		
		this.length++;
	}
	
	
	public void push_back( Vec4 vec )
	{
		int index = this.length * 3;
		
		this.values[index    ] = vec.x;
		this.values[index + 1] = vec.y;
		this.values[index + 2] = vec.z;
		
		this.length++;
	}
	
	
	public void pop_back()
	{
		this.length--;
	}
	
	
	public void pop_back( int count )
	{
		this.length -= clamp( count, 0, this.length );
	}
	
	
	public void swap( int first, int second )
	{
		float temp = 0.0f;
		int   i1   = first * 3;
		int   i2   = second * 3;
		
		temp = this.values[i1];
		this.values[i1] = this.values[i2];
		this.values[i2] = temp;
		
		temp = this.values[i1 + 1];
		this.values[i1 + 1] = this.values[i2 + 1];
		this.values[i2 + 1] = temp;
		
		temp = this.values[i1 + 2];
		this.values[i1 + 2] = this.values[i2 + 2];
		this.values[i2 + 2] = temp;
	}
	
	
	public Vec3 at( int index )
	{
		int i = index * 3;
		return new Vec3( this.values[i   ],
						 this.values[i + 1],
						 this.values[i + 2] );
	}
	
	
	public Vec4 at( int index, float w )
	{
		int i = index * 3;
		return new Vec4( this.values[i   ],
						 this.values[i + 1],
						 this.values[i + 2],
						 w );
	}
	
	
	public void set( float value )
	{
		Arrays.fill( this.values, value );
	}
	
	
	public void set( int index, Vec3 vec )
	{
		int i = index * 3;
		
		this.values[i    ] = vec.x;
		this.values[i + 1] = vec.y;
		this.values[i + 2] = vec.z;
	}
	
	
	public void set( int index, float x, float y, float z )
	{
		int i = index * 3;
		
		this.values[i    ] = x;
		this.values[i + 1] = y;
		this.values[i + 2] = z;
	}
	
	
	public void set( Vec3 vec )
	{
		for( int i = 0; i < this.values.length; i += 3 )
		{
			this.values[i    ] = vec.x;
			this.values[i + 1] = vec.y;
			this.values[i + 2] = vec.z;
		}
	}
	
	
	public void set( Vec3Array vecArray )
	{
		this.length = vecArray.length;
		this.values = Arrays.copyOf( vecArray.values, vecArray.values.length );
	}
	
	
	public void set( Vec3Array vecArray, int range, int startIndex )
	{
		int start = startIndex * 3;
		int end   = start + (range * 3);
		
		this.length = range;
		this.values = Arrays.copyOfRange( vecArray.values, start, end );
	}
	
	
	public void add( int index, Vec3 vec )
	{
		int i = index * 3;
		this.values[i    ] += vec.x;
		this.values[i + 1] += vec.y;
		this.values[i + 2] += vec.z;
	}
	
	
	public void add( Vec3 vec )
	{
		for( int i = 0; i < this.values.length; i += 3 )
		{
			this.values[i    ] += vec.x;
			this.values[i + 1] += vec.y;
			this.values[i + 2] += vec.z;
		}
	}
	
	
	public void add( Vec3Array rightArray, int range, int startIndexLeft, int startIndexRight )
	{
		int offset = startIndexRight - startIndexLeft;
		int iLeft  = startIndexLeft * 3;
		int iRight = iLeft + (3 * offset);
		int end    = iLeft + (3 * range);

		for( ; iLeft < end; iLeft += 3, iRight += 3 )
		{
			this.values[iLeft    ] += rightArray.values[iRight    ];
			this.values[iLeft + 1] += rightArray.values[iRight + 1];
			this.values[iLeft + 2] += rightArray.values[iRight + 2];
		}
	}
	
	
	public static Vec3Array add( Vec3Array leftArray, Vec3Array rightArray, int range, int startIndexLeft, int startIndexRight )
	{
		Vec3Array result = new Vec3Array( 0 );
		result.set( leftArray, range, startIndexLeft );
		result.add( rightArray, range, 0, startIndexRight );
		
		return result;
	}
	
	
	public void sub( int index, Vec3 vec )
	{
		int i = index * 3;
		this.values[i    ] -= vec.x;
		this.values[i + 1] -= vec.y;
		this.values[i + 2] -= vec.z;
	}
	
	
	public void sub( Vec3 vec )
	{
		for( int i = 0; i < this.values.length; i += 3 )
		{
			this.values[i    ] -= vec.x;
			this.values[i + 1] -= vec.y;
			this.values[i + 2] -= vec.z;
		}
	}
	
	
	public void sub( Vec3Array rightArray, int range, int startIndexLeft, int startIndexRight )
	{
		int offset = startIndexRight - startIndexLeft;
		int iLeft  = startIndexLeft * 3;
		int iRight = iLeft + (3 * offset);
		int end    = iLeft + (3 * range);

		for( ; iLeft < end; iLeft += 3, iRight += 3 )
		{
			this.values[iLeft    ] -= rightArray.values[iRight    ];
			this.values[iLeft + 1] -= rightArray.values[iRight + 1];
			this.values[iLeft + 2] -= rightArray.values[iRight + 2];
		}
	}
	
	
	public static Vec3Array sub( Vec3Array leftArray, Vec3Array rightArray, int range, int startIndexLeft, int startIndexRight )
	{
		Vec3Array result = new Vec3Array( 0 );
		result.set( leftArray, range, startIndexLeft );
		result.sub( rightArray, range, 0, startIndexRight );
		
		return result;
	}
	
	
	public void mul( float value )
	{
		for( int i = 0; i < this.values.length; i += 3 )
		{
			this.values[i    ] *= value;
			this.values[i + 1] *= value;
			this.values[i + 2] *= value;
		}
	}
	
	
	public void mul( int index, float value )
	{
		int i = index * 3;
		this.values[i    ] *= value;
		this.values[i + 1] *= value;
		this.values[i + 2] *= value;
	}
	
	
	public void mul( Vec3 vec )
	{
		for( int i = 0; i < this.values.length; i += 3 )
		{
			this.values[i    ] *= vec.x;
			this.values[i + 1] *= vec.y;
			this.values[i + 2] *= vec.z;
		}
	}
	
	
	public void mul( int index, Vec3 vec )
	{
		int i = index * 3;
		this.values[i    ] *= vec.x;
		this.values[i + 1] *= vec.y;
		this.values[i + 2] *= vec.z;
	}
	
	
	public void mul( Vec3Array rightArray, int range, int startIndexLeft, int startIndexRight )
	{
		int offset = startIndexRight - startIndexLeft;
		int iLeft  = startIndexLeft * 3;
		int iRight = iLeft + (3 * offset);
		int end    = iLeft + (3 * range);

		for( ; iLeft < end; iLeft += 3, iRight += 3 )
		{
			this.values[iLeft    ] *= rightArray.values[iRight];
			this.values[iLeft + 1] *= rightArray.values[iRight + 1];
			this.values[iLeft + 2] *= rightArray.values[iRight + 2];
		}
	}
	
	
	public static Vec3Array mul( Vec3Array array, float value )
	{
		Vec3Array result = new Vec3Array( 0 );
		result.set( array, array.length, 0 );
		result.mul( value );
		
		return result;
	}
	
	
	public void normalize()
	{
		for( int i = 0; i < this.values.length; i += 3 )
		{
			float lengthSquared = this.values[i]     * this.values[i]     + 
								  this.values[i + 1] * this.values[i + 1] + 
								  this.values[i + 2] * this.values[i + 2];
			
			float invLength = (float) ( 1.0 / Math.sqrt(lengthSquared) );

			this.values[i    ] *= invLength;
			this.values[i + 1] *= invLength;
			this.values[i + 2] *= invLength;
		}
	}
	
	
	public static Vec3Array cross( Vec3Array leftArray, Vec3Array rightArray, int range, int startIndexLeft, int startIndexRight )
	{
		Vec3Array result = new Vec3Array( range );
		
		int offset = startIndexRight - startIndexLeft;
		int iLeft  = startIndexLeft * 3;
		int iRight = iLeft + (3 * offset);

		float leftX,  leftY,  leftZ;
		float rightX, rightY, rightZ;

		for( int i = 0; i < range; i += 3, iLeft += 3, iRight += 3 )
		{
			leftX = leftArray.values[iLeft + 0];
			leftY = leftArray.values[iLeft + 1];
			leftZ = leftArray.values[iLeft + 2];
			
			rightX = rightArray.values[iRight + 0];
			rightY = rightArray.values[iRight + 1];
			rightZ = rightArray.values[iRight + 2];
			
			result.values[i + 0] = leftY * rightZ - leftZ * rightY;
			result.values[i + 1] = leftZ * rightX - leftX * rightZ;
			result.values[i + 2] = leftX * rightY - leftY * rightX;
		}
		
		return result;
	}
	
	
	public FloatBuffer toFloatBuffer()
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer( this.values.length );
		buffer.put( this.values );
		buffer.flip();
		
		return buffer;
	}
	
	
	public FloatBuffer toFloatBuffer( FloatBuffer buffer )
	{
		assert buffer.capacity() == this.values.length : "FloatBuffer should have a capacity of " + this.values.length + " but has " + buffer.capacity();
		buffer.put( this.values );
		buffer.flip();
		
		return buffer;
	}
	
	
	public String toString()
	{
		String result = "";
		for( int i = 0; i < this.values.length; i += 3 )
			result += "[" + this.values[i] + " " + this.values[i+1] + " " + this.values[i+2] + "], ";
		
		return result.substring( 0, result.length()-2 );
	}
	
	
}
