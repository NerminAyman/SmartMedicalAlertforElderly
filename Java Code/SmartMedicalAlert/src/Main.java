import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.*;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Main {
	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */

	public static void createAndShowGUI() {
		String[] labels = { "Recipient Email: ", "Phone Number: ", "ID: ", "", "" };
		int numPairs = labels.length;
		JTextField[] data = new JTextField[3];
		JButton b = new JButton("Apply");
		JButton e = new JButton("Exit");
		String[] info = new String[3];

		// Create and populate the panel.
		JPanel p = new JPanel(new SpringLayout());
		for (int i = 0; i < numPairs; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			if (i == numPairs - 2) {
				l.setLabelFor(b);
				p.add(b);
			} else {
				if (i == numPairs - 1) {
					l.setLabelFor(e);
					p.add(e);
				} else {
					data[i] = new JTextField(10);
					l.setLabelFor(data[i]);
					p.add(data[i]);
				}
			}
		}

		// Lay out the panel.
		makeCompactGrid(p, numPairs, 2, 6, 6, 6, 6);

		// Create and set up the window.
		JFrame frame = new JFrame("SMAS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		p.setOpaque(true); // content panes must be opaque
		frame.setContentPane(p);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);
		
		e.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
			
		});

		//Check if "Apply" button is pressed
		while (b.getModel().isPressed() == false) {
			System.out.println("Enter Data then press button");
		}
		
		int j = 0;
		while (j<3) {
			j = 0;
			if(b.getModel().isPressed() == true) {
			for(int i = 0; i<data.length;i++) {
				if(data[i].getText().equals("") || data[i].getText().equals(null)) {
					System.out.println("Enter All the required fields.");
				}else {
					j++;
				}
			}
			if(j < 3) {
			Pop p1 = new Pop("Enter All the required fields.", Color.red);
			
			}}
		}
		
		for (int i = 0; i < data.length; i++) {
			info[i] = data[i].getText();
			System.out.println(info[i]);
		}
		// frame.dispose();
		// frame.setVisible(false);

		// Main Code
		SerialPort serialPort = SerialPort.getCommPort("COM3");
		if (serialPort.openPort())
			System.out.println("Port opened successfully.");
		else {
			System.out.println("Unable to open the port.");
			return;
		}
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

		Scanner dataP = new Scanner(serialPort.getInputStream());
		int value = 0;
		while (dataP.hasNextLine()) {
			String dataFromPort = dataP.nextLine();
			System.out.println(dataP.nextLine());

			try {
				value = Integer.parseInt(dataP.nextLine());
			} catch (Exception e1) {
			}
			try {
				String pulse = "";
				String temp = "";
				int t = 0;
				for (int h = 0; h < dataFromPort.length(); h++) {
					if (dataFromPort.charAt(h) == ',') {
						t = h;
						break;
					}
				}
				pulse = dataFromPort.substring(0, t);
				temp = dataFromPort.substring(t + 1);
				String emailBody = "This is the vital of " + info[2] + ": " + "\n" + "Heart Beats rate = " + pulse
						+ " ,Temperature = " + temp + ".";
				int pulseValue = Integer.parseInt(pulse);
				Double tempValue = Double.parseDouble(temp);

				// Send An SMS
				if (tempValue > 38.0 || pulseValue < 60 || pulseValue > 120 || (tempValue == 0.0 && pulseValue == 0)) {
					SMSSending sms = new SMSSending();
					sms.sendSMS(info[1], "There's an emergency case regarding " + info[2] + ", please help.");
				}

				// Send An email
				EmailSending javaEmail = new EmailSending();
				javaEmail.setMailServerProperties();
				javaEmail.createEmailMessage(info[0], emailBody);
				javaEmail.sendEmail();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("Done.");
		// }
		// });

	}

	private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
		SpringLayout layout = (SpringLayout) parent.getLayout();
		Component c = parent.getComponent(row * cols + col);
		return layout.getConstraints(c);
	}

	public static void makeCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad,
			int yPad) {
		SpringLayout layout;
		try {
			layout = (SpringLayout) parent.getLayout();
		} catch (ClassCastException exc) {
			System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
			return;
		}

		// Align all cells in each column and make them the same width.
		Spring x = Spring.constant(initialX);
		for (int c = 0; c < cols; c++) {
			Spring width = Spring.constant(0);
			for (int r = 0; r < rows; r++) {
				width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
			}
			for (int r = 0; r < rows; r++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
				constraints.setX(x);
				constraints.setWidth(width);
			}
			x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
		}

		// Align all cells in each row and make them the same height.
		Spring y = Spring.constant(initialY);
		for (int r = 0; r < rows; r++) {
			Spring height = Spring.constant(0);
			for (int c = 0; c < cols; c++) {
				height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
			}
			for (int c = 0; c < cols; c++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
				constraints.setY(y);
				constraints.setHeight(height);
			}
			y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
		}

		// Set the parent's size.
		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH, y);
		pCons.setConstraint(SpringLayout.EAST, x);
	}

	public static void main(String[] args) throws AddressException, MessagingException {
		// creating and showing this application's GUI.
		createAndShowGUI();

	}

}