package rs.pstech.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
	

	public static void main(String[] args) {
		
		final ExecutorService executor = Executors.newFixedThreadPool(50);
		
		final BlockingQueue<Future<Message>> messages = new LinkedBlockingQueue<Future<Message>>();

		
		MigLayout layout = new MigLayout("fill", "[][]", "[]");
		MainFrame frame = new MainFrame();
		frame.setLayout(layout);
		frame.setSize(650, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new JLabel("Test"));
        final JTextField inputField = new JTextField("5");
        frame.add(inputField);
		JButton button = new JButton("Send");
		frame.add(button, "wrap");

        final MessageListingPanel sentMessages = new MessageListingPanel(20);
        frame.add(sentMessages);

        final MessageListingPanel receivedMessages = new MessageListingPanel(20);
        frame.add(receivedMessages);
        
        frame.setVisible(true);
        
        new Thread(new Runnable() {
			
			public void run() {
				while(true){
					try {
						receivedMessages.addMessage(messages.take().get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
        
        button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				sentMessages.clear();
				receivedMessages.clear();
				
				int numberOfMessages = Integer.parseInt(inputField.getText());
				for (int i = 0; i<numberOfMessages;i++){
					Message message = new Message(getRandomString());
					sentMessages.addMessage(message);
					messages.add(executor.submit(new MessageProcessorTask(message)));
				}
			}
		});
		
	}
	
	private static class MessageProcessorTask implements Callable<Message>{
		private Message message;
		
		public MessageProcessorTask (Message message){
			this.message = message;
		}
		
		public Message call() throws Exception {
			Socket socket = null;
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			try{
				log.info("Sending message to server {}", message);
				socket = new Socket("localhost", 9898);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(message);
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
                Message returnMessage = (Message)in.readObject();
                log.info("Got back message {}-{}ms", message.getMessageString(), message.getProcessingTime());
                return returnMessage;
			} catch (Exception e) {
				log.error("",e);
				return new Message("error");
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getRandomString(){
		int randomLength = RandomUtils.nextInt(5, 15);
		return RandomStringUtils.randomAlphabetic(randomLength);
	}
}
