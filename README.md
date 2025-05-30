## Client and server
	•	Server: A multithreaded Java server that handles client requests like time, file operations, and system commands.
	•	Client: A Java desktop app that connects to the server to send commands and receive responses.




 ## The commands that should be used by client for which the server will give response
 
# GET:TIME                                                
Get current time (e.g., 2025-05-29 14:33:00)
# GET:DATE               
Get current date (e.g., 2025-05-29)
# GET:TIMESTAMP                                            
Get current system timestamp in milliseconds
# GET:FILE:<filename>                                      
Read contents of the specified file
# GET:FILE_LIST                                            
List all files in the current directory
# UPLOAD:FILE:<filename>:<file_content>                    
Upload and save content to a new file
# DELETE:FILE:<filename>                                  
 Delete a specific file
# GET:CLIENTS                                             
 Get total number of connected clients
# COMMAND:SAY_HELLO                                       
 Server responds with a hello message
# COMMAND:SHUTDOWN_SERVER                                 
 Shuts down the server application
# COMMAND:LOGOUT                                          
 Disconnects the client from server
# COMMAND:ECHO:<message>                                  
 Server echoes back the provided message
# COMMAND:TIME_DIFF:<yyyy-MM-dd>                          
 Returns number of days since given date


## Note for using java disktop application
To use the Java desktop application given in this repository, you have to install the JDK and the other packages needed for Java development and execution. Make sure Java is properly installed and configured on your system, whether you’re using Mac or Windows. The application is packaged as a JAR file, so to run it, you must have Java associated with .jar files or use the terminal or command prompt with the java -jar filename.jar command. The JAR must include the correct folder structure and a manifest file specifying the Main-Class to ensure it runs properly. On Mac, you can install Java using Homebrew or directly from Oracle, and on Windows, you can use the official JDK installer. Also, ensure that your system allows Java applications to run and that no firewall or security settings block the application, especially if it connects to a server.