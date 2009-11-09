package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.model.Book;
import org.ebooksearchtool.client.model.Settings;
import org.ebooksearchtool.client.connection.Connector;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: 
 * Date: 31.10.2009
 * Time: 12:23:09
 * To change this template use File | Settings | File Templates.
 */
public class BookPanel {

	private Book myBook;
	private Settings mySettings;
	
    private JPanel myRootPanel;
    private JLabel myImageLable;
    private Box myInfoPanel;
    private JLabel myTitle;
    private JLabel myAuthor;
    private JPanel myButtonPanel;
    private JButton myMoreButton, myDelButton, myLibButton, myDownloadPdfButton, myDownloadEpubButton;
    private JCheckBox myCheckBox;
    private JPanel myMoreInfoPanel;
    private JLabel myLangLabel;
    private JLabel myDateLabel;
    private JLabel myGenreLabel;
    private JTextArea mySummaryArea;
    
    private boolean isMoreInfoShown = false;

    public BookPanel(Book book, Settings set) throws IOException {

        myBook = book;
        mySettings = set;

        myRootPanel = new JPanel();
        myRootPanel.setLayout(new BoxLayout(myRootPanel ,BoxLayout.X_AXIS));
        

        Connector connector = new Connector(myBook.getImage(), mySettings);
        connector.getBookFromURL("images/" + myBook.getTitle() + ".jpg");

        myImageLable = new JLabel();
        myImageLable.setIcon(new ImageIcon("images/" + myBook.getTitle() + ".jpg"));
        myImageLable.setPreferredSize(new Dimension(80, 100));
        myRootPanel.add(myImageLable);

        myTitle = new JLabel(myBook.getTitle());
        myTitle.setFont(new Font("Tahoma", 0, 24));
        myAuthor = new JLabel(myBook.getAuthor().getName());

        myButtonPanel = new JPanel();
        myButtonPanel.setLayout(new FlowLayout());
        myMoreButton = new JButton(new ImageIcon("../ico/info_30.gif"));
        myMoreButton.setPreferredSize(new Dimension(30, 30));
        
        myButtonPanel.add(myMoreButton);
        myDelButton = new JButton(new ImageIcon("../ico/delete_30.gif"));
        myDelButton.setPreferredSize(new Dimension(30, 30));
        myButtonPanel.add(myDelButton);
        myLibButton = new JButton(new ImageIcon("../ico/library_30.gif"));
        myLibButton.setPreferredSize(new Dimension(30, 30));
        myButtonPanel.add(myLibButton);
        myDownloadEpubButton = new JButton(new ImageIcon("../ico/epub_30.gif"));
        myDownloadEpubButton.setPreferredSize(new Dimension(30, 30));
        myButtonPanel.add(myDownloadEpubButton);
        myDownloadPdfButton = new JButton(new ImageIcon("../ico/pdf_30.gif"));
        myDownloadPdfButton.setPreferredSize(new Dimension(30, 30));
        myButtonPanel.add(myDownloadPdfButton);

        myInfoPanel = new Box(BoxLayout.Y_AXIS);
        myInfoPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        /*BoxLayout box = new BoxLayout(myInfoPanel, BoxLayout.Y_AXIS);
        
        myInfoPanel.setLayout(box);*/
        myInfoPanel.add(myTitle);
        myTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        myInfoPanel.add(myAuthor);
        myAuthor.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        myMoreInfoPanel = new JPanel();
        myInfoPanel.add(myMoreInfoPanel);


        myInfoPanel.add(myButtonPanel);
        myButtonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        myInfoPanel.setMinimumSize(new Dimension(350, 100));

        myRootPanel.add(myInfoPanel);
        
        
        myCheckBox = new JCheckBox();
        myCheckBox.setPreferredSize(new Dimension(100, 100));
        myRootPanel.add(myCheckBox);
        myCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);

        myRootPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        
        myMoreButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(!isMoreInfoShown){
                    int curWidth = myInfoPanel.getWidth();
    				myMoreInfoPanel.setLayout(new BoxLayout(myMoreInfoPanel, BoxLayout.Y_AXIS));
    			
    				myLangLabel = new JLabel("Language: " + myBook.getLanguage());
    				myLangLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myLangLabel);
    			
    				myDateLabel = new JLabel("Date of publishing: " + myBook.getDate());
    				myDateLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myDateLabel);
    			
    				myGenreLabel = new JLabel("Genre: " + myBook.getGenre());
    				myGenreLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myGenreLabel);
    			
    				mySummaryArea = new JTextArea(myBook.getSummary());
    				mySummaryArea.setLineWrap(true);
    				mySummaryArea.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(mySummaryArea);
    				
    				isMoreInfoShown = true;
    				myMoreInfoPanel.setVisible(true);

    				myRootPanel.updateUI();
    			}else{
    				myMoreInfoPanel.setVisible(false);
                    myMoreInfoPanel.removeAll();
    				isMoreInfoShown = false;
    				myRootPanel.updateUI();
    			}
    			
				
			}
        });
        
        myDelButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			myRootPanel.setVisible(false);
    		}
        });
        
        myLibButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			myLibButton.setEnabled(false);
    			myRootPanel.setVisible(true);
    		}
        });
        
        myDownloadEpubButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Connector connector;
				try {
					connector = new Connector(myBook.getEpubLink(), mySettings);
					connector.getBookFromURL("books" + myBook.getTitle() + ".epub");
				} catch (IOException e1) {
					e1.printStackTrace();
				}    	        
    		}
        });
        
        myDownloadPdfButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Connector connector;
				try {
					connector = new Connector(myBook.getPdfLink(), mySettings);
					connector.getBookFromURL("books" + myBook.getTitle() + ".pdf");
				} catch (IOException e1) {
					e1.printStackTrace();
				}    	        
    		}
        });
        
    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }

}
