package net.kenji.elytra_adjustments.compat;

import net.kenji.elytra_adjustments.ConfigClient;
import net.kenji.elytra_adjustments.ElytraAdjustments;
import net.kenji.elytra_adjustments.mixins.AccessorCamera;
import net.kenji.epicfight_swordsoaringfixes.compat.SwordSoaringCompat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.client.event.EpicFightClientHooks;
import yesman.epicfight.api.client.event.types.BuildCameraTransform;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public final class EpicFightElytraCameraFix {

  // true from flight start until the camera has fully recovered
    private static double offsetNow = 0.0D;
    private static long lastNanos = 0L;

    private static final double SPEED = 14.0D;
    public static void init() {
        EpicFightClientHooks.Camera.BUILD_TRANSFORM_POST.registerEvent(EpicFightElytraCameraFix::onCameraPost, 8);
    }

    private static boolean isElytraFlying(PlayerPatch<?> patch){
       boolean isFlying = patch.getOriginal().isFallFlying();
       if(ElytraAdjustments.SSF_LOADED){
           if(SwordSoaringCompat.isFlying(patch)){
               return false;
           }
       }

       return isFlying;
    }

   // per second; roughly matches vanilla's eye-height easing

    private static void onCameraPost(BuildCameraTransform.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        LocalPlayerPatch playerPatch = EpicFightCapabilities.getLocalPlayerPatch(player);

        Camera camera = event.getCamera();
        if (player == null) { offsetNow = 0.0D; lastNanos = 0L; return; }
        if(playerPatch == null) return;
        if (camera.getEntity() != player) return;
        long now = System.nanoTime();
        double dt = lastNanos == 0L ? 0.0D : Math.min((now - lastNanos) / 1.0E9D, 0.1D);
        lastNanos = now;

        double target = isElytraFlying(playerPatch) ? ConfigClient.TARGET_ELYTRA_CAMERA_OFFSET.get() : 0.0D;
        offsetNow += (target - offsetNow) * (1.0D - Math.exp(-SPEED * dt));

        if (target == 0.0D && Math.abs(offsetNow) < 0.001D) { offsetNow = 0.0D; return; }

        Vec3 pos = camera.getPosition();
        ((AccessorCamera) camera).invokeSetPos(pos.x, pos.y + offsetNow, pos.z);
    }
}