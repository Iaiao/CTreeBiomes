package mc.iaiao.ctreebiomes;

import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener {
    private final HashMap<TreeType, List<Biome>> biomes = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        biomes.clear();
        getConfig().getKeys(false).forEach(key -> {
            try {
                biomes.put(TreeType.valueOf(key.toUpperCase()), getConfig().getStringList(key).stream().map(biome -> Biome.valueOf(biome.toUpperCase())).collect(Collectors.toList()));
            } catch (IllegalArgumentException ignored) {
            }
        });
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onTreeGrow(StructureGrowEvent event) {
        if (biomes.containsKey(event.getSpecies())) {
            if (event.getBlocks().stream().noneMatch(block -> biomes.get(event.getSpecies()).contains(block.getBlock().getBiome()))) {
                event.setCancelled(true);
            }
        }
    }
}
