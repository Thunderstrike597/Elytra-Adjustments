package net.kenji.elytra_adjustments.mixins;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface AccessorCamera {
    @Accessor("eyeHeightOld")
    float accessorEyeHeightOld();
    @Accessor("eyeHeight")
    float accessorEyeHeight();
    @Invoker("setPosition")
    void invokeSetPos(double pX, double pY, double pZ);
}
