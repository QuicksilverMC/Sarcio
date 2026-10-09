package dev.rdh.sarcio.mixin.mem.alloc.render;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.render.entity.layer.AbstractArmorLayer;
import net.minecraft.item.ArmorItem;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArmorLayer.class)
abstract class AbstractArmorLayerMixin {
	@Unique private static final Map<ArmorItem.Tier, Identifier[]> sarcio$armorTextures = new IdentityHashMap<>();

	@WrapOperation(
		method = {"renderArmor", "getArmorTexture(Lnet/minecraft/item/ArmorItem;Z)Lnet/minecraft/resource/Identifier;"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/layer/AbstractArmorLayer;getArmorTexture(Lnet/minecraft/item/ArmorItem;ZLjava/lang/String;)Lnet/minecraft/resource/Identifier;"),
		require = 2
	)
	private Identifier sarcio$cacheArmorTexture(AbstractArmorLayer<?> layer, ArmorItem armor, boolean leggings, String type, Operation<Identifier> original) {
		Identifier[] textures = sarcio$armorTextures.computeIfAbsent(armor.getTier(), _ -> new Identifier[4]);

		int index = (leggings ? 2 : 0) + (type == null ? 0 : 1);
		Identifier texture = textures[index];
		if (texture == null) {
			texture = original.call(layer, armor, leggings, type);
			textures[index] = texture;
		}

		return texture;
	}
}
