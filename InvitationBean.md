# InvitationBean #

The InvitationBean is a class containing the information about an invitation. The bean is used for client-server communication, and either end can create an Invitation object from an InvitationBean and vice-versa. So far, InvitationBean contains the following data:

  * **int sender** - an int identifier representing the user who created the invitation
  * **int[.md](.md) inviteList** - an array of identifiers for the users who are invited
  * **String id** - a unique identifier for the invitation, which is the current system in milliseconds
  * **float timeout** - the amount of time before the invitation becomes active. If timeout < 0, then the user did not specify a timeout when the invitation was created, and a default value will be used instead. Default timeout is 120 seconds.
  * **boolean active** - true if this is an "active" event, false otherwise
  * **String[.md](.md) data** - additional data used by the Invitation class to reconstruct the invitation