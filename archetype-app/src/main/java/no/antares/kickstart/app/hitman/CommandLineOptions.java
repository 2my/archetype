package no.antares.kickstart.app.hitman;

import org.apache.commons.cli.*;

/**
 * @author tommy skodje
*/
class CommandLineOptions {
	static Option portArg = option("port", "port to bind to", "port");
	static Option commandArg = option("cmd", "command (process) to run", "command");
	static Option help = new Option("help", "print this message");
	static Option messageArg = option("msg", "message to send", "message");

	final int portNo;
	final String message;
	final String command;
	final Options options = new Options();

	protected CommandLineOptions( String[] args ) {
		options.addOption(messageArg);
		options.addOption(commandArg);
		options.addOption(portArg);
		options.addOption(help);

		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch ( ParseException pe ) {
			throw new RuntimeException( "Error parsing" , pe );
		}

		if ( cmd.hasOption( portArg.getOpt() ) )
			portNo = Integer.parseInt( cmd.getOptionValue( portArg.getOpt() ) );
		else
			portNo = -1;

		if ( cmd.hasOption( messageArg.getOpt()) )
			message = cmd.getOptionValue( messageArg.getOpt() );
		else
			message = null;

		if ( cmd.hasOption( commandArg.getOpt() ) )
			command = cmd.getOptionValue( commandArg.getOpt() );
		else
			command = null;
	}

	protected void printHelp( String startCommand ) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( startCommand, options);
	}

	@Override public String toString() {
		return "CommandLineOptions [portNo=" + portNo + ", signal=" + message + ", command=" + command + "]";
	}

	private static Option option( String name, String description, String argName ) {
		return OptionBuilder.withArgName(argName)
				.hasArg()
				.withDescription(description)
				.create(name);
	}

}
