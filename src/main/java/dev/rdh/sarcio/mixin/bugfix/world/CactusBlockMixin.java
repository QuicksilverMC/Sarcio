package dev.rdh.sarcio.mixin.bugfix.world;

import net.minecraft.block.Block;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin extends Block {
    public CactusBlockMixin(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    /**
     * @author rdh
     * @reason vanilla's float inset loses precision; build the outline with doubles
     */
    @Overwrite
    public Box getOutlineShape(World worldIn, BlockPos pos) {
        double f = 0.0625D;
        return new Box(pos.getX() + f, pos.getY(), pos.getZ() + f, (pos.getX() + 1) - f, pos.getY() + 1, (pos.getZ() + 1) - f);
    }
}
