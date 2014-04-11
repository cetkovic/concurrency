package rs.pstech.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = -7631828404857895596L;
	private static final Logger log = LoggerFactory.getLogger(MainFrame.class);
	
	JTextField inputField;
	JButton button;
	MessageListingPanel sentMessages;
	MessageListingPanel receivedMessages;
	
	ExecutorService executor = Executors.newFixedThreadPool(50);
	BlockingQueue<Future<Message>> messages = new LinkedBlockingQueue<Future<Message>>();
	
	public void main() {

		MigLayout layout = new MigLayout("fill", "[][][][]", "[]");
		MainFrame frame = new MainFrame();
		frame.setLayout(layout);
		frame.setSize(650, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new JLabel("Number of test messages: "));
        inputField = new JTextField("5");
        inputField.addActionListener(new MyActionListener());
        frame.add(inputField, "grow x");
		button = new JButton("Send");
		frame.add(button, "wrap, span 2");

        sentMessages = new MessageListingPanel(20);
        frame.add(sentMessages, "span 2, grow");

        receivedMessages = new MessageListingPanel(20);
        frame.add(receivedMessages, "span 2, grow");
        
        frame.setVisible(true);
        
        // Thread for reading the responses, and putting them in the receivedMessages MessageListingPane
        new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						receivedMessages.addMessage(messages.take().get());
					} catch (Exception e) {
						log.error("Error trying to take from the received messages queue",e);
					}
				}
			}
		}).start();
        
        button.addActionListener(new MyActionListener());
		
	}
	
	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sentMessages.clear();
			receivedMessages.clear();
			
			int numberOfMessages = Integer.parseInt(inputField.getText());
			for (int i = 0; i<numberOfMessages;i++){
				Message message = new Message(getRandomString());
				sentMessages.addMessage(message);
				messages.add(executor.submit(new MessageSenderTask(message)));
			}
		}
	}
	
	public static String getRandomString(){
		int randomLength = RandomUtils.nextInt(5, 15);
		return RandomStringUtils.randomAlphabetic(randomLength);
	}
	
	public static void main(String[] args) {
		new MainFrame().main();
	}
}
