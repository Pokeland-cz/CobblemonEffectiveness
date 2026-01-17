package com.cobeffectiveness.fabric.battle;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.battle.ClientBattle;
import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import com.cobblemon.mod.common.client.battle.ClientBattleSide;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobeffectiveness.CobEffectiveness;
import com.cobeffectiveness.battle.OpponentSnapshot;
import com.cobeffectiveness.type.Type;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public final class PokemonTypeFetcher {
    private static Field clientBattlePokemonAspectsField;

    static {
        try {
            clientBattlePokemonAspectsField = ClientBattlePokemon.class.getDeclaredField("aspects");
            clientBattlePokemonAspectsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            CobEffectiveness.logWarn("Could not find 'aspects' field: " + e.getMessage());
        }
    }

    public static void refreshFromCurrentBattle() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) {
            OpponentSnapshot.clear();
            return;
        }

        ClientBattle battle = CobblemonClient.INSTANCE.getBattle();
        if (battle == null) {
            OpponentSnapshot.clear();
            return;
        }

        ClientBattleSide side1 = battle.getSide1();
        ClientBattleSide side2 = battle.getSide2();

        UUID me = mc.player.getUuid();
        boolean iAmSide1 = side1.getActors().stream().anyMatch(a -> me.equals(a.getUuid()));
        ClientBattleSide enemy = iAmSide1 ? side2 : side1;

        Set<Type> out = EnumSet.noneOf(Type.class);
        for (ActiveClientBattlePokemon active : enemy.getActiveClientBattlePokemon()) {
            resolveTypes(active, out);
        }

        if (out.isEmpty()) {
            OpponentSnapshot.clear();
        } else {
            OpponentSnapshot.update(out);
        }
    }

    private static Set<String> getAspects(ClientBattlePokemon bp) {
        if (clientBattlePokemonAspectsField == null) {
            return bp.getProperties().getAspects();
        }
        try {
            Object rawValue = clientBattlePokemonAspectsField.get(bp);
            @SuppressWarnings("unchecked")
            Set<String> aspects = (Set<String>) rawValue;
            return aspects;
        } catch (IllegalAccessException e) {
            return bp.getProperties().getAspects();
        }
    }

    private static void resolveTypes(ActiveClientBattlePokemon active, Set<Type> into) {
        ClientBattlePokemon bp = active.getBattlePokemon();
        if (bp == null) return;

        // 1. Get aspects using our clean helper method
        Set<String> aspects = getAspects(bp);

        // 2. CHECK TERASTALLIZATION
        for (String aspect : aspects) {
            if (aspect.startsWith("tera_")) {
                String typeName = aspect.substring(5);
                try {
                    into.add(Type.fromCobblemon(typeName));
                    return; // Stop here! Tera is a single type override.
                } catch (IllegalArgumentException ex) {
                    CobEffectiveness.logWarn("Unknown tera type '{}'", typeName);
                }
            }
        }

        // 3. RESOLVE SPECIES FORM
        Species species = bp.getSpecies();
        FormData form = species.getForm(aspects);

        if (form.getPrimaryType() != null) {
            try {
                into.add(Type.fromCobblemon(form.getPrimaryType().getName()));
            } catch (Exception ex) {
                CobEffectiveness.logWarn("Unknown primary type '{}'", form.getPrimaryType().getName());
            }
        }

        if (form.getSecondaryType() != null) {
            try {
                into.add(Type.fromCobblemon(form.getSecondaryType().getName()));
            } catch (Exception ex) {
                CobEffectiveness.logWarn("Unknown secondary type '{}'", form.getSecondaryType().getName());
            }
        }
    }
}
