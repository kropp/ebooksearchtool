package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.ebooksearchtool.client.model.books.Book;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.util.ArrayList;

/**
 * Date: 18.04.2010
 * Time: 9:39:56
 * To change this template use File | Settings | File Templates.
 */
public class LibraryPanel {

    Controller myController;
    JPanel myRootPanel;
    JPanel myTextPan;
    ArrayList<LibBookPanel> myBookPanels = new ArrayList<LibBookPanel>();
    DefaultBoundedRangeModel myModel;
    private int myImageWidth;

    public LibraryPanel(Controller cont, DefaultBoundedRangeModel model){
        myController = cont;
        myRootPanel = new JPanel();
        myModel = model;
        drawLibrary();
    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }

    public void drawLibrary(){

        myRootPanel.setLayout(new BorderLayout());
        myTextPan = new JPanel();
    	BoxLayout box = new BoxLayout(myTextPan, BoxLayout.Y_AXIS);
    	myTextPan.setLayout(box);
        JScrollPane sp = new JScrollPane(myTextPan);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        myRootPanel.add(sp, "Center");

        for(int i = 0; i < myController.getLibrary().getBooks().size(); ++i){
            appendBook(myController.getLibrary().getBooks().get(i));
        }

    }

    public void appendBook(Book book) {

        myBookPanels.add(new LibBookPanel(book, myController.getSettings(), myModel, myController));
        if (myImageWidth < myBookPanels.get(myBookPanels.size() - 1).getImageWidth()) {
            myImageWidth = myBookPanels.get(myBookPanels.size() - 1).getImageWidth();

            for (int j = 0; j < myBookPanels.size(); ++j) {
                myBookPanels.get(j).drawRootPanel(myImageWidth);
                myTextPan.add(myBookPanels.get(j).getRootPanel());
                myRootPanel.setVisible(true);

            }
        } else {
            JPanel bookPan = myBookPanels.get(myBookPanels.size() - 1).getRootPanel();
            myTextPan.add(bookPan);
            bookPan.setVisible(true);
            myRootPanel.setVisible(true);
        }
    }

}
