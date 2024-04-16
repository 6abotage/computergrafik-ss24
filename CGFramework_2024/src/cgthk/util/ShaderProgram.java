package cgthk.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import cgthk.math.Mat3;
import cgthk.math.Mat4;
import cgthk.math.Vec2;
import cgthk.math.Vec3;
import cgthk.math.Vec4;
//import util.Texture;

public class ShaderProgram {
	
	private final  int maxActiveTextures  = 8; // Only for set Uniform with Texture
	private static int activeTexture      = 0; // Only for set Uniform with Texture
	
	private int m_Program;
	private HashMap<String, Integer> m_UniformLocations;
	
	public ShaderProgram( String vertexShaderPath, String fragmentShaderPath ){
		m_UniformLocations = new HashMap<String, Integer>();
		m_Program = this.createProgram( vertexShaderPath, fragmentShaderPath );
	}
	
	private int createProgram( String vertexShaderPath, String fragmentShaderPath ){
		int program = glCreateProgram();
		
		int vertexShader   = this.createShader( vertexShaderPath, GL_VERTEX_SHADER );
		int fragmentShader = this.createShader( fragmentShaderPath, GL_FRAGMENT_SHADER );

		glAttachShader( program, vertexShader );
		glAttachShader( program, fragmentShader );
		glLinkProgram(  program );
		
		if( glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE )
			printLog( "Shaderprogram " + vertexShaderPath + " " + fragmentShaderPath, program );
		
		glValidateProgram( program );
		
		if( glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE )
			printLog( "Shaderprogram " + vertexShaderPath + " " + fragmentShaderPath, program );
		
		return program;
	}
	
	private int createShader( String shaderPath, int shaderType ){
		
		String shaderSource = readShaderFile( shaderPath );
		
		int shader = glCreateShader( shaderType );
		
		glShaderSource( shader, shaderSource );
		glCompileShader( shader );
		
		if( glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE )
		{
			printLog( shaderPath, shader );
			
			return -1;
		}
		
		return shader;
	}
	
	public void useProgram()
	{
		glUseProgram( m_Program );
	}
	
	private int getUniformLocation( String uniformName )
	{
		Integer cachedLocation = m_UniformLocations.get( uniformName );
		
		if( cachedLocation == null )
		{
			int location = glGetUniformLocation( m_Program, uniformName );
			m_UniformLocations.put( uniformName, location );
			
			return location;
		}
		
		return cachedLocation;
	}
	
	public void setUniform( String uniformName, int value )
	{
		glUniform1i( this.getUniformLocation(uniformName), value );
	}
	
	
	public void setUniform( String uniformName, float value )
	{
		glUniform1f( this.getUniformLocation(uniformName), value );
	}
	
	
	public void setUniform( String uniformName, float[] values )
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer( values.length );
		buffer.put( values );
		buffer.flip();
			
		glUniform1fv( this.getUniformLocation(uniformName), buffer ); // Not sure if +fv is the right call 
	}
	
	
	public void setUniform( String uniformName, Vec2 vec )
	{
		glUniform2f( this.getUniformLocation(uniformName), vec.x, vec.y );
	}
	
	
	public void setUniform( String uniformName, Vec2[] vecs )
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer( 2 * vecs.length );
		float[] floats = new  float[2 * vecs.length];
		
		for( int i = 0; i < vecs.length; ++i )
		{
			floats[ 2 * i + 0 ] = vecs[i].x; 
		    floats[ 2 * i + 1 ] = vecs[i].y; 
		}
		buffer.put( floats );
		buffer.flip();
			
		glUniform2fv( this.getUniformLocation(uniformName), buffer ); // Not sure if +fv is the right call 
	}
	
	
	public void setUniform( String uniformName, Vec3 vec )
	{
		glUniform3f( this.getUniformLocation(uniformName), vec.x, vec.y, vec.z );
	}
	
	
	public void setUniform( String uniformName, Vec3[] vecs )
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer( 3 * vecs.length );
		float[] floats = new  float[3 * vecs.length];
		
		for( int i = 0; i < vecs.length; ++i )
		{
			floats[ 3 * i + 0 ] = vecs[i].x; 
		    floats[ 3 * i + 1 ] = vecs[i].y; 
			floats[ 3 * i + 2 ] = vecs[i].z; 
		}
		buffer.put( floats );
		buffer.flip();
			
		glUniform3fv( this.getUniformLocation(uniformName), buffer ); // Not sure if +fv is the right call 
	}
	
	
	public void setUniform( String uniformName, Vec4 vec )
	{
		glUniform4f( this.getUniformLocation(uniformName), vec.x, vec.y, vec.z, vec.w );
	}
	
	
	public void setUniform( String uniformName, Vec4[] vecs )
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer( 4 * vecs.length );
		float[] floats = new  float[4 * vecs.length];
		
		for( int i = 0; i < vecs.length; ++i )
		{
			floats[ 4 * i + 0 ] = vecs[i].x; 
		    floats[ 4 * i + 1 ] = vecs[i].y; 
			floats[ 4 * i + 2 ] = vecs[i].z; 
			floats[ 4 * i + 2 ] = vecs[i].w; 
		}
		buffer.put( floats );
		buffer.flip();
			
		glUniform4fv( this.getUniformLocation(uniformName), buffer ); // Not sure if +fv is the right call 
	}
	
	
	public void setUniform( String uniformName, Mat3 mat )
	{
		glUniformMatrix3fv( this.getUniformLocation(uniformName), false, mat.toFloatBuffer() ); // Not sure if +fv is the right call 
	}
	
	
	public void setUniform( String uniformName, Mat4 mat )
	{
		glUniformMatrix4fv( this.getUniformLocation(uniformName), false, mat.toFloatBuffer() ); // Not sure if +fv is the right call 
	}
	
	/*
	 * TODO: Implement Texture Class
	 */
	public void setUniform( String uniformName, Texture texture )
	{
		int textureSlot = GL_TEXTURE0 + activeTexture;
		int textureID   = texture.getID();
		
		glActiveTexture( textureSlot );
		glBindTexture( GL_TEXTURE_2D, textureID );
		glUniform1i( this.getUniformLocation(uniformName), activeTexture );
		
		activeTexture = (activeTexture + 1) % maxActiveTextures;
	}
	
	
	/*
	 * -----------------------------------------------------------
	 *  Helper Functions
	 * -----------------------------------------------------------
	 */
	
	private static void printLog( String shaderFile, int obj )
	{
		IntBuffer iVal = BufferUtils.createIntBuffer( 1 );
		glGetShaderiv( obj, GL_INFO_LOG_LENGTH, iVal );

		int length = iVal.get();
		
		if (length > 1) 
		{
			ByteBuffer infoLog = BufferUtils.createByteBuffer( length );
			iVal.flip();
			
			glGetShaderInfoLog( obj, iVal, infoLog );
			
			byte[] infoBytes = new byte[length];
			infoLog.get( infoBytes );
			
			String out = new String( infoBytes );
			System.err.println( "Error in " + shaderFile + " :\n" + out );
			System.exit(0);
		}
	}
	
	public static String readShaderFile(String shaderPath ){
		String text = "";
		String line;
		
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(shaderPath) );
			while( (line = reader.readLine()) != null )
			{
				text += line + "\n";
			}
			reader.close();
		}
		catch ( Exception e )
		{
			System.err.println( "*ERROR*: Unable to read file: " + shaderPath );
			System.exit(0);
			return "";
		}
		
		return text;
	}
}
