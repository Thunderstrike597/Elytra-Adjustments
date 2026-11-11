package net.kenji.elytra_adjustments.mixins;

import net.kenji.elytra_adjustments.api.ElytraCameraOffset;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import yesman.epicfight.api.client.camera.EpicFightCameraAPI;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    private Entity entity;
    @Shadow private boolean detached;

    @ModifyArg(method = "setup",
               at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V"),
               index = 1)
    private double efElytra$liftPivot(double y) {
        if (!(this.entity instanceof LocalPlayer player)) return y;
        if (!this.detached) return y;                                  // third person only
        if (EpicFightCameraAPI.getInstance().isTPSMode()) return y;    // EF TPS is handled by your event
        return y + ElytraCameraOffset.next(player);
    }
}