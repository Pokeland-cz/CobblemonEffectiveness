package com.cobeffectiveness.neoforge;

import com.cobeffectiveness.CobEffectiveness;
import com.cobeffectiveness.compat.Compat;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(CobEffectiveness.MOD_ID)
public final class CobEffectivenessNeoForge {

    public CobEffectivenessNeoForge() {
        // Check for other mods
        Compat.init(ModList.get().isLoaded(Compat.MODID_GEB));

        CobEffectiveness.initCommon();
    }
}