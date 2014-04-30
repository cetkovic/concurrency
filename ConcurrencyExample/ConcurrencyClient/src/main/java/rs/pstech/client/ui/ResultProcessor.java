package rs.pstech.client.ui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;

public class ResultProcessor implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ResultProcessor.class);
	
	private BlockingQueue<Future<Message>> messages;
	private MessageListingPanel receivedMessages;
	
	public ResultProcessor (BlockingQueue<Future<Message>> messages, MessageListingPanel receivedMessages) {
		this.messages = messages;
		this.receivedMessages = receivedMessages;
	}

	@Override
	public void run() {
		log.info("Result processor thread ready to accept results...");
		while(true){
			try {
				final Message message = messages.take().get();
				log.info("update1");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						log.info("update2");
						receivedMessages.addMessage(message);								
					}
				});
				
			} catch (Exception e) {
				log.error("Error trying to take from the received messages queue",e);
			}
		}
	}

}
