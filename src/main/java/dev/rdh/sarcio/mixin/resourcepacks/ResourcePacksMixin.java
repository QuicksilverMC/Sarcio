package dev.rdh.sarcio.mixin.resourcepacks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resource.pack.ResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ResourcePacks.class)
public class ResourcePacksMixin {
    @Shadow
    private List<ResourcePacks.UnopenedPack> availablePacks;

    @Unique
    private Map<Object, Integer> sarcio$packIndex;

    @WrapMethod(method = "reload")
    private void sarcio$indexAvailablePacks(Operation<Void> original) {
        Map<Object, Integer> index = new HashMap<>();
        for (int i = 0; i < this.availablePacks.size(); i++) {
            index.putIfAbsent(this.availablePacks.get(i), i);
        }
        this.sarcio$packIndex = index;
        try {
            original.call();
        } finally {
            this.sarcio$packIndex = null;
        }
    }

    @WrapOperation(method = "reload", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"))
    private boolean sarcio$hashContains(List<?> packs, Object pack, Operation<Boolean> original) {
        return packs == this.availablePacks ? this.sarcio$packIndex.containsKey(pack) : original.call(packs, pack);
    }

    @WrapOperation(method = "reload", at = @At(value = "INVOKE", target = "Ljava/util/List;indexOf(Ljava/lang/Object;)I"))
    private int sarcio$hashIndexOf(List<?> packs, Object pack, Operation<Integer> original) {
        if (packs != this.availablePacks) {
            return original.call(packs, pack);
        }

        Integer index = this.sarcio$packIndex.get(pack);
        return index == null ? -1 : index;
    }

    @WrapOperation(method = "reload", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z"))
    private boolean sarcio$hashRemoveAll(List<?> packs, Collection<?> kept, Operation<Boolean> original) {
        return original.call(packs, new HashSet<>(kept));
    }
}
