package cgthk.util;

/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2018 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */

import org.lwjgl.nuklear.NkContext;

/**
 * This is the interface between the Main class in the cgthk.util package and the Sandbox classes
 * that can differ depending on exercise.
 * 
 * @author Mario
 * 
 */
public interface SandboxTemplate {

	void init( long window, GLFWWindowPropertyManager popertyManager/*int width, int height*/, NkContext ctx );
	
	void update( float deltaTime );
	
	void draw();
	
	void onResize( int width, int height );
	
	void layoutStandardGUI( NkContext ctx );
	
	void toggleShowGUI();
	
	void setupMouseInteractions( int button, int action );
	
	void setVSync( boolean value );
	
}
