package cgthk.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import cgthk.math.Vec3;



public class OBJMaterial
{
	private Vec3 ambientColor;
	private Vec3 diffuseColor;
	private Vec3 specularColor;
	
	private float specular;

	private String diffuseTexture;
	private String specularTexture;
	private String bumpTexture;
	
	
	public OBJMaterial()
	{
		this.ambientColor  = new Vec3();
		this.diffuseColor  = new Vec3( 1.0f );
		this.specularColor = new Vec3( 1.0f );
		
		this.specular = 1.0f;

		this.diffuseTexture  = null;
		this.specularTexture = null;
		this.bumpTexture     = null;
	}
	
	
	public OBJMaterial( Vec3   ambientColor,
						Vec3   diffuseColor,
						Vec3   specularColor,
						float  specular,
						String diffuseTexture,
						String specularTexture,
						String bumpTexture )
	{
		this.ambientColor  = ambientColor;
		this.diffuseColor  = diffuseColor;
		this.specularColor = specularColor;
		
		this.specular = specular;

		this.diffuseTexture  = diffuseTexture;
		this.specularTexture = specularTexture;
		this.bumpTexture     = bumpTexture;
	}
	
	
	public Vec3 getDiffuseColor()
	{
		return diffuseColor;
	}
	
	
	public Vec3 getAmbientColor()
	{
		return ambientColor;
	}
	
	
	public Vec3 getSpecularColor()
	{
		return specularColor;
	}
	
	
	public float getSpecular()
	{
		return specular;
	}
	
	
	public String getDiffuseTextureName()
	{
		return diffuseTexture;
	}
	
	
	public String getSpecularTextureName()
	{
		return specularTexture;
	}
	
	
	public String getBumpTextureName()
	{
		return bumpTexture;
	}
	
	
	public static HashMap<String, OBJMaterial> parseMTL( String folder, String filename )
	{
		if( filename.startsWith("./") )
			filename = filename.substring( 2 );
		
		String                       fullPath  = folder + filename;
		HashMap<String, OBJMaterial> materials = new HashMap<String, OBJMaterial>();
		
		try
		{
		    FileInputStream fstream = new FileInputStream( fullPath );  
		    DataInputStream in      = new DataInputStream( fstream );  
		    BufferedReader  br      = new BufferedReader( new InputStreamReader(in) );
		    
		    String line = "";
		    OBJMaterial currentMaterial = null;

		    while( (line = br.readLine()) != null )
		    {
		    	String[] parts = line.split( "\\s+" );
				
		    	if( parts[0].equalsIgnoreCase("newmtl") )
				{
					currentMaterial = new OBJMaterial();
					materials.put( parts[1], currentMaterial );
				}
				else if( parts[0].equalsIgnoreCase("Ka") )
				{
					currentMaterial.ambientColor.x = Float.parseFloat( parts[1] );
					currentMaterial.ambientColor.y = Float.parseFloat( parts[2] );
					currentMaterial.ambientColor.z = Float.parseFloat( parts[3] );
				}
				else if( parts[0].equalsIgnoreCase("Kd") )
				{
					currentMaterial.diffuseColor.x = Float.parseFloat( parts[1] );
					currentMaterial.diffuseColor.y = Float.parseFloat( parts[2] );
					currentMaterial.diffuseColor.z = Float.parseFloat( parts[3] );
				}
				else if( parts[0].equalsIgnoreCase("Ks") )
				{
					currentMaterial.specularColor.x = Float.parseFloat( parts[1] );
					currentMaterial.specularColor.y = Float.parseFloat( parts[2] );
					currentMaterial.specularColor.z = Float.parseFloat( parts[3] );
				}
				else if( parts[0].equalsIgnoreCase("Ns") )
				{
					currentMaterial.specular = Float.parseFloat( parts[1] );
				}
				else if( parts[0].equalsIgnoreCase("map_Kd") )
				{
					currentMaterial.diffuseTexture = parts[1];
				}
				else if( parts[0].equalsIgnoreCase("map_Ns") )
				{
					currentMaterial.specularTexture = parts[1];
				}
				else if( parts[0].equalsIgnoreCase("map_Bump") )
				{
					currentMaterial.bumpTexture = parts[1];
				}
		    }
		    
		    fstream.close();
		    in.close();
		    br.close();
		} 
		catch( Exception e ) 
		{
			System.err.println( "*Error* Can't read MTL file: " + folder + filename );
			e.printStackTrace();
		}
		
		return materials;
	}
}
