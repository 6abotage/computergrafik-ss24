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
 * Interface for the Sandbox class, to acces custom GUI elements from DEfaultGUIs layout function.
 * 
 * @author Mario
 *
 */
public interface NuklearCallback {

	void drawCustomGUI( NkContext ctx );
	
	void takeScreenshot();
	
}
