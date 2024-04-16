package cgthk.util;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.util.ArrayList;

import cgthk.math.Mat4;
import cgthk.math.Vec3;




public class Primitive 
{
	private static Mesh            lineBatch  = new Mesh( GL_DYNAMIC_DRAW );
	private static Mesh            pointBatch = new Mesh( GL_DYNAMIC_DRAW );
	private static ArrayList<Vec3> lines      = new ArrayList<Vec3>();
	private static ArrayList<Vec3> points     = new ArrayList<Vec3>();
	
	
	public static void drawBox( Vec3 min, Vec3 max, Vec3 color, Mat4 transform )
	{
		Vec3 position0 = new Vec3( min.x, min.y, min.z ).transform( 1.0f, transform );
		Vec3 position1 = new Vec3( min.x, min.y, max.z ).transform( 1.0f, transform );
		Vec3 position2 = new Vec3( min.x, max.y, max.z ).transform( 1.0f, transform );
		Vec3 position3 = new Vec3( max.x, max.y, max.z ).transform( 1.0f, transform );
		Vec3 position4 = new Vec3( max.x, max.y, min.z ).transform( 1.0f, transform );
		Vec3 position5 = new Vec3( max.x, min.y, min.z ).transform( 1.0f, transform );
		Vec3 position6 = new Vec3( min.x, max.y, min.z ).transform( 1.0f, transform );
		Vec3 position7 = new Vec3( max.x, min.y, max.z ).transform( 1.0f, transform );

		lines.add( position0 );
		lines.add( color );
		lines.add( position5 );
		lines.add( color );
		lines.add( position0 );
		lines.add( color );
		lines.add( position6 );
		lines.add( color );
		lines.add( position0 );
		lines.add( color );
		lines.add( position1 );
		lines.add( color );
		lines.add( position7 );
		lines.add( color );
		lines.add( position3 );
		lines.add( color );
		lines.add( position7 );
		lines.add( color );
		lines.add( position5 );
		lines.add( color );
		lines.add( position7 );
		lines.add( color );
		lines.add( position1 );
		lines.add( color );	
		lines.add( position4 );
		lines.add( color );
		lines.add( position6 );
		lines.add( color );
		lines.add( position4 );
		lines.add( color );
		lines.add( position5 );
		lines.add( color );
		lines.add( position4 );
		lines.add( color );
		lines.add( position3 );
		lines.add( color );
		lines.add( position2 );
		lines.add( color );
		lines.add( position6 );
		lines.add( color );
		lines.add( position2 );
		lines.add( color );
		lines.add( position1 );
		lines.add( color );
		lines.add( position2 );
		lines.add( color );
		lines.add( position3 );
		lines.add( color );
	}
	
	
	public static void drawBox( Vec3 min, Vec3 max, Vec3 color )
	{
		lines.add( new Vec3(min.x, min.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, min.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, min.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, max.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, max.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, max.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, max.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, min.y, min.z) );
		lines.add( color );
		
		lines.add( new Vec3(min.x, min.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, min.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, min.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, max.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, max.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, max.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, max.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, min.y, max.z) );
		lines.add( color );	

		lines.add( new Vec3(min.x, min.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, min.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, min.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, min.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, max.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(max.x, max.y, max.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, max.y, min.z) );
		lines.add( color );
		lines.add( new Vec3(min.x, max.y, max.z) );
		lines.add( color );
	}
	
	
	public static void drawLine( Vec3 start, Vec3 end, Vec3 color )
	{
		lines.add( start );
		lines.add( color );
		lines.add( end   );
		lines.add( color );
	}
	
	
	public static void drawPoint( Vec3 point, Vec3 color )
	{
		points.add( point );
		points.add( color );
	}
	
	
	public static void drawGridFloor()
	{
		int resolution = 10;
		
		for( int z = -resolution; z <= resolution; ++z )
			Primitive.drawLine( new Vec3(-resolution, 0.0f, z), new Vec3(resolution, 0.0f, z), new Vec3(0.8f) );
		
		for( int x = -resolution; x <= resolution; ++x )
			Primitive.drawLine( new Vec3(x, 0.0f, -resolution), new Vec3(x, 0.0f, resolution), new Vec3(0.8f) );
		
		for( int z = -resolution * 2; z <= resolution * 2; ++z )
			Primitive.drawLine( new Vec3(-resolution, 0.0f, 0.5f * z), new Vec3(resolution, 0.0f, 0.5f * z), new Vec3(0.4f) );
		
		for( int x = -resolution * 2; x <= resolution * 2; ++x )
			Primitive.drawLine( new Vec3(0.5f * x, 0.0f, -resolution), new Vec3(0.5f * x, 0.0f, resolution), new Vec3(0.4f) );
		
		for( int z = -resolution * 10; z <= resolution * 10; ++z )
			Primitive.drawLine( new Vec3(-resolution, 0.0f, 0.1f * z), new Vec3(resolution, 0.0f, 0.1f * z), new Vec3(0.2f) );
		
		for( int x = -resolution * 10; x <= resolution * 10; ++x )
			Primitive.drawLine( new Vec3(0.1f * x, 0.0f, -resolution), new Vec3(0.1f * x, 0.0f, resolution), new Vec3(0.2f) );
	}
	
	
	public static void drawBatches( Mat4 viewMatrix, Mat4 projMatrix )
	{
		glPointSize( 4.0f );
		glLineWidth( 1.0f );
		
		drawLineBatch(  viewMatrix, projMatrix );
		drawPointBatch( viewMatrix, projMatrix );
	}
	
	
	private static void drawLineBatch( Mat4 viewMatrix, Mat4 projMatrix )
	{
		float[] positions = new float[lines.size() / 2 * 3];
		float[] colors    = new float[lines.size() / 2 * 3];
		int[]   indices   = new int[lines.size() / 2];
		
		for( int i = 0; i < lines.size() / 2; ++i )
		{
			positions[i*3 + 0] = lines.get(i * 2 + 0).x;
			positions[i*3 + 1] = lines.get(i * 2 + 0).y;
			positions[i*3 + 2] = lines.get(i * 2 + 0).z;
			
			colors[i*3 + 0] = lines.get(i * 2 + 1).x;
			colors[i*3 + 1] = lines.get(i * 2 + 1).y;
			colors[i*3 + 2] = lines.get(i * 2 + 1).z;
			
			indices[i] = i;
		}

		lineBatch.setAttribute( 0, positions, 3 );
		lineBatch.setAttribute( 1, colors,    3 );
		lineBatch.setIndices( indices );
		
		lines.clear();
		lineBatch.draw( GL_LINES );
	}
	
	
	private static void drawPointBatch( Mat4 viewMatrix, Mat4 projMatrix )
	{
		float[] positions = new float[points.size() / 2 * 3];
		float[] colors    = new float[points.size() / 2 * 3];
		int[]   indices   = new int[points.size() / 2];
		
		for( int i = 0; i < points.size() / 2; ++i )
		{
			positions[i*3 + 0] = points.get(i * 2 + 0).x;
			positions[i*3 + 1] = points.get(i * 2 + 0).y;
			positions[i*3 + 2] = points.get(i * 2 + 0).z;
			
			colors[i*3 + 0] = points.get(i * 2 + 1).x;
			colors[i*3 + 1] = points.get(i * 2 + 1).y;
			colors[i*3 + 2] = points.get(i * 2 + 1).z;
			
			indices[i] = i;
		}

		pointBatch.setAttribute( 0, positions, 3 );
		pointBatch.setAttribute( 1, colors,    3 );
		pointBatch.setIndices( indices );
		
		points.clear();
		
		pointBatch.draw( GL_POINTS );
	}
	
}
