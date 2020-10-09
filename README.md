# Sched

Sched is a small library to get and modify the CPU affinity mask of the current Java `Thread` under Linux.

## Requirements

To build (`mvn package` or `mvn install`) the library, you will need the following tools installed (in addition to a JDK and Maven):

* GNU Make
* GCC

You will also need to set the `JAVA_HOME` environment variable to the JDK directory when building.

## License

Copyright 2020 Kasimir Torri

Licensed under the Apache License, Version 2.0. See `LICENSE` for details.
