package cgthk.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import cgthk.BVH_simple.BVH;
import cgthk.math.Mat4;
import cgthk.math.Vec3;
import cgthk.math.Vec4;

import org.lwjgl.BufferUtils;
import org.lwjgl.BufferUtils.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
//import static org.lwjgl.util.glu.GLU.*;



public class Scene 
{
	private HashMap<String, Integer> meshIDs;
	private ArrayList<Mesh>          meshes;
	private ArrayList<Mesh>			 transparentMeshes;
	private ArrayList<Mesh>          lights;
	private Mesh					 skybox;
	private int                      selectedMeshIndex;
	private int                      selectedTransparentMeshIndex;
	private int                      selectedLightIndex;
	private Camera                   camera;
	
	
	
	public Scene()
	{
		meshIDs            = new HashMap<String, Integer>();
		meshes             = new ArrayList<Mesh>();
		lights             = new ArrayList<Mesh>();
		transparentMeshes  = new ArrayList<Mesh>();
		selectedMeshIndex  = -1;
		selectedLightIndex = -1;
		selectedTransparentMeshIndex = -1;
		camera             = null;
	}
	
	
	public void addMesh( String name, Mesh mesh )
	{
		meshIDs.put( name, meshes.size() );
		meshes.add( mesh );
	}
	
	
	public void addMesh( Mesh mesh )
	{
		meshIDs.put( mesh.toString() , meshes.size() );
		meshes.add( mesh );
	}
	
	
	public void addLight( Mesh mesh )
	{
		lights.add( mesh );
	}
	
	
	public void addTransparentMesh( Mesh mesh ){
		transparentMeshes.add(mesh);
	}
	
	public void selectObject( int pixelCoordX, int pixelCoordY )
	{
		Mat4 viewMatrix     = camera.getViewMatrix();
		Mat4 projMatrix     = camera.getProjectionMatrix();
		
		Mat4 viewInverse    = Mat4.inverse( viewMatrix );
		Vec3 pickedPosition = unprojectPosition( viewMatrix, projMatrix, pixelCoordX, pixelCoordY, 0.0f, 0.0f, 1.0f, 1.0f );
		Vec3 cameraPosition = Vec3.transform( new Vec3(), 1.0f, viewInverse );
		
		float closestDistance = Float.MAX_VALUE;
		
		for( int i = 0; i < meshes.size(); ++i )
		{
			Mesh mesh             = meshes.get( i );
			Mat4 transform        = mesh.getModelMatrix();
			Mat4 inverseTransform = Mat4.inverse( transform );
			Vec3 rayStart         = Vec3.transform( cameraPosition, 1.0f, inverseTransform );
			Vec3 rayEnd           = Vec3.transform( pickedPosition, 1.0f, inverseTransform );
			Vec3 rayDirection     = Vec3.sub( rayEnd, rayStart );
			BVH  bvh              = mesh.m_bvh;
			Vec3 nearestPosition  = bvh.findFirstHitPosition( rayStart, rayDirection );
			
			if( bvh.findFirstHitPosition( rayStart, rayDirection ) != null )
			{
				float distance = Vec3.length( nearestPosition.sub(rayStart).transform(0.0f, transform) );
				
				if( distance < closestDistance )
				{
					closestDistance    = distance;
					selectedMeshIndex  = i;
					selectedLightIndex = -1; 
					selectedTransparentMeshIndex = -1;
				}
			}
		}
		
		for( int i = 0; i < lights.size(); ++i )
		{
			Mesh mesh             = lights.get( i );
			Mat4 transform        = mesh.getModelMatrix();
			Mat4 inverseTransform = Mat4.inverse( transform );
			Vec3 rayStart         = Vec3.transform( cameraPosition, 1.0f, inverseTransform );
			Vec3 rayEnd           = Vec3.transform( pickedPosition, 1.0f, inverseTransform );
			Vec3 rayDirection     = Vec3.sub( rayEnd, rayStart );
			BVH  bvh              = mesh.m_bvh;
			Vec3 nearestPosition  = bvh.findFirstHitPosition( rayStart, rayDirection );
			
			if( nearestPosition != null )
			{
				float distance = Vec3.length( nearestPosition.sub(rayStart).transform(0.0f, transform) );
				
				if( distance < closestDistance )
				{
					closestDistance   = distance;
					selectedMeshIndex  = -1;
					selectedTransparentMeshIndex = -1;
					selectedLightIndex = i; 
				}
			}
		}
		
		for( int i = 0; i < transparentMeshes.size(); ++i )
		{
			Mesh mesh             = transparentMeshes.get( i );
			Mat4 transform        = mesh.getModelMatrix();
			Mat4 inverseTransform = Mat4.inverse( transform );
			Vec3 rayStart         = Vec3.transform( cameraPosition, 1.0f, inverseTransform );
			Vec3 rayEnd           = Vec3.transform( pickedPosition, 1.0f, inverseTransform );
			Vec3 rayDirection     = Vec3.sub( rayEnd, rayStart );
			BVH  bvh              = mesh.m_bvh;
			Vec3 nearestPosition  = bvh.findFirstHitPosition( rayStart, rayDirection );
			
			if( bvh.findFirstHitPosition( rayStart, rayDirection ) != null )
			{
				float distance = Vec3.length( nearestPosition.sub(rayStart).transform(0.0f, transform) );
				
				if( distance < closestDistance )
				{
					closestDistance    = distance;
					selectedTransparentMeshIndex  = i;
					selectedLightIndex = -1; 
					selectedMeshIndex = -1;
				}
			}
		}
	}
	
	
	public Mesh getSelectedObject()
	{
		if( selectedMeshIndex == -1 && selectedLightIndex == -1 && selectedTransparentMeshIndex == -1 )
			return null;
		else if( selectedLightIndex == -1 && selectedTransparentMeshIndex == -1)
			return meshes.get( selectedMeshIndex );
		else if( selectedMeshIndex == -1 && selectedTransparentMeshIndex == -1)
			return lights.get( selectedLightIndex );
		else
			return transparentMeshes.get( selectedTransparentMeshIndex );
		
	}
	
