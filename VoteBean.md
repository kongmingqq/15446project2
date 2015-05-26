# VoteBean #

A VoteBean is sent by a user to the server whenever a user votes on an invitation. The server can then call the addVote method on the relevant Invitation, and the voting information contained in the VoteBean will be added.

A VoteBean is also sent from the server to all users once the invitation times out. For this, the Invitation class contains a method called getVotingInfo, which returns a VoteBean that can be sent to all the users.

## description of data ##

  * **int whichInvite** - identifier of the invitation that this vote goes to
  * **int[.md](.md) voters** - indicates the user who voted on this event
  * **String[.md](.md)[.md](.md) data** - data that allows the Invitation object to parse the voting information