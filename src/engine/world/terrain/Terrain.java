package engine.world.terrain;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Vertex;
import engine.maths.Matrix4f;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Terrain {

    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    public float x, z;
    public int meshID;
    public String texture;
    public Matrix4f transformationMatrix;

    public Terrain(int gridX, int gridZ, String texture) {
        this.texture = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.meshID = generateTerrain();
        this.transformationMatrix = Matrix4f.transform(new Vector3f(x, 0, z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    private int generateTerrain(){
        int count = VERTEX_COUNT * VERTEX_COUNT;
        Vertex[] vertices = new Vertex[count];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer] = new Vertex(new Vector3f((float)j/((float)VERTEX_COUNT - 1) * SIZE, 0, (float)i/((float)VERTEX_COUNT - 1) * SIZE), new Vector3f(0, 1, 0), new Vector2f((float)j/((float)VERTEX_COUNT - 1), (float)i/((float)VERTEX_COUNT - 1)), 0);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        float[] floatArray = new float[16];
        Material material = new Material(texture);
        material.createTexture();
        material.textID(0);

        floatArray[0] = material.textID();
        floatArray[1] = material.transparency();
        floatArray[2] = material.specExp();
        floatArray[3] = material.opticalDensity();

        //Vector3f's
        floatArray[4] = material.ambient().x;
        floatArray[5] = material.ambient().y;
        floatArray[6] = material.ambient().z;

        floatArray[7] = material.diffuse().x;
        floatArray[8] = material.diffuse().y;
        floatArray[9] = material.diffuse().z;

        floatArray[10] = material.specular().x;
        floatArray[11] = material.specular().y;
        floatArray[12] = material.specular().z;

        floatArray[13] = material.emissive().x;
        floatArray[14] = material.emissive().y;
        floatArray[15] = material.emissive().z;
        
        return new Mesh(vertices, indices, floatArray, new int[]{material.textureID()}, material.width(), material.height()).constructMesh();
    }

}
