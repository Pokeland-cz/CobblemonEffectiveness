package com.cobeffectiveness.fabric;

import com.cobeffectiveness.CobEffectiveness;
import com.cobeffectiveness.compat.Compat;
import com.cobeffectiveness.fabric.hook.ChartLoader;
import com.cobeffectiveness.fabric.ui.EffectivenessHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class CobEffectivenessFabricClient implements ClientModInitializer {
    private final EffectivenessHud hud = new EffectivenessHud();

    @Override
    public void onInitializeClient() {
        // Check for other mods
        Compat.init(FabricLoader.getInstance().isModLoaded(Compat.MODID_GEB));

        // Register hooks
        ChartLoader.register();
        HudRenderCallback.EVENT.register((DrawContext dc, RenderTickCounter t) -> hud.render(dc));

        CobEffectiveness.initCommon();
    }
}
