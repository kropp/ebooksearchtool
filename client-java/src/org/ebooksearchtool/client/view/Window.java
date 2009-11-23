
package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.ebooksearchtool.client.logic.query.Query;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

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
    private JPanel myQueryButtonPanel;
    private JPanel myQueryPanel;
    private JPanel myQueryPlusPanel;
    private JTextField myQueryField;
    private JButton mySearchButton;
    private JButton myQueryButton;
    private JButton myPlusButton;
    private JLabel mySearchLabel;
    private JMenuItem myNetMenu;
    private JPanel myTextPan;
    private JComboBox myQueryCombo;
    private JProgressBar myProgressBar;
    private JPanel myCentralPanel;
    private JButton myMoreButton;
    private JToolBar myToolBar;
    private JLabel myNumberInfo;
    private JPanel myMorePanel;
    private JButton myEraseButton;
    private JButton myToolLibrary;
    private JButton myToolDelete;
    
    private BookPanel[] myBookPanels = null;
    
    private Query myQuery;
    
    private int myActionIndex;
    private String myAdress;

    private Controller myController;

    public JFrame getFrame() {
        return myFrame;
    }

    public Window() throws SAXException, ParserConfigurationException, IOException {

        myController = new Controller();
        myFrame = new JFrame("ebooksearchtool");
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
        myQueryPanel.setLayout(new BoxLayout(myQueryPanel,BoxLayout.Y_AXIS));
    	myQueryButtonPanel = new JPanel();
    	myQueryButtonPanel.setLayout(new BoxLayout(myQueryButtonPanel,BoxLayout.X_AXIS));
        myQueryPlusPanel = new JPanel();
        myQueryPlusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)/*BoxLayout(myQueryPlusPanel,BoxLayout.X_AXIS)*/);

        myQueryPanel.add(Box.createVerticalStrut(5));
        myQueryPanel.add(myQueryButtonPanel);
        myQueryPanel.add(Box.createVerticalStrut(5));
        myQueryPanel.add(myQueryPlusPanel);
        myQueryPanel.add(Box.createVerticalStrut(5));
    	myCentralPanel.add(myQueryPanel, "North");

        

        myToolBar = new JToolBar();
        myToolDelete = new JButton(new ImageIcon(getClass().getResource("/ico/delete_30.gif")));
        myToolBar.add(myToolDelete);
        myToolLibrary = new JButton(new ImageIcon(getClass().getResource("/ico/library_30.gif")));
        myToolBar.add(myToolLibrary);
        myPanel1.add(myToolBar, "North");

    	myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryButtonPanel.add(myQueryField);
        String[] query = new String[] { "General", "Author", "Title" };
        myQueryCombo = new JComboBox(query);
        myQueryButtonPanel.add(myQueryCombo);
        myQueryButton = new JButton("+");
    	
    	
    	mySearchButton = new JButton("SEARCH");
    	mySearchButton.setEnabled(false);
    	myEraseButton = new JButton("ERASE");
    	myEraseButton.setEnabled(false);
    	
    	myEraseButton.setPreferredSize(mySearchButton.getPreferredSize());
    	myEraseButton.setMaximumSize(mySearchButton.getPreferredSize());
    	myEraseButton.setMinimumSize(mySearchButton.getPreferredSize());
    	
    	myQueryButtonPanel.add(Box.createHorizontalStrut(5));
    	myQueryButtonPanel.add(myQueryButton);
    	myQueryButton.setPreferredSize(myQueryButton.getPreferredSize());
    	myQueryButton.setMaximumSize(myQueryButton.getPreferredSize());
    	myQueryButton.setMinimumSize(myQueryButton.getPreferredSize());
    	
    	myQueryPlusPanel.add(myEraseButton);
    	myQueryPlusPanel.add(Box.createHorizontalStrut(5));
    	myQueryPlusPanel.add(mySearchButton);
    	myQueryPlusPanel.add(Box.createHorizontalStrut(15));
    	mySearchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    	mySearchLabel = new JLabel();
        myQueryPlusPanel.add(mySearchLabel);
    	
    	myTextPan = new JPanel();
    	BoxLayout box = new BoxLayout(myTextPan, BoxLayout.Y_AXIS);
    	myTextPan.setLayout(box);
        myCentralPanel.add(new JScrollPane(myTextPan), "Center");

        myMorePanel = new JPanel(new FlowLayout());
        myMoreButton = new JButton("More books");
        myNumberInfo = new JLabel();
        myMorePanel.add(myMoreButton);
        myMorePanel.add(myNumberInfo);
        myMorePanel.setVisible(false);
        myCentralPanel.add(myMorePanel, "South");
        
        final DefaultBoundedRangeModel model = new DefaultBoundedRangeModel(0, 0, 0, 100);
        myProgressBar = new JProgressBar(model);
        myProgressBar.setStringPainted(true);
        myPanel1.add(myProgressBar, "South");
        
        ActionListener library = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         
            	
            	
            }
        };
        
        ActionListener delete = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         
            	if(myBookPanels != null){
            		for(int i = 0; i < myBookPanels.length; ++i){
            			myBookPanels[i].getRootPanel().setVisible(!myBookPanels[i].isSelected());
            		}
            	}
            	
            }
        };
        
        ActionListener setAdress = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
            	try {
					myQuery = new Query(myController.getSettings());
				} catch (SAXException e2) {
					
					e2.printStackTrace();
				} catch (ParserConfigurationException e2) {
					
					e2.printStackTrace();
				} catch (IOException e2) {
					
					e2.printStackTrace();
				}
            	if(myAdress==null){
            		String queryWord = myQueryField.getText();
            		String queryOption = (String)myQueryCombo.getSelectedItem();
            		try {
						myAdress = myQuery.getQueryAdress(queryWord, queryOption);
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
            		mySearchLabel.setText(queryOption + ":" + queryWord);
            		mySearchButton.setEnabled(true);
            		myEraseButton.setEnabled(true);
            		myQueryField.setText("");
            	}else{
            		String queryWord = myQueryField.getText();
            		String queryOption = (String)myQueryCombo.getSelectedItem();
            		myAdress = myQuery.addQueryAdress(queryWord, queryOption, myAdress);
            		mySearchLabel.setText(mySearchLabel.getText() + " " + queryOption + ":" + queryWord);
            		myQueryField.setText("");
            	}
            }
        };
        
        ActionListener erase = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
            	
            	myAdress = null;
            	mySearchLabel.setText("");
            	mySearchButton.setEnabled(false);
            	myEraseButton.setEnabled(false);
            	
            }
        };
          
        
        ActionListener act = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                
            	Thread process = new Thread(new Runnable() {
            		public void run() {
            			model.setValue(0);
            			myProgressBar.setString("Sending request... 0%");
            			model.setValue(5);
            			
            			model.setValue(8);
            			myProgressBar.setString("Recieving data... 5%");
            			int lastNumber = myController.getData().getBooks().size();
            			String prevPage = myController.getData().getNextPage();
            			try {
            				if(e.getSource() != myMoreButton){
            					myTextPan.removeAll();
            					if(!myController.getQueryAnswer(myAdress)){
            						
                                    model.setValue(100);
                                    JOptionPane.showMessageDialog(new JDialog(), "Connection failed", "error", JOptionPane.ERROR_MESSAGE);
                                    myProgressBar.setString("");
                                    return;
                                }
            					myActionIndex = 1;
            				}else{
            					myController.getNextData();
            					myActionIndex++;
            				}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						}
            			model.setValue(30);
            			myProgressBar.setString("Recieving data... " + model.getValue() + "%");
            			
            			myBookPanels = new BookPanel[myController.getData().getBooks().size()];
            			model.setValue(35);
            			for(int i = lastNumber; i < myController.getData().getBooks().size(); ++i){
            				try {
								myBookPanels[i] = new BookPanel(myController.getData().getBooks().get(i), myController.getSettings());
								model.setValue(model.getValue() + 5);
								myProgressBar.setString("Viewing book... " + model.getValue() + "%");
            				} catch (IOException e) {
								e.printStackTrace();
							} catch (SAXException e) {
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								e.printStackTrace();
							}
            				JPanel bookPan = myBookPanels[i].getRootPanel();
            				myTextPan.add(bookPan);
            				bookPan.setVisible(true);
            				myFrame.setVisible(true);
            			}
            			model.setValue(100);
            			mySearchButton.setEnabled(false);
            			myEraseButton.setEnabled(false);
            			myAdress = null;
                        myProgressBar.setString("");
                        if(!"".equals(myController.getData().getNextPage())){
                        	myNumberInfo.setText("Total books found: " + myController.getData().getTotalBooksNumber() + 
                        							" Books viewed: " + 18 * myActionIndex);
                        	myMorePanel.setVisible(true);
                        	if(prevPage.equals(myController.getData().getNextPage())){
                        		myMorePanel.setVisible(false);
                        	}
                        }
            		}
            	});
            	process.start();
        }
            
        };
        
        mySearchButton.addActionListener(act);
        myQueryField.addActionListener(setAdress);
        myQueryButton.addActionListener(setAdress);
        myMoreButton.addActionListener(act);
        myEraseButton.addActionListener(erase);
        myToolDelete.addActionListener(delete);

        myNetMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                	JDialog.setDefaultLookAndFeelDecorated(true);
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
