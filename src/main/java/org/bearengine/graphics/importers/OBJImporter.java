package main.java.org.bearengine.graphics.importers;

import main.java.org.bearengine.debug.Debug;
import main.java.org.bearengine.graphics.types.*;
import main.java.org.bearengine.utils.ResourceLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 27/05/2016.
 */
public class OBJImporter extends ModelImporter{

    private enum FaceDefType { VertsOnly, VertsUVs, All, VertsNormals };

    private List<Face> faces = new ArrayList<>();

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
                if(line.contains("#"))
                    line = line.substring(0, line.indexOf("#"));//Remove any comments from line

                String[] tokens = line.split("\\s+");

                if(tokens.length == 0) continue;
                //Debug.log(line);
                switch(tokens[0].trim()){
                    case "mtllib":
                        material = ParseMTL(path.substring(0, path.lastIndexOf("/") + 1) + tokens[1]);
                        break;
                    case "V":
                    case "v":
                        Vertices.add(Float.valueOf(tokens[1]));
                        Vertices.add(Float.valueOf(tokens[2]));
                        Vertices.add(Float.valueOf(tokens[3]));
                        break;
                    case "VT":
                    case "vt":
                        UVs.add(Float.valueOf(tokens[1]));
                        UVs.add(Float.valueOf(tokens[2]));
                        break;
                    case "VN":
                    case "vn":
                        Normals.add(Float.valueOf(tokens[1]));
                        Normals.add(Float.valueOf(tokens[2]));
                        Normals.add(Float.valueOf(tokens[3]));
                        break;
                    case "F":
                    case "f":
                        //TODO: Parse Quad Faces(4 Vertices)
                        if(tokens.length > 4){
                            Debug.error("OBJImporter -> Quad Faces NOT Supported!");
                            break;
                        }
                        ParseFace(tokens);
                        break;
                    default: break;
                }
            }
            Debug.log("OBJImporter -> " + faces.size() + " Triangles Read.");
            Debug.log("OBJImporter -> " + Vertices.size()/3 + " Vertices Read.");
            Debug.log("OBJImporter -> " + UVs.size()/2 + " UVs Read.");
            Debug.log("OBJImporter -> " + Normals.size()/3 + " Normals Read.");

            mesh = new Mesh();
            mesh.Mesh_Name = path.substring(path.indexOf("/") + 1);
            mesh = SortData(mesh);
            mesh.CreateRenderModel();
            mesh.material = (material != null) ? material : new Material();

            reader.close();
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
        Face face = new Face();

        FaceDefType defType = null;

        if(!tokens[1].contains("/")) defType = FaceDefType.VertsOnly;
        else if(tokens[1].contains("//")) defType = FaceDefType.VertsNormals;
        else if(tokens[1].split("/").length == 2) defType = FaceDefType.VertsUVs;
        else defType = FaceDefType.All;

        //Do Indices Anyway
        int v1 = Integer.valueOf(tokens[1].split("/")[0]);
        int v2 = Integer.valueOf(tokens[2].split("/")[0]);
        int v3 = Integer.valueOf(tokens[3].split("/")[0]);

        face.indices[0] = v1 - 1;
        face.indices[1] = v2 - 1;
        face.indices[2] = v3 - 1;

        if(defType == FaceDefType.All || defType == FaceDefType.VertsUVs) {
            int uv1 = Integer.valueOf(tokens[1].split("/")[1]);
            int uv2 = Integer.valueOf(tokens[2].split("/")[1]);
            int uv3 = Integer.valueOf(tokens[3].split("/")[1]);

            face.uvs = new int[3];
            face.uvs[0] = uv1 - 1;
            face.uvs[1] = uv2 - 1;
            face.uvs[2] = uv3 - 1;

        }

        if(defType == FaceDefType.All || defType == FaceDefType.VertsNormals) {
            int n1 = Integer.valueOf(tokens[1].split("/")[2]);
            int n2 = Integer.valueOf(tokens[2].split("/")[2]);
            int n3 = Integer.valueOf(tokens[3].split("/")[2]);

            face.normals = new int[3];
            face.normals[0] = n1 - 1;
            face.normals[1] = n2 - 1;
            face.normals[2] = n3 - 1;
        }

        faces.add(face);
    }

    private Mesh SortData(Mesh mesh){
        Debug.log("OBJImporter -> Sorting Mesh Data.");

        List<Integer> indices = new ArrayList<>();
        List<Float> vertices = new ArrayList<>();
        List<Float> uvs = new ArrayList<>();
        List<Float> normals = new ArrayList<>();

        List<VertexGroup> verticeGroups = new ArrayList<>();

        for(Face face : faces){
            for(int i = 0; i < 3; i++){
                VertexGroup group = new VertexGroup();
                group.vertexIndex = face.indices[i];
                if(face.uvs != null)
                    group.uvIndex = face.uvs[i];
                else
                    group.uvIndex = -1;

                if(face.normals != null)
                    group.normalIndex = face.normals[i];
                else
                    group.normalIndex = -1;

                if(verticeGroups.contains(group)){
                    indices.add(verticeGroups.indexOf(group));
                }else{
                    verticeGroups.add(group);
                    indices.add(verticeGroups.indexOf(group));
                }
            }

        }

        for(VertexGroup vertexGroup : verticeGroups){
            vertices.add(Vertices.get(vertexGroup.vertexIndex * 3));
            vertices.add(Vertices.get(vertexGroup.vertexIndex * 3 + 1));
            vertices.add(Vertices.get(vertexGroup.vertexIndex * 3 + 2));
            if(vertexGroup.uvIndex >= 0) {
                uvs.add(UVs.get(vertexGroup.uvIndex * 2));
                uvs.add(UVs.get(vertexGroup.uvIndex * 2 + 1));
            }
            if(vertexGroup.normalIndex >= 0) {
                normals.add(Normals.get(vertexGroup.normalIndex * 3));
                normals.add(Normals.get(vertexGroup.normalIndex * 3 + 1));
                normals.add(Normals.get(vertexGroup.normalIndex * 3 + 2));
            }
        }

        mesh.SetIndices(indices);
        mesh.SetVertices(vertices);
        mesh.SetUVs(uvs);
        mesh.SetNormals(normals);

        Debug.log("OBJImporter -> Mesh Data Sorted.");
        return mesh;
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

    private class Face{

        public int[] indices = new int[3];
        public int[] uvs;
        public int[] normals;

    }

    private class VertexGroup{

        public int vertexIndex;
        public int uvIndex = -1;
        public int normalIndex = -1;

        @Override
        public boolean equals(Object obj) {
            VertexGroup g = (VertexGroup)obj;

            if(g.vertexIndex != this.vertexIndex)
                return false;
            if(g.uvIndex != this.uvIndex)
                return false;
            if(g.normalIndex != this.normalIndex)
                return false;

            return true;
        }
    }

}


