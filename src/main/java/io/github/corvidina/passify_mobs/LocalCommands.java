package io.github.corvidina.passify_mobs;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;


public final class LocalCommands {

    public static LiteralCommandNode<CommandSourceStack> passifyMobs(PassifyMobs plugin) {
        var root = Commands.literal("passive_to");
        Team team = plugin.getTeam();
        root.then(Commands.literal("add")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(commandContext -> {
                            final CommandSender sender = commandContext.getSource().getSender();
                            final Player player = commandContext.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(commandContext.getSource()).getFirst();

                            team.addEntity(player);

                            sender.sendRichMessage("<dark_purple>Added <player> to passify mobs team",
                                    Placeholder.component("player",player.displayName()));

                            return Command.SINGLE_SUCCESS;
                        })));

        root.then(Commands.literal("remove")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(commandContext -> {
                            final CommandSender sender = commandContext.getSource().getSender();
                            final Player player = commandContext.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(commandContext.getSource()).getFirst();

                            team.removeEntity(player);

                            sender.sendRichMessage("<dark_purple>Removed <player> from passify mobs team",
                                    Placeholder.component("player",player.displayName()));

                            return Command.SINGLE_SUCCESS;
                        })));

        root.then(Commands.literal("list")
                .executes(commandContext -> {
                    final CommandSender sender = commandContext.getSource().getSender();
                    OfflinePlayer[] arr = plugin.getServer().getOfflinePlayers().clone();

                    Set<String> players = new TreeSet<>();
                    Arrays.stream(arr).forEach(player -> {
                        players.add(player.getName());
                    });

                    team.getEntries().forEach(entry -> {
                        if(players.contains(entry)) {
                            sender.sendRichMessage("<light_purple> - <entry>",
                                    Placeholder.parsed("entry",entry));
                        }
                    });
                    return Command.SINGLE_SUCCESS;
                }));

        return root.build();
    }

    public static LiteralCommandNode<CommandSourceStack> updatePassifieds(PassifyMobs plugin) {
        return Commands.literal("update_passifieds")
                .executes(commandContext -> {
                    plugin.getServer().getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
                        if (plugin.getEntities().contains(entity.getType().toString().toLowerCase())) {
                            plugin.getTeam().addEntity(entity);
                        } else {
                            plugin.getTeam().removeEntity(entity);
                        }
                    }));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

}
