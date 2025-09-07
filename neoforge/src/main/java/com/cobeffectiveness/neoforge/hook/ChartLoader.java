package com.cobeffectiveness.neoforge.hook;

import com.cobeffectiveness.CobEffectiveness;
import com.cobeffectiveness.compat.Compat;
import com.cobeffectiveness.type.ChartParser;
import com.cobeffectiveness.ui.IconRenderer;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CobEffectiveness.MOD_ID)
public final class ChartLoader {

    private ChartLoader() {}

    private static Identifier chartPath() {
        return Identifier.of(CobEffectiveness.NAMESPACE, Compat.getJsonPath());
    }

    @SubscribeEvent
    public static void registerReload(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener((synchronizer, manager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            IconRenderer.onResourcesReloaded(); // reload sheet
            CompletableFuture<Void> parseTask = CompletableFuture.runAsync(() -> {
                Identifier path = chartPath();
                try {
                    Optional<Resource> res = manager.getResource(path);
                    if (res.isEmpty()) {
                        CobEffectiveness.logWarn("Effectiveness chart not found '{}'", path);
                        return;
                    }
                    try (InputStream in = res.get().getInputStream()) {
                        ChartParser.applyFromStream(in);
                    }
                } catch (Exception ex) {
                    CobEffectiveness.logError("Failed to load effectiveness chart '{}'", path, ex);
                }
            }, backgroundExecutor);
            CobEffectiveness.logInfo("Successfully loaded effectiveness chart from '{}'", chartPath());
            return parseTask.thenCompose(synchronizer::whenPrepared);
        });
    }
}
