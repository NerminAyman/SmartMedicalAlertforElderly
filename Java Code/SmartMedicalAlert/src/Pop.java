// Java Program to create a popup and display 
// it on a parent frame 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
class Pop extends JFrame { 
	// popup 
	Popup p; 

	@SuppressWarnings("deprecation")
	// constructor 
	Pop(String s, Color c) 
	{ 
		// create a frame 
		JFrame f = new JFrame("pop"); 
		f.pack();
		//f.setVisible(true);
		f.setSize(400, 200);
		f.setLocationRelativeTo(null);

		// create a label 
		JLabel l = new JLabel(s); 

		PopupFactory pf = new PopupFactory(); 

		// create a panel 
		JPanel p2 = new JPanel(); 

		// set Background of panel 
		p2.setBackground(c); 

		p2.add(l); 

		// create a popup 
		p = pf.getPopup(f, p2, 600, 300); 


		// create a panel 
		JPanel p1 = new JPanel(); 

		f.add(p1); 
		f.setVisible(true);
		p.show();
	} 

} 
