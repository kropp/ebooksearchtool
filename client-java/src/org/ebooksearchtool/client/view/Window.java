
package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.ebooksearchtool.client.logic.query.Query;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    private JButton myExtQueryButton;
    private JButton myToolLibrary;
    private JButton myToolDelete;
    private JButton myToolSort;
    private JButton myToolBack;
    private JButton myToolForward;
    private JButton myToolUp;
    private JButton myToolDown;
    
    private ArrayList<ArrayList<BookPanel>> myBookPanels = new ArrayList<ArrayList<BookPanel>>();
    
    private Query myQuery;
    
    private int myActionIndex;
    private int myBackIndex = 0;
    private String myAdress;
    private int curModelNumber;

    private Controller myController;

    public JFrame getFrame() {
        return myFrame;
    }

    public Window() throws SAXException, ParserConfigurationException, IOException {

        myController = new Controller();
        curModelNumber = myController.getRequestCount();
        
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
        myToolUp = new JButton(new ImageIcon(getClass().getResource("/ico/up.png")));
        myToolUp.setToolTipText("Previous request");
        myToolBar.add(myToolUp);
        if(myController.getRequestCount() > 1){
			myToolUp.setEnabled(true);
		}else{
			myToolUp.setEnabled(false);
		}
        myToolBack = new JButton(new ImageIcon(getClass().getResource("/ico/back.png")));
        myToolBack.setToolTipText("Back");
        myToolBar.add(myToolBack);
        myToolBack.setEnabled(false);
        myToolForward = new JButton(new ImageIcon(getClass().getResource("/ico/next.png")));
        myToolForward.setToolTipText("Forward");
        myToolBar.add(myToolForward);
        myToolForward.setEnabled(false);
        myToolDown = new JButton(new ImageIcon(getClass().getResource("/ico/up.png")));
        myToolDown.setToolTipText("Next request");
        myToolBar.add(myToolDown);
        myToolDown.setEnabled(false);
        myToolDelete = new JButton(new ImageIcon(getClass().getResource("/ico/delete.png")));
        myToolDelete.setToolTipText("Delete selected books from list");
        myToolBar.add(myToolDelete);
        myToolDelete.setEnabled(false);
        myToolSort = new JButton(new ImageIcon(getClass().getResource("/ico/sort_30.gif")));
        myToolSort.setToolTipText("Sort books by title");
        myToolBar.add(myToolSort);
        myToolSort.setEnabled(false);
        myToolLibrary = new JButton(new ImageIcon(getClass().getResource("/ico/library.png")));
        myToolLibrary.setToolTipText("Library");
        myToolBar.add(myToolLibrary);
        myPanel1.add(myToolBar, "North");

    	myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryButtonPanel.add(myQueryField);
        String[] query = new String[] { "General", "Author", "Title" };
        myQueryCombo = new JComboBox(query);
        myQueryPlusPanel.add(myQueryCombo);

    	
        mySearchButton = new JButton(new ImageIcon(getClass().getResource("/ico/search.png")));
    	mySearchButton.setEnabled(true);
    	mySearchButton.setToolTipText("Search");
    	myQueryButtonPanel.add(mySearchButton);
        
        myExtQueryButton = new JButton(new ImageIcon(getClass().getResource("/ico/ext_search.png")));
        myExtQueryButton.setToolTipText("Extended search");
        myQueryButtonPanel.add(myExtQueryButton);
    	
    	
    	myEraseButton = new JButton("ERASE");
    	myEraseButton.setEnabled(false);
    	
        myQueryButton = new JButton("ADD");
        myQueryButton.setToolTipText("Add a word to complex request");
    	myQueryPlusPanel.add(Box.createHorizontalStrut(7));
    	myQueryPlusPanel.add(myQueryButton);
    	myQueryButton.setPreferredSize(myEraseButton.getPreferredSize());
    	myQueryButton.setMaximumSize(myEraseButton.getPreferredSize());
    	myQueryButton.setMinimumSize(myEraseButton.getPreferredSize());
    	myQueryPlusPanel.add(Box.createHorizontalStrut(5));
    	myQueryPlusPanel.add(myEraseButton);
    	myQueryPlusPanel.add(Box.createHorizontalStrut(5));
    	
    	myQueryPlusPanel.add(Box.createHorizontalStrut(15));
    	mySearchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    	mySearchLabel = new JLabel();
        myQueryPlusPanel.add(mySearchLabel);
        myQueryPlusPanel.setVisible(false);
    	
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

        ActionListener sort = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(myBookPanels != null){
                	ArrayList<BookPanel> newView = new ArrayList<BookPanel>();
                	newView = (ArrayList<BookPanel>)(myBookPanels.get(myBackIndex).clone());
                	for(int i = myBackIndex + 1; i < myBookPanels.size(); ++i){
                		myBookPanels.remove(i);
                	}
                	
                	myBookPanels.add(newView);
            		Collections.sort(myBookPanels.get(myBookPanels.size()-1));
            	}

                myTextPan.removeAll();

                for(int i = 0; i < myBookPanels.get(myBookPanels.size()-1).size(); ++i){
                    JPanel bookPan = myBookPanels.get(myBookPanels.size()-1).get(i).getRootPanel();
            		myTextPan.add(bookPan);
            		bookPan.setVisible(true);
            		myFrame.setVisible(true);
                }
                myToolBack.setEnabled(true);
                myToolForward.setEnabled(false);
                ++myBackIndex;

            }
        };
        
        ActionListener delete = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(myBookPanels != null){
            		ArrayList<BookPanel> newView = new ArrayList<BookPanel>();
            		newView = (ArrayList)(myBookPanels.get(myBackIndex).clone());
            		for(int i = myBackIndex + 1; i < myBookPanels.size(); ++i){
                		myBookPanels.remove(i);
                	}
            		myBookPanels.add(newView);
         
            		myTextPan.removeAll();
            		
            		for(int i = 0; i < myBookPanels.get(myBookPanels.size()-1).size(); ++i){
            			if(myBookPanels.get(myBookPanels.size()-1).get(i).isSelected()){
            				myBookPanels.get(myBookPanels.size()-1).remove(i);
            				--i;
            			}
            		}
            		for(int i = 0; i < myBookPanels.get(myBookPanels.size()-1).size(); ++i){
                        JPanel bookPan = myBookPanels.get(myBookPanels.size()-1).get(i).getRootPanel();
                		myTextPan.add(bookPan);
                		bookPan.setVisible(true);
                		myFrame.setVisible(true);
                    }
            	}
            	myToolBack.setEnabled(true);
            	myToolForward.setEnabled(false);
            	++myBackIndex;
            }
        };
        
        ActionListener back = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
            	myTextPan.removeAll();
            	
           		for(int i = 0; i < myBookPanels.get(myBackIndex-1).size(); ++i){
           			JPanel bookPan = myBookPanels.get(myBackIndex-1).get(i).getRootPanel();
           			myTextPan.add(bookPan);
            		bookPan.setVisible(true);
            		myFrame.setVisible(true);
           		}
           		myTextPan.updateUI();
           		--myBackIndex;
           		if(myBackIndex == 0){
           			myToolBack.setEnabled(false);
           		}
           		myToolForward.setEnabled(true);
            }
        };
        
        ActionListener up = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
            	myTextPan.removeAll();
            	
            	myBookPanels = new ArrayList<ArrayList<BookPanel>>();
                myBookPanels.add(new ArrayList<BookPanel>());
               	myController.clearModel();
                
               	--curModelNumber;
				myController.loadModel(curModelNumber);					
                
				for(int i = 0; i < myController.getAnswer().getData().getBooks().size(); ++i){
    				
					try {
						myBookPanels.get(myBookPanels.size()-1).add(new BookPanel(myController.getAnswer().getData().getBooks().get(i), myController.getSettings(), model));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    				
    				JPanel bookPan = myBookPanels.get(myBookPanels.size()-1).get(i).getRootPanel();
    				myTextPan.add(bookPan);
    				bookPan.setVisible(true);
    				myFrame.setVisible(true);
    			}
				myMorePanel.setVisible(false);
				myActionIndex = 1;
           		if(curModelNumber == 0){
           			myToolUp.setEnabled(false);
           		}
           		if(curModelNumber < myController.getRequestCount()-1){
           			myToolDown.setEnabled(true);
           		}
            }
        };
        
        ActionListener forward = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
            	myTextPan.removeAll();
            	
           		for(int i = 0; i < myBookPanels.get(myBackIndex+1).size(); ++i){
           			JPanel bookPan = myBookPanels.get(myBackIndex+1).get(i).getRootPanel();
           			myTextPan.add(bookPan);
            		bookPan.setVisible(true);
            		myFrame.setVisible(true);
           		}
           		myTextPan.updateUI();
           		++myBackIndex;
           		if(myBackIndex == myBookPanels.size() - 1){
           			myToolForward.setEnabled(false);
           		}
           		myToolBack.setEnabled(true);
            }
        };
        
        ActionListener down = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
            	myTextPan.removeAll();
            	
            	myBookPanels = new ArrayList<ArrayList<BookPanel>>();
                myBookPanels.add(new ArrayList<BookPanel>());
               	myController.clearModel();
                
               	++curModelNumber;
				myController.loadModel(curModelNumber);					
                
				for(int i = 0; i < myController.getAnswer().getData().getBooks().size(); ++i){
    				
					try {
						myBookPanels.get(myBookPanels.size()-1).add(new BookPanel(myController.getAnswer().getData().getBooks().get(i), myController.getSettings(), model));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    				
    				JPanel bookPan = myBookPanels.get(myBookPanels.size()-1).get(i).getRootPanel();
    				myTextPan.add(bookPan);
    				bookPan.setVisible(true);
    				myFrame.setVisible(true);
    			}
				myMorePanel.setVisible(false);
				myActionIndex = 1;
				if(curModelNumber > 0){
					myToolUp.setEnabled(true);
				}
				if(curModelNumber == myController.getRequestCount()-1){
           			myToolDown.setEnabled(false);
           		}
            }
        };
        
        ActionListener setAdress = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
				myQuery = new Query(myController.getSettings());
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
        
        ActionListener extSearch = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
            	
            	if(myQueryPlusPanel.isVisible()){
            		myQueryPlusPanel.setVisible(false);
            		myQueryCombo.setSelectedIndex(0);
            	}else{
            		myQueryPlusPanel.setVisible(true);
            	}
            	
            }
        };
          
        
        ActionListener act = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                
            	Thread process = new Thread(new Runnable() {
            		public void run() {

                        myMorePanel.setVisible(false);

            			model.setValue(0);
            			myProgressBar.setString("Sending request... 0%");
            			model.setValue(5);
            			
            			model.setValue(8);
            			myProgressBar.setString("Receiving data... 5%");
            			int lastNumber = myController.getAnswer().getData().getBooks().size();
            			String prevPage = myController.getAnswer().getNextPage();
            			try {
            				if(e.getSource() != myMoreButton){
            					if(myAdress == null){
            						myQuery = new Query(myController.getSettings());
            						String queryWord = myQueryField.getText();
            						String queryOption = (String)myQueryCombo.getSelectedItem();
            						try {
            							myAdress = myQuery.getQueryAdress(queryWord, queryOption);
            						} catch (IOException e1) {

            							e1.printStackTrace();
            						}
            					}
            					++curModelNumber;
            					myTextPan.removeAll();
                                myBookPanels = new ArrayList<ArrayList<BookPanel>>();
                                myBookPanels.add(new ArrayList<BookPanel>());
                                if(myController.getAnswer().getData().getBooks().size() != 0){
                                	myController.clearModel();
                                }
                                lastNumber = 0;
            					if(!myController.getQueryAnswer(myAdress)){
            						
                                    model.setValue(100);
                                    JOptionPane.showMessageDialog(new JDialog(), "Connection failed", "error", JOptionPane.ERROR_MESSAGE);
                                    myProgressBar.setString("");
                                    return;
                                }
            					if(myController.getAnswer().getData().getBooks().size() != 0){
                                	myController.saveModel();
                                }
            					myActionIndex = 1;
            				}else{
            					myController.getNextData();
            					myController.extendModel();
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
            			
            			model.setValue(35);
            			for(int i = lastNumber; i < myController.getAnswer().getData().getBooks().size(); ++i){
            				try {
								myBookPanels.get(myBookPanels.size()-1).add(new BookPanel(myController.getAnswer().getData().getBooks().get(i), myController.getSettings(), model));
								model.setValue(model.getValue() + 5);
								myProgressBar.setString("Viewing book... " + model.getValue() + "%");
            				} catch (IOException e) {
								e.printStackTrace();
							}
            				JPanel bookPan = myBookPanels.get(myBookPanels.size()-1).get(i).getRootPanel();
            				myTextPan.add(bookPan);
            				bookPan.setVisible(true);
            				myFrame.setVisible(true);
            			}
            			model.setValue(100);
            			myAdress = null;
            			myQueryCombo.setSelectedIndex(0);
            			myEraseButton.setEnabled(false);
            			myToolDelete.setEnabled(true);
            			myToolSort.setEnabled(true);
            			myQueryPlusPanel.setVisible(false);
            			curModelNumber = myController.getRequestCount() - 1;
            			if(myController.getRequestCount() > 1){
            				myToolUp.setEnabled(true);
            			}
            			myAdress = null;
                        myProgressBar.setString("");
                        if(!"".equals(myController.getAnswer().getNextPage())){
                            String totalBooks;
                            if(myController.getAnswer().getTotalBooksNumber() == 0){
                                totalBooks = "more than " + myController.getAnswer().getData().getBooks().size();
                            }else{
                                totalBooks = "" + myController.getAnswer().getTotalBooksNumber();
                            }
                        	myNumberInfo.setText("Total books found: " + totalBooks +
                        							" Books viewed: " + myController.getAnswer().getData().getBooks().size());
                        	myMorePanel.setVisible(true);
                        	if(prevPage.equals(myController.getAnswer().getNextPage())){
                        		myMorePanel.setVisible(false);
                        	}
                        }
            		}
            	});
            	process.start();
        }
            
        };
        
        
        
        mySearchButton.addActionListener(act);
        myQueryField.addActionListener(act);
        myQueryButton.addActionListener(setAdress);
        myMoreButton.addActionListener(act);
        myEraseButton.addActionListener(erase);
        myToolDelete.addActionListener(delete);
        myToolSort.addActionListener(sort);
        myToolBack.addActionListener(back);
        myToolForward.addActionListener(forward);
        myToolUp.addActionListener(up);
        myToolDown.addActionListener(down);
        myExtQueryButton.addActionListener(extSearch);

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
