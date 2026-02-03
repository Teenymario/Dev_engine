package engine.graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static engine.maths.Vector2.Vector2f;
import static engine.maths.Vector3.Vector3f;

public class ObjectMesh {

    public static Mesh construct(String objPath) {
        //Mesh data
        List<Vector3f> verts = new ArrayList<>();
        List<Vector3f> norms = new ArrayList<>();
        List<Vector2f> texts = new ArrayList<>();
        List<Integer> inds = new ArrayList<>();
        List<Vertex> vertexList = new ArrayList<>();
        //Mesh data

        //Materials
        String mtlPath;
        int curMtl = 0;
        Map<String, Integer> materialList = new HashMap<>();
        Map<String, int[]> textureList = new HashMap<>();
        int matCounter = -1;
        int textCounter = -1;
        List<Material> materials = new ArrayList<>();
        //Materials

        //Reader variables
        String line;
        String[] currentLine;
        String[][] vertexData = new String[3][3];
        String[] tokens;
        //Reader variables

        //Obj file loader
        try (BufferedReader reader = new BufferedReader(new FileReader(objPath))) {
            while((line = reader.readLine()) != null) {
                tokens = line.split(" ");

                switch(tokens[0]) {
                    case "mtllib":
                        String[] pathElements = objPath.split("/");
                        pathElements[pathElements.length - 1] = tokens[1];
                        mtlPath = String.join("/", pathElements);


                        //Mtl file loader
                        try (BufferedReader mtlReader = new BufferedReader(new FileReader(mtlPath))) {
                            while ((line = mtlReader.readLine()) != null) {
                                tokens = line.split(" ");

                                switch(tokens[0]) {
                                    case "newmtl":
                                        matCounter++;
                                        materialList.put(tokens[1], matCounter);
                                        materials.add(new Material());
                                        break;
                                    case "Ns":
                                        materials.get(matCounter).specExp(Float.parseFloat(tokens[1]));
                                        break;
                                    case "Ka":
                                        materials.get(matCounter).ambient(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                                        break;
                                    case "Kd":
                                        materials.get(matCounter).diffuse(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                                        break;
                                    case "Ks":
                                        materials.get(matCounter).specular(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                                        break;
                                    case "Ke":
                                        materials.get(matCounter).emissive(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                                        break;
                                    case "Ni":
                                        materials.get(matCounter).opticalDensity(Float.parseFloat(tokens[1]));
                                        break;
                                    case "d":
                                        materials.get(matCounter).transparency(Float.parseFloat(tokens[1]));
                                        break;
                                    case "Tr":
                                        materials.get(matCounter).transparency(1 - Float.parseFloat(tokens[1]));
                                        break;
                                    case "map_Kd":
                                        materials.get(matCounter).path(tokens[1]);
                                        if(textureList.containsKey(tokens[1])) {
                                            materials.get(matCounter).textID(textureList.get(tokens[1])[0]);
                                            materials.get(matCounter).textureID(textureList.get(tokens[1])[1]);
                                        } else {
                                            textCounter++;
                                            materials.get(matCounter).createTexture();
                                            textureList.put(tokens[1], new int[]{textCounter, materials.get(matCounter).textureID()});
                                            materials.get(matCounter).textID(textCounter);
                                        }
                                        break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Mtl file loader


                        break;
                    case "o":
                        curMtl = 0;
                        System.gc();
                        break;
                    case "v":
                        verts.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                        break;
                    case "vn":
                        norms.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                        break;
                    case "vt":
                        texts.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                        break;
                    case "usemtl":
                        curMtl = materialList.get(tokens[1]);
                        break;
                    case "f":
                        currentLine = line.split(" ");
                        vertexData[0] = currentLine[1].split("/");
                        vertexData[1] = currentLine[2].split("/");
                        vertexData[2] = currentLine[3].split("/");

                        for(int i = 0; i < 3; i++) {
                            int curVert = Integer.parseInt(vertexData[i][0]) - 1;
                            inds.add(vertexList.size());
                            Vector2f curTex = texts.get(Integer.parseInt(vertexData[i][1]) - 1);
                            Vector3f curNorm = norms.get(Integer.parseInt(vertexData[i][2]) - 1);

                            vertexList.add(new Vertex(verts.get(curVert), new Vector3f(curNorm.x, curNorm.y, curNorm.z), new Vector2f(curTex.x, 1 - curTex.y), curMtl));
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Obj file loader

        System.gc();

        //Construct mesh
        int width = 0;
        int height = 0;
        List<Integer> textureIDs = new ArrayList<>();
        for(Material material : materials) {
            if(!material.path().equals("") && !textureIDs.contains(material.textureID())) {
                textureIDs.add(material.textureID());
                width = material.width();
                height = material.height();
            }
        }

        float[] floatArray = new float[materials.size() * 16];
        int counter = 0;

        //Conversion to float array
        for(Material material : materials) {
            floatArray[counter * 16] = material.textID();
            floatArray[counter * 16 + 1] = material.transparency();
            floatArray[counter * 16 + 2] = material.specExp();
            floatArray[counter * 16 + 3] = material.opticalDensity();

            //Vector3f's
            floatArray[counter * 16 + 4] = material.ambient().x;
            floatArray[counter * 16 + 5] = material.ambient().y;
            floatArray[counter * 16 + 6] = material.ambient().z;

            floatArray[counter * 16 + 7] = material.diffuse().x;
            floatArray[counter * 16 + 8] = material.diffuse().y;
            floatArray[counter * 16 + 9] = material.diffuse().z;

            floatArray[counter * 16 + 10] = material.specular().x;
            floatArray[counter * 16 + 11] = material.specular().y;
            floatArray[counter * 16 + 12] = material.specular().z;

            floatArray[counter * 16 + 13] = material.emissive().x;
            floatArray[counter * 16 + 14] = material.emissive().y;
            floatArray[counter * 16 + 15] = material.emissive().z;
            counter++;
            //Vector3f's
        }
        //Conversion to float array

        return new Mesh(vertexList.toArray(new Vertex[0]), inds.stream().mapToInt(Integer::intValue).toArray(), floatArray, textureIDs.stream().mapToInt(Integer::intValue).toArray(), width, height);
        //Construct mesh
    }
}