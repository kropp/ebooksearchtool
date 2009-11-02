package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: 
 * Date: 17.10.2009
 * Time: 21:57:00
 * To change this template use File | Settings | File Templates.
 */
public class NetworkDialog {

    JFrame myFrame = new JFrame("Network settings");
    JPanel myPanel;
    JLabel myTitle;
    JTextField myServerText;
    JTextField myIPText;
    JTextField myPortText;
    JPanel myIpPanel;
    JPanel myPortPanel;
    JCheckBox myProxyCheck;

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
        JPanel mainPan = new JPanel(new GridLayout(4, 2));
        myPanel.add(mainPan, "Center");
        
        mainPan.add(new JLabel("server"));
        myServerText = new JTextField();
        mainPan.add(myServerText);
        mainPan.add(new JLabel("Use proxy"));
        myProxyCheck = new JCheckBox();
        mainPan.add(myProxyCheck);
        mainPan.add(new JLabel("IP"));
        myIPText = new JTextField();
        mainPan.add(myIPText);
        mainPan.add(new JLabel("port"));
        myPortText = new JTextField();
        mainPan.add(myPortText);
        myProxyCheck.setSelected(myController.getSettings().isProxyEnabled());
        if(myProxyCheck.isSelected()){
            myIPText.setEnabled(true);
            myPortText.setEnabled(true);
        }else{
            myIPText.setEnabled(false);
            myPortText.setEnabled(false);
        }

        myProxyCheck.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
                if(myProxyCheck.isSelected()){
                    myIPText.setEnabled(true);
                    myPortText.setEnabled(true);
                }else{
                    myIPText.setEnabled(false);
                    myPortText.setEnabled(false);
                }
    			myPanel.updateUI();
    		}
        });
        

        myServerText.setText(myController.getSettings().getServer());
        myIPText.setText(myController.getSettings().getIP());
        myPortText.setText(String.valueOf(myController.getSettings().getPort()));

        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        myServerText.setEditable(true);
        
        myFrame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
            	try {
					myController.setSettings(myServerText.getText(),myProxyCheck.isSelected(), myIPText.getText(), Integer.parseInt(myPortText.getText()));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {					
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {					
					e1.printStackTrace();
				}
        	}
        });
        
    }

}
