package org.ebooksearchtool.client.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 11.10.2009
 * Time: 15:13:55
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow {

    public static void main(String args[]){

        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new Window().getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        /*Container c = frame.getContentPane();
        c.setLayout(new GridLayout(2, 0, 10, 10));
        c.add(new JButton("Search"));*/
        frame.setSize(400, 400);
        frame.setLocation(10,10);
        frame.setVisible(true);

    }

}
