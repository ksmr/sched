# Sched

Sched is a small library to get and modify the CPU affinity mask of the current Java `Thread` under Linux.

## Download

Available on the Maven Central Repository. Add the following dependency to your `pom.xml`:

    <dependency>
      <groupId>net.ksmr</groupId>
      <artifactId>sched</artifactId>
      <version>0.1.1</version>
    </dependency>

**Note:** The prebuilt version will currently only work on x86_64 and ARM64 systems (`amd64` and `aarch64` architectures in Java). On other architectures the methods will throw `UnsupportedOperationException`. However, compiling the library on another architecture should make a JAR that supports the given architecture.

## Build requirements

To build (`mvn package` or `mvn install`) the library, you will need the following tools installed (in addition to a JDK and Maven):

* GNU Make
* GCC

You will also need to set the `JAVA_HOME` environment variable to the JDK directory when building.

## License

Copyright 2020 Kasimir Torri

Licensed under the Apache License, Version 2.0. See `LICENSE` for details.
