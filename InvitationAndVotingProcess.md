# Introduction #

This page describes some of the functions in the Invitation and Bean classes and how they might be used on the server side.

# Procedure #

## Step 1: Server receives a new invitation from the client ##
The invitation will be in the form of a Vector containing <INIT, InvitationBean> as per the protocol. The InvitationBean has getSender and getInviteList methods that can retrieve the number of the person who created the invitation and the numbers of the people that are invited. And the setId method can assign an id number to the invitation.

The timeout value for the invitation can be accessed using the InvitationBean's getTimeout method. If this method returns a number less than 0, it means that the user who created the invitation did not specify a timeout and that a default value should be used.

The InvitationBean can then be sent directly to the clients that are invited, and those clients will be able to reconstruct the original invitation.

Also, the Invitation class contains a static method Invitation.fromInvitationBean that takes an InvitationBean as its only parameter. On the server side, you can use this to create an Invitation object that can then be stored.

## Step 2: Voting ##

Whenever a user votes on an invitation, the client will send a vector containing <VOTE, VoteBean> to the server. The VoteBean has a getWhichInvite method that returns the id of the invitation that the user is voting on.

Once the server knows which Invitation the vote goes to, it can call that invitation's addVote method and pass in the VoteBean as a parameter, and the Invitation object will take care of updating itself.

## Step 3: Timeout ##

Once the timeout has passed on an Invitation the server can create a new VoteBean by calling that Invitation's getVotingInfo() method. This method returns a VoteBean that can then be sent out to all of the invited users.