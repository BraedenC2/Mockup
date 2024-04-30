import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;

/**
 * @author Braeden
 */
public class Main extends JFrame {


    // TODO: Make the JComboBox an object (<>)

    // * Connection
    // ! Don't change
    static final String databasePrefix = "cs366-2241_connorsbc22";
    static final String netID = "connorsbc22";
    static final String hostName = "washington.uww.edu";
    static final String databaseURL = "jdbc:mariadb://" + hostName + "/" + databasePrefix;
    static final String password = "bc2800";
    String tableName = "";

    // * Loads from SQL and shows it on JTable
    private void loadData() {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("databaseURL: " + databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");

            callableStatement = connection.prepareCall("{call FetchPetDetails}");
            ResultSet resultSet = callableStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            // * names of columns
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int column = 1; column <= columnCount; column++) {
                columnNames[column - 1] = metaData.getColumnName(column);
            }

            // * data of the table
            Object[][] data = new Object[11000][columnCount];
            int row = 0;
            while (resultSet.next() && row < 11000) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    data[row][columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                row++;
            }

            // * Updates the table
            breifDataTable.setModel(new DefaultTableModel(data, columnNames));

            // ! Error handling. Don't change.
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

    // * Loads from SQL and shows it on JTable
    private void loadTableData(String tableName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("databaseURL: " + databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();

            // * names of columns
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int column = 1; column <= columnCount; column++) {
                columnNames[column - 1] = metaData.getColumnName(column);
            }

            // * data of the table
            Object[][] data = new Object[11000][columnCount];
            int row = 0;
            while (resultSet.next() && row < 11000) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    data[row][columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                row++;
            }

            // * Updates the table
            table1.setModel(new DefaultTableModel(data, columnNames));

            // ! Error handling. Don't change.
            resultSet.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
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

    // Searching functionality

    // * Searching Method
    private void search(ActionEvent e) {
        searchData( tableName, (String) searchBox.getSelectedItem(), searchField.getText());
    }

    // * Searches the Trial table and shows it on JTable
    private void searchData(String tableName, String columnName, String value) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("databaseURL: " + databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully connected to the database");

            if (columnName.isEmpty()){
                loadTableData(tableName);
            }
            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?;";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            // * names of columns
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int column = 1; column <= columnCount; column++) {
                columnNames[column - 1] = metaData.getColumnName(column);
            }

            // * data of the table
            Object[][] data = new Object[11000][columnCount];
            int row = 0;
            while (resultSet.next() && row < 11000) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    data[row][columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                row++;
            }

            // * Updates the table
            table1.setModel(new DefaultTableModel(data, columnNames));

            // * Display the number of rows (results)
            resultNumberLabel.setText("Number of results: " + row);

            // ! Error handling. Don't change.
            resultSet.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
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

    // The Viewing Functions

    // * Switches to the HomePage
    private void home(ActionEvent e) {
        cardSwitcher(0);
    }

    // * Switches to the TrialsPage
    private void trials(ActionEvent e) {
        cardSwitcher(1);
    }

    // * Switches to the MovementsPage
    private void movements(ActionEvent e) {
        cardSwitcher(2);
    }

    // * Switches to the PetsPage
    private void pets(ActionEvent e) {
        cardSwitcher(3);
    }

    // * Switches to the DeceasedPage
    private void deceasedAnimals(ActionEvent e) {
        cardSwitcher(4);
    }

    // * Switches to the SheltersPage
    private void shelters(ActionEvent e) {
        cardSwitcher(5);
    }

    // * The Switcher
    private void cardSwitcher(int cardIndex) {
        CardLayout cl = (CardLayout)(programStructure.getLayout());
        cl.show(programStructure, "card2");
        switch (cardIndex){
            case 0: cl.show(programStructure, "card1");
                specificLabel.setText("null Database");
                break;
            case 1: specificLabel.setText("Trials Database");
                loadTableData("Trial");
                for (int i = searchBox.getItemCount() - 1; i >= 0; i--) {
                    searchBox.removeItemAt(i);
                }
                tableName = "Trial";
                searchBox.addItem("id");
                addLabel1.setText("id");
                searchBox.addItem("istrial");
                addLabel2.setText("istrial");
                searchBox.addItem("returndate");
                addLabel3.setText("returndate");
                searchBox.addItem("returnedreason");
                addLabel4.setText("returnedreason");
                searchBox.addItem("");
                // TODO: Do all the other addLabels and make them blank
                break;
            case 2: specificLabel.setText("Movements Database");
                loadTableData("Movement");
                for (int i = searchBox.getItemCount() - 1; i >= 0; i--) {
                    searchBox.removeItemAt(i);
                }
                tableName = "Movement";
                searchBox.addItem("id");
                searchBox.addItem("location");
                searchBox.addItem("movementdate");
                searchBox.addItem("movementtype");
                searchBox.addItem("");
                break;
            case 3: specificLabel.setText("Pets Database");
                loadTableData("Pet");
                for (int i = searchBox.getItemCount() - 1; i >= 0; i--) {
                    searchBox.removeItemAt(i);
                }
                tableName = "Pet";
                searchBox.addItem("sexname");
                searchBox.addItem("speciesname");
                searchBox.addItem("breedname");
                searchBox.addItem("id");
                searchBox.addItem("intakedate");
                searchBox.addItem("basecolour");
                searchBox.addItem("intakereason");
                searchBox.addItem("istransfer");
                searchBox.addItem("identichipnumber");
                searchBox.addItem("animalname");
                searchBox.addItem("animalage");
                searchBox.addItem("");
                break;
            case 4: specificLabel.setText("Deceased Animals Database");
                loadTableData("DeceasedAnimals");
                for (int i = searchBox.getItemCount() - 1; i >= 0; i--) {
                    searchBox.removeItemAt(i);
                }
                tableName = "DeceasedAnimals";
                searchBox.addItem("id");
                searchBox.addItem("deceaseddate");
                searchBox.addItem("deceasedreason");
                searchBox.addItem("diedoffshelter");
                searchBox.addItem("puttosleep");
                searchBox.addItem("isdoa");
                searchBox.addItem("");
                break;
            case 5: specificLabel.setText("Shelters Database");
                loadTableData("Shelter");
                for (int i = searchBox.getItemCount() - 1; i >= 0; i--) {
                    searchBox.removeItemAt(i);
                }
                tableName = "Shelter";
                searchBox.addItem("id");
                searchBox.addItem("sheltercode");
                searchBox.addItem("");
                break;
            default:
                System.out.println("error");
        }
    }

    // The Dynamic Functions

    // * Initializes updateComponentSizes()
    private void homePagePanelComponentResized(ComponentEvent e) {
        updateComponentSizes();
    }

    // * Updates the JObjects Dynamically
    private void updateComponentSizes() {
        // Initializing what the text size and title size should be
        int newTextSize = calculateFontSizeBasedOnPanelSize(homePagePanel.getSize(), 5);
        int newTitleSize = calculateFontSizeBasedOnPanelSize(homePagePanel.getSize(), 15);

        // Dynamically updating the JObjects
        // * HomePage Objects
        homePageLabel.setFont(new Font("Segoe UI", Font.BOLD, newTitleSize));
        trialsButton.setFont(new Font("Segoe UI", Font.PLAIN, newTextSize));
        movementsButton.setFont(new Font("Segoe UI", Font.PLAIN, newTextSize));
        petsButton.setFont(new Font("Segoe UI", Font.PLAIN, newTextSize));
        deceasedAnimalsButton.setFont(new Font("Segoe UI", Font.PLAIN, newTextSize));
        sheltersButton.setFont(new Font("Segoe UI", Font.PLAIN, newTextSize));
        // * SpecificPage Objects
        specificLabel.setFont(new Font("Segoe UI", Font.BOLD, newTitleSize));
        homeButton_TP.setFont(new Font("Segoe UI", Font.PLAIN, newTextSize));
        breifDataScrollPane.setSize(new Dimension(newTextSize , newTextSize*3));
        // TODO: Add every object
    }

    // * Calculates the Dynamic J-Objects
    private int calculateFontSizeBasedOnPanelSize(Dimension panelSize, int textSize) {

        int panelWidth = panelSize.width;

        // Calculates the font size proportionally to the panel width
        int newTextSize = (int)(textSize * ((double)panelWidth / 300));

        // Ensure the font size doesn't get too small or too large
        newTextSize = Math.max(newTextSize, 0); // Minimum font size
        newTextSize = Math.min(newTextSize, 1000); // Maximum font size

        return newTextSize;
    }

    // Structure of the program

    // * Starts the program
    public static void main(String[] args) {
        // ! Do not manipulate
        new Main();
    }

    // * Runs the GUI Components
    public Main() {
        // ! Do not manipulate
        initComponents();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadData();
    }

    // * The Components
    // ! Do not manipulate anything past this point
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Braeden Connors (Braeden C Connors)
        programStructure = new JPanel();
        homePagePanel = new JPanel();
        homePageLabel = new JLabel();
        trialsButton = new JButton();
        movementsButton = new JButton();
        petsButton = new JButton();
        deceasedAnimalsButton = new JButton();
        sheltersButton = new JButton();
        breifDataScrollPane = new JScrollPane();
        breifDataTable = new JTable();
        specificPagePanel = new JPanel();
        specificLabel = new JLabel();
        trials_Pane = new JTabbedPane();
        trials_SearchPane = new JPanel();
        searchBox = new JComboBox();
        searchField = new JTextField();
        searchButton = new JButton();
        resultNumberLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        trials_AddPane = new JPanel();
        addLabel1 = new JLabel();
        addLabel10 = new JLabel();
        addLabel2 = new JLabel();
        addLabel11 = new JLabel();
        addLabel3 = new JLabel();
        addLabel12 = new JLabel();
        addLabel4 = new JLabel();
        addLabel5 = new JLabel();
        addLabel6 = new JLabel();
        addLabel7 = new JLabel();
        addLabel8 = new JLabel();
        addLabel9 = new JLabel();
        trials_RemovePane = new JPanel();
        homeButton_TP = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== programStructure ========
        {
            programStructure.setLayout(new CardLayout());

            //======== homePagePanel ========
            {
                homePagePanel.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        homePagePanelComponentResized(e);
                    }
                });
                homePagePanel.setLayout(new MigLayout(
                    "fill,insets 1,hidemode 3,align center center",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- homePageLabel ----
                homePageLabel.setText("Homepage");
                homePageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 21));
                homePageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                homePageLabel.setMaximumSize(null);
                homePageLabel.setMinimumSize(null);
                homePagePanel.add(homePageLabel, "cell 2 0 5 1");

                //---- trialsButton ----
                trialsButton.setText("Trials");
                trialsButton.setFocusPainted(false);
                trialsButton.setBackground(Color.white);
                trialsButton.setMaximumSize(null);
                trialsButton.setMinimumSize(null);
                trialsButton.addActionListener(e -> trials(e));
                homePagePanel.add(trialsButton, "cell 2 2");

                //---- movementsButton ----
                movementsButton.setText("Movements");
                movementsButton.setFocusPainted(false);
                movementsButton.setBackground(Color.white);
                movementsButton.addActionListener(e -> movements(e));
                homePagePanel.add(movementsButton, "cell 3 2");

                //---- petsButton ----
                petsButton.setText("Pets");
                petsButton.setFocusPainted(false);
                petsButton.setBackground(Color.white);
                petsButton.addActionListener(e -> pets(e));
                homePagePanel.add(petsButton, "cell 4 2");

                //---- deceasedAnimalsButton ----
                deceasedAnimalsButton.setText("Deceased Animals");
                deceasedAnimalsButton.setFocusPainted(false);
                deceasedAnimalsButton.setBackground(Color.white);
                deceasedAnimalsButton.addActionListener(e -> deceasedAnimals(e));
                homePagePanel.add(deceasedAnimalsButton, "cell 5 2");

                //---- sheltersButton ----
                sheltersButton.setText("Shelters");
                sheltersButton.setFocusPainted(false);
                sheltersButton.setBackground(Color.white);
                sheltersButton.addActionListener(e -> shelters(e));
                homePagePanel.add(sheltersButton, "cell 6 2");

                //======== breifDataScrollPane ========
                {
                    breifDataScrollPane.setViewportView(breifDataTable);
                }
                homePagePanel.add(breifDataScrollPane, "cell 0 3 9 6,growy");
            }
            programStructure.add(homePagePanel, "card1");

