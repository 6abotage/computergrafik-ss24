/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2018 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */


#version 330
#extension GL_ARB_explicit_attrib_location : enable


layout(location=0) in vec3 aPosition;
layout(location=1) in vec3 aNormal;

out vec3 vPosition;
out vec3 vNormal;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;


void main(void) 
{
	vPosition = vec3( uModel * vec4(aPosition, 1.0) );
	vNormal   = vec3( uModel * vec4(aNormal,   0.0) );

	gl_Position = uProjection * uView * uModel * vec4(aPosition, 1.0);
}