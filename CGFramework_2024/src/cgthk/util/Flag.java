package cgthk.util;

import cgthk.BVH_simple.BVH;

public class Flag
{
  public static Mesh createFlagMesh(int resolutionX, int resolutionY, float width, float height)
  {
    Mesh flagMesh = new Mesh(35044);
    
    int numX = resolutionX;
    int numY = resolutionY;
    float[] positions = new float[3 * numX * numY];
    int[] indices = new int[6 * (numX - 1) * (numY - 1)];
    for (int i = 0; i < numY; i++) {
      for (int j = 0; j < numX; j++)
      {
        positions[(i * 3 * numX + 3 * j)] = (-width / 2.0F + j * width / (numX - 1));
        positions[(i * 3 * numX + 3 * j + 1)] = (-height / 2.0F + i * height / (numY - 1));
        positions[(i * 3 * numX + 3 * j + 2)] = 0.0F;
      }
    }
    for (int i = 0; i < numY - 1; i++) {
      for (int j = 0; j < numX - 1; j++)
      {
        int offset = i * 6 * (numX - 1) + 6 * j;
        
        indices[(offset + 0)] = (i * numX + j);
        indices[(offset + 1)] = ((i + 1) * numX + j + 1);
        indices[(offset + 2)] = ((i + 1) * numX + j);
        
        indices[(offset + 3)] = (i * numX + j);
        indices[(offset + 4)] = (i * numX + j + 1);
        indices[(offset + 5)] = ((i + 1) * numX + j + 1);
      }
    }
    flagMesh.setAttribute(0, positions, 3);
    flagMesh.setIndices(indices);
    
    flagMesh.m_bvh = new BVH( positions, positions, positions, indices.length / 3, 10.0f, 0.01f );
    flagMesh.m_bvh.addFaces( indices, 0, indices.length / 3 );
    flagMesh.m_bvh.update( positions, positions, positions );
    
    return flagMesh;
  }
}
