# Brainstorm
Question and answer portal

## How to build
Ensure you have JDK 11 on your system. You can check the JDK version by executing the following 
command: `java -version`
````
> java -version
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.2+9)
Eclipse OpenJ9 VM AdoptOpenJDK (build openj9-0.12.1, JRE 11 Windows 10 amd64-64-Bit Compressed References 20190204_123 (JIT enabled, AOT enabled)
OpenJ9   - 90dd8cb40
OMR      - d2f4534b
JCL      - 289c70b684 based on jdk-11.0.2+9)
````
Note that the above result is produced by JDK built from AdoptOpenJDK.

Ensure you have Apache Maven 3.6.0 above cor compatibility with JDK 11 or you can use the provided
_maven wrapper_ in the source code.

To build the source code use the following command: 
````
mvn clean install
````

Or using _maven wrapper_:
````
mvnw clean install
````  