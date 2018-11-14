import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ErrorWindow extends JPanel {
	private JButton jcomp1;
	private JButton jcomp2;
	private JButton jcomp3;
	private JTextField jcomp4;

	public ErrorWindow() {
		// construct components
		jcomp1 = new JButton("test1");
		jcomp2 = new JButton("test2");
		jcomp3 = new JButton("test3");
		jcomp4 = new JTextField(5);

		// adjust size and set layout
		setPreferredSize(new Dimension(395, 156));
		setLayout(null);

		// add components
		add(jcomp1);
		add(jcomp2);
		add(jcomp3);
		add(jcomp4);

		// set component bounds (only needed by Absolute Positioning)
		jcomp1.setBounds(20, 45, 100, 25);
		jcomp2.setBounds(135, 60, 100, 25);
		jcomp3.setBounds(260, 35, 100, 25);
		jcomp4.setBounds(105, 115, 100, 25);
	}
}