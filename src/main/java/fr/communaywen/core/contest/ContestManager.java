package fr.communaywen.core.contest;


import fr.communaywen.core.utils.database.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ContestManager extends DatabaseConnector {
    public static int getInt(String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contest");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(column);
            }
        }
        catch(SQLException e) {
            System.out.println("Un problème avec la fonction getInt() dans ContestManager");
        }
        return 999;
    }
    public static String getString(String column) {
        try {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM contest");
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getString(column);
        }
    }
        catch(SQLException e) {
            System.out.println("Un problème avec la fonction getInt() dans ContestManager");
        }
        return "";
    }
    public static void updateColumn(String table, int phase) {
        String sql = "UPDATE " + table + " SET phase = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, phase);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static DayOfWeek getCurrentDayOfWeek() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);

        LocalDate currentDate = LocalDate.now();
        String currentDayString = currentDate.format(formatter);

        //conversion ex ven. => FRIDAY
        DayOfWeek currentDayOfWeek = DayOfWeek.from(formatter.parse(currentDayString));
        return currentDayOfWeek;
    }
}
