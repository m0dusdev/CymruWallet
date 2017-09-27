package cymruWallet;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;

/**
 * <li>The gui and menu bar of the wallet, handles user input and displays cymru wallet daemon info</li>
 * @see cymruWallet.FromProcess - deamon ouput passed here
 *
 */
final class Gui extends JFrame {


    private static JEditorPane consoleOutput; // console window


    private static StringBuilder build = new StringBuilder();


    private static JLabel balanceText = new JLabel();  // balance text
    private static JLabel versionText = new JLabel();   //  version text

    private Gui() {
        super("cymrucoin wallet");





        setVisible(true);
        setDefaultCloseOperation(3);
        setPreferredSize(new Dimension(400, 800));
        setSize(400, 700);

        //setResizable(false); broken on ubuntu mate 16.04


        setLocationRelativeTo(null);

        // set menu bar
        setJMenuBar(new menubar());

        // panels
        JPanel topPanel;
        JPanel bottomPanel;

        // fonts
        Font blanceFont = new Font("Franklin Gothic Light", Font.PLAIN, 20);
        Font versionFont = new Font("Ebrima", Font.PLAIN, 12);


        balanceText.setFont(blanceFont);
        balanceText.setBounds(0, 10, 300, 18);

        versionText.setFont(versionFont);
        versionText.setBounds(12, 41, 300, 18);


        topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(Color.decode("#EEEEEE"));
        topPanel.setVisible(true);
        topPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        topPanel.add(balanceText);
        topPanel.add(versionText);


        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setVisible(true);


        consoleOutput = new JEditorPane();
        consoleOutput.setAutoscrolls(false);
        consoleOutput.setBackground(Color.decode("#D3D3D3"));
        consoleOutput.setEditable(false);


        bottomPanel.add(new JScrollPane(consoleOutput));


        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                topPanel, bottomPanel);
        pane.setVisible(true);
        pane.setResizeWeight(0.20);


        add(pane);

        try {
            // start thread
            triggerThread();
        } catch (IOException | InterruptedException io) {
            io.printStackTrace();
        }

    }



    private void triggerThread() throws IOException, InterruptedException {
        // start the update thread
        FromProcess process = new FromProcess();
        FromProcess.getLog();
        process.startUpdate();
    }

    // set version text
    static void updateVersion(String version) {
        versionText.setText(version);
    }

    /**
     * add text to the console
     * @param content - The content to send to the console
     */
    static void addToConsole(String content) {
        build.append("cc -> ");
        build.append(content);
        build.append("\n");
        consoleOutput.setText(build.toString());
    }

    // set balance
    static void setBlance(String bal) {
        balanceText.setText(bal);
    }


    /**
     * inner class representing a JmenuBar
     */
    private final class menubar extends JMenuBar {

        private menubar() {
            setVisible(true);
            JMenu wallet;
            JMenu deamon;
            JMenu about;
            JMenu console;


            // wallet menu item
            wallet = new JMenu("Wallet");
            JMenuItem exit = new JMenuItem("Exit");
            exit.addActionListener((e) -> System.exit(0));
            wallet.add(exit);
            wallet.setVisible(true);
            exit.setVisible(true);

            add(wallet);


            // console menu
            console = new JMenu("Console");
            console.setToolTipText("Access options regarding the console");


            // console -> showConsole menu item
            JMenuItem showConsole = new JMenuItem("Dump log to console");

            showConsole.addActionListener((e) -> FromProcess.getLog());

            console.add(showConsole);

            // console -> hide console window
            JMenuItem hideCosnole = new JMenuItem("Hide console");
            hideCosnole.addActionListener((eh) -> Gui.addToConsole("Not implemented yet") );


            console.add(hideCosnole);

            // console clear -> menu item
            JMenuItem clearConsole = new JMenuItem("clear");

            clearConsole.addActionListener((cc) -> {
                consoleOutput.setText(""); //clear the console

                build.delete(0, build.length()); // clear the console buffer
            });

            console.add(clearConsole);
            add(console);

            // about menu
            about = new JMenu("About");
            JMenuItem version = new JMenuItem("version");
            version.addActionListener((e) -> new About());
            about.add(version);

            add(about);

            deamon = new JMenu("Daemon");
            deamon.setToolTipText("Wallet deamon Options");

            JMenuItem stopDaemon = new JMenuItem("Stop daemon");
            stopDaemon.addActionListener((e) ->
                    new FromProcess().stopDeamon());

            deamon.add(stopDaemon);


            JMenuItem startDeamon = new JMenuItem("Restart daemon");

            startDeamon.addActionListener((se) -> FromProcess.doCmd("./cymrucoind &", true));


            deamon.add(startDeamon);


            JMenuItem refreshLog = new JMenuItem("Refresh log");
            refreshLog.addActionListener((re) -> FromProcess.getLog());

            deamon.add(refreshLog);

            add(deamon);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Gui();
        });
    }


}
