package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.model.Book;
import org.ebooksearchtool.client.model.Settings;
import org.ebooksearchtool.client.connection.Connector;

import javax.swing.*;
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

    public BookPanel(Book book, Settings set) throws IOException {

        myBook = book;
        mySettings = set;

        myRootPanel = new JPanel();
        myRootPanel.setLayout(new BoxLayout(myRootPanel ,BoxLayout.X_AXIS));
        

        Connector connector = new Connector(myBook.getImage(), mySettings);
        connector.getBookFromURL(myBook.getTitle() + ".jpg");

        myImageLable = new JLabel();
        myImageLable.setIcon(new ImageIcon(myBook.getTitle() + ".jpg"));
        myRootPanel.add(myImageLable);

        myTitle = new JLabel(myBook.getTitle());
        myTitle.setFont(new Font("Tahoma", 0, 24));
        myAuthor = new JLabel(myBook.getAuthor().getName());

        myButtonPanel = new JPanel();
        myButtonPanel.setLayout(new BoxLayout(myButtonPanel ,BoxLayout.X_AXIS));
        myMoreButton = new JButton("More");
        myButtonPanel.add(myMoreButton);
        myDelButton = new JButton("Delete");
        myButtonPanel.add(myDelButton);
        myLibButton = new JButton("To Library");
        myButtonPanel.add(myLibButton);
        myDownloadEpubButton = new JButton("Download epub");
        myButtonPanel.add(myDownloadEpubButton);
        myDownloadPdfButton = new JButton("Download pdf");
        myButtonPanel.add(myDownloadPdfButton);

        myInfoPanel = new Box(BoxLayout.Y_AXIS);
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

        myRootPanel.add(myInfoPanel);
        
        
        myCheckBox = new JCheckBox();
        myRootPanel.add(myCheckBox);
        myCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
        
        myMoreButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(myMoreButton.getText().equals("More")){
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
    				
    				myMoreButton.setText("Hide");
    				myMoreInfoPanel.setVisible(true);
    			
    				myRootPanel.updateUI();
    			}else{
    				myMoreInfoPanel.setVisible(false);
                    myMoreInfoPanel.removeAll();
    				myMoreButton.setText("More");
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
					connector.getBookFromURL(myBook.getTitle() + ".epub");
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
					connector.getBookFromURL(myBook.getTitle() + ".pdf");
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
