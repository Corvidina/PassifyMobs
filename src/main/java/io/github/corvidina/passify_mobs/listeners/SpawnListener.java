package io.github.corvidina.passify_mobs.listeners;

import io.github.corvidina.passify_mobs.PassifyMobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.logging.Logger;

public class SpawnListener implements Listener {
    Logger logger;
    PassifyMobs plugin;
    public SpawnListener(PassifyMobs plugin, Logger logger) {
        this.logger=logger;
        this.plugin=plugin;
    }

    @EventHandler
    public void creatureSpawn(CreatureSpawnEvent event) {
        // On creature spawn check if should be passified and passify

        if(plugin.getEntities().contains(
                event.getEntity().getType().toString().toLowerCase())
        ){
            plugin.getTeam().addEntity(event.getEntity());

        }
    }
}
