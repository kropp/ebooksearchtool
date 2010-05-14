package org.ebooksearchtool.client.view;

import org.ebooksearchtool.client.exec.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
            pdfReader = myController.getSettings().getPdfReader();
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
            epubReader = myController.getSettings().getEpubReader();
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