            //======== specificPagePanel ========
            {
                specificPagePanel.setLayout(new MigLayout(
                    "fill,insets 1,hidemode 3,align center center",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- specificLabel ----
                specificLabel.setText("null Database");
                specificLabel.setFont(new Font("Segoe UI", Font.PLAIN, 21));
                specificLabel.setHorizontalAlignment(SwingConstants.CENTER);
                specificPagePanel.add(specificLabel, "cell 0 0 9 1");

                //======== trials_Pane ========
                {
                    trials_Pane.setBackground(Color.white);
                    trials_Pane.setTabPlacement(SwingConstants.RIGHT);
                    trials_Pane.setPreferredSize(new Dimension(208, 220));

                    //======== trials_SearchPane ========
                    {
                        trials_SearchPane.setLayout(new MigLayout(
                            "fill,insets 1,hidemode 3,align center center",
                            // columns
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                            // rows
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));
                        trials_SearchPane.add(searchBox, "cell 0 2");
                        trials_SearchPane.add(searchField, "cell 1 2 6 1");

                        //---- searchButton ----
                        searchButton.setText("Search");
                        searchButton.addActionListener(e -> search(e));
                        trials_SearchPane.add(searchButton, "cell 7 2");

                        //---- resultNumberLabel ----
                        resultNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        trials_SearchPane.add(resultNumberLabel, "cell 8 2");

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setViewportView(table1);
                        }
                        trials_SearchPane.add(scrollPane1, "cell 0 4 9 7,grow");
                    }
                    trials_Pane.addTab("Search", trials_SearchPane);

                    //======== trials_AddPane ========
                    {
                        trials_AddPane.setLayout(new MigLayout(
                            "fill,insets 1,hidemode 3,align center center",
                            // columns
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                            // rows
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));

                        //---- addLabel1 ----
                        addLabel1.setText("text");
                        trials_AddPane.add(addLabel1, "cell 0 0");

                        //---- addLabel10 ----
                        addLabel10.setText("text");
                        trials_AddPane.add(addLabel10, "cell 4 0");

                        //---- addLabel2 ----
                        addLabel2.setText("text");
                        trials_AddPane.add(addLabel2, "cell 0 1");

                        //---- addLabel11 ----
                        addLabel11.setText("text");
                        trials_AddPane.add(addLabel11, "cell 4 1");

                        //---- addLabel3 ----
                        addLabel3.setText("text");
                        trials_AddPane.add(addLabel3, "cell 0 2");

                        //---- addLabel12 ----
                        addLabel12.setText("text");
                        trials_AddPane.add(addLabel12, "cell 4 2");

                        //---- addLabel4 ----
                        addLabel4.setText("text");
                        trials_AddPane.add(addLabel4, "cell 0 3");

                        //---- addLabel5 ----
                        addLabel5.setText("text");
                        trials_AddPane.add(addLabel5, "cell 0 4");

                        //---- addLabel6 ----
                        addLabel6.setText("text");
                        trials_AddPane.add(addLabel6, "cell 0 5");

                        //---- addLabel7 ----
                        addLabel7.setText("text");
                        trials_AddPane.add(addLabel7, "cell 0 6");

                        //---- addLabel8 ----
                        addLabel8.setText("text");
                        trials_AddPane.add(addLabel8, "cell 0 7");

                        //---- addLabel9 ----
                        addLabel9.setText("text");
                        trials_AddPane.add(addLabel9, "cell 0 8");
                    }
                    trials_Pane.addTab("Add", trials_AddPane);

                    //======== trials_RemovePane ========
                    {
                        trials_RemovePane.setLayout(new MigLayout(
                            "fill,insets 1,hidemode 3,align center center",
                            // columns
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]" +
                            "[fill]",
                            // rows
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]" +
                            "[]"));
                    }
                    trials_Pane.addTab("Remove", trials_RemovePane);
                }
                specificPagePanel.add(trials_Pane, "cell 0 1 9 7,growy");

