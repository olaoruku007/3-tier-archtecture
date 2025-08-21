# 3-tier-archtecture Web application on Rocky Linux OS 9.6 

## Step 1 – Prepare the VMs
**Create 3 Rocky Linux 9.6 VMs:**

* **VM1: web01 (Websever running Nginx)**

* **VM2: app01 (App-server- running tomcat 10)**

* **VM3: db01 (Database Engine running MySQL/MariaDB)**

## Assign static private IPs (so they can talk to each other). Example:

web01 → 192.168.10.x

app01 → 192.168.10.x

db01 → 192.168.10.x

## Update all servers:

```bash
sudo dnf update -y
sudo dnf install -y epel-release vim git curl wget
```

## Servers Configuration Setup:

## Step 2 – Setup the Database Server (VM3:db01)

* **1. Install Mysql-server or MariaDB server**

  ```bash
  sudo dnf install -y mariadb-server
  sudo systemctl enable --now mariadb
 ```

* **2. Run secure installation:**

     * Set root password
     * Remove anonymous users
     * Disallow remote root login
     * Reload privileges

* **3. Create application(app) database & user:**


```bash
sudo mysql -u root -p

CREATE DATABASE appdb;
CREATE USER 'appuser'@'192.168.10.%' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL PRIVILEGES ON appdb.* TO 'appuser'@'192.168.10.%';
FLUSH PRIVILEGES;
EXIT;
```

* **4. Allow remote connections:**
    ```bash
     sudo vi /etc/my.cnf.d/mariadb-server.cnf
    ```
   **change:**
  ```bash
    bind-address = 0.0.0.0
  ```
  **Restart mariadb:**

```bash
  sudo systemctl restart mariadb
```

* **5. Open firewall for Mariadb:**

  ```bash
  sudo firewall-cmd --permanent --add-service=mysql
  or
  sudo firewall-cmd --permanent --add-port=3306/tcp
  sudo firewall-cmd --reload
  ```

##  Step 3 – Setup the Application Server (VM2:App01) 

 * **3.1 Install Java**
   
   ```bash
   sudo dnf install -y java-17-openjdk java-17-openjdk-devel
  ```
 * **3.2 Download & Install Tomcat(App Server)**

```bash
cd /opt
sudo curl -O https://downloads.apache.org/tomcat/tomcat-10/v10.1.30/bin/apache-tomcat-10.1.30.tar.gz
sudo tar xvf apache-tomcat-10.1.30.tar.gz
sudo mv apache-tomcat-10.1.30 tomcat
sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
sudo chown -R tomcat:tomcat /opt/tomcat
```

* **3.3 Create systemd Service**

  ```bash
  sudo vi /etc/systemd/system/tomcat.service
  ```
**Add:**

```bash
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/jre"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_BASE=/opt/tomcat"
Environment="CATALINA_PID=/opt/tomcat/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"
Environment="JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

Restart=on-failure

[Install]
WantedBy=multi-user.target
```

**Start and enable Tomcat:**

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now tomcat
```

* **3.4 Deploy a Sample Java App**
 
 ```bash
 sudo dnf install -y maven
```

# Steps to create a demoapp called "luxe"  

## zthapp Web Application connecting to database through user-servlet

### Introduction

This README.md will guide you through setting up, building, and running your Maven-based Java project. Maven is a powerful build automation tool for Java projects. It helps manage dependencies, compile code, run tests, and package applications.
Prerequisites
Before you begin, ensure you have the following installed:
Java Development Kit (JDK): Version 8 or later.
Apache Maven: Ensure Maven is installed and accessible from your command line. You can verify your installation by running mvn -v in your terminal.

A Maven project adheres to a standard directory layout to ensure consistency and ease of understanding for developers. This structure helps Maven manage the build process, compile code, run tests, and package artifacts efficiently.



### Project Setup

### 1. Create a project directory
First, create a new directory for your project. This will be the root of your Maven project.

```bash
mkdir luxe
cd luxe
```

### 2. Initialize the Maven project (using Archetype)
You can use the Maven Archetype plugin to generate a basic project structure. This creates a skeleton project, according to DZone.

```bash
 mvn archetype:generate -DgroupId=com.zthcloud.app -DartifactId=luxe \
 -DarchetypeArtifactId=maven-archetype-webapp \
 -DinteractiveMode=false
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
        │       └── luxe
        │           └── UserServlet.java
        ├── resources
        └── webapp
            ├── index.jsp
            └── WEB-INF
                └── web.xml
```


## The core components of a standard Maven project structure are:
### Root Directory:
This is the main project folder, typically named after the project's artifact ID. It contains the **pom.xml** file and the **src** and **target** directories.

### pom.xml:
The Project Object Model (POM) file is the central configuration file for a Maven project. It defines project metadata, dependencies, build plugins, and other essential information.

### src Directory:

This directory contains all source material for the project. It is further divided into:

* **src/main:** Contains the main application source code and resources.
    * **src/main/java:** Holds the Java source code for the main application. Package structure follows standard Java conventions (e.g., **src/main/java/com/zthcloud/luxe**).
    * **src/main/resources:** Stores non-Java resource files used by the application, such as configuration files, property files, or XML files.
    * **src/main/webapp (for WAR projects):** Contains web application specific files like HTML, CSS, JavaScript, and JSPs.
    * **src/test:** Contains the test source code and resources.
    * **src/test/java:** Holds the Java source code for unit tests.
    * **src/test/resources:** Stores resource files used during testing, such as test data or test-specific configuration.

* **target Directory:**
This directory is generated by Maven during the build process and houses all output files, including compiled classes, test classes, and packaged artifacts **(e.g., JAR, WAR, or EAR files)**.
This standardized structure simplifies project navigation, promotes collaboration, and allows Maven to apply its conventions for building and managing projects effectively.


     ### Further Expalanation
   * **pom.xml:** The Project Object Model file, central to Maven projects. It describes the project, its dependencies, and how it should be built.
   * **src/main/java:** Contains your application's source code.
   * **src/test/java:** Contains your unit tests.
     
   * **target (created after build) :** Houses the output of the build, including compiled classes and the packaged application artifact. This directory should be added to your version control ignore list (e.g.,
        .gitignore).   

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



