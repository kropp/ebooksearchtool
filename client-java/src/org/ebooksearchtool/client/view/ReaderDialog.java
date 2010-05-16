package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**Date: 14.05.2010
 * Time: 16:14:56
 */
public class ReaderDialog extends JDialog {

    JButton myChangePdf, myChangeEpub;
    JLabel myPdfLabel, myEpubLabel;
    Controller myController;

    public ReaderDialog(Controller con){

        super(new JFrame(), "Reader Options");

        myController = con;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel pdfPan = new JPanel();
        pdfPan.setLayout(new BoxLayout(pdfPan, BoxLayout.X_AXIS));
        myChangePdf = new JButton("Change");
        pdfPan.add(myChangePdf);
        pdfPan.add(Box.createHorizontalStrut(12));
        String pdfReader;
        if(myController.getSettings().getPdfReader() != null){
            File prog = new File(myController.getSettings().getPdfReader());
            pdfReader = prog.getName();
        }else{
            pdfReader = "none";
        }
        myPdfLabel = new JLabel(pdfReader);
        pdfPan.add(myPdfLabel);

        JPanel epubPan = new JPanel();
        epubPan.setLayout(new BoxLayout(epubPan, BoxLayout.X_AXIS));
        myChangeEpub = new JButton("Change");
        epubPan.add(myChangeEpub);
        epubPan.add(Box.createHorizontalStrut(12));
        String epubReader;
        if (myController.getSettings().getEpubReader() != null) {
            File prog = new File(myController.getSettings().getEpubReader());
            epubReader = prog.getName();
        }else{
            epubReader = "none";
        }
        myEpubLabel = new JLabel(epubReader);
        epubPan.add(myEpubLabel);

        myEpubLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        myPdfLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pdfPan.setAlignmentX(Component.LEFT_ALIGNMENT);
        epubPan.setAlignmentX(Component.LEFT_ALIGNMENT);

        pdfPan.setBorder(new TitledBorder("Reader for pdf files"));
        epubPan.setBorder(new TitledBorder("Reader for epub files"));

        main.add(pdfPan);
        main.add(Box.createVerticalStrut(15));
        main.add(epubPan);

        myChangePdf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                JFileChooser pdfReader = new JFileChooser();
                pdfReader.setDialogTitle("Choose reader for pdf files");
                if(pdfReader.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION){
                    myController.getSettings().setPdfReader(pdfReader.getSelectedFile().toString());
                    try {
                        myController.writeSettings();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    dispose();
                    new ReaderDialog(myController);
                }

            }
            
        });

        myChangeEpub.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                JFileChooser epubReader = new JFileChooser();
                epubReader.setDialogTitle("Choose reader for epub files");
                epubReader.showOpenDialog(new JFrame());
                if(epubReader.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION){
                    myController.getSettings().setEpubReader(epubReader.getSelectedFile().toString());
                    try {
                        myController.writeSettings();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    dispose();
                    new ReaderDialog(myController);
                }

            }

        });

        getContentPane().add(main);

        getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        //pack();
        setSize(260, 200);
        setModal(true);
        setLocation(200, 200);
        setVisible(true);

        setResizable(false);

    }

}
