concurrency
===========

Concurrency client-server example

Client:
 - UI with an input field to specify the number of messages to be sent
 - A button to start sending the messages
 - A progress bar to monitor how many messages have been processed by the server
 - Upon completion, a text area with statistics (total time processing, average time for a message, min/max time for a message)
 - The client shouldn't wait for processing of one message to start processing another
 - The client should get responses from the server in the order in which they were sent
 
Server:
 - The server has 2 queues, one for even length words, one for odd length words
 - The server processes one word in word.lenght*100 ms
 - When the server has completed the processing of the word, it gets sent back to the client
 - The server shouldn't block incoming client requests

Additional points:
 - Version control
 - Documenting your code
 - Writing automated tests
 - Maven/Gradle project

