package cgthk.util;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Main class responsible for most of the GLFW, OpenGL and Nuklear setup as well as running the
 * game loop. 
 * 
 * @author Mario
 * @version 0.9
 */
public class Main {
	
	// Main window
	private static long 	s_window;
	
	// Keeps track of window/framebuffer size or position changes
	private GLFWWindowPropertyManager windowProperties;
	
	// State saves
	private boolean 		m_vsync = true;
	private static boolean 	s_isFullscreen = false;
	
	// GUI Stuff
	private NuklearHelper 	m_nuklearHelper;
    
	private SandboxTemplate m_sb;
	
	public Main( SandboxTemplate sb, int windowWidth, int windowHeight ){
		this.m_sb = sb;
		windowProperties = new GLFWWindowPropertyManager( windowWidth, windowHeight );
	}
	
	public void run() {

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(s_window);
		glfwDestroyWindow(s_window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	/**
	 * Initialization function
	 */
	private void init() {
		// Setup GLFW error callback
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);  // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		// Setup for specific OpenGL versions
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); 
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	   
	    // Special handling for MacOS
	    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if (Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }	    
	    
		// Create the window
		s_window = glfwCreateWindow(windowProperties.m_windowWidth, windowProperties.m_windowHeight, "CG Framework 2023", NULL, NULL);
		if ( s_window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(s_window, pWidth, pHeight);
			
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			int windowPosX = (vidmode.width() - pWidth.get(0)) / 2;
			int windowPosY = (vidmode.height() - pHeight.get(0)) / 2;
			// Center the window
			glfwSetWindowPos(
				s_window,
				windowPosX,
				windowPosY
			);
			windowProperties.m_oldWindowWidth  = windowProperties.m_windowWidth;
			windowProperties.m_oldWindowHeight = windowProperties.m_windowHeight;
			
			windowProperties.m_windowPositionX = windowProperties.m_oldWindowPositionX = windowPosX;
			windowProperties.m_windowPositionY = windowProperties.m_oldWindowPositionY = windowPosY;
			
			glfwGetFramebufferSize(s_window, pWidth, pHeight);
			windowProperties.m_framebufferWidth = pWidth.get(0);
			windowProperties.m_framebufferHeight = pHeight.get(0);
			
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(s_window);
				
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(s_window);
	}
	
	/**
	 * The main loop.
	 */
	private void loop() {
		// Critical for interoperation of GLFW and OpenGL
		GL.createCapabilities();
		
		// GUI setup
		m_nuklearHelper = new NuklearHelper( s_window );
		m_nuklearHelper.init();
		
		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Instantiate the Sandbox
		m_sb.init( s_window, windowProperties,/*windowProperties.m_framebufferWidth, windowProperties.m_framebufferHeight,*/ m_nuklearHelper.ctx );
		
		// Setup Callback functions
		setupKeyInput( m_sb, m_nuklearHelper );
		setSizeCallback();
		
		// Instantiate time variables
		float   deltaTime = 0.0f;
		long    lastTime  = 0;
		
		// Run the rendering loop
		while ( !glfwWindowShouldClose(s_window) ) {
			
			// Calculate DeltaTime
			long time = System.nanoTime();
        	deltaTime = (float)(time - lastTime) * 1e-9f;
			lastTime  = time;
			
			// Update Resolutions
			try (MemoryStack stack = stackPush()) {
	            IntBuffer w = stack.mallocInt(1);
	            IntBuffer h = stack.mallocInt(1);

	            glfwGetWindowSize(s_window, w, h);
	            windowProperties.m_windowWidth = w.get(0);
	            windowProperties.m_windowHeight = h.get(0);
	            
	            glfwGetFramebufferSize(s_window, w, h);
	            windowProperties.m_framebufferWidth = w.get(0);
	            windowProperties.m_framebufferHeight = h.get(0);
	            
	            glfwGetWindowPos(s_window, w, h);
	            windowProperties.m_windowPositionX = w.get(0);
	            windowProperties.m_windowPositionY = h.get(0);
//	            sb.setWindowSize(display_width, display_height);
	        }
			
			// Update the scene
			m_sb.update(deltaTime);
			
			// Draw the updated scene
			m_sb.draw();		
			
			// Disables GUI during Fullscreen mode
			if(!s_isFullscreen)
				m_sb.layoutStandardGUI(m_nuklearHelper.ctx); 
			
			// Poll events - calls the GLFWPollEvents
			m_nuklearHelper.pollEvents();
			
			// Draw GUI
			m_nuklearHelper.render(1, NuklearHelper.MAX_VERTEX_BUFFER, NuklearHelper.MAX_ELEMENT_BUFFER, 
					windowProperties.m_framebufferWidth, windowProperties.m_framebufferHeight, 
					windowProperties.m_windowWidth, windowProperties.m_windowHeight);
			
			// Swap the color buffers to display current frame
			glfwSwapBuffers(s_window); 
		}
		
		// Let Nuklear do some cleanup when program is finished
		m_nuklearHelper.shutdown();
	}
	
	
	/*
	 * Helper functions
	 */
	
	/**
	 *  Setting up the key-input by calling GLFW's glfwSetKeyCallback() function with the according keys.
	 *  The KeyCallback means that an action is performed every time a key is pressed or released.
	 *  
	 *  This function is for keys that are pressed once - constant key input is handled by 'handleConstantKEyInput()'
	 */
	public void setupKeyInput( SandboxTemplate sb, NuklearHelper nH ){
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(s_window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop	
			if ( key == GLFW_KEY_V && action == GLFW_RELEASE )
				toggleVSync();	
			if ( key == GLFW_KEY_F && action == GLFW_RELEASE ){
				toggleFullscreen();
			}
			if ( key == GLFW_KEY_G && action == GLFW_RELEASE ){
				m_sb.toggleShowGUI();
			}
		});
		
		glfwSetMouseButtonCallback(s_window, ((window, button, action, mods) -> {
		
			// Setup initial position of the mousecursor for camera rotation
			sb.setupMouseInteractions(button, action);
			
			nH.setupMouseInteractions(nH.ctx, window, button, action);				
		}));
	}
	
