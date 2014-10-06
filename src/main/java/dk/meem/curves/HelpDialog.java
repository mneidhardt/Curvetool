package dk.meem.curves;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class HelpDialog extends JDialog implements ActionListener, WindowListener
{
	HelpDialog(Mainframe cr, String title, boolean modal)
	{
		super(cr, title, modal);

		String overviewString = "<html><body><p>The controls, at the bottom of the screen, are grouped in 4:</p><p>To the left you see the 4 different interpolation methods.</p><p>Next up are the actions you can perform, i.e. Interpolate, Rotate, AutoRotate and Erase.</p><p> To the right of those, you find the states to program can be in.</p><p> Finally, at the far right are the three sliders for TCB.</p><p>Click in upper pane to create points.</p><p>Press Interpolate to see curve (when you have entered more than 2 points).</p><p>Pressing Rotate will rotate the curve a few degrees. AutoRotate will rotate the curve 360 degrees, drawing the curve for every few degrees, as it turns.</p><p>A point can be dragged around with the left mouse button.</p> " +
								"<p>The whole curve can be moved around by dragging a point with the right mouse button.</p><p>When TCB is selected, you can \"Select point (TCB only)\"; the tangents in the selected point can now be adjusted with the three sliders. </p><p> When Bezier is selected, the control points can be moved by dragging them with left mouse button.<br>Drag both control points (for internal nodes only) by using right mouse button.</p></body></html>";
		String countUpString = "<html><body>nothing here...</p></body></html>";
		String countTimeOfDayString = "<html><body>heller intet her...</body></html>";

		//CENTER
		JPanel centerPanel = new JPanel();
		JTabbedPane tabbedPane = new JTabbedPane();
		centerPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		JEditorPane overviewPane = new JEditorPane("text/html", overviewString);
		JScrollPane countDownPane2 = new JScrollPane(overviewPane);
		//countDownPane2.setPreferredSize(new Dimension(400,200));
		tabbedPane.addTab("First tab", countDownPane2);

		JEditorPane countUpPane = new JEditorPane("text/html", countUpString);
		JScrollPane countUpPane2 = new JScrollPane(countUpPane);
		countUpPane2.setPreferredSize(new Dimension(400,200));
		tabbedPane.addTab("Second tab", countUpPane2);

		JEditorPane countTimeOfDayPane = new JEditorPane("text/html", countTimeOfDayString);
		JScrollPane countTimeOfDayPane2 = new JScrollPane(countTimeOfDayPane);
		countTimeOfDayPane2.setPreferredSize(new Dimension(400,200));
		tabbedPane.addTab("Third tab", countTimeOfDayPane2);

        centerPanel.add(tabbedPane);
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
