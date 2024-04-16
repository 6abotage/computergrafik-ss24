package cgthk.util;

/**
 * Management class to keep track of varying properties like window size
 * and -position and framebuffer size. Differentiating framebuffer and 
 * window size is important for correct display on MacOS.
 * 
 * @author Mario
 * @version 0.9
 */
public final class GLFWWindowPropertyManager {
	
	// Attributes are package protected to allow changes from the main
	// class but protect them from access from the sandbox.
	
	// Current attributes
	int m_windowWidth;
	int m_windowHeight;
	
	int	m_framebufferWidth;
	int	m_framebufferHeight;
	
	int	m_windowPositionX;
	int	m_windowPositionY;
	
	// State saves
	int	m_oldWindowWidth;
	int m_oldWindowHeight;
	
	int m_oldFramebufferWidth;
	int m_oldFramebufferHeight;
	
	int	m_oldWindowPositionX;
	int	m_oldWindowPositionY;
	
	/**
	 * When instantiated old values equal the current ones. 
	 * Window positions are later set by Main.
	 * 
	 * @param windowWidth window width when being instantiated
	 * @param windowHeight window height when being instantiated
	 */
	GLFWWindowPropertyManager( int windowWidth, int windowHeight ){
		m_windowWidth 		= m_oldWindowWidth 			= windowWidth;
		m_windowHeight 		= m_oldWindowHeight 		= windowHeight;
		
		m_framebufferWidth 	= m_oldFramebufferWidth 	= windowWidth;
		m_framebufferHeight = m_oldFramebufferHeight 	= windowHeight;
		
		m_windowPositionX 	= m_oldWindowPositionX 		= 0;
		m_windowPositionY	= m_oldWindowPositionY 		= 0;
	}
	
	/**
	 * Saves the current values into the 'old'-variables.
	 */
	public void saveCurrentState(){
		m_oldWindowWidth 		= m_windowWidth;
		m_oldWindowHeight 		= m_windowHeight;
		
		m_oldFramebufferWidth 	= m_framebufferWidth;
		m_oldFramebufferHeight  = m_framebufferHeight;
		
		m_oldWindowPositionX 	= m_windowPositionX;
		m_oldWindowPositionY 	= m_windowPositionY;
	}
	
	/**
	 * Returns the current window width
	 * 
	 * @return window width
	 */
	public int getWindowWidth(){
		return this.m_windowWidth;
	}
	
	/**
	 * Returns the current window height
	 * 
	 * @return window height
	 */
	public int getWindowHeight(){
		return this.m_windowHeight;
	}
	
	/**
	 * Returns the current framebuffer width
	 * 
	 * @return framebuffer width
	 */
	public int getFramebufferWidth(){
		return this.m_framebufferWidth;
	}
	
	/**
	 * Returns the current framebuffer height
	 * 
	 * @return framebuffer height
	 */
	public int getFramebufferHeight(){
		return this.m_framebufferHeight;
	}
}
