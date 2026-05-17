package org.geysermc.hydraulic.mixin.ext;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.unnamed.creative.model.ItemTransform;

import java.util.Map;

@Mixin(
        targets = "team.unnamed.creative.serialize.minecraft.model.ModelSerializer",
        remap = false
)
public class ModelSerializerMixin {

    @Redirect(
            method =
                    "deserializeFromJson(" +
                            "Lcom/google/gson/JsonElement;" +
                            "Lnet/kyori/adventure/key/Key;" +
                            "Lteam/unnamed/creative/metadata/pack/PackFormat;" +
                            ")Lteam/unnamed/creative/model/Model;",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lteam/unnamed/creative/model/ItemTransform$Type;" +
                                    "valueOf(Ljava/lang/String;)" +
                                    "Lteam/unnamed/creative/model/ItemTransform$Type;"
            )
    )
    private ItemTransform.Type redirectItemTransformTypeValueOf(String name) {
        try {
            return ItemTransform.Type.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    @Redirect(
            method =
                    "deserializeFromJson(" +
                            "Lcom/google/gson/JsonElement;" +
                            "Lnet/kyori/adventure/key/Key;" +
                            "Lteam/unnamed/creative/metadata/pack/PackFormat;" +
                            ")Lteam/unnamed/creative/model/Model;",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Ljava/util/Map;" +
                                    "put(Ljava/lang/Object;Ljava/lang/Object;)" +
                                    "Ljava/lang/Object;"
            )
    )
    private Object redirectDisplayMapPut(
            Map instance,
            Object k,
            Object v
    ) {
        if (k == null) {
            return null;
        }

        return instance.put(k, v);
    }
}
