package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 11.10.2009
 * Time: 13:59:33
 * To change this template use File | Settings | File Templates.
 */
public class Window {
    private JPanel myPanel1;
    private JTextField myQueryField;
    private JTextField myIpField;
    private JTextField myPortField;
    private JButton mySearchButton;
    private JPanel myConnectionPanel;
    private JTextArea myDataTextArea;
    private JTextPane myDataTextPane;
    private Controller myController = new Controller();

    public JPanel getRootPanel() {
        return myPanel1;
    }

    public Window(){

        myConnectionPanel.setVisible(false);

        mySearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    String queryWord = myQueryField.getText();
                    myController.act(queryWord);
                    myDataTextArea.setLineWrap(true);
                    /*myDataTextArea.setColumns(myController.getData().getAttributes().length);
                    myDataTextArea.setRows(myController.getData().getInfo().size());*/
                    for (int i = 0; i < myController.getData().getInfo().size(); ++i){
                        for (int j = 0; j < myController.getData().getAttributes().length; ++j){
                            myDataTextArea.append(myController.getData().getInfo().get(i).getFields()[j] + "\n");
                        }
                         myDataTextArea.append("\n");
    	            }
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SAXException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });

    }

}
