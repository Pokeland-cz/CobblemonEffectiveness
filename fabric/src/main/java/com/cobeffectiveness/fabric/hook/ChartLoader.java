package com.cobeffectiveness.fabric.hook;

import com.cobeffectiveness.CobEffectiveness;
import com.cobeffectiveness.compat.Compat;
import com.cobeffectiveness.type.ChartParser;
import com.cobeffectiveness.ui.IconRenderer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.Optional;

public final class ChartLoader {
    private ChartLoader() {}

    private static Identifier chartPath() {
        return Identifier.of(CobEffectiveness.NAMESPACE, Compat.getJsonPath());
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of(CobEffectiveness.NAMESPACE, "chart_loader");
            }

            @Override
            public void reload(ResourceManager manager) {
                IconRenderer.onResourcesReloaded(); // reload sheet
                Identifier id = chartPath();
                try {
                    Optional<Resource> res = manager.getResource(id);
                    if (res.isEmpty()) {
                        CobEffectiveness.logWarn("Effectiveness JSON not found '{}'", id);
                        return;
                    }
                    try (InputStream in = res.get().getInputStream()) {
                        ChartParser.applyFromStream(in);
                    }
                } catch (Exception e) {
                    CobEffectiveness.logError("Failed to load effectiveness JSON '{}'", id, e);
                }
                CobEffectiveness.logInfo("Successfully loaded effectiveness chart from '{}'", chartPath());
            }
        });
    }
}
