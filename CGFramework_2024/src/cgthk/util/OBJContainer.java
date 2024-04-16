package cgthk.util;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cgthk.math.Vec3;



public class OBJContainer
{
	private static final int POSITION = 0;
	private static final int TEXCOORD = 1;
	private static final int NORMAL = 2;
	
	private ArrayList<OBJGroup> facegroups;
	
	
	private OBJContainer()
	{
		facegroups = new ArrayList<OBJGroup>();
	}
	
	
	public ArrayList<OBJGroup> getGroups()
	{
		return facegroups;
	}
	
	
	public static OBJContainer loadFile( String filepath )
	{
		return OBJContainer.loadFile( filepath, new Vec3(0.0f), new Vec3(1.0f) );
	}
	
	
	public static OBJContainer loadFile( String filepath, Vec3 translation, Vec3 scale )
	{
		String path    = IOUtils.pathOf( filepath );
		int    pathEnd = 1 + Math.max( path.lastIndexOf('\\'), path.lastIndexOf('/') );
		String folder  = path.substring( 0, pathEnd );
		
		OBJContainer   container = new OBJContainer();
		FloatArrayList positions = new FloatArrayList();
		FloatArrayList normals   = new FloatArrayList();
		FloatArrayList texCoords = new FloatArrayList();
		
		HashMap<String, ArrayList<String>> faceGroups   = new HashMap<String, ArrayList<String>>();
		ArrayList<String>                  currentGroup = new ArrayList<String>();
		HashMap<String, OBJMaterial>       materials    = new HashMap<String, OBJMaterial>();
		
		OBJMaterial currentMaterial  = new OBJMaterial();
		String      currentGroupName = "default";
		
		materials.put(  currentGroupName, currentMaterial );
		faceGroups.put( currentGroupName, currentGroup );
		
		try
		{
		    FileInputStream fstream = new FileInputStream( path );  
		    DataInputStream in      = new DataInputStream( fstream );  
		    BufferedReader  br      = new BufferedReader( new InputStreamReader(in) );
		    
		    String line = "";	   

		    while( (line = br.readLine()) != null )
		    {
		    	String[] line_parts = line.split( "\\s+" );
				
				if( line_parts[0].equalsIgnoreCase("v") )
				{
					positions.add( translation.x + scale.x * toFloat(line_parts[1], 0.0f) );
					positions.add( translation.y + scale.y * toFloat(line_parts[2], 0.0f) );
					positions.add( translation.z + scale.z * toFloat(line_parts[3], 0.0f) );
				}
				else if( line_parts[0].equalsIgnoreCase("vt") )
				{
					texCoords.add( toFloat(line_parts[1], 0.0f) );
					texCoords.add( toFloat(line_parts[2], 0.0f) );
					if( line_parts.length > 3 )
						texCoords.add( toFloat(line_parts[3], 2.0f) );
					else
						texCoords.add( 0.0f );
				}
				else if( line_parts[0].equalsIgnoreCase("vn") )
				{
					normals.add( toFloat(line_parts[1], 0.0f) );
					normals.add( toFloat(line_parts[2], 1.0f) );
					normals.add( toFloat(line_parts[3], 0.0f) );
				}
				else if( line_parts[0].equalsIgnoreCase("f") )
				{
					currentGroup.add( line_parts[1] );
					currentGroup.add( line_parts[2] );
					currentGroup.add( line_parts[3] );
					
					if( line_parts.length == 5 ) // the face is a quad
					{
						currentGroup.add( line_parts[1] );
						currentGroup.add( line_parts[3] );
						currentGroup.add( line_parts[4] );
					}
					else if( line_parts.length > 5 )
						System.err.println( "N-gons with more than 4 vertices are not supported!" );
				}
				else if( line_parts[0].equalsIgnoreCase("mtllib") )
				{
					materials = OBJMaterial.parseMTL( folder, line_parts[1] );
				}
				else if( line_parts[0].equalsIgnoreCase("usemtl") )
				{
					String      materialName = line_parts[1];
					OBJMaterial tempMaterial = materials.get( materialName );
					
					if( tempMaterial != null )
						currentMaterial = tempMaterial;
					
					materials.put( currentGroupName, currentMaterial );
				}
				else if( line_parts[0].equalsIgnoreCase("g") )
				{
					currentGroup = faceGroups.get( line_parts[1] );
					currentGroupName = line_parts[1];
					
					if( currentGroup == null )
					{
						currentGroup = new ArrayList<String>();
						faceGroups.put( line_parts[1], currentGroup );
					}
				}
		    }
		    
		    fstream.close();
		    in.close();
		    br.close();

		} 
		catch( Exception e ) 
		{
			System.err.println( "*Error* Can't read OBJ file: " + path );
			e.printStackTrace();
		}
		
	    
		for( Entry<String, ArrayList<String>> entry : faceGroups.entrySet() )
		{
			ArrayList<String> faceGroup = entry.getValue();
			String            groupName = entry.getKey();
			OBJMaterial       material  = materials.get( groupName );
	    	
			if( faceGroup.size() < 3 )
				continue;
			
			container.facegroups.add( createOBJGroup(positions, texCoords, normals, faceGroup, material) );
		}
		
		return container;
	}
	
		
	private static String vertexToString( int positionIndex, int texCoordIndex, int normalIndex )
	{
		return "p" + positionIndex + "t" + texCoordIndex + "n" + normalIndex;
	}
	
	
	private static int toInt( String string, int defaultValue )
	{
		try
		{
			return Integer.parseInt( string );
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	
	private static float toFloat( String string, float defaultValue )
	{
		try
		{
			return Float.parseFloat( string );
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	
	private static OBJGroup createOBJGroup( FloatArrayList    positions, 
											FloatArrayList    texCoords, 
											FloatArrayList    normals, 
											ArrayList<String> faceGroup, 
											OBJMaterial       material )
	{
		FloatArrayList groupPositions = new FloatArrayList();
		FloatArrayList groupTexCoords = new FloatArrayList();
		FloatArrayList groupNormals   = new FloatArrayList(); 
		IntArrayList   groupIndices   = new IntArrayList();
				    
	    HashMap<String, Integer> uniqueVertices = new HashMap<>();
	    int                      nextVertexID   = 0;
	    
    	for( int index = 0; index < faceGroup.size(); index += 3 )
	    {
    		// ===========================
    		// attributeIndices0: p0/t0/n0
    		// attributeIndices1: p1/t1/n1
    		// attributeIndices2: p2/t2/n2
    		// ===========================
    		
	    	String[] attributeIndices0 = faceGroup.get( index     ).split( "/" );
	    	String[] attributeIndices1 = faceGroup.get( index + 1 ).split( "/" );
	    	String[] attributeIndices2 = faceGroup.get( index + 2 ).split( "/" );

	    	
	    	// =======================================================
	    	// subtracting 1 since obj indices start at 1 instead of 0
	    	// =======================================================
	    	
	    	int position0 = toInt( attributeIndices0[POSITION], 1 ) - 1;
	    	int position1 = toInt( attributeIndices1[POSITION], 1 ) - 1;
	    	int position2 = toInt( attributeIndices2[POSITION], 1 ) - 1;
	    	
	    	int texcoord0 = position0;
	    	int texcoord1 = position1;
	    	int texcoord2 = position2;

	    	int normal0 = position0;
	    	int normal1 = position1;
	    	int normal2 = position2;
	    	
	    	if( attributeIndices0.length > 1 )
	    	{
		    	texcoord0 = toInt( attributeIndices0[TEXCOORD], 1 ) - 1;
		    	texcoord1 = toInt( attributeIndices1[TEXCOORD], 1 ) - 1;
		    	texcoord2 = toInt( attributeIndices2[TEXCOORD], 1 ) - 1;
		    	
		    	if( attributeIndices0.length > 2 )
		    	{
			    	normal0 = toInt( attributeIndices0[NORMAL], 1 ) - 1;
			    	normal1 = toInt( attributeIndices1[NORMAL], 1 ) - 1;
			    	normal2 = toInt( attributeIndices2[NORMAL], 1 ) - 1;
		    	}
	    	}
	    	

	    	// ==============================================================
	    	// really ugly and most likely extremely slow to use strings here
	    	// ==============================================================
	    	
	    	String v0String = vertexToString( position0, texcoord0, normal0 );
	    	String v1String = vertexToString( position1, texcoord1, normal1 );
	    	String v2String = vertexToString( position2, texcoord2, normal2 );

	    	Integer index0 = uniqueVertices.get( v0String );
	    	Integer index1 = uniqueVertices.get( v1String );
	    	Integer index2 = uniqueVertices.get( v2String );
	    	
	    	if( index0 == null )
	    	{
	    		OBJContainer.addAttributeVec3( groupPositions, positions, position0 );
	    		OBJContainer.addAttributeVec3( groupTexCoords, texCoords, texcoord0 );
	    		OBJContainer.addAttributeVec3( groupNormals,   normals,   normal0 );
		    	
	    		index0 = nextVertexID;
	    		uniqueVertices.put( v0String, index0 );
	    		
	    		nextVertexID++;
	    	}
	    	
	    	if( index1 == null )
	    	{
	    		OBJContainer.addAttributeVec3( groupPositions, positions, position1 );
	    		OBJContainer.addAttributeVec3( groupTexCoords, texCoords, texcoord1 );
	    		OBJContainer.addAttributeVec3( groupNormals,   normals,   normal1 );
		    	
	    		index1 = nextVertexID;
	    		uniqueVertices.put( v1String, index1 );
	    		
	    		nextVertexID++;
	    	}
	    	
	    	if( index2 == null )
	    	{
	    		OBJContainer.addAttributeVec3( groupPositions, positions, position2 );
	    		OBJContainer.addAttributeVec3( groupTexCoords, texCoords, texcoord2 );
	    		OBJContainer.addAttributeVec3( groupNormals,   normals,   normal2 );
		    	
	    		index2 = nextVertexID;
	    		uniqueVertices.put( v2String, index2 );
	    		
	    		nextVertexID++;
	    	}

	    	groupIndices.add( index0 );
	    	groupIndices.add( index1 );
	    	groupIndices.add( index2 );
	    }
    	
    	groupPositions.trimToSize();
    	groupTexCoords.trimToSize();
    	groupNormals.trimToSize();
    	groupIndices.trimToSize();
    	
    	return new OBJGroup( groupPositions.toArray(), 
							 groupNormals.toArray(), 
							 groupTexCoords.toArray(), 
							 groupIndices.toArray(),
							 material );
	}
	
	
	/**
	 * Convenience function to reduce duplicate code
	 */
	private static void addAttributeVec3( FloatArrayList destination, FloatArrayList source, int sourceIndex )
	{
		if( source.size() > sourceIndex * 3 + 2 )
    	{
			destination.add( source.get(sourceIndex * 3 + 0) );
			destination.add( source.get(sourceIndex * 3 + 1) );
			destination.add( source.get(sourceIndex * 3 + 2) );
    	}
    	else
    	{
    		destination.add( 0.0f );
    		destination.add( 0.0f );
    		destination.add( 0.0f );
    	}
	}
}
