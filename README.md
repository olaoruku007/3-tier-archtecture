# 3-tier-archtecture

## zthapp Web Application connecting to database through user-servlet

### Introduction

This README.md will guide you through setting up, building, and running your Maven-based Java project. Maven is a powerful build automation tool for Java projects. It helps manage dependencies, compile code, run tests, and package applications.
Prerequisites
Before you begin, ensure you have the following installed:
Java Development Kit (JDK): Version 8 or later.
Apache Maven: Ensure Maven is installed and accessible from your command line. You can verify your installation by running mvn -v in your terminal.

### Project Setup

### 1. Create a project directory
First, create a new directory for your project. This will be the root of your Maven project.

```bash
mkdir zthapp
cd zthapp
```

### 2. Initialize the Maven project (using Archetype)
You can use the Maven Archetype plugin to generate a basic project structure. This creates a skeleton project, according to DZone.

```bash
 mvn archetype:generate -DgroupId=com.zthcloud.app -DartifactId=zthapp \
 -DarchetypeArtifactId=maven-archetype-webapp \
 -DarchetypeVersion=4.0.0 -DinteractiveMode=false
```

```
 -DgroupId: Your project's unique identifier (e.g., in reverse domain name format).
 -DartifactId: The name of your Maven project.
 -DarchetypeArtifactId=maven-archetype-webapp: Specifies the archetype (template) to use. maven-archetype-webapp creates a simple WAR-based project.
 -DarchetypeVersion=4.0.0: Specifies the version of the archetype to use.
 -DinteractiveMode=false: Skips the interactive prompts for project details.
```

### 3. Understanding the Project Structure
After the archetype generation, your project directory should look like this:

```
.
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── zthapp
        │           └── UserServlet.java
        ├── resources
        └── webapp
            ├── index.jsp
            └── WEB-INF
                └── web.xml
```

#### pom.xml: The Project Object Model file, central to Maven projects. It describes the project, its dependencies, and how it should be built.
#### src/main/java: Contains your application's source code.
#### src/test/java: Contains your unit tests.
#### target (created after build) :Houses the output of the build, including compiled classes and the packaged application artifact. This directory should be added to your version control ignore list (e.g., .gitignore).   

### Building the Project

### 1. Clean and install
Use the mvn clean install command to clean the project (remove previously compiled artifacts), compile the source code, run tests, and install the resulting artifact (e.g., JAR file) into your local Maven repository.

```bash
mvn clean install
```

### 2. Package the application
To package your application into a distributable format (e.g., JAR file), use the mvn package command.

```bash
mvn package
```

### This will create the JAR file in the target directory.

### Running Tests
To run the unit tests, use the mvn test command. Maven uses the Surefire plugin to execute unit tests during the test phase.

```bash
mvn test
```

### Running the Application
After building the project, you can run the application. If your application is packaged as a JAR, you can execute it with the java -jar command from the target directory.

```bash
cd target
java -jar zthapp-1.0-SNAPSHOT.war
```


### Note: Replace my-java-app-1.0-SNAPSHOT.jar with the actual name of your generated JAR file.

### Conclusion
This README.md provides a basic guide to setting up, building, and running a Maven-based Java project. For more advanced features like dependency management, plugins, and multi-module projects, refer to the official Maven documentation Apache Maven.



