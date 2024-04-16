package cgthk.util;



public class OBJGroup
{
	private float[] positions;
	private float[] normals;
	private float[] texCoords;
	private int[]   indices;
	
	private OBJMaterial material;
	
	
	public OBJGroup( float[] positions, float[] normals, float[] texCoords, int[] indices, OBJMaterial material )
	{
		this.positions = positions;
		this.normals   = normals;
		this.texCoords = texCoords;
		this.indices   = indices;
		this.material  = material;
	}
	
	
	public float[] getPositions()
	{
		return positions;
	}
	
	
	public float[] getNormals()
	{
		return normals;
	}
	
	
	public float[] getTexCoords()
	{
		return texCoords;
	}
	
	
	public int[] getIndices()
	{
		return indices;
	}
	
	
	public OBJMaterial getMaterial()
	{
		return material;
	}
}
