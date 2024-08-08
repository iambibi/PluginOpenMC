package fr.communaywen.core.commands.contest;

import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import java.time.DayOfWeek;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Feature("Contest")
@Credit("iambibi_")
public class ContestCommand extends DatabaseConnector {

    @Command("contest")
    @Description("Ouvre l'interface des festivals, et quand un festival commence, vous pouvez choisir votre camp")
    public void onCommand(Player player) throws SQLException {
        int phase = ContestManager.getInt("phase");
        String dayStartContest = ContestManager.getString("startdate");
            if (phase==2) {
System.out.println("ouvrir panel");

            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
                DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(dayStartContest));

                int days = (dayStartContestOfWeek.getValue() - ContestManager.getCurrentDayOfWeek().getValue() + 7) % 7;

                player.sendMessage(ChatColor.RED + "Il n'y a aucun Contest ! Revenez dans " + days + " jours.");
            }

    }

}
