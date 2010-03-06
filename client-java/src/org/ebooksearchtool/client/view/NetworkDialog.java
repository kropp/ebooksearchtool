package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.exec.Controller;
import org.ebooksearchtool.client.logic.parsing.Parser;
import org.ebooksearchtool.client.logic.parsing.SAXQueryHandler;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: 
 * Date: 17.10.2009
 * Time: 21:57:00
 * To change this template use File | Settings | File Templates.
 */
public class NetworkDialog extends JDialog{

    JComboBox myServerCombo;
    JTextField myServerText;
    JTextField myIPText;
    JTextField myPortText;
    JCheckBox myProxyCheck;
    JButton myOk, myCancel;

    private Controller myController;

    public NetworkDialog(Controller controller) throws IOException, SAXException, ParserConfigurationException {

        super(new JFrame(), "Network Options");

        myController = controller;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel serverChange = new JPanel();
        serverChange.setLayout(new BoxLayout(serverChange, BoxLayout.Y_AXIS));

        JLabel supServerLabel = new JLabel("supported server:");
        serverChange.add(supServerLabel);
        serverChange.add(Box.createHorizontalStrut(12));

        String[] servers = new String[myController.getSettings().getSupportedServers().size() + 1];
        String[] temp = myController.getSettings().getSupportedServers().keySet().toArray(new String[myController.getSettings().getSupportedServers().size()]);
        for(int i = 0; i < myController.getSettings().getSupportedServers().size(); ++i){
            servers[i] = temp[i];
        }
        servers[myController.getSettings().getSupportedServers().size()] = "other";

        JPanel[] servPans = new JPanel[myController.getSettings().getSupportedServers().size()];
        for(int i = 0; i < myController.getSettings().getSupportedServers().size(); ++i){
            servPans[i] = new JPanel();
            JCheckBox check = new JCheckBox();
            JLabel nameLabel = new JLabel(temp[i]);
            servPans[i].setLayout(new BoxLayout(servPans[i], BoxLayout.X_AXIS));
            servPans[i].add(check);
            servPans[i].add(Box.createHorizontalStrut(12));
            servPans[i].add(nameLabel);
            serverChange.add(servPans[i]);
        }

        myServerCombo = new JComboBox(servers);
        serverChange.add(myServerCombo);

        JPanel server = new JPanel();
        server.setLayout(new BoxLayout(server, BoxLayout.X_AXIS));

        JLabel serverLabel = new JLabel("other server:");
        server.add(serverLabel);
        server.add(Box.createHorizontalStrut(12));
        myServerText = new JTextField(25);
        server.add(myServerText);

        JPanel proxy = new JPanel();
        proxy.setLayout(new BoxLayout(proxy, BoxLayout.X_AXIS));

        JLabel proxyLabel = new JLabel("use proxy");
        proxy.add(proxyLabel);
        proxy.add(Box.createHorizontalStrut(12));
        myProxyCheck = new JCheckBox();
        proxy.add(myProxyCheck);

        JPanel IP = new JPanel();
        IP.setLayout(new BoxLayout(IP, BoxLayout.X_AXIS));

        JLabel ipLabel = new JLabel("IP:");
        IP.add(ipLabel);
        IP.add(Box.createHorizontalStrut(12));
        myIPText = new JTextField(25);
        IP.add(myIPText);

        JPanel port = new JPanel();
        port.setLayout(new BoxLayout(port, BoxLayout.X_AXIS));

        JLabel portLabel = new JLabel("port:");
        port.add(portLabel);
        port.add(Box.createHorizontalStrut(12));
        myPortText = new JTextField(25);
        port.add(myPortText);

        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0));

        myOk = new JButton("Apply");
        myCancel = new JButton("Cancel");
        grid.add(myOk);
        grid.add(myCancel);
        flow.add(grid);

        serverChange.setAlignmentX(Component.LEFT_ALIGNMENT);
        server.setAlignmentX(Component.LEFT_ALIGNMENT);
        IP.setAlignmentX(Component.LEFT_ALIGNMENT);
        port.setAlignmentX(Component.LEFT_ALIGNMENT);
        proxy.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.setAlignmentX(Component.LEFT_ALIGNMENT);
        flow.setAlignmentX(Component.LEFT_ALIGNMENT);

        myServerText.setAlignmentY(Component.CENTER_ALIGNMENT);
        myPortText.setAlignmentY(Component.CENTER_ALIGNMENT);
        myProxyCheck.setAlignmentY(Component.CENTER_ALIGNMENT);
        myIPText.setAlignmentY(Component.CENTER_ALIGNMENT);
        proxyLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        portLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        serverLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        ipLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        Component[] comps = {supServerLabel, serverLabel, proxyLabel, ipLabel, portLabel};
        int labelWidth = 0;
        Dimension dim = new Dimension();
        
        for(int i = 0; i < 5; ++i){
            if(labelWidth < comps[i].getPreferredSize().width){
                labelWidth = comps[i].getPreferredSize().width;
                dim = comps[i].getPreferredSize();
            }
        }

        for(int i = 0; i < 5; ++i){
            comps[i].setPreferredSize(dim);
            comps[i].setMaximumSize(dim);
            comps[i].setMinimumSize(dim);
        }

        Insets margin = myOk.getMargin();
        margin.left = 12;
        margin.right = 12;
        myOk.setMargin(margin);

        margin = myCancel.getMargin();
        margin.left = 12;
        margin.right = 12;
        myCancel.setMargin(margin);

        Dimension size = myServerText.getPreferredSize();
        size.width = myServerText.getMaximumSize().width;
        myServerText.setMaximumSize(size);

        size = myIPText.getPreferredSize();
        size.width = myIPText.getMaximumSize().width;
        myIPText.setMaximumSize(size);

        main.add(serverChange);
        main.add(Box.createVerticalStrut(12));
        main.add(server);
        main.add(Box.createVerticalStrut(12));
        main.add(proxy);
        main.add(Box.createVerticalStrut(12));
        main.add(IP);
        main.add(Box.createVerticalStrut(12));
        main.add(port);
        main.add(Box.createVerticalStrut(17));
        main.add(flow);

        for(int i = 0; i < servers.length; ++i){
            if(myController.getSettings().getServer().equals(servers[i])){
                myServerCombo.setSelectedItem(servers[i]);
                myServerText.setEnabled(false);
            }
        }
