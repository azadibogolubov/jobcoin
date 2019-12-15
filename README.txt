Language: Java
Local Compiler JDK Version: 11
Source/Target Compatibility: Java 8
Build System: Apache Maven

Assumptions:
- The deposit address and house address are addresses as shown in the UI like Bob and Alice.

Issues:
- This only takes into account one user who has 3 source addresses, though with a small tweak, could easily accept N source addresses.
- This only takes into account the one user listed above, but could easily be scaled to accept multiple users, and an idea I had for this might be to break out the steps into a Spring MVC project where you can POST the information for each, then call a /mixer endpoint to mix up everything in the deposit account for the multiple users and pass back out, rather than ending on a single transaction.

Unit tests:
- Not entirely sure how I would go about testing this app, as it is using Spring CommandLineRunner, but wrote a couple sample tests of how I might get started.

To run:
mvn clean install