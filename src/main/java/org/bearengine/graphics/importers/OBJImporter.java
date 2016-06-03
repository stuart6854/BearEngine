package main.java.org.bearengine.graphics.importers;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.*;
import main.java.org.bearengine.utils.ResourceLoader;

import java.io.*;

/**
 * Created by Stuart on 27/05/2016.
 */
public class OBJImporter extends ModelImporter{

    @Override
    public Mesh LoadMesh(String path) {
        path = "/main/java/resources/" + path;
        Debug.log("OBJImporter -> Loading Mesh: " + path);

        Mesh mesh = null;
        Material material = null;

        try {
            InputStream stream = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
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
                        vertices.add(Float.valueOf(tokens[1]));
                        vertices.add(Float.valueOf(tokens[2]));
                        vertices.add(Float.valueOf(tokens[3]));
                        break;
                    case "VT":
                    case "vt":
                        uvs.add(Float.valueOf(tokens[1]));
                        uvs.add(Float.valueOf(tokens[2]));
                        break;
                    case "VN":
                    case "vn":
                        normals.add(Float.valueOf(tokens[1]));
                        normals.add(Float.valueOf(tokens[2]));
                        normals.add(Float.valueOf(tokens[3]));
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
            Debug.log("OBJImporter -> " + indices.size()/3 + " Triangles Read(" + indices.size() + " verts).");
            Debug.log("OBJImporter -> " + vertices.size()/3 + " Vertices Read.");
            Debug.log("OBJImporter -> " + uvs.size()/2 + " UVs Read.");
            Debug.log("OBJImporter -> " + normals.size()/3 + " Normals Read.");

            mesh = new Mesh();
            mesh.Mesh_Name = path.substring(path.indexOf("/") + 1);
            mesh.SetIndices(indices);
            mesh.SetVertices(vertices);
            mesh.SetUVs(uvs);
            mesh.SetNormals(normals);
            mesh.CreateRenderModel();
            mesh.material = (material != null) ? material : new Material();
            Debug.log("OBJImporter -> Mesh Loaded!");
        } catch(IOException e) {
            Debug.exception("OBJImporter -> Failed to Parse OBJ: " + path);
            e.printStackTrace();
        }

        if(mesh == null)
            Debug.error("OBJImporter -> Mesh to Load Mesh!");

        return mesh;
    }

    private void ParseFace(String[] tokens){
        int v1 = Integer.valueOf(tokens[1].split("/")[0]);
        int v2 = Integer.valueOf(tokens[2].split("/")[0]);
        int v3 = Integer.valueOf(tokens[3].split("/")[0]);

        indices.add(v1 - 1);
        indices.add(v2 - 1);
        indices.add(v3 - 1);
    }

    private Material ParseMTL(String path){
        Debug.log("OBJImporter -> Parsing MTL: " + path);
        Material material = new Material();

        try {
            InputStream stream = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
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
                        Texture texture = new Texture();
                        texture.UploadTexture(Image.GetImage(imagePath.replace("/main/java/resources/", ""), ResourceLoader.FileType.Internal));
                        material.SetTexture(texture);
                        break;
                    default:
                        break;
                }
            }
            Debug.log("OBJImporter -> Parsed MTL.");
        }catch(IOException e){
            Debug.exception("OBJImporter -> Failed to Parse MTL: " + path);
            e.printStackTrace();
        }

        return material;
    }

}


