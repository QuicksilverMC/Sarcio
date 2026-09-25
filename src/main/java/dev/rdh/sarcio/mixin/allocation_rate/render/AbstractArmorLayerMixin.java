package dev.rdh.sarcio.mixin.allocation_rate.render;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.render.entity.layer.AbstractArmorLayer;
import net.minecraft.item.ArmorItem;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractArmorLayer.class)
abstract class AbstractArmorLayerMixin {
	@Unique private static final Map<ArmorItem.Tier, Identifier[]> sarcio$armorTextures = new IdentityHashMap<>();

	/**
	 * @author rdh
	 * @reason cache armor textures
	 */
	@Overwrite
	private Identifier getArmorTexture(ArmorItem armor, boolean leggings, String type) {
		ArmorItem.Tier material = armor.getTier();
		Identifier[] textures = sarcio$armorTextures.computeIfAbsent(material, _ -> new Identifier[4]);

		int index = (leggings ? 2 : 0) + (type == null ? 0 : 1);
		Identifier texture = textures[index];
		if (texture == null) {
			String path = "textures/models/armor/" + material.getKey() + "_layer_" + (leggings ? 2 : 1) + (type == null ? "" : "_" + type) + ".png";
			texture = new Identifier(path);
			textures[index] = texture;
		}

		return texture;
	}
}
