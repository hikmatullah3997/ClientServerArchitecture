## Client and server
	•	Server: A multithreaded Java server that handles client requests like time, file operations, and system commands.
	•	Client: A Java desktop app that connects to the server to send commands and receive responses.




 ## The commands that should be used by client for which the server will give response
 
 Commands                                                Description

GET:TIME                                                 Get current time (e.g., 2025-05-29 14:33:00)
GET:DATE                                                 Get current date (e.g., 2025-05-29)
GET:TIMESTAMP                                            Get current system timestamp in milliseconds
GET:FILE:<filename>                                      Read contents of the specified file
GET:FILE_LIST                                            List all files in the current directory
UPLOAD:FILE:<filename>:<file_content>                    Upload and save content to a new file
DELETE:FILE:<filename>                                   Delete a specific file
GET:CLIENTS                                              Get total number of connected clients
COMMAND:SAY_HELLO                                        Server responds with a hello message
COMMAND:SHUTDOWN_SERVER                                  Shuts down the server application
COMMAND:LOGOUT                                           Disconnects the client from server
COMMAND:ECHO:<message>                                   Server echoes back the provided message
COMMAND:TIME_DIFF:<yyyy-MM-dd>                           Returns number of days since given date
