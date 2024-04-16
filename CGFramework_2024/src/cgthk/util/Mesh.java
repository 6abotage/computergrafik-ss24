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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import cgthk.BVH_simple.BVH;
import cgthk.math.Mat4;
import cgthk.math.Vec3;
import cgthk.math.Vec4;



public class Mesh
{
	private Mat4                     m_ModelMatrix;
	private Vec4                     m_DiffuseColor;
	private HashMap<String, Texture> m_Textures;
	
	private int m_iUsage;
	private int m_iVAOid;
	private int m_iIndexBufferID;
	private int m_iNumIndices;
	public BVH  m_bvh;
	
	IntArrayList m_AttribBuffers;
	IntArrayList m_AttribComponents;
	IntArrayList m_AttribLocations;
	
	
	/**
	 * @param usage Specifies the expected usage pattern of the data store. 
	 * The symbolic constant must be GL_STREAM_DRAW, GL_STREAM_READ, GL_STREAM_COPY, 
	 * GL_STATIC_DRAW, GL_STATIC_READ, GL_STATIC_COPY, GL_DYNAMIC_DRAW, GL_DYNAMIC_READ, or GL_DYNAMIC_COPY.
	 */
	public Mesh( float[] positions, int[] indices, int usage )
	{
		m_ModelMatrix    = new Mat4();
		m_DiffuseColor   = new Vec4(1.0f);
		m_Textures       = new HashMap<String, Texture>();         
		
		m_iUsage         = usage;
		m_iVAOid         = 0;
		m_iIndexBufferID = glGenBuffers();
		m_iNumIndices    = 0;

		m_AttribBuffers    = new IntArrayList( 1 );
		m_AttribComponents = new IntArrayList( 1 );
		m_AttribLocations  = new IntArrayList( 1 );
		
		m_bvh = new BVH( positions, positions, positions, indices.length / 3, 10.0f, 0.01f );
		m_bvh.addFaces( indices, 0, indices.length / 3 );
		m_bvh.update( positions, positions, positions );
		
		this.rebuildVAO();
	}
	
	
	/**
	 * @param usage Specifies the expected usage pattern of the data store. 
	 * The symbolic constant must be GL_STREAM_DRAW, GL_STREAM_READ, GL_STREAM_COPY, 
	 * GL_STATIC_DRAW, GL_STATIC_READ, GL_STATIC_COPY, GL_DYNAMIC_DRAW, GL_DYNAMIC_READ, or GL_DYNAMIC_COPY.
	 */
	public Mesh( int usage )
	{
		m_ModelMatrix    = new Mat4();
		m_DiffuseColor   = new Vec4(1.0f);
		
		m_iUsage         = usage;
		m_iVAOid         = 0;
		m_iIndexBufferID = glGenBuffers();
		m_iNumIndices    = 0;

		m_AttribBuffers    = new IntArrayList( 1 );
		m_AttribComponents = new IntArrayList( 1 );
		m_AttribLocations  = new IntArrayList( 1 );
		
		this.rebuildVAO();
	}
	
	
	/**
	 * @param attribLocation The attribute location to set
	 * @param values
	 * @param componentsPerAttrib Specifies the number of components per generic vertex attribute. Must be either 1, 2, 3 or 4.
	 */
	public void setAttribute( int attribLocation, float[] values, int componentsPerAttrib )
	{
		int index    = m_AttribLocations.indexOf( attribLocation );
		int bufferID = 0;
		
		if( index < 0 )
		{
			bufferID = glGenBuffers();
			m_AttribBuffers.add(    bufferID );
			m_AttribLocations.add(  attribLocation );
			m_AttribComponents.add( componentsPerAttrib );
		}
		else
		{
			bufferID = m_AttribBuffers.get( index );
			m_AttribLocations.set(  index, attribLocation );
			m_AttribComponents.set( index, componentsPerAttrib );
		}
		
		FloatBuffer attribData = BufferUtils.createFloatBuffer( values.length );
		attribData.put( values, 0, values.length );
		attribData.flip();
		
		glBindBuffer( GL_ARRAY_BUFFER, bufferID );
		glBufferData( GL_ARRAY_BUFFER, 0,          m_iUsage );
		glBufferData( GL_ARRAY_BUFFER, attribData, m_iUsage );
		glBindBuffer( GL_ARRAY_BUFFER, 0 );
		
		this.rebuildVAO();
	}
	
	
	public void setIndices( int[] indices )
	{
		m_iNumIndices = indices.length;
		
		IntBuffer indexBuffer = BufferUtils.createIntBuffer( indices.length );
		indexBuffer.put( indices, 0, indices.length );
		indexBuffer.flip();
		
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, m_iIndexBufferID );
		glBufferData( GL_ELEMENT_ARRAY_BUFFER, 0,           m_iUsage );
		glBufferData( GL_ELEMENT_ARRAY_BUFFER, indexBuffer, m_iUsage );
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 );
		
		this.rebuildVAO();
	}
	
	
	public Mat4 getModelMatrix()
	{
		return new Mat4(m_ModelMatrix);
	}
	
	
	public Vec3 getMin()
	{
		return new Vec3( m_bvh.getMin() );
	}
	
	
	public Vec3 getMax()
	{
		return new Vec3( m_bvh.getMax() );
	}
	
	
	public void setModelMatrix( Mat4 modelMatrix )
	{
		m_ModelMatrix = new Mat4(modelMatrix);
	}
	
	
	public Vec3 getDiffuseColor()
	{
		return new Vec3(m_DiffuseColor);
	}
	
	public Vec4 getDiffuseColorRGBA()
	{
		return m_DiffuseColor;
	}
	
	public void setDiffuseColor( Vec3 diffuseColor )
	{
		m_DiffuseColor = new Vec4( diffuseColor, 1.0f );
	}
	
	public void setDiffuseColor( Vec4 diffuseColor ){
		m_DiffuseColor = diffuseColor;
	}
	
	/**
	 * Mesh has 5 texture slots, that can be freely adressed
	 */
	public void setTexture( String name, Texture texture )
	{
		m_Textures.put( name, texture );
	}
	
	
	public Texture getTexture( String name )
	{
		return m_Textures.get( name );
	}
	
	
	/**
	 * Deletes all internally created OpenGL Resources (vertex and index buffers).<br>
	 * Externally created buffers set via setVertexBuffer() will not be deleted and are expected to be freed elsewhere.
	 */
	public void freeGLResources()
	{
		for( int i = 0; i < m_AttribBuffers.size(); ++i )
			glDeleteBuffers( m_AttribBuffers.get(i) );
		
		m_AttribBuffers.clear();
		m_AttribLocations.clear();
		m_AttribComponents.clear();
		
		glDeleteBuffers( m_iIndexBufferID );
		glDeleteVertexArrays( m_iVAOid );
	}
	
	
	/**
	 * Draws the mesh as a set of indexed triangles.
	 */
	public void draw()
	{
		glBindVertexArray( m_iVAOid );
		glDrawElements( GL_TRIANGLES, m_iNumIndices, GL_UNSIGNED_INT, 0 );
		glBindVertexArray( 0 );
	}
	
	
	/**
	 * Draws the mesh using the given mode.
	 * @param mode GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_LINE_STRIP_ADJACENCY, 
	 * GL_LINES_ADJACENCY, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, GL_TRIANGLES, 
	 * GL_TRIANGLE_STRIP_ADJACENCY, GL_TRIANGLES_ADJACENCY and GL_PATCHES are accepted.
	 */
	public void draw( int mode )
	{
		glBindVertexArray( m_iVAOid );
		glDrawElements( mode, m_iNumIndices, GL_UNSIGNED_INT, 0 );
		glBindVertexArray( 0 );
	}
	
	
	/**
	 * Draws a specific range of indices using the given mode;
	 * @param mode GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_LINE_STRIP_ADJACENCY, 
	 * GL_LINES_ADJACENCY, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, GL_TRIANGLES, 
	 * GL_TRIANGLE_STRIP_ADJACENCY, GL_TRIANGLES_ADJACENCY and GL_PATCHES are accepted.
	 * @param indexOffset The first index to use
	 * @param indexCount The number of indices to use
	 */
	public void drawRange( int mode, int indexOffset, int indexCount )
	{
		long byteOffset = (long) indexOffset * 4;
		glBindVertexArray( m_iVAOid );
		glDrawElements( mode, indexCount, GL_UNSIGNED_INT, byteOffset );
		glBindVertexArray( 0 );
	}
	
	
	private void rebuildVAO()
	{
		if( m_iVAOid != 0 )
			glDeleteVertexArrays( m_iVAOid );
		
		m_iVAOid = glGenVertexArrays();
		
		glBindVertexArray( m_iVAOid );
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, m_iIndexBufferID );
		
		for( int i = 0; i < m_AttribBuffers.size(); ++i )
		{
			glBindBuffer( GL_ARRAY_BUFFER, m_AttribBuffers.get(i) );
			glEnableVertexAttribArray( m_AttribLocations.get(i) );
			glVertexAttribPointer( m_AttribLocations.get(i), m_AttribComponents.get(i), GL_FLOAT, false, 0, 0 );
		}

		glBindVertexArray( 0 );
		
		for( int i = 0; i < m_AttribLocations.size(); ++i )
			glDisableVertexAttribArray( m_AttribLocations.get(i) );
		
		glBindBuffer( GL_ARRAY_BUFFER, 0 );
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 );
	}
	
	public Vec3 getPosition(){
		return new Vec3(m_ModelMatrix.m03,m_ModelMatrix.m13, m_ModelMatrix.m23);
	}
}
