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


    // * Connection
    // ! Don't change
    static final String databasePrefix = "cs366-2241_connorsbc22";
    static final String netID = "connorsbc22";
    static final String hostName = "washington.uww.edu";
    static final String databaseURL = "jdbc:mariadb://" + hostName + "/" + databasePrefix;
    static final String password = "bc2800";

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
                break;
            case 2: specificLabel.setText("Movements Database");
                loadTableData("Movement");
                break;
            case 3: specificLabel.setText("Pets Database");
                loadTableData("Pet");
                break;
            case 4: specificLabel.setText("Deceased Animals Database");
                loadTableData("DeceasedAnimals");
                break;
            case 5: specificLabel.setText("Shelters Database");
                loadTableData("Shelter");
                break;
            // TODO: Add more pages
            default:
                System.out.println("error");
        }
    }


    // The Dynamic Functions

    // * Initializes updateComponentSizes()
    private void homePagePanelComponentResized(ComponentEvent e) {
        updateComponentSizes();
    }

    // this is a test change

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

    // Test edit

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
        textField1 = new JTextField();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        trials_AddPane = new JPanel();
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
                        trials_SearchPane.add(textField1, "cell 1 2 6 1");

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
    private JTextField textField1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel trials_AddPane;
    private JPanel trials_RemovePane;
    private JButton homeButton_TP;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
