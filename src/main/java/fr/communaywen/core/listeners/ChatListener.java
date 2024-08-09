package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.utils.DiscordWebhook;
import fr.communaywen.core.utils.chatchannel.Channel;
import fr.communaywen.core.utils.database.Blacklist;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

public class ChatListener implements Listener {
    private final DiscordWebhook discordWebhook;

    private AywenCraftPlugin plugin;

    public ChatListener(AywenCraftPlugin plugin, DiscordWebhook discordWebhook) {
        this.plugin = plugin;
        this.discordWebhook = discordWebhook;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        plugin.getManagers().getQuizManager().onPlayerChat(event);

        String username = event.getPlayer().getName();
        String avatarUrl = "https://minotar.net/helm/" + username;
        String message = event.getMessage();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> discordWebhook.sendMessage(username, avatarUrl, message));

        if(plugin.getManagers().getChatChannel().getChannel(event.getPlayer()) == Channel.TEAM){
            Player player = event.getPlayer();
            event.setCancelled(true);

            Team team = plugin.getManagers().getTeamManager().getTeamByPlayer(player.getUniqueId());
            if(team == null) return;

            team.sendMessage(player, event.getMessage());
        }

        if (!(event.getPlayer() instanceof Player)) { return; }
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.equals(event.getPlayer())) {
                return;
            }
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {
                if (Blacklist.isBlacklisted(player, event.getPlayer())) {
                    return;
                }
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1, 1);
            }
        });
    }

    @EventHandler
    public void onBroadcastMessage(BroadcastMessageEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> discordWebhook.sendBroadcast(event.getMessage()));
    }
}
