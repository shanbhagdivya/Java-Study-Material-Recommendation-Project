package main.java.com.example.myloginpage;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Recommender {
    protected Connection connection;
    private String url = "jdbc:mysql://localhost:3306/jdbc";
    private String username = "root";
    private String password = "Aryan@08";

    public Recommender() throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    public List<StudyMaterial> filterPaperbacks(String subject, double maxPrice, double minRating, String publisher, int topN) throws SQLException, InsufficientRecommendationsException {
        List<StudyMaterial> recommendations = new ArrayList<>();
        String sql;
        if (publisher.isEmpty()) {
            sql = "SELECT *, (Paperback_Rating * Paperback_No_of_Ratings) / Paperback_Price AS rating_score FROM Paperback WHERE Paperback_Subject = ? AND Paperback_Price <= ? AND Paperback_Rating >= ? ORDER BY rating_score DESC LIMIT ?";
        } else {
            sql = "SELECT *, (Paperback_Rating * Paperback_No_of_Ratings) / Paperback_Price AS rating_score FROM Paperback WHERE Paperback_Subject = ? AND Paperback_Price <= ? AND Paperback_Rating >= ? AND Paperback_Publisher = ? ORDER BY rating_score DESC LIMIT ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject);
            statement.setDouble(2, maxPrice);
            statement.setDouble(3, minRating);
            int parameterIndex = 4;

            if (!publisher.isEmpty()) {
                statement.setString(parameterIndex++, publisher);
            }

            statement.setInt(parameterIndex, topN);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Paperback paperback = new Paperback(
                            resultSet.getString("PI_Path"),
                            resultSet.getString("Paperback_Title"),
                            resultSet.getString("Paperback_Subject"),
                            "Paperback",
                            resultSet.getDouble("Paperback_Price"),
                            resultSet.getDouble("Paperback_Rating"),
                            resultSet.getInt("Paperback_No_of_Ratings"),
                            resultSet.getString("Paperback_URL"),
                            resultSet.getString("Paperback_Author"),
                            resultSet.getString("Paperback_Publisher")
                    );
                    recommendations.add(paperback);
                }
            }

            if (recommendations.size() < topN) {
                throw new InsufficientRecommendationsException("Insufficient recommendations found. Required: " + topN + ", Found: " + recommendations.size());
            }
            recommendations.sort((p1, p2) -> Double.compare(((Paperback) p2).getRating(), ((Paperback) p1).getRating()));

            return recommendations.stream().limit(topN).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch paperback data", e);
        }
    }

    public List<StudyMaterial> filterEBooks(String subject, double maxPrice, double minRating, int topN) throws SQLException, InsufficientRecommendationsException {
        List<StudyMaterial> recommendations = new ArrayList<>();
        String sql = "SELECT *, (EBooks_Rating * EBooks_No_of_Ratings) / EBooks_Price AS rating_score FROM EBooks WHERE EBooks_Subject = ? AND EBooks_Price <= ? AND EBooks_Rating >= ? ORDER BY rating_score DESC LIMIT ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject);
            statement.setDouble(2, maxPrice);
            statement.setDouble(3, minRating);
            statement.setInt(4, topN);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EBook eBook = new EBook(
                            resultSet.getString("EBI_Path"),
                            resultSet.getString("EBooks_Title"),
                            resultSet.getString("EBooks_Subject"),
                            "EBook",
                            resultSet.getDouble("EBooks_Price"),
                            resultSet.getDouble("EBooks_Rating"),
                            resultSet.getInt("EBooks_No_of_Ratings"),
                            resultSet.getString("EBooks_URL"),
                            resultSet.getString("EBooks_Author")
                    );
                    recommendations.add(eBook);
                }
            }

            if (recommendations.size() < topN) {
                throw new InsufficientRecommendationsException("Insufficient recommendations found. Required: " + topN + ", Found: " + recommendations.size());
            }
            recommendations.sort((p1, p2) -> Double.compare(((EBook) p2).getRating(), ((EBook) p1).getRating()));

            return recommendations.stream().limit(topN).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch eBook data", e);
        }
    }

    public List<StudyMaterial> filterCourses(String subject, double maxPrice, double minRating, int topN, String offeredBy, String languages) throws SQLException, InsufficientRecommendationsException {
        List<StudyMaterial> recommendations = new ArrayList<>();
        String sql = "SELECT *, (Course_Rating * Course_No_of_Ratings) / Course_Price AS rating_score FROM Course WHERE Course_Subject = ? AND Course_Price <= ? AND Course_Rating >= ? ";

        if (!offeredBy.isEmpty()) {
            sql += "AND Offered_By = ? ";
        }

        if (!languages.isEmpty()) {
            sql += "AND Course_Languages LIKE ? ";
        }

        sql += "ORDER BY rating_score DESC LIMIT ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject);
            statement.setDouble(2, maxPrice);
            statement.setDouble(3, minRating);
            int parameterIndex = 4;

            if (!offeredBy.isEmpty()) {
                statement.setString(parameterIndex++, offeredBy);
            }

            if (!languages.isEmpty()) {
                statement.setString(parameterIndex++, "%" + languages + "%");
            }

            statement.setInt(parameterIndex, topN);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course(
                            resultSet.getString("Course_Title"),
                            resultSet.getString("Course_Subject"),
                            "Course",
                            resultSet.getDouble("Course_Price"),
                            resultSet.getDouble("Course_Rating"),
                            resultSet.getInt("Course_No_of_Ratings"),
                            resultSet.getString("Offered_By"),
                            resultSet.getInt("Course_No_of_Languages_Available"),
                            resultSet.getString("Course_Languages"),
                            resultSet.getLong("Course_No_of_People_Enrolled"),
                            resultSet.getString("Course_URL")
                    );
                    recommendations.add(course);
                }
            }

            if (recommendations.size() < topN) {
                throw new InsufficientRecommendationsException("Insufficient recommendations found. Required: " + topN + ", Found: " + recommendations.size());
            }
            recommendations.sort((p1, p2) -> Double.compare(((Course) p2).getRating(), ((Course) p1).getRating()));

            return recommendations.stream().limit(topN).collect(Collectors.toList());
        } catch (SQLException e) {
            // Log and rethrow or handle it based on your application's error handling policy
            throw new SQLException("Failed to fetch course data", e);
        }
    }
}