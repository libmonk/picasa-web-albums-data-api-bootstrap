# picasa-web-albums-data-api-bootstrap
An example implementation in Java to get started with Picasa Web Albums Data API.

The official documentation of the Google Picasa Web Albums API, 
used currently for 
interacting with Google Photos is available at 
https://developers.google.com/picasa-web/ and 
https://developers.google.com/picasa-web/docs/3.0/developers_guide_java
 
But the official documentation does not really provide a complete picture of 
using the API and a new developer would struggle to get started. 
This demo implementation aims to assist a new developer without a 
familiarity to get started with using **Google Photos API** / 
**Picasa Web Albums Data API**.

Before getting started, it is preferable to understand that lot of the 
functionalities expected to be available have been supressed in the 
latest versions and thus API does not provide support for few fundamental 
features like creating / updating / deleting albums. 
Many more such limitations are listed at - 
https://developers.google.com/picasa-web/docs/3.0/releasenotes

To get started, it is first required to log in to 
https://console.developers.google.com/ and register your application and 
obtain the OAuth client id and secret which would be required later. 
From API console, it can be downloaded as a JSON file. Rename the file into 
client_secret.json and add it to `resources` directory.  

`com.google.api-client:google-api-client`, added as dependency brings in multiple 
transitive dependencies for its operations including but not limited to a 
HTTP client, HTTP server, JSON parser etc.

    <dependency>
        <groupId>com.google.api-client</groupId>
        <artifactId>google-api-client</artifactId>
        <version>1.23.0</version>
    </dependency> 

`com.google.gdata:core` defines service client objects for various Google APIs. 
More on this can be found here - 
https://developers.google.com/gdata/articles/java_client_lib
 
    <dependency>
        <groupId>com.google.gdata</groupId>
        <artifactId>core</artifactId>
        <version>1.47.1</version>
    </dependency>
    
By adding just the above dependencies, the code compiles successfully and executes without 
any error, but the desired output is not observed without adding 
`com.google.gdata.gdata-java-client:gdata-photos-meta-2.0`.

    <dependency>
        <groupId>com.google.gdata.gdata-java-client</groupId>
        <artifactId>gdata-photos-meta-2.0</artifactId>
        <version>1.47.1</version>
    </dependency>

It is unavailable in the Maven Central repository and thus, a new repository 
section is required to be added into the `pom.xml`. 
More on this - https://github.com/eburtsev/gdata-maven

    <repositories>
        <repository>
            <id>burtsev-net-maven</id>
            <name>Burtsev.Net Maven Repository</name>
            <url>http://maven.burtsev.net</url>
        </repository>
    </repositories> 

In order to enable logging, run the application with VM arugments -
 
    -Djava.util.logging.config.file=/path/to/logging.properties