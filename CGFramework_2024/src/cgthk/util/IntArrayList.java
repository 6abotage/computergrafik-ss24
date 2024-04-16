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


import java.util.Arrays;



public class IntArrayList
{
	private static final float c_fGrowthFactor = 1.25f;
	
	private int[] m_iValues;
	private int   m_iSize;
	
	
	
	public IntArrayList()
	{
		m_iValues = new int[1];
		m_iSize   = 0;
	}
	
	public IntArrayList( int initialCapacity )
	{
		m_iValues = new int[initialCapacity];
		m_iSize   = 0;
	}
	
	
	public void add( int value )
	{
		if( m_iSize >= m_iValues.length )
		{
			int minCapacity = (int) (m_iSize * c_fGrowthFactor);
			minCapacity = minCapacity > m_iSize ? minCapacity : m_iSize + 1;
			
			this.ensureCapacity( minCapacity );
		}
		
		m_iValues[m_iSize++] = value;
	}
	
	
	public void addAll( int[] values )
	{
		this.ensureCapacity( m_iSize + values.length );
		
		System.arraycopy( values, 0, m_iValues, m_iSize, values.length );
		
		m_iSize += values.length;
	}
	
	
	public void addAll( IntArrayList intArrayList )
	{
		this.ensureCapacity( m_iSize + intArrayList.size() );
		
		System.arraycopy( intArrayList.toArray(), 0, m_iValues, m_iSize, intArrayList.size() );
		
		m_iSize += intArrayList.size();
	}
	
	
	public void set( int index, int value )
	{
		m_iValues[index] = value;
	}
	
	
	public int get( int index )
	{
		return m_iValues[index];
	}
	
	
	public boolean contains( int value )
	{
		return this.indexOf( value ) != -1;
	}
	
	
	public void ensureCapacity( int minCapacity )
	{
		if( m_iValues.length < minCapacity)
			m_iValues = Arrays.copyOf( m_iValues, minCapacity );
	}
	
	
	public int indexOf( int value )
	{
		for( int i = 0; i < m_iSize; ++i )
		{
			if( m_iValues[i] == value )
				return i;
		}
		
		return -1;
	}
	
	
	public int lastIndexOf( int value )
	{
		for( int i = m_iSize-1; i >= 0; --i )
		{
			if( m_iValues[i] == value )
				return i;
		}
		
		return -1;
	}
	
	
	public int removeAt( int index )
	{
		int value = m_iValues[index];
		
		System.arraycopy( m_iValues, index + 1, m_iValues, index, m_iSize - index - 1 );
		
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
		return m_iValues.length;
	}
	
	
	public int[] toArray()
	{
		return m_iValues;
	}
	
	
	public void trimToSize()
	{
		m_iValues = Arrays.copyOf( m_iValues, m_iSize );
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
			string += m_iValues[i] + ", ";
		
		string += m_iValues[m_iSize-1] + "]";
		
		return string;
	}
	
	
	public static void test()
	{
		System.out.println( "IntArrayList growth factor: " + c_fGrowthFactor );
		
		IntArrayList list = new IntArrayList( 10 );

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
