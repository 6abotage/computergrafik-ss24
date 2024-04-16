package cgthk.util;

import cgthk.math.Mat4;
import cgthk.math.Vec3;
import cgthk.math.Vec4;

public class TurnTableCamera extends Camera
{
	private float aspect;
	private float fov;
	private Mat4  rotation;
	private Vec4  position;
	private float distance;
	private Mat4  viewMatrix;
	
	
	public TurnTableCamera( Vec4 position, float aspect, float fov, float zNear, float zFar )
	{
		this.aspect = aspect;
		this.fov    = fov;
		this.zNear  = zNear;
		this.zFar   = zFar;
		
		this.viewMatrix       = Mat4.lookAt( new Vec3(position), new Vec3(), Vec3.yAxis() ).inverse();
		this.projectionMatrix = Mat4.perspective( this.fov, this.aspect, 1.0f, this.zNear, this.zFar );
		
		this.rotation = new Mat4(this.viewMatrix);
		this.rotation.m03 = 0.0f;
		this.rotation.m13 = 0.0f;
		this.rotation.m23 = 0.0f;
		
		this.distance = Vec3.length( new Vec3(position) );
		this.position = new Vec4( 0.0f, 0.0f, distance, 1.0f );
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
	
	
	public void yaw( float radians )
	{
		Mat4 translation = Mat4.translation( position.x, position.y, position.z );
		     rotation    = rotateGlobal( rotation, Vec3.yAxis(), radians );
		
		viewMatrix = Mat4.mul( rotation, translation );
	}
	
	
	public void pitch( float radians )
	{
		Mat4 translation = Mat4.translation( position.x, position.y, position.z );
	         rotation    = rotateLocal( rotation, Vec3.xAxis(), radians );
	
	    viewMatrix = Mat4.mul( rotation, translation );
	}
	
	
	public void roll( float radians ){}

	
	public void forward( float distance )
	{
		this.distance -= distance;
		
		if( this.distance < 0.5f )
		{
			this.distance = 0.5f;
			return;
		}
		
		Vec4 step = Vec4.zAxis();
		step.mul( -distance );
		
		position = translateGlobal( position, step );
		
		Mat4 translation = Mat4.translation( position.x, position.y, position.z );
	    viewMatrix = Mat4.mul( rotation, translation );
	}
	
	public Vec4 getWorldPosition(){
		return new Vec4( viewMatrix.m03, viewMatrix.m13, viewMatrix.m23, 1f );
	}
	
	
	public void right( float distance ){}
	
	
	public void up( float distance ){}
	

	public Mat4 getViewMatrix()
	{
		return Mat4.inverse( this.viewMatrix );
	}
}
