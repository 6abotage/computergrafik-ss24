package cgthk.util;

import cgthk.math.Mat4;
import cgthk.math.Vec3;
import cgthk.math.Vec4;


public abstract class Camera 
{	
	public enum CameraType
	{
		PERSPECTIVE_FIRST_PERSON,
		TURN_TABLE,
		ORTHOGRAPHIC_FIRST_PERSON
	}
	
	
	protected int   width;
	protected int   height;
	protected float zNear;
	protected float zFar;
	protected Mat4  projectionMatrix;
	
	public abstract void yaw(   float radians );
	public abstract void pitch( float radians );
	public abstract void roll(  float radians );
	
	public abstract void forward( float distance );
	public abstract void right(   float distance );
	public abstract void up(      float distance );
	
	public abstract Mat4 getViewMatrix();
	
	public abstract Vec4 getWorldPosition();

	public abstract void setZNear( float zNear );
	public abstract void setZFar(  float zFar  );
	
	
	public int getWidth()
	{
		return width;
	}
		
		
	public int getHeight()
	{
		return height;
	}
	
	
	public Mat4 getProjectionMatrix()
	{
		return projectionMatrix;
	}
	
	
	protected Mat4 rotateLocal( Mat4 rotation, Vec3 axis, float radians )
	{
		Mat4 rot = Mat4.rotation( axis, radians );
		return Mat4.mul( rotation, rot );
	}
	
	
	protected Mat4 rotateGlobal( Mat4 rotation, Vec3 axis, float radians )
	{
		Mat4 rot = Mat4.rotation( axis, radians );
		return Mat4.mul( rot, rotation );
	}
	
	
	protected Vec4 translateLocal( Vec4 position, Mat4 rotation, Vec4 translation )
	{
		Vec4 localTranslation = Vec4.transform(translation, rotation );
		return Vec4.add( position, localTranslation );
	}
	
	
	protected Vec4 translateGlobal( Vec4 position, Vec4 translation )
	{
		return Vec4.add( position, translation );
	}
}
