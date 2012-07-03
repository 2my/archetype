package no.antares.kickstart.app.hitman;

import org.apache.commons.lang.Validate;

public class ProcessControl extends Thread {

	Process process	= null;
	final String execStr;

	public ProcessControl( String execStr ) {
		Validate.notNull( execStr, "ProcessControl( null )" );
		this.execStr = execStr;
	}
	public void startProcess() {
		try {
			process = Runtime.getRuntime().exec( execStr );  
		} catch ( Exception e ) {
			throw new RuntimeException( "Error starting process: " + execStr, e);
		}
	}
	public void stopProcess() {
		if ( process != null )
			process.destroy();
    	process	= null;
    }
	public void restartProcess() {
		stopProcess();
		startProcess();  
	}
}