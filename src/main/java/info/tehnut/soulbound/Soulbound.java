package info.tehnut.soulbound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.tehnut.soulbound.api.SoulboundContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Soulbound implements ModInitializer {

    public static final EnchantmentSoulbound ENCHANT_SOULBOUND = new EnchantmentSoulbound();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Lazy<Config> CONFIG = new Lazy<>(() -> {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "soulbound.json");
        if (!configFile.exists()) {
            Config config = new Config();
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(GSON.toJson(config));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return config;
        }

        try (FileReader reader = new FileReader(configFile)) {
            return GSON.fromJson(reader, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Config();
        }
    });

    @Override
    public void onInitialize() {
        Registry.register(Registry.ENCHANTMENT, new Identifier("soulbound", "soulbound"), ENCHANT_SOULBOUND);

        SoulboundContainer.CONTAINERS.put(new Identifier("soulbound", "inv_main"), player -> player.inventory.main);
        SoulboundContainer.CONTAINERS.put(new Identifier("soulbound", "inv_off"),player -> player.inventory.offHand);
        SoulboundContainer.CONTAINERS.put(new Identifier("soulbound", "inv_armor"),player -> player.inventory.armor);
    }

    public static class Config {

        private float soulboundRemovalChance = 0.0F;

        public float getSoulboundRemovalChance() {
            return Math.min(1.0F, Math.max(0.0F, soulboundRemovalChance));
        }
    }
}
