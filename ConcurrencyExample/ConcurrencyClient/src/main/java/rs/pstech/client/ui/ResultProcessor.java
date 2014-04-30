package rs.pstech.client.ui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

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
				log.debug("update0");
				final Message message = messages.take().get();
				log.debug("update1");
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						log.debug("update2");
						receivedMessages.addMessage(message);								
					}
				});
				
			} catch (Exception e) {
				log.error("Error trying to take from the received messages queue",e);
			}
		}
	}

}
