package rs.pstech.server.task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;

public class MessageProcessTask implements Callable<Message>{
	
	private static final Logger log = LoggerFactory.getLogger(MessageProcessTask.class);
	
	private Message message;

	public MessageProcessTask (Socket socket) {
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			this.message = (Message)inputStream.readObject();
		} catch (IOException|ClassNotFoundException e) {
			log.error("Error while processing message",e);
			e.printStackTrace();
		}
	}
	
	public Message call() throws Exception {
		log.info("Starting to process message {}", message);
		long startTime = System.currentTimeMillis();
		Thread.sleep(message.getMessageString().length() * 100);
		log.info("End processing message {}", message);
		message.setProcessingTime(System.currentTimeMillis() - startTime);
		return message;
	}

}
