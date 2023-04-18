Welcome to the StoreApplication! 
=================================
--------------------------------

StoreApplication is a simple Java-based e-commerce web application that allows users to browse, search,
and purchase products from various producers. It provides a range of services for managing customers,
producers, products, and purchases, as well as tools for generating and reloading test data.

Technologies
------------------
The project uses Java 18, Spring Framework, and PostgreSQL. It is built and managed with Gradle, and utilizes 
a number of third-party libraries and frameworks, such as Lombok, Mockito, and Logback with Sfl4j.

Project Structure
-----------------
The main codebase is located in the src/main/java directory, which contains several packages for different
layers and components of the application. Here are the most important ones:

- business - the service layer, which provides the main business logic and operations of the application.
- domain - the domain layer, which defines the core entities and value objects of the application, such as
Customer, Producer, Product, and Purchase.
- infrastructure.database - the infrastructure layer, which implements the persistence and retrieval of data
from the PostgreSQL database using Spring Data JPA repositories and mappers.

Running the Application
------------------------
To run the application locally, you will need to have PostgreSQL installed and configured on your machine.
You will also need to set up a database and modify the application.properties file to match your database credentials.

Once you have the prerequisites set up, you can start the application by running the following Gradle command in the root directory:

  ./gradlew bootRun
 
 Testing and Development
 ------------------------
The project contains a number of tests that can be run using the Gradle command:

    ./gradlew test

The RandomDataPreparationService and ReloadDataService classes can be used to generate and reload test data respectively.
The ShoppingCartService class provides a simple example of how to use the application's services in a more complex scenario.


Contributing
-------------
We welcome contributions to this project. If you would like to contribute, please fork the repository, make your changes,
and submit a pull request with a clear description of the changes you have made.
