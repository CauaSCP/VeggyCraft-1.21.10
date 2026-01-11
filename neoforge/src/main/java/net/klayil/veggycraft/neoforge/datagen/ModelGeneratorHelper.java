package net.klayil.veggycraft.neoforge.datagen;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;

public class ModelGeneratorHelper {

    public static JsonObject createMolassesModel(ResourceLocation modelLocation, TextureMapping textures) {
        JsonObject root = new JsonObject();

        // 1. Set parent
        root.addProperty("parent", "block/block");

        // 2. Add textures object
//        JsonObject texturesObj = new JsonObject();
//        texturesObj.addProperty("particle", textures.get(VeggyModelTemplates.PARTICLE).toString());
//        texturesObj.addProperty("down", textures.get(VeggyModelTemplates.DOWN).toString());
//        texturesObj.addProperty("up", textures.get(VeggyModelTemplates.UP).toString());
//        texturesObj.addProperty("side", textures.get(VeggyModelTemplates.SIDE).toString());
//        root.add("textures", texturesObj);

        // 3. Create the elements array
        JsonArray elements = new JsonArray();

        // First element: Outer cube [0,0,0] to [16,16,16]
        JsonObject outerCube = new JsonObject();
        outerCube.add("from", createArray(0, 0, 0));
        outerCube.add("to", createArray(16, 16, 16));

        JsonObject outerFaces = new JsonObject();
        String[] outerDirections = {"down", "up", "north", "south", "west", "east"};
        for (String face : outerDirections) {
            JsonObject faceObj = new JsonObject();
            faceObj.addProperty("texture", "#down"); // All faces use #down
            faceObj.addProperty("cullface", face);
            outerFaces.add(face, faceObj);
        }
        outerCube.add("faces", outerFaces);
        elements.add(outerCube);

        // Second element: Inner cube [1,1,1] to [15,15,15]
        JsonObject innerCube = new JsonObject();
        innerCube.add("from", createArray(1, 1, 1));
        innerCube.add("to", createArray(15, 15, 15));

        JsonObject innerFaces = new JsonObject();

        // Down face
        JsonObject downFace = new JsonObject();
        downFace.add("uv", createArray(1, 1, 15, 15));
        downFace.addProperty("texture", "#down");
        innerFaces.add("down", downFace);

        // Up face
        JsonObject upFace = new JsonObject();
        upFace.add("uv", createArray(1, 1, 15, 15));
        upFace.addProperty("texture", "#up");
        innerFaces.add("up", upFace);

        // Side faces
        String[] sideDirections = {"north", "south", "west", "east"};
        for (String face : sideDirections) {
            JsonObject sideFace = new JsonObject();
            sideFace.add("uv", createArray(1, 1, 15, 15));
            sideFace.addProperty("texture", "#side");
            innerFaces.add(face, sideFace);
        }

        innerCube.add("faces", innerFaces);
        elements.add(innerCube);

        root.add("elements", elements);
        return root;
    }

    // Helper to create JSON arrays for coordinates
    private static JsonArray createArray(int... values) {
        JsonArray array = new JsonArray();
        for (int value : values) {
            array.add(value);
        }
        return array;
    }

    // Helper to create JSON arrays for UV coordinates (floats)
    private static JsonArray createArray(float... values) {
        JsonArray array = new JsonArray();
        for (float value : values) {
            array.add(value);
        }
        return array;
    }
}