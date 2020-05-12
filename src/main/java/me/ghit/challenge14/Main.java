package me.ghit.challenge14;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {

    FileConfiguration config;
    File configFile;

    @Override
    public void onEnable() {
        config = getConfig();
        configFile = new File(getDataFolder(), "config.yml");

        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(isBlocked(e.getMessage().replace("/", ""))) {
            e.getPlayer().sendMessage(ChatColor.RED + "Sorry! This command is disabled!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        if (isBlocked(e.getCommand())) {
            e.getSender().sendMessage(ChatColor.RED + "Sorry! This command is disabled!");
            e.setCancelled(true);
        }
    }

    public boolean isBlocked(String command) {
        return config.getStringList("blocked-commands").contains(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("blreload"))
            return true;

        if (sender.hasPermission("blacklist.reload")) {
            config = YamlConfiguration.loadConfiguration(configFile);
            sender.sendMessage(ChatColor.GREEN + "Reloaded config");
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use that!");
        }
        return true;
    }
}
