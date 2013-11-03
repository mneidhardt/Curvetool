import javax.swing.*;

class Intrpol {

	public static void main(String args[]) {

		// Set cross-platform Java L&F (also called "Metal")
		//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JFrame frame = new Mainframe();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}