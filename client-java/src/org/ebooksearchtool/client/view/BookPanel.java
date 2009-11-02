package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.model.Book;
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

    private JPanel myRootPanel;
    private JLabel myImageLable;
    private Book myBook;
    private Box myInfoPanel;
    private JLabel myTitle;
    private JLabel myAuthor;
    private JPanel myButtonPanel;
    private JButton myMoreButton, myDelButton, myLibButton, myDownloadPdfButton, myDownloadEpubButton;
    private JCheckBox myCheckBox;
    private JPanel myMoreInfoPanel;
    private JLabel myDateLabel;
    private JLabel myGenreLabel;
    private JTextArea mySummaryArea;

    public BookPanel(Book book, String IP, int port) throws IOException {

        myBook = book;

        myRootPanel = new JPanel();
        myRootPanel.setLayout(new BoxLayout(myRootPanel ,BoxLayout.X_AXIS));
        

        Connector connector = new Connector(myBook.getImage(), IP, port);
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
    			myMoreInfoPanel.setLayout(new BoxLayout(myMoreInfoPanel, BoxLayout.Y_AXIS));
    			
    			myDateLabel = new JLabel(myBook.getDate());
    			myMoreInfoPanel.add(myDateLabel);
    			
    			myGenreLabel = new JLabel(myBook.getGenre());
    			myMoreInfoPanel.add(myGenreLabel);
    			
    			mySummaryArea = new JTextArea(myBook.getSummary());
    			mySummaryArea.setLineWrap(true);
    			//mySummaryArea.setBounds(new Rectangle(myInfoPanel.getWidth(), 0));
    			myMoreInfoPanel.add(mySummaryArea);
    			
    			myMoreInfoPanel.setVisible(true);
				
			}
        });
        
    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }

}
