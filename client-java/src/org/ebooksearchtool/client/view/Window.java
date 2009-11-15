
package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

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
    
    private int myActionIndex;
    private int myPlusAction = 0;
    private String myAdress;

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
        myQueryPanel.setLayout(new BoxLayout(myQueryPanel,BoxLayout.Y_AXIS));
    	myQueryButtonPanel = new JPanel();
    	myQueryButtonPanel.setLayout(new BoxLayout(myQueryButtonPanel,BoxLayout.X_AXIS));
        myQueryPlusPanel = new JPanel();
        myQueryPlusPanel.setLayout(new BoxLayout(myQueryPlusPanel,BoxLayout.X_AXIS));
        myQueryPanel.add(myQueryButtonPanel);
        myQueryPanel.add(myQueryPlusPanel);
    	myCentralPanel.add(myQueryPanel, "North");

        myPlusButton = new JButton("+");
        mySearchLabel = new JLabel();
        myQueryPlusPanel.add(myPlusButton);
        myQueryPlusPanel.add(mySearchLabel);

        myToolBar = new JToolBar();
        myToolBar.add(new JButton(new ImageIcon(getClass().getResource("/ico/library_30.gif"))));
        myPanel1.add(myToolBar, "North");

    	myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryButtonPanel.add(myQueryField);
        String[] query = new String[] { "General", "Author", "Title" };
        myQueryCombo = new JComboBox(query);
        myQueryButtonPanel.add(myQueryCombo);
    	mySearchButton = new JButton();
    	mySearchButton.setText("SEARCH");
    	myQueryButtonPanel.add(mySearchButton);
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
        /*
        myPlusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
          */
        
        ActionListener act = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                
            	Thread process = new Thread(new Runnable() {
            		public void run() {
            			model.setValue(0);
            			myProgressBar.setString("Sending request... 0%");
            		
            			String queryWord = myQueryField.getText();
            			model.setValue(5);
            			String queryOption = (String)myQueryCombo.getSelectedItem();
            			model.setValue(8);
            			myProgressBar.setString("Recieving data... 5%");
            			int lastNumber = myController.getData().getBooks().size();
            			String prevPage = myController.getData().getNextPage();
            			try {
            				if(e.getSource() != myMoreButton){
            					myTextPan.removeAll();
            					if(!myController.getQueryAnswer(queryWord, queryOption)){
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
            			
            			BookPanel[] BP = new BookPanel[myController.getData().getBooks().size()];
            			model.setValue(35);
            			for(int i = lastNumber; i < myController.getData().getBooks().size(); ++i){
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
        myQueryField.addActionListener(act);
        myMoreButton.addActionListener(act);

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
