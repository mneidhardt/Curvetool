import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;


class Mainframe extends JFrame implements MouseListener, MouseMotionListener, ActionListener {
   DrawPanel drawarea;
   JPanel controlarea;
   JDialog psDialog;
   JDialog helpDialog;
   JDialog aboutDialog;
   JRadioButton addPoint;       // Global as I need to access this.
   // Just a comment to trigger Git changes

   int maxX=950, maxY=600;
   int ctrlareaHeight=120;

   Cursor currcursor = new Cursor(Cursor.DEFAULT_CURSOR);

   private static final int st_neutral=0, st_selectpoint=1, st_deletepoint=2, st_newstartpoint=3;

   Mainframe() {
      super("Interpol");
      setPreferredSize(new Dimension(maxX, maxY));

      addMouseListener(this);
      addMouseMotionListener(this);

      // This sets up the drawing area:
      drawarea = new DrawPanel(maxX, maxY-ctrlareaHeight, this);

      // And this sets up the control (or button) area:
      controlarea = new JPanel(new GridBagLayout());
      setupControls(maxX, ctrlareaHeight);         // Fills buttons etc. into controlarea.


      // ------------- Set up the menu system:
      psDialog = new PSDialog(this, "Bezier curve as Postscript", true, "");
      helpDialog = new HelpDialog(this, "Help", true);
      aboutDialog = new AboutDialog(this, "About Interpol", true);

      JMenuBar bar = new JMenuBar();
      setJMenuBar(bar);
      JMenu fileMenu = new JMenu("File");
      bar.add(fileMenu);
      JMenu viewMenu = new JMenu("View");
      bar.add(viewMenu);
      JMenu helpMenu = new JMenu("Help");
      bar.add(helpMenu);


      JMenuItem filebzpsExport = new JMenuItem("Bezier curve as Postscript");
      filebzpsExport.addActionListener(this);
      fileMenu.add(filebzpsExport);

      JMenuItem dumpPointset = new JMenuItem("Dump coordinates of point set");
      dumpPointset.addActionListener(this);
      fileMenu.add(dumpPointset);

      JMenuItem fileExit = new JMenuItem("Exit");
      fileExit.addActionListener(this);
      fileMenu.add(fileExit);

      JMenuItem gridDisplay = new JMenuItem("Grid on/off");
      gridDisplay.addActionListener(this);
      viewMenu.add(gridDisplay);

      JMenuItem help = new JMenuItem("Help");
      help.addActionListener(this);
      helpMenu.add(help);
      JMenuItem about = new JMenuItem("About Interpol");
      about.addActionListener(this);
      helpMenu.add(about);
      // --------------- End menu system.


      Container pane = getContentPane();
      pane.add(drawarea, BorderLayout.CENTER);
      pane.add(controlarea, BorderLayout.SOUTH);

      setVisible(true);
      pack();
   }


   public void actionPerformed(ActionEvent e) {
      saySomething("Action performed: " + e.getActionCommand());


      if (e.getActionCommand() == "type2") {
         setInNeutral();
         //drawarea.setInterpolType(2);
      }
      else if (e.getActionCommand() == "type3") {
         setInNeutral();
         //drawarea.setInterpolType(3);
      }
      else if (e.getActionCommand() == "interpoler") {
         drawarea.interPolate();
      }
      else if (e.getActionCommand() == "clearcurve") {
         drawarea.clearCurve();
      }
      else if (e.getActionCommand() == "expandcurve") {
         drawarea.expandCurve();
      }
      else if (e.getActionCommand() == "addpoint") {
         //drawarea.setPointState(st_neutral);
         //currcursor = new Cursor(Cursor.DEFAULT_CURSOR);
      }
      else if (e.getActionCommand() == "delpoint") {
         //drawarea.setPointState(st_deletepoint);
         //currcursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
      }
      else if(e.getActionCommand() == "Exit") {
         System.exit(0);
      }
      else if(e.getActionCommand() == "About Interpol") {
         aboutDialog.setVisible(true);
      }
   }


   public void setAboutDialogVisible() {
      aboutDialog.setVisible(true);
   }

    void saySomething(String eventDescription) {
        System.out.println(eventDescription);
    }


   private void setInNeutral() {
      /*
      if (drawarea.getPointState() == st_selectpoint) {
         addPoint.doClick();
         drawarea.setPointState(st_neutral);
         setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
      */
   }


