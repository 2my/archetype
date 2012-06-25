package no.antares.kickstart;


/**
 * mvn exec:java -Dexec.mainClass="no.antares.kickstart.ProcessControl"
 * @author Tommy Skodje
 */
public class ProcessControl {

	/**	Start a program / process */
	public static Runnable startProcess() throws Exception {
		String execStr = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";  
		final Process proc = Runtime.getRuntime().exec(execStr);  
		System.out.println("proc: " + proc);
		return new Runnable() {
			@Override public void run() {
				proc.destroy();  
				System.out.println("destroyed");  
			}
		};
	}

	/**	Start and stop a program / process */
	public static void main(String[] args) throws Exception { 
		final Runnable killer	= startProcess();
		Runtime.getRuntime().addShutdownHook( new Thread( killer ) );
		Thread.sleep(10000);
	}  

}
