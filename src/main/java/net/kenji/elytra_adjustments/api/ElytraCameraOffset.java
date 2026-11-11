package net.kenji.elytra_adjustments.api;

import net.minecraft.world.entity.player.Player;

public final class ElytraCameraOffset {
    private static double offsetNow = 0.0D;
    private static long lastNanos = 0L;
    private static final double TARGET_OFFSET = 1.0D;
    private static final double SPEED = 14.0D;

    /** Call once per camera build; returns the current smoothed offset. */
    public static double next(Player player) {
        long now = System.nanoTime();
        double dt = lastNanos == 0L ? 0.0D : Math.min((now - lastNanos) / 1.0E9D, 0.1D);
        lastNanos = now;

        double target = player.isFallFlying() ? TARGET_OFFSET : 0.0D;
        offsetNow += (target - offsetNow) * (1.0D - Math.exp(-SPEED * dt));
        if (target == 0.0D && Math.abs(offsetNow) < 0.001D) offsetNow = 0.0D;
        return offsetNow;
    }

    public static void reset() { offsetNow = 0.0D; lastNanos = 0L; }
}