#include <connectionHandler.h>




/**
 * this is the Task that handles the reading from the socket (i.e getting the data from the server)
 * this Task will run in a new Thread using boost
 */
class Task {
private: ConnectionHandler* Handler;
    int* shutdown;
public:
    Task (ConnectionHandler* handle , int* SD) : Handler(handle),shutdown(SD){};
    void operator()() {
        while (*shutdown == 0) {
            std::string answer;
            long len;

            if (Handler->getLine(answer)) {
                len = answer.length();
                answer.resize(len - 1);
                std::cout << answer <<std::endl;
                if (answer.compare("ACK signout succeeded") == 0){
                    *shutdown = 1;
                    std::cout << "Ready to exit. Press enter"<<std::endl;

                }
            }
        }
    }

};
/**
this client is based on the assumption that after each command, the client WAITS for an ACK or ERROR
 response from the server BEFORE sending another command
 */
int main (int argc, char *argv[]) {

    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);
    int* shutdown= new int(0);
    std:: string signout = "SIGNOUT";


    ConnectionHandler* connectionHandler = new ConnectionHandler(host, port);
    if (!connectionHandler->connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    // this is the other Thread that will handle reading the data from the server
    Task read(connectionHandler,shutdown);
    boost::thread readingThread(read);

    // this is the loop of the main thread, it is in charge of handling the keyboard input from the client
    while (*shutdown == 0) {

        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);

        if ( !connectionHandler->sendLine(line) && *shutdown!=2 ) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        //in the special case of SIGNOUT , enter readline mode again
        // if the SIGNOUT succeeded don't do anything with the readline
        // else send it to the connection handler again
        if (signout.compare(line) == 0){
            std::cin.getline(buf, bufsize);
            if(*shutdown == 0){
                std::string line(buf);
                connectionHandler->sendLine(line);
            }
        }

     }
    // waiting for the reading thread to finish its last iteration before closing the handler
    // delete the pointers to avoid memory
    readingThread.join();
    connectionHandler->close();
    delete(connectionHandler);
    delete(shutdown);

    return 0;
}

