package org.bearengine.graphics.importers;

import org.bearengine.debug.Debug;
import org.bearengine.graphics.types.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Stuart on 27/05/2016.
 */
public class OBJImporter extends ModelImporter{

    @Override
    public Mesh LoadMesh(String path) {
        Debug.log("OBJImporter -> Loading Mesh: " + path);
        Mesh mesh = null;
        Material material = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;

            while((line = reader.readLine()) != null){
                String[] tokens = line.split("\\s+");

                if(tokens.length == 0) continue;
                //Debug.log(line);
                switch(tokens[0].trim()){
                    case "mtllib":
                        material = ParseMTL(path.substring(0, path.lastIndexOf("/") + 1) + tokens[1]);
                        break;
                    case "V":
                    case "v":
                        Vector3f vert = new Vector3f();
                        vert.x = Float.valueOf(tokens[1]);
                        vert.y = Float.valueOf(tokens[2]);
                        vert.z = Float.valueOf(tokens[3]);
                        vertices.add(vert);
                        break;
                    case "VT":
                    case "vt":
                        Vector2f uv = new Vector2f();
                        uv.x = Float.valueOf(tokens[1]);
                        uv.y = Float.valueOf(tokens[2]);
                        uvs.add(uv);
                        break;
                    case "VN":
                    case "vn":
                        Vector3f norm = new Vector3f();
                        norm.x = Float.valueOf(tokens[1]);
                        norm.y = Float.valueOf(tokens[2]);
                        norm.z = Float.valueOf(tokens[3]);
                        normals.add(norm);
                        break;
                    case "F":
                    case "f":
                        //TODO: Parse Quad Faces(4 vertices)
                        if(tokens.length > 4){
                            Debug.error("OBJImporter -> Quad Faces NOT Supported!");
                            break;
                        }
                        ParseFace(tokens);
                        break;
                    default: break;
                }
            }
            mesh = new Mesh();
            mesh.SetVertices(verticesOrdered);
            mesh.SetUVs(uvsOrdered);
            mesh.SetNormals(normalsOrdered);
            mesh.material = material;
        } catch(IOException e) {
            Debug.exception("OBJImporter -> Failed to Parse OBJ: " + path);
            e.printStackTrace();
        }

        if(mesh == null)
            Debug.error("OBJImporter -> Mesh Failed to Load!");
        else Debug.log("OBJImporter -> Mesh Loaded!");

        return mesh;
    }

    private void ParseFace(String[] tokens){
        int v1 = Integer.valueOf(tokens[1].split("/")[0]);
        int v2 = Integer.valueOf(tokens[2].split("/")[0]);
        int v3 = Integer.valueOf(tokens[3].split("/")[0]);

        AddOrderedVertex(vertices.get(v1 - 1));
        AddOrderedVertex(vertices.get(v2 - 1));
        AddOrderedVertex(vertices.get(v3 - 1));

        int vt1, vt2, vt3;
        if(!tokens[1].contains("//")){
            vt1 = Integer.valueOf(tokens[1].split("/")[1]);
            vt2 = Integer.valueOf(tokens[2].split("/")[1]);
            vt3 = Integer.valueOf(tokens[3].split("/")[1]);

            AddOrderedUV(uvs.get(vt1 - 1));
            AddOrderedUV(uvs.get(vt2 - 1));
            AddOrderedUV(uvs.get(vt3 - 1));
        }else{
            AddOrderedUV(new Vector2f());
            AddOrderedUV(new Vector2f());
            AddOrderedUV(new Vector2f());
        }



        int vn1, vn2, vn3;
        if(tokens[1].split("/").length == 3){
            vn1 = Integer.valueOf(tokens[1].split("/")[2]);
            vn2 = Integer.valueOf(tokens[2].split("/")[2]);
            vn3 = Integer.valueOf(tokens[3].split("/")[2]);

            AddOrderedNormal(normals.get(vn1 - 1));
            AddOrderedNormal(normals.get(vn2 - 1));
            AddOrderedNormal(normals.get(vn3 - 1));
        }else{
            AddOrderedNormal(new Vector3f());
            AddOrderedNormal(new Vector3f());
            AddOrderedNormal(new Vector3f());
        }
    }

    private Material ParseMTL(String path){
        Debug.log("OBJImporter -> Parsing MTL: " + path);
        Material material = new Material();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;

            while((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\s+");

                switch(tokens[0].trim()){
                    case "Ka":
                        Color ambient = new Color(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3]));
                        material.Ambient = ambient;
                        break;
                    case "Kd":
                        Color diffuse = new Color(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3]));
                        material.Diffuse = diffuse;
                        break;
                    case "Ks":
                        Color specular = new Color(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3]));
                        material.Specular = specular;
                        break;
                    case "map_Kd":
                        String imagePath = path.substring(0, path.lastIndexOf("/") + 1) + tokens[1];
                        Debug.log("Test: " + imagePath);
                        Texture texture = new Texture();
                        texture.UploadTexture(Image.loadImage(imagePath));
                        material.SetTexture(texture);
                        break;
                    default:
                        break;
                }
            }
        }catch(IOException e){
            Debug.exception("OBJImporter -> Failed to Parse MTL: " + path);
            e.printStackTrace();
        }

        return material;
    }

    private void AddOrderedVertex(Vector3f vert){
        verticesOrdered.add(vert.x);
        verticesOrdered.add(vert.y);
        verticesOrdered.add(vert.z);
    }

    private void AddOrderedUV(Vector2f uv){
        uvsOrdered.add(uv.x);
        uvsOrdered.add(uv.y);
    }

    private void AddOrderedNormal(Vector3f norm){
        normalsOrdered.add(norm.x);
        normalsOrdered.add(norm.y);
        normalsOrdered.add(norm.z);
    }

}