//       
        myServerText.setText(myController.getSettings().getServer());
        myProxyCheck.setSelected(myController.getSettings().isProxyEnabled());
        myIPText.setText(myController.getSettings().getIP());
        myPortText.setText(String.valueOf(myController.getSettings().getPort()));
        myIPText.setEnabled(myProxyCheck.isSelected());
        myPortText.setEnabled(myProxyCheck.isSelected());

        

        myCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                dispose();

            }

        });

        myOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                for(int i = 0; i < myController.getSettings().getSupportedServers().size(); ++i){
                    if(myServerText.getText().startsWith((String)myServerCombo.getItemAt(i))){
                        myServerCombo.setSelectedItem(myServerCombo.getItemAt(i));
                        myServerText.setEnabled(false);
                        break;
                    }
                    if(i == myController.getSettings().getSupportedServers().size() - 1){
                        try {
                            Connector con = new Connector(myServerText.getText(), myController.getSettings());
                            if(con.getFileFromURL("searchprobe.xml")){
                                Parser parser = new Parser();
                                SAXQueryHandler handler = new SAXQueryHandler();
                                parser.parse("searchprobe.xml", handler);
                                if(handler.getSearchLink() != null){
                                    if(handler.getSearchLink().contains("{searchTerms}") || handler.getSearchLink().contains("{searchTerms?}")){
                                        StringBuffer link = new StringBuffer(handler.getSearchLink());
                                        link.delete(link.indexOf("{searchTerms"), link.length());
                                        myController.getSettings().getSupportedServers().put(myServerText.getText(), link.toString());
                                    } else {

                                        if (!handler.getSearchLink().startsWith("http://")) {
                                            if (handler.getSearchLink().startsWith("/")) {
                                                StringBuffer link = new StringBuffer(myServerText.getText());
                                                if (link.indexOf("/", 9) != -1) {
                                                    link.delete(link.indexOf("/", 8), link.length());
                                                }
                                                con = new Connector(link.toString() + handler.getSearchLink(), myController.getSettings());
                                                myServerText.setText(link.toString());
                                            } else {
                                                if (myServerText.getText().lastIndexOf("/") == myServerText.getText().length()) {
                                                    con = new Connector(myServerText.getText() + handler.getSearchLink(), myController.getSettings());
                                                } else {
                                                    con = new Connector(myServerText.getText() + "/" + handler.getSearchLink(), myController.getSettings());
                                                }
                                            }
                                        } else {
                                            con = new Connector(handler.getSearchLink(), myController.getSettings());
                                        }

                                        if (con.getFileFromURL("searchprobe.xml")) {
                                            handler = new SAXQueryHandler();
                                            parser.parse("searchprobe.xml", handler);
                                            if (handler.getSearchLink() != null) {
                                                if (handler.getSearchLink().contains("{searchTerms}") || handler.getSearchLink().contains("{searchTerms?}")) {
                                                    StringBuffer link = new StringBuffer(handler.getSearchLink());
                                                    link.delete(link.indexOf("{searchTerms"), link.length());
                                                    myController.getSettings().getSupportedServers().put(myServerText.getText(), link.toString());
                                                } else {
                                                    JOptionPane.showMessageDialog(new JDialog(), "This feed hasn't search terms and can't be used as searchable catalog", "Wrong catalog", JOptionPane.WARNING_MESSAGE);
                                                    return;
                                                }
                                            } else {
                                                JOptionPane.showMessageDialog(new JDialog(), "This feed hasn't search terms and can't be used as searchable catalog", "Wrong catalog", JOptionPane.WARNING_MESSAGE);
                                                return;
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(new JDialog(), "This feed hasn't search terms and can't be used as searchable catalog", "Wrong catalog", JOptionPane.WARNING_MESSAGE);
                                            return;
                                        }

                                    }
                                } else {
                                    JOptionPane.showMessageDialog(new JDialog(), "This feed hasn't search terms and can't be used as searchable catalog", "Wrong catalog", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            } else {
                                JOptionPane.showMessageDialog(new JDialog(), "Page not found", "Wrong catalog", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(new JDialog(), "Error in connection", "Wrong catalog", JOptionPane.ERROR_MESSAGE);
                            return;
                        } catch (SAXException e1) {
                            JOptionPane.showMessageDialog(new JDialog(), "Error in parsing", "Wrong catalog", JOptionPane.ERROR_MESSAGE);
                            return;
                        } catch (ParserConfigurationException e1) {
                            JOptionPane.showMessageDialog(new JDialog(), "Error in parsing", "Wrong catalog", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                try {
                    myController.getSettings().setIP(myIPText.getText());
                    myController.getSettings().setPort(Integer.parseInt(myPortText.getText()));
                    myController.getSettings().setProxyEnabled(myProxyCheck.isSelected());
                    myController.getSettings().setServer(myServerText.getText());
                    myController.writeSettings();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                dispose();

            }

        });
        
        myProxyCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                myIPText.setEnabled(myProxyCheck.isSelected());
                myPortText.setEnabled(myProxyCheck.isSelected());
                
            }

        });

        myServerCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(myServerCombo.getSelectedItem().equals("other")){
                    myServerText.setEnabled(true);
                }else{
                    myServerText.setText((String)myServerCombo.getSelectedItem());
                    myServerText.setEnabled(false);
                }

            }

        });
        
        getContentPane().add(main);       

        getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        pack();
        setModal(true);
        setLocation(200, 200);
        setVisible(true);
        
        setResizable(false);
        
    }

}
