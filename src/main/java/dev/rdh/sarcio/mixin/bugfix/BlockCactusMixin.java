package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockCactus.class)
public abstract class BlockCactusMixin extends Block {
    public BlockCactusMixin(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    @WrapMethod(method = "getSelectedBoundingBox")
    public AxisAlignedBB sarcio$fixCactusBoundingBoxPrecision(World worldIn, BlockPos pos, Operation<AxisAlignedBB> original) {
        double f = 0.0625D;
        return new AxisAlignedBB(pos.getX() + f, pos.getY(), pos.getZ() + f, (pos.getX() + 1) - f, pos.getY() + 1, (pos.getZ() + 1) - f);
    }
}
