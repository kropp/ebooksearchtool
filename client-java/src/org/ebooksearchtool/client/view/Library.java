package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;

import javax.swing.*;

/**
 * Date: 18.04.2010
 * Time: 9:39:56
 * To change this template use File | Settings | File Templates.
 */
public class Library {

    Controller myController;
    JPanel myRootPanel;

    public Library(Controller cont){
        myController = cont;
        myRootPanel = new JPanel();
        drawLibrary();
    }

    public JPanel getRootPanel(){
        return myRootPanel;
    }

    public void drawLibrary(){

        Object[] data = new Object[]{"����� 1", "����� 2", "����� 3", new String[]{"����� 1", "����� 2"}};
        JTree tree = new JTree(data);
        myRootPanel.add(tree);

    }

}
