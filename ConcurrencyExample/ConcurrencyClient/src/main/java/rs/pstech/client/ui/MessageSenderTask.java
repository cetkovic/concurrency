package rs.pstech.client.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;
import rs.pstech.common.ServerProperties;

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
			
			//Send message out
			socket = new Socket(ServerProperties.LOCALHOST.getURL(),ServerProperties.LOCALHOST.getPort());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			out.flush();
			
			//Get response
			in = new ObjectInputStream(socket.getInputStream());
            Message returnMessage = (Message)in.readObject();
            
            log.info("Got back message {}-{}ms", message.getMessageString(), System.currentTimeMillis() - start);
            return returnMessage;
            
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				log.error("Cannot close client socket");
			}
		}
	}
}
