
package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.ebooksearchtool.client.model.books.Book;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

public class Window implements Observer{
    private JFrame myFrame;
    private JPanel myPanel1 = new JPanel();
    private JPanel myQueryButtonPanel;
    private JPanel myQueryPanel;
    private JPanel myQueryPlusPanel;
    JTextField myGenTextField;
    JTextField myTitleTextField;
    JTextField myAuthorTextField;
    private JTextField myQueryField;
    private JButton mySearchButton;
    private JButton myQueryButton;
    private JButton myPlusButton;
    private JLabel mySearchLabel;
    private JMenuItem myNetMenu;
    private JPanel myTextPan;
 //   private JComboBox myQueryCombo;
    private JProgressBar myProgressBar;
    private JPanel myCentralPanel;
    private JButton myMoreButton;
    private JToolBar myToolBar;
    private JLabel myNumberInfo;
    private JPanel myMorePanel;
 //   private JButton myEraseButton;
    private JButton myExtQueryButton;
    private JToggleButton myToolLibrary;
    private JButton myToolDelete;
    private JButton myToolSort;
    private JButton myToolBack;
    private JButton myToolForward;
    private JButton myToolUp;
    private JButton myToolDown;
    JButton myToolStop;

    ItemListener myLibraryLis;
    ActionListener myStopLis, mySortLis, myDeleteLis, myUpLis, myBackLis, myDownLis, myForwardLis, myActLis;

    final DefaultBoundedRangeModel myModel = new DefaultBoundedRangeModel(0, 0, 0, 100);

    private ArrayList<ArrayList<BookPanel>> myBookPanels = new ArrayList<ArrayList<BookPanel>>();

    private int myBackIndex = 0;
    private int curModelNumber;
    boolean myIsNewModel = false;

    private Controller myController;
    private int myImageWidth;
    long myTime;
    boolean myIsModelSaved;

    public JFrame getFrame() {
        return myFrame;
    }

    public Window() throws SAXException, ParserConfigurationException, IOException {

        myController = new Controller();
        myController.getData().addObserver(this);
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
        myToolLibrary = new JToggleButton(new ImageIcon(getClass().getResource("/ico/library.png")));
        myToolLibrary.setToolTipText("LibraryPanel");
        myToolBar.add(myToolLibrary);
        myToolStop = new JButton(new ImageIcon(getClass().getResource("/ico/stop_30.gif")));
        myToolStop.setToolTipText("Stop process");
        myToolBar.add(myToolStop);
        myToolStop.setEnabled(false);
        myPanel1.add(myToolBar, "North");

        myLibraryLis = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {

                if (myToolLibrary.isSelected()) {
                    System.out.println("library");
                    myPanel1.remove(myCentralPanel);
                    LibraryPanel lib = new LibraryPanel(myController, myModel);
                    myCentralPanel = lib.getRootPanel();
                    myPanel1.add(myCentralPanel, "Center");
                } else {
                    System.out.println("search");
                    myPanel1.remove(myCentralPanel);
                    showSearch();
                }
                myFrame.setVisible(true);

            }
        };

