import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class AboutDialog extends JDialog implements ActionListener, WindowListener
{
   AboutDialog(Frame owner, String title, boolean modal)
   {
      super(owner, title, modal);

      //CENTER
      JPanel centerPanel = new JPanel();
      centerPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));

      JEditorPane aboutPane = new JEditorPane("text/html", "Copyright Michael Neidhardt, 2006. Version 0.4");
      JScrollPane aboutPane2 = new JScrollPane(aboutPane);
      aboutPane2.setPreferredSize(new Dimension(400,70));

        centerPanel.add(aboutPane2);
      getContentPane().add(centerPanel, BorderLayout.CENTER);

      //SOUTH
      JPanel southPanel = new JPanel();
      JButton okButton = new JButton("Ok");
      okButton.addActionListener(this);
      southPanel.add(okButton);
      getContentPane().add(southPanel, BorderLayout.SOUTH);

      addWindowListener(this);
      pack();
   }


   //Implement ActionListener interface
   public void actionPerformed(ActionEvent e)
   {
      setVisible(false);
   }

   //Implement WindowListener interface
   public void windowClosing(WindowEvent e) {}
   public void windowClosed(WindowEvent e) {}
   public void windowActivated(WindowEvent e) {}
   public void windowDeactivated(WindowEvent e) {}
   public void windowIconified(WindowEvent e) {}
   public void windowDeiconified(WindowEvent e) {}
   public void windowOpened(WindowEvent e) {}
}
