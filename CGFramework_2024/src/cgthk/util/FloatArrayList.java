package cgthk.util;

import java.util.Arrays;



public class FloatArrayList
{
	private static final float GROWTH_FACTOR = 1.25f;
	
	private float[] m_fValues;
	private int     m_iSize;
	
	
	
	public FloatArrayList()
	{
		m_fValues = new float[1];
		m_iSize   = 0;
	}
	
	public FloatArrayList( int initialCapacity )
	{
		m_fValues = new float[initialCapacity];
		m_iSize   = 0;
	}
	
	
	public void add( float value )
	{
		if( m_iSize >= m_fValues.length )
		{
			int minCapacity = (int) (m_iSize * GROWTH_FACTOR);
			minCapacity = minCapacity > m_iSize ? minCapacity : m_iSize + 1;
			
			this.ensureCapacity( minCapacity );
		}
		
		m_fValues[m_iSize++] = value;
	}
	
	
	public void addAll( float[] values )
	{
		this.ensureCapacity( m_iSize + values.length );
		
		System.arraycopy( values, 0, m_fValues, m_iSize, values.length );
		
		m_iSize += values.length;
	}
	
	
	public void addAll( FloatArrayList floatArrayList )
	{
		this.ensureCapacity( m_iSize + floatArrayList.size() );
		
		System.arraycopy( floatArrayList.toArray(), 0, m_fValues, m_iSize, floatArrayList.size() );
		
		m_iSize += floatArrayList.size();
	}
	
	
	public void set( int index, float value )
	{
		m_fValues[index] = value;
	}
	
	
	public float get( int index )
	{
		return m_fValues[index];
	}
	
	
	public boolean contains( float value )
	{
		return this.indexOf( value ) != -1;
	}
	
	
	public void ensureCapacity( int minCapacity )
	{
		if( m_fValues.length < minCapacity)
			m_fValues = Arrays.copyOf( m_fValues, minCapacity );
	}
	
	
	public int indexOf( float value )
	{
		for( int i = 0; i < m_iSize; ++i )
		{
			if( m_fValues[i] == value )
				return i;
		}
		
		return -1;
	}
	
	
	public int lastIndexOf( float value )
	{
		for( int i = m_iSize-1; i >= 0; --i )
		{
			if( m_fValues[i] == value )
				return i;
		}
		
		return -1;
	}
	
	
	public float removeAt( int index )
	{
		float value = m_fValues[index];
		
		System.arraycopy( m_fValues, index + 1, m_fValues, index, m_iSize - index - 1 );
		
		m_iSize--;
		
		return value;
	}
	
	
	public boolean removeValue( int value )
	{
		int index = this.indexOf( value );
		
		if( index < 0 )
			return false;
		else
			this.removeAt( index );
		
		return true;
	}
	
	
	public boolean isEmpty()
	{
		return m_iSize == 0;
	}
	
	
	public int size()
	{
		return m_iSize;
	}
	
	
	public int capacity()
	{
		return m_fValues.length;
	}
	
	
	public float[] toArray()
	{
		return m_fValues;
	}
	
	
	public void trimToSize()
	{
		m_fValues = Arrays.copyOf( m_fValues, m_iSize );
	}
	
	
	public void clear()
	{
		m_iSize = 0;
	}
	
	
	public String toString()
	{
		if( m_iSize == 0 )
			return "[empty]";
		
		String string = "[";
		
		for( int i = 0; i < m_iSize-1; ++i )
			string += m_fValues[i] + ", ";
		
		string += m_fValues[m_iSize-1] + "]";
		
		return string;
	}
	
	
	public static void test()
	{
		System.out.println( "FloatArrayList growth factor: " + GROWTH_FACTOR );
		
		FloatArrayList list = new FloatArrayList( 10 );

		System.out.println();
		System.out.println( "capacity: " + list.capacity() );
		System.out.println( "size:     " + list.size() );
		System.out.println( list );

		System.out.println();
		System.out.println( "adding 11 values..." );
		
		for( int i = 0; i < 11; ++i  )
			list.add( i );
		
		System.out.println( "capacity: " + list.capacity() );
		System.out.println( "size:     " + list.size() );
		System.out.println( list );

		System.out.println();
		System.out.println( "removing element at index 4..." );
		list.removeAt( 4 );
		System.out.println( "capacity: " + list.capacity() );
		System.out.println( "size:     " + list.size() );
		System.out.println( list );


		System.out.println();
		System.out.println( "removing first occurrence of value 6..." );
		list.removeValue( 6 );
		System.out.println( "capacity: " + list.capacity() );
		System.out.println( "size:     " + list.size() );
		System.out.println( list );

		System.out.println();
		System.out.println( "trimming to size..." );
		list.trimToSize();
		System.out.println( "capacity: " + list.capacity() );
		System.out.println( "size:     " + list.size() );
		System.out.println( list );

		System.out.println();
		System.out.println( "adding the list to itself..." );
		list.addAll( list );
		System.out.println( "capacity: " + list.capacity() );
		System.out.println( "size:     " + list.size() );
		System.out.println( list );
	}
}
