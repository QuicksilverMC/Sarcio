package dev.rdh.sarcio.mixin.allocation_rate.render;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.render.entity.layer.AbstractArmorLayer;
import net.minecraft.item.ArmorItem;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractArmorLayer.class)
abstract class AbstractArmorLayerMixin {
	@Unique private static final Map<ArmorItem.Tier, Identifier[]> sarcio$armorTextures = new IdentityHashMap<>();

	@WrapMethod(method = "getArmorTexture(Lnet/minecraft/item/ArmorItem;ZLjava/lang/String;)Lnet/minecraft/resource/Identifier;")
	private Identifier sarcio$cacheArmorTexture(ArmorItem armor, boolean leggings, String type, Operation<Identifier> original) {
		Identifier[] textures = sarcio$armorTextures.computeIfAbsent(armor.getTier(), _ -> new Identifier[4]);

		int index = (leggings ? 2 : 0) + (type == null ? 0 : 1);
		Identifier texture = textures[index];
		if (texture == null) {
			texture = original.call(armor, leggings, type);
			textures[index] = texture;
		}

		return texture;
	}
}
