package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 17.10.2009
 * Time: 21:57:00
 * To change this template use File | Settings | File Templates.
 */
public class NetworkDialog {

    JFrame myFrame = new JFrame("Network settings");
    JPanel myPanel;
    JLabel myTitle;
    JTextField myIPText;
    JTextField myPortText;
    JPanel myIpPanel;
    JPanel myPortPanel;

    private Controller myController;

    public NetworkDialog(Controller controller) throws IOException, SAXException, ParserConfigurationException {

        myController = controller;

        myPanel = new JPanel();
        myFrame.setContentPane(myPanel);
        myFrame.setSize(400, 300);
        myFrame.setLocation(20,20);
        myFrame.setVisible(true);
        myFrame.setAlwaysOnTop(true);

        myPanel.setLayout(new BorderLayout());
        myTitle = new JLabel("Set Proxy Properties");
        JPanel pan1 = new JPanel(new FlowLayout());
        myPanel.add(pan1, "North");
        pan1.add(myTitle);
        JPanel pan2 = new JPanel(new GridLayout(2, 2));
        myPanel.add(pan2, "Center");
        pan2.add(new JLabel("IP"));
        myIPText = new JTextField();
        myPortText = new JTextField();
        pan2.add(myIPText, "Center");
        pan2.add(new JLabel("port"));
        pan2.add(myPortText);

        myIPText.setText(myController.getSettings().getIP());
        myPortText.setText(String.valueOf(myController.getSettings().getPort()));

        myFrame.setDefaultCloseOperation(exit());

    }

    public int exit() throws IOException, SAXException, ParserConfigurationException {
        myController.setSettings(myIPText.getText(), Integer.parseInt(myPortText.getText()));
        return JFrame.DISPOSE_ON_CLOSE;
    }

}
