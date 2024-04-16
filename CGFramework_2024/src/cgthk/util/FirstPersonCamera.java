package cgthk.util;

import cgthk.math.Mat4;
import cgthk.math.Vec3;
import cgthk.math.Vec4;

public abstract class FirstPersonCamera extends Camera
{
	protected Vec4  position;
	protected Mat4  rotation;
	
	
	public final void yaw( float radians )
	{
		rotation = rotateGlobal( rotation, Vec3.yAxis(), radians );
	}
	
	
	public final void pitch( float radians )
	{
		rotation = rotateLocal( rotation, Vec3.xAxis(), radians );
	}
	
	
	public final void roll( float radians )
	{
		rotation = rotateLocal( rotation, Vec3.zAxis(), radians );
	}
	
	public Vec4 getWorldPosition(){
		return position;
	}
	
	public Mat4 getViewMatrix()
	{
		Mat4 viewMatrix = new Mat4(rotation);
		viewMatrix.m03 = position.x;
		viewMatrix.m13 = position.y;
		viewMatrix.m23 = position.z;
		
		return viewMatrix.inverse();
	}
	
	public abstract void forward( float distance );
	public abstract void right(   float distance );
	public abstract void up(      float distance );
}