   private void setupControls(int maxX, int maxY) {
      GridBagConstraints c = new GridBagConstraints();


      // Method RadioButtons start:
      JRadioButton bezButton = new JRadioButton("Bezier");
      bezButton.setMnemonic(KeyEvent.VK_B);
      bezButton.setActionCommand("type2");
      bezButton.setSelected(true);
      bezButton.addActionListener(this);
      //bezButton.setEnabled(false);         // Until its ready!!!!!

      JRadioButton lgrButton = new JRadioButton("Lagrange");
      lgrButton.setMnemonic(KeyEvent.VK_L);
      lgrButton.setActionCommand("type3");
      lgrButton.addActionListener(this);

      ButtonGroup group = new ButtonGroup();
      group.add(bezButton);
      group.add(lgrButton);

      JPanel radiopanel = new JPanel(new GridLayout(4, 1));
      radiopanel.add(bezButton);
      radiopanel.add(lgrButton);

      // controlarea.add(radiopanel);
      // Method RadioButtons end.

      // ActionButtons start:
      JButton intrpcrv = new JButton("Interpolate");
      intrpcrv.setMnemonic(KeyEvent.VK_I);
      intrpcrv.setActionCommand("interpoler");
      intrpcrv.addActionListener(this);

      JButton rotatecrv = new JButton("Expand");
      rotatecrv.setToolTipText("Add curves on the outside of the primary one.");
      rotatecrv.setMnemonic(KeyEvent.VK_X);
      rotatecrv.setActionCommand("expandcurve");
      rotatecrv.addActionListener(this);

      JButton autorotatecrv = new JButton("Auto rotate");
      autorotatecrv.setToolTipText("Rotates curve 360 degrees.");
      autorotatecrv.setMnemonic(KeyEvent.VK_U);
      autorotatecrv.setActionCommand("autorotatecurve");
      autorotatecrv.addActionListener(this);

      JButton clearcrv = new JButton("Erase curve");
      clearcrv.setMnemonic(KeyEvent.VK_E);
      clearcrv.setToolTipText("Will erase curve and data.");
      clearcrv.setActionCommand("clearcurve");
      clearcrv.addActionListener(this);

      // One of the actions is in a ComboBox - the test data:
      String[] demoStrings = { "Test data:", "1/x^2", "Akima", "Fritsch/Carlson", "NAG" };
      JComboBox demoList = new JComboBox(demoStrings);
      demoList.setToolTipText("Creates a new curve with given data. NB: Destroys current curve.");
      demoList.setSelectedIndex(0);
      demoList.addActionListener(this);
      // End dropdown list.


      JPanel actionbuttons = new JPanel(new GridLayout(4, 2, 0, 0));
      actionbuttons.add(intrpcrv);
      //actionbuttons.add(random);
      actionbuttons.add(rotatecrv);
      actionbuttons.add(autorotatecrv);
      //actionbuttons.add(demoList);         // Temporarily (?) taken out.
      actionbuttons.add(clearcrv);

      controlarea.add(actionbuttons);
      // ActionButtons end.


      // State radio buttons:
      addPoint = new JRadioButton("Add/move point");
      addPoint.setMnemonic(KeyEvent.VK_A);
      addPoint.setActionCommand("addpoint");
      addPoint.setSelected(true);
      addPoint.addActionListener(this);

      JRadioButton delPoint = new JRadioButton("Delete point");
      delPoint.setMnemonic(KeyEvent.VK_D);
      delPoint.setActionCommand("delpoint");
      delPoint.addActionListener(this);

      JRadioButton newstartPoint = new JRadioButton("New start point");
      newstartPoint.setMnemonic(KeyEvent.VK_N);
      newstartPoint.setActionCommand("newstartpoint");
      newstartPoint.addActionListener(this);

      ButtonGroup group2 = new ButtonGroup();
      group2.add(addPoint);
      group2.add(newstartPoint);
      group2.add(delPoint);

      JPanel radiopanel2 = new JPanel(new GridLayout(4, 1));
      radiopanel2.add(addPoint);
      radiopanel2.add(newstartPoint);
      radiopanel2.add(delPoint);

      controlarea.add(radiopanel2);
      // State RadioButtons end.

      controlarea.setPreferredSize(new Dimension(maxX, maxY));
      controlarea.setBorder(BorderFactory.createLineBorder(Color.black));
   }

   public Cursor getCurrCursor() {
      return currcursor;
   }

    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
      if (e.getY() >= getHeight() - ctrlareaHeight) {
         setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
      else {
         setCursor(currcursor);
      }
   }


}