                //---- homeButton_TP ----
                homeButton_TP.setText("Home");
                homeButton_TP.setBackground(Color.white);
                homeButton_TP.setPreferredSize(new Dimension(40, 23));
                homeButton_TP.setMinimumSize(null);
                homeButton_TP.setMaximumSize(null);
                homeButton_TP.addActionListener(e -> home(e));
                specificPagePanel.add(homeButton_TP, "cell 0 8");
            }
            programStructure.add(specificPagePanel, "card2");
        }
        contentPane.add(programStructure, BorderLayout.CENTER);
        setSize(600, 360);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Educational license - Braeden Connors (Braeden C Connors)
    private JPanel programStructure;
    private JPanel homePagePanel;
    private JLabel homePageLabel;
    private JButton trialsButton;
    private JButton movementsButton;
    private JButton petsButton;
    private JButton deceasedAnimalsButton;
    private JButton sheltersButton;
    private JScrollPane breifDataScrollPane;
    private JTable breifDataTable;
    private JPanel specificPagePanel;
    private JLabel specificLabel;
    private JTabbedPane trials_Pane;
    private JPanel trials_SearchPane;
    private JComboBox searchBox;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel resultNumberLabel;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel trials_AddPane;
    private JLabel addLabel1;
    private JLabel addLabel10;
    private JLabel addLabel2;
    private JLabel addLabel11;
    private JLabel addLabel3;
    private JLabel addLabel12;
    private JLabel addLabel4;
    private JLabel addLabel5;
    private JLabel addLabel6;
    private JLabel addLabel7;
    private JLabel addLabel8;
    private JLabel addLabel9;
    private JPanel trials_RemovePane;
    private JButton homeButton_TP;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
