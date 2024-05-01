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
    String[] AddLabels;
    String[] changeComboLabels;

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
        searchData(tableName, (String) searchBox.getSelectedItem(), searchField.getText());
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

    // Adding functionality

    // * Creates user's data
    private void add(ActionEvent e) {
        String[] columnNames;
        String[] values;

        if (specificLabel.getText().equals("Trials Database") || specificLabel.getText().equals("Movements Database")){
            columnNames = new String[4];
            values = new String[4];

            columnNames[0] = addLabel1.getText();
            columnNames[1] = addLabel2.getText();
            columnNames[2] = addLabel3.getText();
            columnNames[3] = addLabel4.getText();

            values[0] = addField1.getText();
            values[1] = addField2.getText();
            values[2] = addField3.getText();
            values[3] = addField4.getText();

            addData(tableName, columnNames, values);
        } else if (specificLabel.getText().equals("Pets Database")) {
            columnNames = new String[11];
            values = new String[11];

            columnNames[0] = addLabel1.getText();
            columnNames[1] = addLabel2.getText();
            columnNames[2] = addLabel3.getText();
            columnNames[3] = addLabel4.getText();
            columnNames[4] = addLabel5.getText();
            columnNames[5] = addLabel6.getText();
            columnNames[6] = addLabel7.getText();
            columnNames[7] = addLabel8.getText();
            columnNames[8] = addLabel9.getText();
            columnNames[9] = addLabel10.getText();
            columnNames[10] = addLabel11.getText();

            values[0] = addField1.getText();
            values[1] = addField2.getText();
            values[2] = addField3.getText();
            values[3] = addField4.getText();
            values[4] = addField5.getText();
            values[5] = addField6.getText();
            values[6] = addField7.getText();
            values[7] = addField8.getText();
            values[8] = addField9.getText();
            values[9] = addField10.getText();
            values[10] = addField11.getText();

            addData(tableName, columnNames, values);
        } else if (specificLabel.getText().equals("Deceased Animals Database")) {
            columnNames = new String[6];
            values = new String[6];

            columnNames[0] = addLabel1.getText();
            columnNames[1] = addLabel2.getText();
            columnNames[2] = addLabel3.getText();
            columnNames[3] = addLabel4.getText();
            columnNames[4] = addLabel5.getText();
            columnNames[5] = addLabel6.getText();

            values[0] = addField1.getText();
            values[1] = addField2.getText();
            values[2] = addField3.getText();
            values[3] = addField4.getText();
            values[4] = addField5.getText();
            values[5] = addField6.getText();

            addData(tableName, columnNames, values);

        }else if (specificLabel.getText().equals("Shelters Database")) {
            columnNames = new String[2];
            values = new String[2];

            columnNames[0] = addLabel1.getText();
            columnNames[1] = addLabel2.getText();

            values[0] = addField1.getText();
            values[1] = addField2.getText();

            addData(tableName, columnNames, values);
        } else {
            System.out.println("Error at add Button Event");
        }
    }

    // * Add data to the table
    private void addData(String tableName, String[] columnNames, String[] values) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("databaseURL: " + databaseURL);
            connection = DriverManager.getConnection(databaseURL, netID, password);
            System.out.println("Successfully updated");

            String query = "INSERT INTO " + tableName + " (";
            for (int i = 0; i < columnNames.length; i++) {
                query += columnNames[i];
                if (i < columnNames.length - 1) {
                    query += ", ";
                }
            }

            query += ") VALUES (";
            for (int i = 0; i < values.length; i++) {
                query += "?";
                if (i < values.length - 1) {
                    query += ", ";
                }
            }
            query += ");";

            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();

            //! Error handling. Don't change.
            preparedStatement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection!= null) connection.close();
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
            case 1:
                specificLabel.setText("Trials Database");
                changeComboLabels = new String[] {"id", "istrial", "returndate", "returnedreason", ""};
                AddLabels = new String[]{"id", "istrial", "returndate", "returnedreason", "", "", "", "", "", "", "", ""};
                setSearchBox(changeComboLabels, "Trial");
                setAddLabels(AddLabels, 4);
                break;
            case 2:
                specificLabel.setText("Movements Database");
                changeComboLabels = new String[] {"id", "location", "movementdate", "movementtype", ""};
                AddLabels = new String[]{"id", "location", "movementdate", "movementtype", "", "", "", "", "", "", "", ""};
                setSearchBox(changeComboLabels, "Movement");
                setAddLabels(AddLabels, 4);
                break;
            case 3:
                specificLabel.setText("Pets Database");
                changeComboLabels = new String[] {"sexname", "speciesname", "breedname", "id", "intakedate", "basecolour", "intakereason", "istransfer", "identichipnumber", "animalname", "animalage", ""};
                AddLabels = new String[]{"sexname", "speciesname", "breedname", "id", "intakedate", "basecolour", "intakereason", "istransfer", "identichipnumber", "animalname", "animalage", ""};
                setSearchBox(changeComboLabels, "Pet");
                setAddLabels(AddLabels, 11);
                break;
            case 4:
                specificLabel.setText("Deceased Animals Database");
                changeComboLabels = new String[] {"id", "deceaseddate", "deceasedreason", "diedoffshelter", "puttosleep", "isdoa", ""};
                AddLabels = new String[]{"id", "deceaseddate", "deceasedreason", "diedoffshelter", "puttosleep", "isdoa", "", "", "", "", "", ""};
                setSearchBox(changeComboLabels, "DeceasedAnimals");
                setAddLabels(AddLabels, 6);
                break;
            case 5:
                specificLabel.setText("Shelters Database");
                changeComboLabels = new String[] {"id", "sheltercode", ""};
                AddLabels = new String[]{"id", "sheltercode", "", "", "", "", "", "", "", "", "", ""};
                setSearchBox(changeComboLabels, "Shelter");
                setAddLabels(AddLabels, 2);
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

    // * Sets the ComboBox names for searching. And initializes the table.
    public void setSearchBox(String[] x, String y){
        loadTableData(y);
        for (int i = searchBox.getItemCount() - 1; i >= 0; i--) {
            searchBox.removeItemAt(i);
        }
        tableName = y;
        for (String s : x) {
            searchBox.addItem(s);
        }
    }

    // * Changes the Labels for adding data and the Fields whether they're visible or not.
    public void setAddLabels(String[] x, int y) {
        addLabel1.setText(x[0]);
        addLabel2.setText(x[1]);
        addLabel3.setText(x[2]);
        addLabel4.setText(x[3]);
        addLabel5.setText(x[4]);
        addLabel6.setText(x[5]);
        addLabel7.setText(x[6]);
        addLabel8.setText(x[7]);
        addLabel9.setText(x[8]);
        addLabel10.setText(x[9]);
        addLabel11.setText(x[10]);
        addLabel12.setText(x[11]);

        boolean[] onOff = new boolean[12];
        for (int i = 0; i < y; i++) {
            onOff[i] = true;
        }
        for (int i = y; i < onOff.length; i++) {
            onOff[i] = false;
        }
        addField1.setVisible(onOff[0]);
        addField2.setVisible(onOff[1]);
        addField3.setVisible(onOff[2]);
        addField4.setVisible(onOff[3]);
        addField5.setVisible(onOff[4]);
        addField6.setVisible(onOff[5]);
        addField7.setVisible(onOff[6]);
        addField8.setVisible(onOff[7]);
        addField9.setVisible(onOff[8]);
        addField10.setVisible(onOff[9]);
        addField11.setVisible(onOff[10]);
        addField12.setVisible(onOff[11]);




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
        addField1 = new JTextField();
        addLabel10 = new JLabel();
        addField10 = new JTextField();
        addLabel2 = new JLabel();
        addField2 = new JTextField();
        addLabel11 = new JLabel();
        addField11 = new JTextField();
        addLabel3 = new JLabel();
        addField3 = new JTextField();
        addLabel12 = new JLabel();
        addField12 = new JTextField();
        addLabel4 = new JLabel();
        addField4 = new JTextField();
        addLabel5 = new JLabel();
        addField5 = new JTextField();
        addLabel6 = new JLabel();
        addField6 = new JTextField();
        addLabel7 = new JLabel();
        addField7 = new JTextField();
        addLabel8 = new JLabel();
        addField8 = new JTextField();
        addLabel9 = new JLabel();
        addField9 = new JTextField();
        addButton = new JButton();
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
                        trials_AddPane.add(addField1, "cell 1 0 3 1");

                        //---- addLabel10 ----
                        addLabel10.setText("text");
                        trials_AddPane.add(addLabel10, "cell 4 0");
                        trials_AddPane.add(addField10, "cell 5 0 3 1");

                        //---- addLabel2 ----
                        addLabel2.setText("text");
                        trials_AddPane.add(addLabel2, "cell 0 1");
                        trials_AddPane.add(addField2, "cell 1 1 3 1");

                        //---- addLabel11 ----
                        addLabel11.setText("text");
                        trials_AddPane.add(addLabel11, "cell 4 1");
                        trials_AddPane.add(addField11, "cell 5 1 3 1");

                        //---- addLabel3 ----
                        addLabel3.setText("text");
                        trials_AddPane.add(addLabel3, "cell 0 2");
                        trials_AddPane.add(addField3, "cell 1 2 3 1");

                        //---- addLabel12 ----
                        addLabel12.setText("text");
                        trials_AddPane.add(addLabel12, "cell 4 2");
                        trials_AddPane.add(addField12, "cell 5 2 3 1");

                        //---- addLabel4 ----
                        addLabel4.setText("text");
                        trials_AddPane.add(addLabel4, "cell 0 3");
                        trials_AddPane.add(addField4, "cell 1 3 3 1");

                        //---- addLabel5 ----
                        addLabel5.setText("text");
                        trials_AddPane.add(addLabel5, "cell 0 4");
                        trials_AddPane.add(addField5, "cell 1 4 3 1");

                        //---- addLabel6 ----
                        addLabel6.setText("text");
                        trials_AddPane.add(addLabel6, "cell 0 5");
                        trials_AddPane.add(addField6, "cell 1 5 3 1");

                        //---- addLabel7 ----
                        addLabel7.setText("text");
                        trials_AddPane.add(addLabel7, "cell 0 6");
                        trials_AddPane.add(addField7, "cell 1 6 3 1");

                        //---- addLabel8 ----
                        addLabel8.setText("text");
                        trials_AddPane.add(addLabel8, "cell 0 7");
                        trials_AddPane.add(addField8, "cell 1 7 3 1");

                        //---- addLabel9 ----
                        addLabel9.setText("text");
                        trials_AddPane.add(addLabel9, "cell 0 8");
                        trials_AddPane.add(addField9, "cell 1 8 3 1");

                        //---- addButton ----
                        addButton.setText("Create");
                        addButton.addActionListener(e -> add(e));
                        trials_AddPane.add(addButton, "cell 7 8");
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
    private JTextField addField1;
    private JLabel addLabel10;
    private JTextField addField10;
    private JLabel addLabel2;
    private JTextField addField2;
    private JLabel addLabel11;
    private JTextField addField11;
    private JLabel addLabel3;
    private JTextField addField3;
    private JLabel addLabel12;
    private JTextField addField12;
    private JLabel addLabel4;
    private JTextField addField4;
    private JLabel addLabel5;
    private JTextField addField5;
    private JLabel addLabel6;
    private JTextField addField6;
    private JLabel addLabel7;
    private JTextField addField7;
    private JLabel addLabel8;
    private JTextField addField8;
    private JLabel addLabel9;
    private JTextField addField9;
    private JButton addButton;
    private JPanel trials_RemovePane;
    private JButton homeButton_TP;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
