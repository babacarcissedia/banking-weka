package banking;

import banking.client.ClientIndex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App extends JFrame {
    private Container mContainer;
    private JPanel mElementsContainer;
    private JMenuBar mMenuBar;

    public App (String title) {
        setTitle(title);
        mContainer = getContentPane();
        mContainer.setLayout(new BorderLayout());
        mElementsContainer = new JPanel(new FlowLayout());
        setSize(800, 620);

        mElementsContainer.add(new ClientIndex());

        setUpMenuBar();
        mContainer.add(BorderLayout.NORTH, mMenuBar);
        mContainer.add(BorderLayout.CENTER, mElementsContainer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent windowEvent) { System.exit(0); }
        });
    }


    private void setUpMenuBar () {
        mMenuBar = new JMenuBar();
        mMenuBar.add(setUpFileMenu());
        mMenuBar.add(Box.createHorizontalGlue());
        mMenuBar.add(setUpHelpMenu());
    }

    private JMenu setUpFileMenu () {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        JMenuItem openItem = new JMenuItem("Open");
        // openItem.addActionListener(actionEvent -> {});
        fileMenu.add(openItem);
        fileMenu.add(new JMenuItem("Import"));
        fileMenu.add(new JMenuItem("Export"));
        return fileMenu;
    }

    private JMenu setUpHelpMenu () {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.getAccessibleContext().setAccessibleDescription("Help menu");
        helpMenu.add(new JMenuItem("About"));
        return helpMenu;
    }
}