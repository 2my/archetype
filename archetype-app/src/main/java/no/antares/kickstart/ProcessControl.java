package no.antares.kickstart;


/**
 * @author Tommy Skodje
 */
public class ProcessControl {

	/**	Start and stop a program / process */
	public static void startAndStop() throws Exception {
		String execStr = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";  
		Process proc = Runtime.getRuntime().exec(execStr);  
		System.out.println("proc: " + proc);  
		Thread.sleep(10000);  
		System.out.println("destroying");  
		proc.destroy();  
		System.out.println("destroyed");  
	}  

	public static void main(String[] args) throws Exception { 
		startAndStop();
	}  

}
