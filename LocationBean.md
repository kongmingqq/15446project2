# LocationBean #

The bean client sends to server during TRACE.

  * **long time** - each LocationBean has a timestamp such that it can be ordered
  * **[Location](Location.md) location** - the location data structure
  * **boolean sleepMode** - whether the client is in sleep mode or not, if in sleepmode, no response from server required