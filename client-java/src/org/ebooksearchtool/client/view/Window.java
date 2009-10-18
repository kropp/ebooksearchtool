package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 11.10.2009
 * Time: 13:59:33
 * To change this template use File | Settings | File Templates.
 */
public class Window {
    private JFrame myFrame;
    private JPanel myPanel1 = new JPanel();
    private JPanel myQueryPanel;
    private JTextField myQueryField;
    private JButton mySearchButton;
    private JTextArea myDataTextArea;
    private JMenuItem myNetMenu;
    
    private Controller myController = new Controller();

    public JFrame getFrame() {
        return myFrame;
    }

    public Window(){

        myFrame = new JFrame("MainWindow");
        JMenuBar myMenuBar;
        myMenuBar = new JMenuBar();
        myMenuBar.add(new JMenu("Settings"));
        myNetMenu = new JMenuItem("Network");
        myMenuBar.getMenu(0).add(myNetMenu);
        myFrame.setJMenuBar(myMenuBar);
        myFrame.setContentPane(myPanel1);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(400, 400);
        myFrame.setLocation(10,10);
        myFrame.setVisible(true);

    	myPanel1.setLayout(new BorderLayout());
    	myQueryPanel = new JPanel();
    	myQueryPanel.setLayout(new BoxLayout(myQueryPanel ,BoxLayout.X_AXIS));
    	myPanel1.add(myQueryPanel, "North");
    	myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryPanel.add(myQueryField);
    	mySearchButton = new JButton();
    	mySearchButton.setText("SEARCH");
    	myQueryPanel.add(mySearchButton);
    	//myDataPanel = new JPanel();
    	myDataTextArea = new JTextArea();
        myPanel1.add(new JScrollPane(myDataTextArea), "Center");
        myDataTextArea.setEditable(false);

    	/*myConnectionPanel = new JPanel();
        myConnectionPanel.setVisible(false);
*/
        mySearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    String queryWord = myQueryField.getText();
                    myController.act(queryWord);
                    myDataTextArea.setLineWrap(true);
                    /*myDataTextArea.setColumns(myController.getData().getAttributes().length);
                    myDataTextArea.setRows(myController.getData().getInfo().size());*/
                    for (int i = 0; i < myController.getData().getInfo().size(); ++i){
                        myDataTextArea.append(myController.getData().getInfo().get(i).getTitle() + "\n");
                        myDataTextArea.append(myController.getData().getInfo().get(i).getAuthor() + "\n");
                        myDataTextArea.append(myController.getData().getInfo().get(i).getLanguage() + "\n");
                        myDataTextArea.append(myController.getData().getInfo().get(i).getDate() + "\n");
                        myDataTextArea.append(myController.getData().getInfo().get(i).getSummary() + "\n");
                        myDataTextArea.append("\n");
    	            }
                    
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SAXException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });

        myNetMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                NetworkDialog dialogFrame = new NetworkDialog();

            }

        });


    }

}