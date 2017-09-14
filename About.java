package cymruWallet;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *      DIALOG FOR ""
 */
 final class About extends JDialog  {

    private final JEditorPane jEditorPane;

    About() {

        setDefaultCloseOperation(1); // Hide on close
        //ADD AN ICON

        setIconImage(null);

        setLayout(new BorderLayout());
        setTitle("About");
        setSize(170, 90);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        jEditorPane = new JEditorPane();
        jEditorPane.setContentType("text/html");
        jEditorPane.setEditable(false);


        jEditorPane.setFont(new Font("Futura", Font.BOLD, 25));
        jEditorPane.setBackground(Color.decode("#EEEEEE"));
        jEditorPane.setForeground(Color.decode("#212121"));
        jEditorPane.setText("cymrucoinwallet v02");

        add(jEditorPane, SwingConstants.CENTER);
    }
}


