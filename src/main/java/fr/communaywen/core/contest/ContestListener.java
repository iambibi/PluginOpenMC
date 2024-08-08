package fr.communaywen.core.contest;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.ContestManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.sk89q.worldguard.protection.flags.Flags.TIME_LOCK;


public class ContestListener implements Listener {
    private AywenCraftPlugin plugin;
    private BukkitRunnable eventRunnable;

    public ContestListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
                DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(ContestManager.getString("startdate")));

                if (ContestManager.getInt("phase") == 1 && ContestManager.getCurrentDayOfWeek().getValue() == dayStartContestOfWeek.getValue()) {
                    System.out.println("contest phase 2");
                    ContestManager.updateColumn("contest", 2);
                    Bukkit.broadcastMessage(

                            "§8§m                                                     §r\n" +
                                    "§7\n" +
                                    "§6§lCONTEST!§r §7 Les votes sont ouverts !§7" +
                                    "§7\n" +
                                    "§8§o*on se retrouve au spawn pour pouvoir voter ou /contest...*\n" +
                                    "§7\n" +
                                    "§8§m                                                     §r"
                    );

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 0.2F);
                    }

                    org.bukkit.World world = Bukkit.getWorld("world");
                    com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(world);

                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager regions = container.get(wgWorld);
                    ProtectedRegion region = regions.getRegion("spawn");


                    region.setFlag(Flags.TIME_LOCK, "12700");

                    try {
                        regions.save();
                    } catch (StorageException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        // tout les minutes
        eventRunnable.runTaskTimer(plugin, 0, 1200);
    }
}
