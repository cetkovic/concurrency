package rs.pstech.common;

public enum ServerProperties {
	LOCALHOST ("localhost",9988);
	
	String url;
	int port;
	
	ServerProperties (String url, int port){
		this.url = url;
		this.port = port;
	}

	public String getURL() {
		return this.url;
	}
	
	public int getPort() {
		return this.port;
	}
}
