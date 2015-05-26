# AggLocationBean #

The bean server sends to client during TRACE.

  * **long time** - each AggLocationBean has a timestamp such that it can be ordered
  * **List`<`[Location](Location.md)`>` locations** - a collection of location
  * **int invitationId** - the invitation id for this packet, (can the application support multiple invitations at the same time?)

Notice that when sending AggLocationBean, the server may need split the list into several beans if the list gets too large. These beans should have same timestamp such that the client can resemble them. For the scope of this project, it is OK to not implement this.