        myStopLis = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                myController.stopProcesses();
                myToolStop.setEnabled(false);

            }
        };

        mySortLis = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(myBookPanels != null){
                	ArrayList<BookPanel> newView;
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

        myDeleteLis = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(myBookPanels != null){
            		ArrayList<BookPanel> newView;
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

        myBackLis = new ActionListener() {

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

        myUpLis = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            	myTextPan.removeAll();

            	myBookPanels = new ArrayList<ArrayList<BookPanel>>();
                myBookPanels.add(new ArrayList<BookPanel>());
               	myController.clearModel();
                myController.getData().addObserver(Window.this);

               	--curModelNumber;
				myImageWidth = 0;
                myController.loadModel(curModelNumber);

				/*myMorePanel.setVisible(false);
           		if(curModelNumber == 0){
           			myToolUp.setEnabled(false);
           		}*/
           		if(curModelNumber < myController.getRequestCount()-1){
           			myToolDown.setEnabled(true);
           		}
            }
        };

        myForwardLis = new ActionListener() {

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

        myDownLis = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            	myTextPan.removeAll();

            	myBookPanels = new ArrayList<ArrayList<BookPanel>>();
                myBookPanels.add(new ArrayList<BookPanel>());
               	myController.clearModel();
                myController.getData().addObserver(Window.this);

               	++curModelNumber;
                myImageWidth = 0;
				myController.loadModel(curModelNumber);

				if(curModelNumber == myController.getRequestCount()-1){
           			myToolDown.setEnabled(false);
           		}
            }
        };

        ActionListener extSearch = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

            	if(myQueryPlusPanel.isVisible()){
            		myQueryPlusPanel.setVisible(false);
                    myQueryField.setEnabled(true);
            		//myQueryCombo.setSelectedIndex(0);
            	}else{
            		myQueryPlusPanel.setVisible(true);
                    myQueryField.setEnabled(false);
            	}

            }
        };


        myActLis = new ActionListener() {
          public void actionPerformed(final ActionEvent e) {
            Callable<Boolean> process = new Callable<Boolean>() {
              public Boolean call() {
                //myMorePanel.setVisible(false);
                //myController.stopProcesses();

                myToolStop.setEnabled(true);
                System.out.println("act");
                myModel.setValue(0);
                myProgressBar.setString("Sending request... 0%");
                myModel.setValue(5);
                System.out.println("act");
                myModel.setValue(8);
                myProgressBar.setString("Receiving data... 5%");
                ++curModelNumber;
                myIsNewModel = true;
                myTextPan.removeAll();
                myBookPanels = new ArrayList<ArrayList<BookPanel>>();
                myBookPanels.add(new ArrayList<BookPanel>());
                if (myController.getData().getBooks().size() != 0) {
                  myController.clearModel();
                }
                myController.getData().addObserver(Window.this);
                myImageWidth = 0;
                String[] terms = new String[3];
                if (myQueryField.isEnabled()) {
                  terms[0] = myQueryField.getText();
                  terms[1] = "";
                  terms[2] = "";
                } else {
                  terms[0] = myGenTextField.getText();
                  terms[1] = myTitleTextField.getText();
                  terms[2] = myAuthorTextField.getText();
                }
                myTime = System.currentTimeMillis();
                myController.getQueryAnswer(terms);
                myModel.setValue(100);
                myProgressBar.setString("Complete");
                myIsNewModel = false;
                /*		myQueryCombo.setSelectedIndex(0);
                              myEraseButton.setEnabled(false);    */
                myToolDelete.setEnabled(true);
                myToolSort.setEnabled(true);
                myToolStop.setEnabled(false);
                //	myQueryPlusPanel.setVisible(false);
                curModelNumber = myController.getRequestCount() - 1;
                if(myController.getRequestCount() > 1){
                  myToolUp.setEnabled(true);
                }

                return true;
              }
            };

            myController.addTask(process);
          }
        };

        myToolDelete.addActionListener(myDeleteLis);
        myToolSort.addActionListener(mySortLis);
        myToolBack.addActionListener(myBackLis);
        myToolForward.addActionListener(myForwardLis);
        myToolUp.addActionListener(myUpLis);
        myToolDown.addActionListener(myDownLis);
        myToolLibrary.addItemListener(myLibraryLis);
        myToolStop.addActionListener(myStopLis);


        showSearch();

    }

    public void showSearch(){

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




        myQueryField = new JTextField();
    	myQueryField.setSize(40, 10);
    	myQueryButtonPanel.add(myQueryField);
    /*    String[] query = new String[] { "General", "Author", "Title" };
        myQueryCombo = new JComboBox(query);
        myQueryPlusPanel.add(myQueryCombo);
      */

        mySearchButton = new JButton(new ImageIcon(getClass().getResource("/ico/search.png")));
    	mySearchButton.setEnabled(true);
    	mySearchButton.setToolTipText("Search");
    	myQueryButtonPanel.add(mySearchButton);

        myExtQueryButton = new JButton(new ImageIcon(getClass().getResource("/ico/ext_search.png")));
        myExtQueryButton.setToolTipText("Extended search");
        myQueryButtonPanel.add(myExtQueryButton);
        setExtSearchEnabled();


        myQueryPlusPanel.setLayout(new BoxLayout(myQueryPlusPanel, BoxLayout.Y_AXIS));

        JLabel genLable = new JLabel("General");
        JLabel titleLable = new JLabel("Title");
        JLabel authorLable = new JLabel("Author");

        Component[] comps = {genLable, titleLable, authorLable};
        int labelWidth = 0;
        Dimension dim = new Dimension();

        for (int i = 0; i < comps.length; ++i) {
            if (labelWidth < comps[i].getPreferredSize().width) {
                labelWidth = comps[i].getPreferredSize().width;
                dim = comps[i].getPreferredSize();
            }
        }

        for (int i = 0; i < comps.length; ++i) {
            comps[i].setPreferredSize(dim);
            comps[i].setMaximumSize(dim);
            comps[i].setMinimumSize(dim);
        }

        JPanel gen = new JPanel();
        gen.setLayout(new BoxLayout(gen, BoxLayout.X_AXIS));
        gen.add(Box.createHorizontalStrut(12));
        gen.add(genLable);
        gen.add(Box.createHorizontalStrut(8));
        myGenTextField = new JTextField();
        gen.add(myGenTextField);
        gen.add(Box.createHorizontalStrut(12));

        JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));
        title.add(Box.createHorizontalStrut(12));
        title.add(titleLable);
        title.add(Box.createHorizontalStrut(8));
        myTitleTextField = new JTextField();
        title.add(myTitleTextField);
        title.add(Box.createHorizontalStrut(12));

        JPanel author = new JPanel();
        author.setLayout(new BoxLayout(author, BoxLayout.X_AXIS));
        author.add(Box.createHorizontalStrut(12));
        author.add(authorLable);
        author.add(Box.createHorizontalStrut(8));
        myAuthorTextField = new JTextField();
        author.add(myAuthorTextField);
        author.add(Box.createHorizontalStrut(12));

        myQueryPlusPanel.add(Box.createVerticalStrut(8));
        myQueryPlusPanel.add(gen);
        myQueryPlusPanel.add(Box.createVerticalStrut(8));
        myQueryPlusPanel.add(title);
        myQueryPlusPanel.add(Box.createVerticalStrut(8));
        myQueryPlusPanel.add(author);
        myQueryPlusPanel.add(Box.createVerticalStrut(8));

        myQueryPlusPanel.setVisible(false);

    	myTextPan = new JPanel();
    	BoxLayout box = new BoxLayout(myTextPan, BoxLayout.Y_AXIS);
    	myTextPan.setLayout(box);
        myCentralPanel.add(new JScrollPane(myTextPan), "Center");

        myProgressBar = new JProgressBar(myModel);
        myProgressBar.setStringPainted(true);
        myPanel1.add(myProgressBar, "South");

        mySearchButton.addActionListener(myActLis);
        myQueryField.addActionListener(myActLis);
        myGenTextField.addActionListener(myActLis);
        myTitleTextField.addActionListener(myActLis);
        myAuthorTextField.addActionListener(myActLis);

        myNetMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                	JDialog.setDefaultLookAndFeelDecorated(true);
                    NetworkDialog dialogFrame = new NetworkDialog(myController);
                    setExtSearchEnabled();
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
        myFrame.setLocation(300,30);
        myFrame.setVisible(true);
    }



    public void setExtSearchEnabled(){

        myExtQueryButton.setEnabled(true);

    }

    public void appendBook(Book book, DefaultBoundedRangeModel model) {

        if (myIsNewModel) {
            if ((System.currentTimeMillis() - myTime) > 3000) {
                if (!myController.isModelSaved()) {
                    myController.saveModel();
                } else {
                    myController.reWriteModel();
                }
                myTime = System.currentTimeMillis();
            }
        }

        myBookPanels.get(myBookPanels.size() - 1).add(new BookPanel(book, myController.getSettings(), model, myController));
        if (myImageWidth < myBookPanels.get(myBookPanels.size() - 1).get(myBookPanels.get(myBookPanels.size() - 1).size() - 1).getImageWidth()) {
            myImageWidth = myBookPanels.get(myBookPanels.size() - 1).get(myBookPanels.get(myBookPanels.size() - 1).size() - 1).getImageWidth();

            for (int j = 0; j < myBookPanels.get(myBookPanels.size() - 1).size(); ++j) {
                myBookPanels.get(myBookPanels.size() - 1).get(j).drawRootPanel(myImageWidth);
                myTextPan.add(myBookPanels.get(myBookPanels.size() - 1).get(j).getRootPanel());
                myFrame.setVisible(true);

            }
        } else {
            JPanel bookPan = myBookPanels.get(myBookPanels.size() - 1).get(myBookPanels.get(myBookPanels.size() - 1).size() - 1).getRootPanel();
            myTextPan.add(bookPan);
            bookPan.setVisible(true);
            myFrame.setVisible(true);
        }
        model.setValue(model.getValue() + 1);
    }

    public void update(Observable o, Object arg) {

        appendBook((Book)arg, myModel);

    }
}
