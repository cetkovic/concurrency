package rs.pstech.client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

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
		MigLayout layout = new MigLayout("fill", "[][]", "[]");
		MainFrame frame = new MainFrame();
		frame.setLayout(layout);
		frame.setSize(650, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new JLabel("Test"));
		JButton button = new JButton("Send");
		frame.add(button, "wrap");

        final MessageListingPanel sentMessages = new MessageListingPanel(20);
        frame.add(sentMessages);

        final MessageListingPanel receivedMessages = new MessageListingPanel(20);
        frame.add(receivedMessages);


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
                    sentMessages.addMessage(message);
				} catch (IOException e) {
					log.error("", e);
				} finally {
					try {
						out.close();
					} catch (IOException e) {
                        log.error("", e);
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
