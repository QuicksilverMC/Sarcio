package dev.rdh.sarcio.mixin.bugfix.render;

import java.nio.ByteBuffer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VertexBuffer.class)
public class VertexBufferMixin {
    @Shadow
    private int glBufferId;

    @WrapMethod(method = "bufferData")
    private void sarcio$skipDeletedBuffer(ByteBuffer data, Operation<Void> original) {
        if (this.glBufferId != -1) {
            original.call(data);
        }
    }
}
