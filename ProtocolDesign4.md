# Assumptions: #
  * Vector format: Vector**<Object**> sampleVector = new Vector();
Object contains two element: data type(String), data content(Java Bean)
  * C: client / S: server
  * The data type is retrieved from the Protocol class
  * The client would not change IP address

---

# Normal Case: (the type could be only one character) #
## Stage1 Initialization ##
Clients get to the initialization status. (e.g ID) from the server. Once the clients receive INIT message from the server, it proceeds to the voting stage.
  * C -> S: INIT, InvitationBean
  * S -> C: SMS message with party ID
  * C -> S: REQ, party ID
  * S -> C(all): INIT, InvitationBean
Server sends SMS to the client according to the phone number, client side application is called when it received the message.
## Stage2 Voting stage ##
After this stage finished, server start listening to the map message sent from the client. And when the client received the SMS, it starts communicating with server.
  * C -> S: VOTE, Invitation Bean
Server waits for 1. All the node finished voting. OR 2. Time out to start the voting stage.
  * S -> C: VOTE, Invitation
Server calculates the voting result and send the result back the all the clients.
## Stage3 Tracing stage ##
  * C -> S: TRACE:LOC, LocationBean
Each client sends its location info to the server.
  * S -> C (only when requested): TRACE:AGGLOC, AggLocationBean
Server sends back aggregated location info to client.
Notice that the beans in two ways are different, but the type name is the same.
