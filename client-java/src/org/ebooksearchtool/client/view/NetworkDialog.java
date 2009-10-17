package org.ebooksearchtool.client.view;

import javax.swing.*;
import java.awt.*;

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

    public NetworkDialog(){
        myPanel = new JPanel();
        myFrame.setContentPane(myPanel);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        pan2.add(new JTextField());
        pan2.add(new JLabel("port"));
        pan2.add(new JTextField());

    }

}
