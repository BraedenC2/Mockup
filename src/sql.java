import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class sql {
    static final String databasePrefix = "cs366-2241_connorsbc22";
    static final String netID = "connorsbc22";
    static final String hostName = "washington.uww.edu";
    static final String databaseURL = "jdbc:mariadb://" + hostName + "/" + databasePrefix;
    static final String password = "bc2800";

    public static void main(String args[]) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("databaseURL: " + databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");
            callableStatement = connection.prepareCall("{call GetAllData}");
            ResultSet resultSet = callableStatement.executeQuery();

            // Get metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            // names of columns
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int column = 1; column <= columnCount; column++) {
                columnNames[column - 1] = metaData.getColumnName(column);
            }

            // data of the table
            Object[][] data = new Object[100][columnCount]; // assuming max 100 rows
            int row = 0;
            while (resultSet.next() && row < 100) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    data[row][columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                row++;
            }

            // Now create the table
            JTable table = new JTable(new DefaultTableModel(data, columnNames));

            // Create a JFrame and add table to it
            JFrame frame = new JFrame();
            frame.add(new JScrollPane(table));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            resultSet.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