	/**
	 * Set up the size-callback. Gets called whenever the window size changes.
	 */
	public void setSizeCallback(){
		glfwSetWindowSizeCallback(s_window, (window, width, height) -> {
			windowProperties.m_windowWidth = width;
			windowProperties.m_windowHeight = height;
			
			try ( MemoryStack stack = stackPush() ) {
				IntBuffer pWidth = stack.mallocInt(1); // int*
				IntBuffer pHeight = stack.mallocInt(1); // int*				
				glfwGetFramebufferSize(s_window, pWidth, pHeight);
				windowProperties.m_framebufferWidth = pWidth.get(0);
				windowProperties.m_framebufferHeight = pHeight.get(0);
			}
			m_sb.onResize(windowProperties.m_framebufferWidth, windowProperties.m_framebufferHeight);
		});
	}
	
	/**
	 * Toggles between window and fullscreen mode.
	 */
	private void toggleFullscreen(){
		if( !s_isFullscreen ){
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			m_sb.onResize(vidmode.width(), vidmode.height());
			
			windowProperties.saveCurrentState();
			
			glfwSetWindowMonitor(s_window, glfwGetPrimaryMonitor(), 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
			int toggle = m_vsync ? 1 : 0;
			glfwSwapInterval( toggle );
		}else{
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			m_sb.onResize(windowProperties.m_oldFramebufferWidth, windowProperties.m_oldFramebufferHeight);
			glfwSetWindowMonitor(s_window, 0, windowProperties.m_oldWindowPositionX, windowProperties.m_oldWindowPositionY, 
					windowProperties.m_oldWindowWidth, windowProperties.m_oldWindowHeight, vidmode.refreshRate());
		}
		s_isFullscreen = !s_isFullscreen;
	}
	
	/**
	 * Toggle VSYNC on and off
	 */
	private void toggleVSync(){
		m_vsync = !m_vsync;
		m_sb.setVSync(m_vsync);
	}

}
