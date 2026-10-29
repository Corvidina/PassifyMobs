package io.github.corvidina.passify_mobs;


import io.github.corvidina.passify_mobs.listeners.SpawnListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public final class PassifyMobs extends JavaPlugin {

    private final Set<String> entities = new TreeSet<>();
    private Team team;

    @Override
    public void onEnable() {
        // Plugin startup logic


        // detect first time opening and initialize scoreboard and team
        if(getScoreboard().getTeam("passify")==null) {
            getLogger().info("creating new team");
            team = getScoreboard().registerNewTeam("passify");
        } else
        // initializing variables
        {
            getLogger().info("accessing previous team");
            team = getScoreboard().getTeam("passify");
        }

        ArrayList<String> arr = getConfig().getObject("entities",ArrayList.class);
        getLogger().info("Entities in list>>>"+ arr.toString());
        entities.addAll(arr);


        //register listeners
        this.getServer().getPluginManager().registerEvents(new SpawnListener(this,this.getLogger()),this);

        //register commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(LocalCommands.passifyMobs(this));
            event.registrar().register(LocalCommands.updatePassifieds(this));
        });
    }

    public Set<String> getEntities(){
        return entities;
    }

    public Scoreboard getScoreboard(){
        return getServer().getScoreboardManager().getMainScoreboard();
    }

    public Team getTeam(){
        return team;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        saveDefaultConfig();
    }
}
