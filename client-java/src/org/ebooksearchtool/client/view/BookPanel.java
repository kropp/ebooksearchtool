package org.ebooksearchtool.client.view;

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

public class BookPanel implements Comparable {

	private Book myBook;
	private Settings mySettings;
	
	private DefaultBoundedRangeModel ourModel;
	
    private JPanel myRootPanel;
    private JLabel myImageLable;
    private Box myInfoPanel;
    private JLabel myTitle;
    private JLabel myAuthor;
    private JPanel myButtonPanel;
    private JButton myMoreButton, myDelButton, myLibButton, myDownloadPdfButton, myDownloadEpubButton;
    private JCheckBox myCheckBox;
    private JPanel myMoreInfoPanel;
    private JLabel myUpdateLabel;
    private JLabel myLangLabel;
    private JLabel myPublisherLabel;
    private JLabel myDateLabel;
    private JLabel myGenreLabel;
    private JTextPane mySummaryArea;
    
    private boolean myIsMoreInfoShown = false;

    public BookPanel(Book book, Settings set, DefaultBoundedRangeModel model) throws IOException {

        myBook = book;
        mySettings = set;
        ourModel = model;

        myRootPanel = new JPanel();
        myRootPanel.setLayout(new BoxLayout(myRootPanel ,BoxLayout.X_AXIS));
        
        if(myBook.getImage() != null && !"".equals(myBook.getImage()) && !myBook.getImage().equals("None")){
        	File cover = new File("images" + File.separatorChar + myBook.getTitle() + ".jpg");
        	if(!cover.exists()){
            	Connector connector = new Connector(myBook.getImage(), mySettings);
            	connector.getBookFromURL("images" + File.separatorChar + myBook.getTitle() + ".jpg", new DefaultBoundedRangeModel(0, 0, 0, 100));
        	}
        }

        myImageLable = new JLabel();
        ImageIcon img = new ImageIcon("images" + File.separatorChar + myBook.getTitle() + ".jpg");
         
        
        myImageLable.setIcon(img);
        myImageLable.setPreferredSize(new Dimension(80, 100));
        myRootPanel.add(myImageLable);

        myTitle = new JLabel(myBook.getTitle());
        myTitle.setFont(new Font("Tahoma", 0, 24));
        myAuthor = new JLabel(myBook.getAuthor().getName());

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
        myInfoPanel.add(myAuthor);
        myAuthor.setAlignmentX(JLabel.LEFT_ALIGNMENT);

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

                    myUpdateLabel = new JLabel("Updated: " + myBook.getUpdateTime());
    				myUpdateLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myUpdateLabel);

    				myLangLabel = new JLabel("Language: " + myBook.getLanguage());
    				myLangLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myLangLabel);
    			
    				myDateLabel = new JLabel("Date of publishing: " + myBook.getDate().getYear());
    				myDateLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myDateLabel);

                    myPublisherLabel = new JLabel("Publisher: " + myBook.getPublisher());
    				myPublisherLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myPublisherLabel);

    				myGenreLabel = new JLabel("Genre: " + myBook.getGenre());
    				myGenreLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    				myMoreInfoPanel.add(myGenreLabel);

                    HTMLEditorKit ek = new HTMLEditorKit();
                    mySummaryArea = new JTextPane();
                    mySummaryArea.setEditorKit(ek);
                    if(myBook.getSummary() != null){
    				    mySummaryArea.setText(myBook.getSummary());
                    }else{
                        mySummaryArea.setText(myBook.getContent());
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
    			
    			Thread process = new Thread(new Runnable() {
            		public void run() {
            			Connector connector;
            			try {
            				connector = new Connector(myBook.getLinks().get("epub"), mySettings);
            				connector.getBookFromURL("books" + File.separatorChar + myBook.getTitle() + ".epub", ourModel);
            			} catch (IOException e1) {
            				e1.printStackTrace();
            			}
            		}
    			});
    			process.start();
    		}
        });
        
        myDownloadPdfButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Thread process = new Thread(new Runnable() {
            		public void run() {
            			Connector connector;
            			try {
            				connector = new Connector(myBook.getLinks().get("pdf"), mySettings);
            				connector.getBookFromURL("books" + File.separatorChar + myBook.getTitle() + ".pdf", ourModel);
            			} catch (IOException e1) {
            				e1.printStackTrace();
            			}
            		}
    			});
    			process.start();  	        
    		}
        });
        
    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }
    
    public boolean isSelected(){
    	return myCheckBox.isSelected(); 
    }

    public int compareTo(Object obj){

        BookPanel tmp = (BookPanel)obj;
        return this.myBook.getTitle().compareTo(tmp.myBook.getTitle());

    }

}
