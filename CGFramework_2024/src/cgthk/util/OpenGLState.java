package cgthk.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

/**
 * Class to save the current OpenGL state.
 * Does currently NOT save all states, just
 * most important ones.
 * 
 * @author Mario
 *
 */
public class OpenGLState {

	private static int m_GL_BLEND;						// Blend state (true, false)
	private static int m_GL_BLEND_FUNC_SRC; 			// glBlendFunc( SOURCE, DESTINATION )
	private static int m_GL_BLEND_FUNC_DST; 			// glBlendFunc( SOURCE, DESTINATION )
	
	private static int m_GL_CULL;						// Culling (true, false )
	private static int m_GL_CULL_FACE;					// glCullFace( * )
	
	private static int m_GL_PROGRAM;					// glProgram( * )
	private static int m_GL_ARRAY_BUFFER;				// glBindBuffer( GL_ARRAY_BUFFER, * )
	private static int m_GL_ELEMENT_ARRAY_BUFFER;		// glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, * )
	private static int m_GL_VERTEX_ARRAY;				// glBindVertexArray( * )
	
	private static int m_GL_SCISSOR_TEST;				// Scissor Test ( true, false )
	
	public OpenGLState() {
		saveState();
	}
	
	public void saveState() {
		
		IntBuffer intState = BufferUtils.createIntBuffer(1);
		
		glGetIntegerv(GL_BLEND, intState);
		m_GL_BLEND = intState.get(0);
		glGetIntegerv(GL_BLEND_DST_ALPHA, intState);
		m_GL_BLEND_FUNC_DST = intState.get(0);
		glGetIntegerv(GL_BLEND_SRC_ALPHA, intState);
		m_GL_BLEND_FUNC_SRC = intState.get(0);
		glGetIntegerv(GL_CULL_FACE, intState);
		m_GL_CULL = intState.get(0);
		glGetIntegerv(GL_CULL_FACE_MODE, intState);
		m_GL_CULL_FACE = intState.get(0);
		glGetIntegerv(GL_CURRENT_PROGRAM, intState);
		m_GL_PROGRAM = intState.get(0);
		glGetIntegerv(GL_ARRAY_BUFFER_BINDING, intState);
		m_GL_ARRAY_BUFFER = intState.get(0);
		glGetIntegerv(GL_ELEMENT_ARRAY_BUFFER_BINDING, intState);
		m_GL_ELEMENT_ARRAY_BUFFER = intState.get(0);
		glGetIntegerv(GL_VERTEX_ARRAY_BINDING, intState);
		m_GL_VERTEX_ARRAY = intState.get(0);
		glGetIntegerv(GL_SCISSOR_TEST, intState);
		m_GL_SCISSOR_TEST = intState.get(0);		
	}
	
	public void restoreState(){
		
		if( m_GL_BLEND == 1 )
			glEnable(GL_BLEND);
		else
			glDisable(m_GL_BLEND);
		
		glBlendFunc(m_GL_BLEND_FUNC_SRC, m_GL_BLEND_FUNC_DST);
		
		if( m_GL_CULL == 1 )
			glEnable(GL_CULL_FACE);
		else
			glDisable(GL_CULL_FACE);
		
		glCullFace(m_GL_CULL_FACE);
		
		glUseProgram(m_GL_PROGRAM);
		glBindBuffer(GL_ARRAY_BUFFER, m_GL_ARRAY_BUFFER);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_GL_ELEMENT_ARRAY_BUFFER);
		glBindVertexArray(m_GL_VERTEX_ARRAY);
		
		if( m_GL_SCISSOR_TEST == 1 )
			glEnable(GL_SCISSOR_TEST);
		else
			glDisable(GL_SCISSOR_TEST);
	}
	
	static void printStates(){
		
	}
	
}
