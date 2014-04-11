package rs.pstech.client.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;

public class MessageSenderTask implements Callable<Message>{
	private static final Logger log = LoggerFactory.getLogger(MessageSenderTask.class);
	private Message message;
	
	public MessageSenderTask (Message message){
		this.message = message;
	}
	
	public Message call() throws Exception {
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try{
			log.info("Sending message to server {}", message);
			long start = System.currentTimeMillis();
			socket = new Socket("localhost", 9898);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
            Message returnMessage = (Message)in.readObject();
            log.info("Got back message {}-{}ms", message.getMessageString(), System.currentTimeMillis() - start);
            return returnMessage;
		} catch (Exception e) {
			log.error("",e);
			return new Message("error");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("Cannot close client socket");
			}
		}
	}
}
