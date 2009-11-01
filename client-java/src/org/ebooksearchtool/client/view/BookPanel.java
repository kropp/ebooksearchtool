package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.model.Book;
import org.ebooksearchtool.client.connection.Connector;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 31.10.2009
 * Time: 12:23:09
 * To change this template use File | Settings | File Templates.
 */
public class BookPanel {

    private JPanel myRootPanel;
    private JLabel myImageLable;
    private Book myBook;
    private JPanel myInfoPanel;
    private JLabel myTitle;
    private JLabel myAuthor;
    private JPanel myButtonPanel;
    private JButton myMoreButton, myDelButton, myLibButton, myDownloadPdfButton, myDownloadEpubButton;
    private JCheckBox myCheckBox;

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

        myInfoPanel = new JPanel();
        BoxLayout box = new BoxLayout(myInfoPanel, BoxLayout.Y_AXIS);
        
        myInfoPanel.setLayout(box);
        myInfoPanel.add(myTitle);
        myTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        myInfoPanel.add(myAuthor);
        myAuthor.setAlignmentX(JLabel.CENTER_ALIGNMENT);


        myInfoPanel.add(myButtonPanel);

        myRootPanel.add(myInfoPanel);

        myCheckBox = new JCheckBox();
        myRootPanel.add(myCheckBox, "Center");
        myCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
        
    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }

}
