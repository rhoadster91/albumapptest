/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albumappnew;

/**
 *
 * @author tararamanan
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;


public class AlbumAppNew1
{

    //field declarations
    private static JMenuBar mainMenu;
    private static JMenu fileMenu;
    private static JMenuItem importOpt;
    private static JMenuItem deleteOpt;
    private static JMenuItem exitOpt;
    private static JMenu viewMenu;
    private static JRadioButtonMenuItem photoViewOpt;
    private static JRadioButtonMenuItem gridViewOpt;
    private static JRadioButtonMenuItem splitViewOpt;
    private static ButtonGroup buttonMenuGroup;
    private static JPanel photoPanel;
    private static JPanel workPane;
    private static JLabel descLabel;
    private static JCheckBox vacationBox;
    private static JCheckBox familyBox;
    private static JCheckBox schoolBox;
    private static JCheckBox workBox;
    private static JRadioButtonMenuItem drawModeRadio;
    private static JRadioButtonMenuItem textModeRadio;
    private static JButton prevButton;
    private static JButton nextButton;

    private static PhotoComponent photoComp;

    public static void addComponentsToPane(Container pane)  //top level layout
    {

        pane.setPreferredSize( new Dimension( 640, 480 ) );

        addComponentsToWorkPane(workPane);

        descLabel = new JLabel("App status messages go here...");
        pane.add(descLabel, BorderLayout.PAGE_END);
        pane.add(workPane, BorderLayout.CENTER);

        //create the side toolbar
        JToolBar toolBar = new JToolBar("Extra Tools", JToolBar.VERTICAL);
        toolBar.setFloatable(false);
        pane.add(toolBar, BorderLayout.LINE_START);

        JLabel drawModeLabel = new JLabel("Mode");
        toolBar.add(drawModeLabel);

        drawModeRadio = new javax.swing.JRadioButtonMenuItem("Drawing");
        textModeRadio = new javax.swing.JRadioButtonMenuItem("Text");

        drawModeRadio.setMinimumSize(new Dimension(30,30));
        textModeRadio.setMinimumSize(new Dimension(30,30));

        ButtonGroup modeMenuGroup = new javax.swing.ButtonGroup();
        modeMenuGroup.add(drawModeRadio);
        modeMenuGroup.add(textModeRadio);
        toolBar.add(textModeRadio);
        toolBar.add(drawModeRadio);

        toolBar.addSeparator();

        JLabel tagLabel = new JLabel("Tags");
        toolBar.add(tagLabel);

        vacationBox = new JCheckBox("Vacation");
        vacationBox.setSelected(false);
        familyBox = new JCheckBox("Family");
        familyBox.setSelected(false);
        schoolBox = new JCheckBox("School");
        schoolBox.setSelected(false);
        workBox = new JCheckBox("Work");
        workBox.setSelected(false);

        toolBar.add(vacationBox);
        toolBar.add(familyBox);
        toolBar.add(schoolBox);
        toolBar.add(workBox);

        //separator
        toolBar.addSeparator();
        toolBar.add(new JLabel("Page Control"));
        //make buttons for the side toolbar
        prevButton = new JButton("<<");
        //prevButton.setPreferredSize(new Dimension(100, 100));
        toolBar.add(prevButton);
        //TODO: add a space here later
        nextButton = new JButton(">>");
        //prevButton.setPreferredSize(new Dimension(100, 100));
        toolBar.add(nextButton);

        toolBar.addSeparator(new Dimension(10,200));

        pane.setVisible(true);
    }


    public static void addComponentsToWorkPane(Container pane) //work area where the photoPanel will go
    {
        //photocomp - photoSCrollPane - photoPanel
        photoComp = new PhotoComponent();
        photoComp.setPreferredSize( new Dimension( 500, 350 ) );
        photoComp.setBackground(Color.red);
        photoComp.setVisible(true);

        //fix this scrollbar stuff later!!!
        JScrollPane photoScrollPane = new JScrollPane();
        photoScrollPane.setBackground(Color.black);
        photoScrollPane.add(photoComp);
        photoScrollPane.setViewportView(photoComp);


        photoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        photoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        photoPanel.setBackground(Color.gray);
        photoPanel.add(photoScrollPane);

        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(photoPanel, BorderLayout.CENTER);
    }


    private static void initBasicComponents(JFrame frame)
    {
        //create objects for blank declarations
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importOpt = new javax.swing.JMenuItem();
        deleteOpt = new javax.swing.JMenuItem();
        exitOpt = new javax.swing.JMenuItem();

        photoPanel = new JPanel();
        workPane = new JPanel();
        viewMenu = new javax.swing.JMenu();

        photoViewOpt = new javax.swing.JRadioButtonMenuItem("Photo View");
        gridViewOpt = new javax.swing.JRadioButtonMenuItem("Grid View");
        splitViewOpt = new javax.swing.JRadioButtonMenuItem("Split View");

        fileMenu.setText("File");
        importOpt.setText("Import");
        deleteOpt.setText("Delete");
        exitOpt.setText("Exit");
        viewMenu.setText("View");

        fileMenu.add(importOpt);
        fileMenu.add(deleteOpt);
        fileMenu.add(exitOpt);

        buttonMenuGroup = new javax.swing.ButtonGroup();
        buttonMenuGroup.add(photoViewOpt);
        buttonMenuGroup.add(gridViewOpt);
        buttonMenuGroup.add(splitViewOpt);

        viewMenu.add(photoViewOpt);
        viewMenu.add(gridViewOpt);
        viewMenu.add(splitViewOpt);

        mainMenu.add(fileMenu);
        mainMenu.add(viewMenu);

        frame.setJMenuBar(mainMenu);

    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private  static void createAndShowGUI()
    {

        //Create and set up the window.
        JFrame frame = new JFrame("AlbumApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //basic init stuff
        initBasicComponents(frame);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());


        //Display the window.
        frame.pack();
        frame.setVisible(true);
        addActionListeners(frame);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();

            }
        });
    }


    public static void addActionListeners(final JFrame frame)         //Method for handling all action listener call backs
    {

        textModeRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("Text View mode selected");
                photoComp.setTextMode(true);
            }
        });

        drawModeRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("Draw View mode selected");
                photoComp.setDrawingMode(true);
            }
        });

        vacationBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(vacationBox.isSelected())
                    descLabel.setText("Vacation tag selected");
                else
                    descLabel.setText("Vacation tag de-selected");
            }
        });
        familyBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(familyBox.isSelected())
                    descLabel.setText("Family tag selected");
                else
                    descLabel.setText("Family tag de-selected");
            }
        });
        workBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(workBox.isSelected())
                    descLabel.setText("Work tag selected");
                else
                    descLabel.setText("Work tag de-selected");
            }
        });
        schoolBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(schoolBox.isSelected())
                    descLabel.setText("School tag selected");
                else
                    descLabel.setText("School tag de-selected");
            }
        });

        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("Go to previous Photo...");
            }
        });
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("Go to next Photo...");
            }
        });


        photoViewOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("View in Photo View");
            }
        });

        gridViewOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("View in Grid View");
            }
        });

        splitViewOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("View in Split View");
            }
        });

        importOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("Import Photos..");
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog((new JPanel()));

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    descLabel.setText("Opening: " + file.getName());
                    photoComp = new PhotoComponent();
                    photoComp.loadPhoto(file);
                    photoComp.setVisible(true);

                }
                else
                {
                    descLabel.setText("Open command cancelled by user.");
                }

            }
        });

        deleteOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                descLabel.setText("Deleted selected Photo...");
                photoComp.setVisible(false);
                photoComp = null;
            }
        });

        exitOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        photoComp.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount()==2)
                {
                    // your code here
                    photoComp.flip();
                    descLabel.setText("Flipped photo to annotate!");
                }
            }
        });

    }

}
