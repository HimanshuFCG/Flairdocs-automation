# Maven Quickstart Project

This is a standard Maven quickstart archetype project created manually.

## Project Structure

```
my-app/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── App.java
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── AppTest.java
└── README.md
```

## Building the Project

To build the project, you need Maven installed. If you don't have Maven installed, you can:

1. Download Maven from https://maven.apache.org/download.cgi
2. Extract it to a directory
3. Add the Maven bin directory to your PATH environment variable

Once Maven is installed, you can run:

```bash
mvn compile
```

## Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Running Tests

```bash
mvn test
```

## Package the Application

```bash
mvn package
```

This will create a JAR file in the `target` directory.

## Clean the Project

```bash
mvn clean
```

This will remove the `target` directory and all compiled files. 