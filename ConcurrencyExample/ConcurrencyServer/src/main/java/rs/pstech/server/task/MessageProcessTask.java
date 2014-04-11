package rs.pstech.server.task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;

public class MessageProcessTask implements Callable<MessageResult>{
	
	private static final Logger log = LoggerFactory.getLogger(MessageProcessTask.class);
	
	private Message message;
	private Socket socket;

	public MessageProcessTask (Socket socket) {
		this.socket = socket;
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			this.message = (Message)inputStream.readObject();
			log.info("Received message {} for processing from client",message);
		} catch (IOException ioex) {
			log.error("Error while processing message",ioex);
			ioex.printStackTrace();
		} catch (ClassNotFoundException ce){
			log.error("Error while processing message",ce);
			ce.printStackTrace();
		}
	}
	
	public MessageResult call() throws Exception {
		log.debug("Starting to process message {}", message);
		long startTime = System.currentTimeMillis();
		Thread.sleep(message.getMessageString().length() * 100);
		log.debug("End processing message {}", message);
		message.setProcessingTime(System.currentTimeMillis() - startTime);
		return new MessageResult(message,socket);
	}
	
	

}


