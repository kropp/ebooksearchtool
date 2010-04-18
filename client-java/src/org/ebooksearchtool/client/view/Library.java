package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

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

        Object[] data = new Object[]{"полка 1", "полка 2", "полка 3", new String[]{"книга 1", "книга 2"}};
        MutableTreeNode node = new DefaultMutableTreeNode("library");
        DefaultTreeModel model = new DefaultTreeModel(node);
        model.insertNodeInto(new DefaultMutableTreeNode("shelf 1"), node, 0);
        model.insertNodeInto(new DefaultMutableTreeNode("shelf 2"), node, 1);
        model.insertNodeInto(new DefaultMutableTreeNode("shelf 3"), node, 2);

        JTree tree = new JTree();
        tree.setAlignmentX(JTree.LEFT_ALIGNMENT);
        //tree.setPreferredSize(myRootPanel.getSize());
        tree.setModel(model);
        myRootPanel.add(tree);

    }

}
