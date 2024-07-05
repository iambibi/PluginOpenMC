package fr.communaywen.core;

import dev.xernas.menulib.MenuLib;
import fr.communaywen.core.commands.TeamCommand;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.commands.ProutCommand;
import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.commands.VersionCommand;
import fr.communaywen.core.utils.PermissionCategory;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;
    private TeamManager teamManager;
    private static AywenCraftPlugin instance;

    @Override
    public void onEnable() {
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        instance = this;

        MenuLib.init(this);

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);
        teamManager = new TeamManager();



        this.getCommand("version").setExecutor(new VersionCommand(this));
        PluginCommand teamCommand = this.getCommand("team");
        teamCommand.setExecutor(new TeamCommand());
        teamCommand.setTabCompleter(new TeamCommand());

        final @Nullable PluginCommand proutCommand = super.getCommand("prout");
        if (proutCommand != null)
            proutCommand.setExecutor(new ProutCommand());
    }

    @Override
    public void onDisable() {
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public static AywenCraftPlugin getInstance() {
        return instance;
    }
    /**
     * Format a permission with the permission prefix.
     *
     * @param category the permission category
     * @param suffix the permission suffix
     * @return The formatted permission.
     * @see PermissionCategory#PERMISSION_PREFIX
     */
    public static @NotNull String formatPermission(final @NotNull PermissionCategory category,
                                                   final @NotNull String suffix) {
        return category.formatPermission(suffix);
    }

}
