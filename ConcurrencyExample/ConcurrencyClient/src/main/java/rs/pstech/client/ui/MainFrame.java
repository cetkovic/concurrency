package rs.pstech.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.miginfocom.swing.MigLayout;
import rs.pstech.common.Message;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = -7631828404857895596L;
	private static final Logger log = LoggerFactory.getLogger(MainFrame.class);

	public static void main(String[] args) {
		MigLayout layout = new MigLayout("fill", "[]", "[]");
		MainFrame frame = new MainFrame();
		frame.setLayout(layout);
		frame.setSize(200, 200);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.add(new JLabel("Test"));
		JButton button = new JButton("Send");
		frame.add(button);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				Message message = new Message(getRandomString());
				log.info("Sending message to server {}", message);
				Socket socket;
				ObjectOutputStream out = null;
				try {
					socket = new Socket("localhost", 9898);
					out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject(message);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		frame.setVisible(true);
	}
	
	public static String getRandomString(){
		int randomLength = RandomUtils.nextInt(5, 15);
		return RandomStringUtils.randomAlphabetic(randomLength);
	}
}
