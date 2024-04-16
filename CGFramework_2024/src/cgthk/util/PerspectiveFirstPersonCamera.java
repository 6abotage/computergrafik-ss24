package cgthk.util;

import cgthk.math.Mat4;
import cgthk.math.Vec3;
import cgthk.math.Vec4;

public class PerspectiveFirstPersonCamera extends FirstPersonCamera
{
	private float aspect;
	private float fov;
	
	
	public PerspectiveFirstPersonCamera( Vec4 position, float aspect, float fov, float zNear, float zFar )
	{
		
		
		this.aspect = aspect;
		this.fov    = fov;
		this.zNear  = zNear;
		this.zFar   = zFar;
		
		this.position         = position;
		this.projectionMatrix = Mat4.perspective( this.fov, this.aspect, 1.0f, this.zNear, this.zFar );
		this.rotation         = new Mat4();
	}
	
	public void setAspect( int width, int height )
	{
		this.width  = width;
		this.height = height;
		this.aspect = (float)width / (float)height;
		this.projectionMatrix = Mat4.perspective( this.fov, this.aspect, 1.0f, this.zNear, this.zFar );
	}
	
	
	public void setFov( float fov )
	{
		this.fov = fov;
		this.projectionMatrix = Mat4.perspective( this.fov, this.aspect, 1.0f, this.zNear, this.zFar );
	}
	
	
	public void setZNear( float zNear )
	{
		this.zNear = zNear;
		this.projectionMatrix = Mat4.perspective( this.fov, this.aspect, 1.0f, this.zNear, this.zFar );
	}
	
	
	public void setZFar( float zFar )
	{
		this.zFar = zFar;
		this.projectionMatrix = Mat4.perspective( this.fov, this.aspect, 1.0f, this.zNear, this.zFar );
	}
	
	
	public void forward( float distance )
	{
		Vec4 step = Vec4.zAxis();
		step.mul( -distance );
		
		position = translateLocal( position, rotation, step );
	}
	
	
	public void right( float distance )
	{
		Vec4 step = Vec4.xAxis();
		step.mul( distance  );
		
		position = translateLocal( position, rotation, step );
	}
	
	
	public void up( float distance )
	{
		Vec4 step = Vec4.yAxis();
		step.mul( distance );
		
		position = translateGlobal( position, step );
	}
	

	public Mat4 getViewMatrix()
	{
		Mat4 viewMatrix = new Mat4(rotation);
		viewMatrix.m03 = position.x;
		viewMatrix.m13 = position.y;
		viewMatrix.m23 = position.z;
		
		return viewMatrix.inverse();
	}
}
