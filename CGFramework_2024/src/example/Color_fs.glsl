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

out     vec4 FragColor;

in      vec3 vPosition;
in      vec3 vNormal;

uniform vec3 uColor;


void main(void)
{
    FragColor = vec4( uColor.rgb, 1.0 );
}