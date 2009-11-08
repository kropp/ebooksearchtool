
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
    private JProgressBar myProgressBar;
    private JPanel myCentralPanel;
    private JButton myMoreButton;
    private JToolBar myToolBar;
    

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
        myCentralPanel = new JPanel(new BorderLayout());
        myPanel1.add(myCentralPanel, "Center");


    	myQueryPanel = new JPanel();
    	myQueryPanel.setLayout(new BoxLayout(myQueryPanel ,BoxLayout.X_AXIS));
    	myCentralPanel.add(myQueryPanel, "North");

        myToolBar = new JToolBar();
        myToolBar.add(new JButton(new ImageIcon("ico\\library_30.gif")));
        myPanel1.add(myToolBar, "North");

    	myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryPanel.add(myQueryField);
        String[] query = new String[] { "General", "Author", "Title" };
        myQueryCombo = new JComboBox(query);
        myQueryPanel.add(myQueryCombo);
    	mySearchButton = new JButton();
    	mySearchButton.setText("SEARCH");
    	myQueryPanel.add(mySearchButton);
    	myTextPan = new JPanel();
    	BoxLayout box = new BoxLayout(myTextPan, BoxLayout.Y_AXIS);
    	myTextPan.setLayout(box);
        myCentralPanel.add(new JScrollPane(myTextPan), "Center");

        JPanel morePanel = new JPanel(new FlowLayout());
        myMoreButton = new JButton("More books");
        morePanel.add(myMoreButton);
        myCentralPanel.add(morePanel, "South");
        
        final DefaultBoundedRangeModel model = new DefaultBoundedRangeModel(0, 0, 0, 100);
        myProgressBar = new JProgressBar(model);
        myProgressBar.setStringPainted(true);
        myPanel1.add(myProgressBar, "South");

        
        
        ActionListener act = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                
            	Thread process = new Thread(new Runnable() {
            		public void run() {
            			model.setValue(0);
            			myProgressBar.setString("Sending request... 0%");
            		
            			String queryWord = myQueryField.getText();
            			model.setValue(5);
            			String queryOption = (String)myQueryCombo.getSelectedItem();
            			model.setValue(8);
            			myProgressBar.setString("Recieving data... 5%");
            			try {
							myController.getQueryAnswer(queryWord, queryOption);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						}
            			model.setValue(30);

            			BookPanel[] BP = new BookPanel[myController.getData().getBooks().size()];
            			model.setValue(35);
            			for(int i = 0; i < myController.getData().getBooks().size(); ++i){
            				try {
								BP[i] = new BookPanel(myController.getData().getBooks().get(i), myController.getSettings());
								model.setValue(model.getValue() + 5);
								myProgressBar.setString("Viewing book... " + model.getValue() + "%");
            				} catch (IOException e) {
								e.printStackTrace();
							} catch (SAXException e) {
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								e.printStackTrace();
							}
            				JPanel bookPan = BP[i].getRootPanel();
            				myTextPan.add(bookPan);
            				bookPan.setVisible(true);
            				myFrame.setVisible(true);
            			}
            			model.setValue(100);
                        myProgressBar.setString("");
            		}
            	});
            	process.start();
        }
            
        };
        
        mySearchButton.addActionListener(act);
        myQueryField.addActionListener(act);

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


        myFrame.setSize(700, 700);
        myFrame.setLocation(10,10);
        myFrame.setVisible(true);
    }

}
