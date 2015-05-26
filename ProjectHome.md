# Description of Modules #

## The Invitation Module ##
The invitation module displays a simple UI to the user and allows them to view, create, and vote on invitations.

### class Invitation ###
The invitation class contains all relevant information for a single invitation, including the id, the invited users, and the options that users can vote on. The static fromInvitationBean method can create an invitation from an InvitationBean object sent to the server, allowing the server to reconstruct an invitation without having to parse the information contained in the bean.

The invitation class also contains a method toInvitationBean, which creates an InvitationBean object that the server can then send to other users. And an addVote method, which takes a VoteBean as a parameter and adds the voting information contained in the VoteBean to the invitation.