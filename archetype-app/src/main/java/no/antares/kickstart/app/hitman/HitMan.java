package no.antares.kickstart.app.hitman;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

/** 
 * -cmd "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE"
java -jar "Archetype app 1.0-SNAPSHOT-jar-with-dependencies.jar" -port 4444 -msg "HIT ME IN 5"
java -jar "Archetype app 1.0-SNAPSHOT-jar-with-dependencies.jar" -port 4444 -cmd "C:\Program Files\Internet Explorer\iexplore.exe"
 * @author tommy skodje
 */
public class HitMan {
	private static long FAR_FUTURE	= Long.MAX_VALUE;
	private static String HOST	= "localhost";

	private long deadLine	= FAR_FUTURE;
	private int[] synchLock	= {};	// monitor for deadLine

	private final ServerSocket serverSocket;
	final int ticksPerSecond	= 1000;
	final ProcessControl killer;
	private final Thread shutDown	= new Thread(){
		public void run() {
			killer.stopProcess();
	    }
	};

	/** 3 functions based on command line arguments
	 * ( port+message ): send message to hitMan (on port)
	 * ( port+cmd ): start hitMan (on port)
	 * ( no port ): print help / usage
	*/
	public static void main(String[] args) throws Exception {
		CommandLineOptions options	= new CommandLineOptions( args );
		if ( 0 < options.portNo ) {
			System.err.println( options.toString() );
			if ( ! StringUtils.isEmpty( options.message ) )
				messageToHitMan( options.portNo, options.message );
			if ( ! StringUtils.isEmpty( options.command ) ) {
				HitMan hitMan	= new HitMan( options.portNo, options.command );
				hitMan.listenForMessages();
				hitMan.closeServer();
			}
		} else {
			options.printHelp();
		}
	}

	private static void messageToHitMan( int port, String message ) throws IOException {
        Socket kkSocket = null;
        PrintWriter out = null;
        try {
            kkSocket = new Socket( HOST, port );
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            out.println( message );
            /*
        	BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;
            }
            close( in );*/
        } catch (IOException e) {
            System.err.println( "Couldn't get I/O for the connection to: " + HOST );
        } finally {
            close( out );
            close( kkSocket );
        }
    }

	private HitMan( int port, String command ) throws Exception {
		killer	= new ProcessControl( command );
		Runtime.getRuntime().addShutdownHook( shutDown );
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("Could not listen on port: " + port, e);
		}
		scheduleKilling();
		killer.startProcess();
	}

	// schedule( expiry ).periodic( 15, seconds ).withInitialDelay( 15, seconds );
	interface ExpirePolicy {
		public boolean expired();
	}
	private void scheduleKilling() throws Exception {
		int delay = 15 * ticksPerSecond;
		int period = 15 * ticksPerSecond;;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
		        public void run() {
		            if ( System.currentTimeMillis() <= deadLine )
		            	return;
		        	synchronized ( synchLock ) {
			            if ( System.currentTimeMillis() <= deadLine )
			            	return;
		            	deadLine	= FAR_FUTURE;
		        	}
		        	killer.restartProcess();
		        }
		    }, delay, period);
	}

	/** Sets deadline if appropriate */
	private void handleMessage( String inputLine ) {
		if ( StringUtils.isBlank( inputLine ) )
			return;
		if ( inputLine.startsWith( "HIT ME IN " ) ) {
			synchronized ( synchLock ) {
				// block is wide because we want to spare client that talks to us
				String nSeconds	= inputLine.replace( "HIT ME IN ", "" );
				int seconds2wait	= Integer.parseInt( nSeconds );
				deadLine	= System.currentTimeMillis() + ( seconds2wait * ticksPerSecond );
			}
		}
	}

	/**  */
	private void listenForMessages() throws IOException {
		try {
			boolean stopped = false;
			while ( ! stopped ) {
				Socket clientSocket = null;
				BufferedReader in = null;
				PrintWriter out = null;
				try {
					clientSocket = serverSocket.accept();
					in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						handleMessage(inputLine);
						/*
						if ( ! StringUtils.isBlank(outputLine) ) {
							out = new PrintWriter( clientSocket.getOutputStream(), true);
							out.println(outputLine);
							if (outputLine.equals("Bye."))
								stopped = true;
						}*/
					}
				} catch (IOException e) {
					System.err.println("Accept failed.");
					stopped = true;
				} finally {
					close( clientSocket );
					close( out );
					close( in );
				}
			}
		} finally {
			closeServer();
		}
	}

	private static void close(Closeable s) {
		try {
			if (s != null)
				s.close();
		} catch (IOException ioe) {
		}
	}

	private static void close(Socket s) {
		try {
			if (s != null)
				s.close();
		} catch (IOException ioe) {
		}
	}

	private void closeServer() {
		try {
			if ( serverSocket != null)
				serverSocket.close();
		} catch (IOException ioe) {
		}
	}

}
