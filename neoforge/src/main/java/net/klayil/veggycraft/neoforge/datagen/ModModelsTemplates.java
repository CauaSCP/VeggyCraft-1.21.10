package net.klayil.veggycraft.neoforge.datagen;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModModelsTemplates {
    public static final TextureSlot PARTICLE = TextureSlot.create("particle");
    public static final TextureSlot DOWN = TextureSlot.create("down");
    public static final TextureSlot UP = TextureSlot.create("up");
    public static final TextureSlot SIDE = TextureSlot.create("side");



    // 2. Create the ExtendedModelTemplate
    public static ModelTemplate CUSTOM_MOLASSES_TEMPLATE = CUSTOM_MOLASSES_TEMPLATE = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/block")),
            Optional.empty(),
            PARTICLE, DOWN, UP, SIDE
    ) {
        public Map<TextureSlot, ResourceLocation> createMap(TextureMapping textureMapping) {
            return (Map<TextureSlot, ResourceLocation>) Streams.concat(this.requiredSlots.stream(), textureMapping.getForced())
                    .collect(ImmutableMap.toImmutableMap(Function.identity(), textureMapping::get));
        }

        @Override
        public ResourceLocation create(ResourceLocation modelLocation,
                                       TextureMapping textureMapping,
                                       BiConsumer<ResourceLocation, ModelInstance> output) {
            // Get the base template instance
            JsonObject baseInstance = this.createBaseTemplate(modelLocation,
                    this.createMap(textureMapping));

            // Create custom instance with our elements
            output.accept(modelLocation, () -> {
                JsonElement json = (JsonElement) baseInstance;

                // Build the elements array
                JsonArray elements = new JsonArray();

                // First element: Outer cube [0,0,0] to [16,16,16]
                JsonObject outerCube = new JsonObject();
                outerCube.add("from", createJsonArray(0, 0, 0));
                outerCube.add("to", createJsonArray(16, 16, 16));

                JsonObject outerFaces = new JsonObject();
                for (Direction dir : Direction.values()) {
                    JsonObject face = new JsonObject();
                    face.addProperty("texture", "#down");
                    face.addProperty("cullface", dir.getSerializedName());
                    outerFaces.add(dir.getSerializedName(), face);
                }
                outerCube.add("faces", outerFaces);
                elements.add(outerCube);

                // Second element: Inner cube [1,1,1] to [15,15,15]
                JsonObject innerCube = new JsonObject();
                innerCube.add("from", createJsonArray(1, 1, 1));
                innerCube.add("to", createJsonArray(15, 15, 15));

                JsonObject innerFaces = new JsonObject();

                // Down face
                JsonObject downFace = new JsonObject();
                downFace.add("uv", createJsonArray(1, 1, 15, 15));
                downFace.addProperty("texture", "#down");
                innerFaces.add("down", downFace);

                // Up face
                JsonObject upFace = new JsonObject();
                upFace.add("uv", createJsonArray(1, 1, 15, 15));
                upFace.addProperty("texture", "#up");
                innerFaces.add("up", upFace);

                // Side faces
                String[] sideNames = {"north", "south", "west", "east"};
                for (String side : sideNames) {
                    JsonObject sideFace = new JsonObject();
                    sideFace.add("uv", createJsonArray(1, 1, 15, 15));
                    sideFace.addProperty("texture", "#side");
                    innerFaces.add(side, sideFace);
                }

                innerCube.add("faces", innerFaces);
                elements.add(innerCube);

                JsonObject obj = json.getAsJsonObject();

                obj.add("elements", elements);


                return obj;
            });

            return modelLocation;
        }
    };

    private static JsonArray createJsonArray(int... values) {
        JsonArray array = new JsonArray();
        for (int value : values) {
            array.add(value);
        }
        return array;
    }
}
