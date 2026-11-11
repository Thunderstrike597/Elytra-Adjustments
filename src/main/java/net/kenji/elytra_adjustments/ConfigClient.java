package net.kenji.elytra_adjustments;

import net.minecraftforge.common.ForgeConfigSpec;

// Client-side config, registered in the main class as ModConfig.Type.CLIENT
public class ConfigClient {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Double> TARGET_ELYTRA_CAMERA_OFFSET;

    public static final ForgeConfigSpec SPEC;

    static {
        TARGET_ELYTRA_CAMERA_OFFSET = BUILDER.comment("The offset amount for the camera during elytra flight").defineInRange("Elytra Camera Offset",
                0.65, 0, 2.0);

        SPEC = BUILDER.build();
    }
}