	public void clearSelectedObject(){
		selectedMeshIndex = -1;
		selectedLightIndex = -1;
		selectedTransparentMeshIndex = -1;
	}

	
	public void setCamera( Camera camera )
	{
		this.camera = camera;
	}
	
	
	public Camera getCamera()
	{
		return camera;
	}


	public Mesh getMesh( String name )
	{
		return meshes.get( meshIDs.get(name) );
	}
	
	
	public ArrayList<Mesh> getMeshes()
	{
		return meshes;
	}
	
	public ArrayList<Mesh> getTransparentMeshes()
	{
		return transparentMeshes;
	}
	
	public ArrayList<Mesh> getLights()
	{
		return lights;
	}
	
	
	private Vec3 unprojectPosition( Mat4 viewMatrix, Mat4 projMatrix, int coordX, int coordY, float viewportX, float viewportY, float viewportWidth, float viewportHeight )
	{
		viewMatrix = new Mat4( viewMatrix );
		projMatrix = new Mat4( projMatrix );
		
		IntBuffer   viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer depth    = BufferUtils.createFloatBuffer(1);
//		FloatBuffer position = BufferUtils.createFloatBuffer(3);
		
		int width  = camera.getWidth();
		int height = camera.getHeight();
		
		viewport.put( (int) (viewportX * width) );
		viewport.put( (int) (viewportY * height) );
		viewport.put( (int) (viewportWidth  * width) );
		viewport.put( (int) (viewportHeight * height) );
		viewport.flip();
		
		glBindFramebuffer( GL_DRAW_FRAMEBUFFER, 0 );
		glBlitFramebuffer( 0, 0, (int) (viewportX * width), (int) (viewportX * height), 
						   0, 0, (int) (viewportX * width), (int) (viewportX * height), 
						   GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, GL_NEAREST );

		glBindFramebuffer( GL_READ_FRAMEBUFFER, 0 );
		glReadPixels( coordX, coordY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, depth );
		//gluUnProject( coordX, coordY, depth.get(0), viewMatrix.toFloatBuffer(), projMatrix.toFloatBuffer(), viewport, position );
		Vec3 position = unprojectErsatz( coordX, coordY, depth.get(0), viewMatrix, projMatrix, viewport );
		
		return position;
		//return new Vec3( position.get(0), position.get(1), position.get(2) );
	}
	
	// Replaces the gluUnProject function
	private Vec3 unprojectErsatz(float winx, float winy, float winz, Mat4  modelview, Mat4 projection, IntBuffer viewport )
	  {
	     Mat4 inverse_transform = Mat4.mul(projection, modelview);
	     inverse_transform.inverse();
	     Vec4 point = new Vec4( (winx - (float)viewport.get(0)) / (float)viewport.get(2) * 2.0f -1.0f,
	 				(winy - (float)viewport.get(1)) / (float)viewport.get(3) * 2.0f -1.0f,
	 				(float)(2.0f * winz - 1.0f),
	 				1.0f);
	     point.transform(inverse_transform);
	     if( point.w == 0.0f )
	    	 return new Vec3(0f);
	     
	     float division = 1.0f / point.w;
	     point.mul(division);
	     return new Vec3(point);
	  }
	
	public Mesh getSkybox() {
		return skybox;
	}


	public void setSkybox(Mesh skybox) {
		this.skybox = skybox;
	}
	
}
