/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */


#version 330


out vec4 FragColor;

in  vec3 vColor;

uniform vec3 uColor;

void main(void)
{
	if( uColor.r > 0.0 || uColor.g > 0.0 || uColor.b > 0.0 )
		FragColor = vec4( uColor, 1.0 );
    else
    	FragColor = vec4( vColor, 1.0 );

	//FragColor = vec4( 1.0, 1.0, 0.0, 1.0);
}