package de.themoep.commandblockpermissions;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.themoep.commandblockpermissions.listeners.CommandBlockPacketListener;
import de.themoep.commandblockpermissions.listeners.PlayerEventListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

/**
 * CommandBlockPermissions
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License as published by
 * the Mozilla Foundation, version 2.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License v2.0 for more details.
 * <p/>
 * You should have received a copy of the Mozilla Public License v2.0
 * along with this program. If not, see <http://mozilla.org/MPL/2.0/>.
 */

public class CommandBlockPermissions extends JavaPlugin {

    private ProtocolManager protocolManager;
    private boolean logWarnings;
    private boolean checkOps;
    private boolean usePlayerPermissions;
    private List<String> disableWorlds;

    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        try {
            protocolManager.addPacketListener(new CommandBlockPacketListener(this));
            loadConfig();
            getCommand(getName().toLowerCase()).setExecutor(new CommandBlockPermissionsCommand(this));
            getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            getLogger().log(Level.SEVERE, "Error while enabling the packet listener!", e);
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        logWarnings = getConfig().getBoolean("logWarnings");
        checkOps = getConfig().getBoolean("checkOps");
        usePlayerPermissions = getConfig().getBoolean("usePlayerPermissions");
        disableWorlds = getConfig().getStringList("disableWorlds");
    }

    public void warning(String msg) {
        if (logWarnings) {
            getLogger().log(Level.INFO, msg);
        }
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.hasPermission("cbp.receivewarnings")) {
                player.sendMessage(getPrefix() + ChatColor.RED + " " + msg);
            }
        }
    }

    private String getPrefix() {
        return ChatColor.AQUA + "[" + ChatColor.YELLOW + "CBP" + ChatColor.AQUA + "]" + ChatColor.RESET;
    }

    public boolean checkOps() {
        return checkOps;
    }

    public boolean usePlayerPermissions() {
        return usePlayerPermissions;
    }

    public List<String> getDisableWorlds(){
        return disableWorlds;
    }
}
