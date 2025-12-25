package org.geysermc.hydraulic.pack.modules;

import com.google.auto.service.AutoService;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.util.GeoUtil;
import org.geysermc.pack.converter.util.JsonMappings;
import org.jetbrains.annotations.NotNull;

@AutoService(PackModule.class)
public class HydraulicPackModule extends PackModule<HydraulicPackModule> {
    private static final JsonMappings mappings = JsonMappings.getMapping("textures");

    public HydraulicPackModule() {
        this.postProcess(context -> {
            // Map all block textures files as valid names
            // The JsonMappings returns keys like "block/stone" which we need to process
            mappings.map("block").forEach(blockPath -> {
                // blockPath will be in format like "block/stone" or just the texture name
                String textureName = blockPath.startsWith("block/") ? blockPath.substring(6) : blockPath;
                context.bedrockResourcePack().addBlockTexture(Constants.MOD_ID + ":" + textureName, "textures/blocks/" + textureName);
            });

            // Map all item textures files as valid names
            mappings.map("item").forEach(itemPath -> {
                String textureName = itemPath.startsWith("item/") ? itemPath.substring(5) : itemPath;
                context.bedrockResourcePack().addItemTexture(Constants.MOD_ID + ":" + textureName, "textures/items/" + textureName);
            });

            // Add the empty geometry
            context.bedrockResourcePack().addBlockModel(GeoUtil.empty("geometry." + Constants.MOD_ID + ".empty"), "empty.json");
        });
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<HydraulicPackModule> context) {
        return context.mod().id().equals(Constants.MOD_ID);
    }
}
