package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.ebooksearchtool.client.model.books.Book;
import org.ebooksearchtool.client.model.settings.Settings;
import org.ebooksearchtool.client.connection.Connector;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class BookPanel implements Comparable {

	private Book myBook;
	private Settings mySettings;
    private Controller myController;
	
	private DefaultBoundedRangeModel ourModel;
	
    private JPanel myRootPanel;
    private JLabel myImageLable;
    private int myImageWidth;
    private Box myInfoPanel;
    private JLabel myTitle;
    private JLabel mySource;
    private JLabel mySubtitle;
    private JLabel myAuthor;
    private JPanel myButtonPanel;
    private JButton myMoreButton, myDelButton, myLibButton, myDownloadPdfButton, myDownloadEpubButton;
    private JCheckBox myCheckBox;
    private JPanel myMoreInfoPanel;
    private JLabel myUpdateLabel;
    private JLabel myLangLabel;
    private JLabel myPublisherLabel;
    private JLabel myRightsLabel;
    private JLabel myDateLabel;
    private JLabel myGenreLabel;
    private JComponent mySummaryArea;
    
    private boolean myIsMoreInfoShown = false;

    class FileDownloader implements Runnable{

        String myName;
        String myLink;

        public FileDownloader(String name, String link){
            myName = name;
            myLink = link;
        }

        public void run(){
            File cover = new File(myName);
        	if(!cover.exists()){
                Connector connector = null;
                try {
                    connector = new Connector(myLink, mySettings);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                connector.getBookFromURL(myName, new DefaultBoundedRangeModel(0, 0, 0, 100));
        	}
        }

    }

    public BookPanel(Book book, Settings set, DefaultBoundedRangeModel model, Controller cont) {

        myBook = book;
        mySettings = set;
        ourModel = model;
        myController = cont;

        myRootPanel = new JPanel();
        drawRootPanel(80);
        
    }

    public int getImageWidth(){

        return myImageWidth;

    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }

    public void drawRootPanel(int imageWidth){

        myRootPanel = new JPanel();
        myRootPanel.setLayout(new BoxLayout(myRootPanel ,BoxLayout.X_AXIS));

        if(myBook.getImage() != null && !"".equals(myBook.getImage()) && !myBook.getImage().equals("None")){
            FileDownloader fd = new FileDownloader("images" + File.separatorChar + myBook.getTitle() + ".jpg", myBook.getImage());
            fd.run();
        }

        myImageLable = new JLabel();
        ImageIcon img = new ImageIcon("images" + File.separatorChar + myBook.getTitle() + ".jpg");

        if(img.getIconWidth() > imageWidth){
            myImageWidth = img.getIconWidth();
        }else{
            myImageWidth = imageWidth;
        }

        myImageLable.setIcon(img);
        myImageLable.setPreferredSize(new Dimension(myImageWidth, 100));
        myRootPanel.add(myImageLable);

        myTitle = new JLabel(myBook.getTitle());
        myTitle.setFont(new Font("Tahoma", 0, 24));
        mySource = new JLabel(myBook.getSource());
        mySource.setFont(new Font("Tahoma", 0, 16));
        mySource.setForeground(Color.green);
        if (myBook.getAuthor() != null) {
            myAuthor = new JLabel(myBook.getAuthor().getName());
        }
        if(myBook.getSubtitle() != null){
            mySubtitle = new JLabel(myBook.getSubtitle());
            myTitle.setFont(new Font("Tahoma", 0, 16));
        }

        myButtonPanel = new JPanel();
        myButtonPanel.setLayout(new FlowLayout());
        myMoreButton = new JButton(new ImageIcon(getClass().getResource("/ico/info.png")));
        myMoreButton.setToolTipText("Information about book");
        myMoreButton.setPreferredSize(new Dimension(30, 30));


        myButtonPanel.add(myMoreButton);
        myDelButton = new JButton(new ImageIcon(getClass().getResource("/ico/delete.png")));
        myDelButton.setToolTipText("Delete book from list");
        myDelButton.setPreferredSize(new Dimension(30, 30));
        myButtonPanel.add(myDelButton);
        myLibButton = new JButton(new ImageIcon(getClass().getResource("/ico/library.png")));
        myLibButton.setToolTipText("Add to library");
        myLibButton.setPreferredSize(new Dimension(30, 30));
        myButtonPanel.add(myLibButton);
        myDownloadEpubButton = new JButton(new ImageIcon(getClass().getResource("/ico/epub_30.gif")));
        myDownloadEpubButton.setToolTipText("Download ePub book");
        myDownloadEpubButton.setPreferredSize(new Dimension(30, 30));
        if(myBook.getLinks().get("epub") == null){
        	myDownloadEpubButton.setEnabled(false);
        }
        myButtonPanel.add(myDownloadEpubButton);
        myDownloadPdfButton = new JButton(new ImageIcon(getClass().getResource("/ico/pdf_30.gif")));
        myDownloadPdfButton.setToolTipText("Download pdf book");
        myDownloadPdfButton.setPreferredSize(new Dimension(30, 30));
        if(myBook.getLinks().get("pdf") == null){
        	myDownloadPdfButton.setEnabled(false);
        }
        myButtonPanel.add(myDownloadPdfButton);

        myInfoPanel = new Box(BoxLayout.Y_AXIS);
        myInfoPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        /*BoxLayout box = new BoxLayout(myInfoPanel, BoxLayout.Y_AXIS);

        myInfoPanel.setLayout(box);*/
        myInfoPanel.add(myTitle);
        myTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        myInfoPanel.add(mySource);
        mySource.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        if (myAuthor != null) {
            myInfoPanel.add(myAuthor);
            myAuthor.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        }
            if (myBook.getSubtitle() != null) {
            myInfoPanel.add(mySubtitle);
            mySubtitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        }

        myMoreInfoPanel = new JPanel();
        myInfoPanel.add(myMoreInfoPanel);


        myInfoPanel.add(myButtonPanel);
        myButtonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        myInfoPanel.setMinimumSize(new Dimension(350, 100));

        myRootPanel.add(myInfoPanel);


        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        myCheckBox = new JCheckBox();
        //checkPanel.setPreferredSize(new Dimension(100, 100));
        //myCheckBox.setPreferredSize(new Dimension(100, 100));

        checkPanel.add(myCheckBox);
        myCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
        myCheckBox.setAlignmentY(JCheckBox.CENTER_ALIGNMENT);
        myRootPanel.add(myCheckBox);

        myRootPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        myMoreButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(!myIsMoreInfoShown){
    				myMoreInfoPanel.setLayout(new BoxLayout(myMoreInfoPanel, BoxLayout.Y_AXIS));

                    if(myBook.getUpdateTime() != null){
                        myUpdateLabel = new JLabel("Updated: " + myBook.getUpdateTime());
    				    myUpdateLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				    myMoreInfoPanel.add(myUpdateLabel);
                    }

                    if(myBook.getLanguage() != null){
    				    myLangLabel = new JLabel("Language: " + myBook.getLanguage());
    				    myLangLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				    myMoreInfoPanel.add(myLangLabel);
                    }

                    if(myBook.getDate() != null){
    				    myDateLabel = new JLabel("Date of publishing: " + myBook.getDate().getYear());
    				    myDateLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				    myMoreInfoPanel.add(myDateLabel);
                    }

                    if(myBook.getPublisher() != null){
                        myPublisherLabel = new JLabel("Publisher: " + myBook.getPublisher());
    				    myPublisherLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				    myMoreInfoPanel.add(myPublisherLabel);
                    }

                    if(myBook.getRights() != null){
                        myRightsLabel = new JLabel("Rights: " + myBook.getRights());
    				    myRightsLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				    myMoreInfoPanel.add(myRightsLabel);
                    }

                    if(myBook.getGenre() != null){
    				    myGenreLabel = new JLabel("Genre: ");
                        for(int i = 0; i < myBook.getGenre().size(); ++i){
                            if(i != myBook.getGenre().size() - 1){
                                myGenreLabel.setText(myGenreLabel.getText() + myBook.getGenre().get(i) + ", ");
                            }else{
                                myGenreLabel.setText(myGenreLabel.getText() + myBook.getGenre().get(i));
                            }
                        }
    				    myGenreLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				    myMoreInfoPanel.add(myGenreLabel);
                    }


                    if(myBook.getSummary() != null){
                        mySummaryArea = new JTextArea(myBook.getSummary());
    				    ((JTextArea)mySummaryArea).setLineWrap(true);
                    }else{
                        HTMLEditorKit ek = new HTMLEditorKit();
                        mySummaryArea = new JTextPane();
                        ((JTextPane)mySummaryArea).setEditorKit(ek);
                        ((JTextPane)mySummaryArea).setText(myBook.getContent());
                    }

    				mySummaryArea.setBackground(myMoreInfoPanel.getBackground());
    				//mySummaryArea.setLineWrap(true);
    				mySummaryArea.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(mySummaryArea);

    				myIsMoreInfoShown = true;

    				myMoreInfoPanel.setVisible(true);
    				myRootPanel.updateUI();
    			}else{
    				myMoreInfoPanel.setVisible(false);
                    myMoreInfoPanel.removeAll();
    				myIsMoreInfoShown = false;
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

                myController.addTask(new FileDownloader("books" + File.separatorChar + myBook.getTitle() + ".epub", myBook.getLinks().get("epub")));

            }
        });

        myDownloadPdfButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
                myController.addTask(new FileDownloader("books" + File.separatorChar + myBook.getTitle() + ".pdf", myBook.getLinks().get("pdf")));
    		}
        });

    }
    
    public boolean isSelected(){
    	return myCheckBox.isSelected(); 
    }

    public int compareTo(Object obj){

        BookPanel tmp = (BookPanel)obj;
        return this.myBook.getTitle().compareTo(tmp.myBook.getTitle());

    }

}
