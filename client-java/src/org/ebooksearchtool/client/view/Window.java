
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
    private JPanel myTextPan;
    private int ind;
    private JComboBox myQueryCombo;
    
    private JButton[] infB;
    
    private Controller myController;

    public JFrame getFrame() {
        return myFrame;
    }

    public Window() throws SAXException, ParserConfigurationException, IOException {

        myController = new Controller();
        myFrame = new JFrame("MainWindow");
        JMenuBar myMenuBar;
        myMenuBar = new JMenuBar();
        myMenuBar.add(new JMenu("Settings"));
        myNetMenu = new JMenuItem("Network");
        myMenuBar.getMenu(0).add(myNetMenu);
        myFrame.setJMenuBar(myMenuBar);
        myFrame.setContentPane(myPanel1);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	myPanel1.setLayout(new BorderLayout());
    	myQueryPanel = new JPanel();
    	myQueryPanel.setLayout(new BoxLayout(myQueryPanel ,BoxLayout.X_AXIS));
    	myPanel1.add(myQueryPanel, "North");
    	myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryPanel.add(myQueryField);
        String[] query = new String[] { "General", "Author", "Title" };
        myQueryCombo = new JComboBox(query);
        myQueryPanel.add(myQueryCombo);
    	mySearchButton = new JButton();
    	mySearchButton.setText("SEARCH");
    	myQueryPanel.add(mySearchButton);
    	//myDataPanel = new JPanel();
    	myDataTextArea = new JTextArea();
    	myTextPan = new JPanel(new GridLayout(0, 1));
        myPanel1.add(new JScrollPane(myTextPan), "Center");
        myDataTextArea.setEditable(false);

        mySearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    String queryWord = myQueryField.getText();
                    String queryOption = (String)myQueryCombo.getSelectedItem();
                    myController.getQueryAnswer(queryWord, queryOption);
                    JTextArea[] info = new JTextArea[myController.getData().getBooks().size()];
                    infB = new JButton[myController.getData().getBooks().size()];
                    
                    ActionListener[] AL = new ActionListener[myController.getData().getBooks().size()];
                    for (int i = 0; i < myController.getData().getBooks().size(); ++i){
                    	info[i] = new JTextArea();
                    	myTextPan.add(info[i]);
                    	infB[i] = new JButton("download");
                    	
                    	ind = i;
                    	
                    	/*AL[i] = new ActionListener(){
                    		public void actionPerformed(ActionEvent e) {
                    			try {
                                	System.out.println(i);
                                	
                                	myController.getBookFile(i);
                                	
                                }catch (IOException e1) {
                                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                            }
                    	};
                    	infB[i].addActionListener(AL[i]);*/
                    	infB[i].addActionListener(new ActionListener() {
                    		public void actionPerformed(ActionEvent e) {
                    			try {
                                	
                                	for (int k = 0; k < myController.getData().getBooks().size(); ++k){
                                		if(e.getSource() == infB[k]){
                                			myController.getBookFile(k);
                                			System.out.println(k);
                                		}
                                	}
                                	
                                	
                                }catch (IOException e1) {
                                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                            }
                        });
                    	myTextPan.add(infB[i]);
                    	info[i].append(myController.getData().getBooks().get(i).getTitle() + "\n");
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

                try {
                    NetworkDialog dialogFrame = new NetworkDialog(myController);
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SAXException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }

        });


        myFrame.setSize(600, 700);
        myFrame.setLocation(10,10);
        myFrame.setVisible(true);
    }

}
