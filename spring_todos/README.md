<!-- PROJECT LOGO -->
<p align="right">
<a href="https://www.marcusbornman.com">
<img src="https://raw.githubusercontent.com/marcus-bornman/todos/master/spring_todos/assets/project_badge.png" height="100" alt="Marcus Bornman">
</a>
</p>
<p align="center">
<img src="https://raw.githubusercontent.com/marcus-bornman/todos/master/spring_todos/assets/project_logo.png" height="200" alt="Todos" />
</p>

<!-- PROJECT SHIELDS -->
<p align="center">
<a href="https://github.com/marcus-bornman/todos/actions?query=workflow%3Abuild-spring-todos"><img src="https://img.shields.io/github/workflow/status/marcus-bornman/todos/build-spring-todos?label=build" alt="build"></a>
<a href="https://github.com/marcus-bornman/todos/issues"><img src="https://img.shields.io/github/issues/marcus-bornman/todos" alt="issues"></a>
<a href="https://github.com/marcus-bornman/todos/network"><img src="https://img.shields.io/github/forks/marcus-bornman/todos" alt="forks"></a>
<a href="https://github.com/marcus-bornman/todos/stargazers"><img src="https://img.shields.io/github/stars/marcus-bornman/todos" alt="stars"></a>
<a href="https://google.github.io/styleguide/javaguide.html"><img src="https://img.shields.io/badge/style-google_java-40c4ff.svg" alt="style"></a>
<a href="https://github.com/marcus-bornman/todos/blob/master/LICENSE"><img src="https://img.shields.io/github/license/marcus-bornman/todos" alt="license"></a>
</p>

---

<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)



<!-- ABOUT THE PROJECT -->
## About The Project
<p align="center">
<img src="https://raw.githubusercontent.com/marcus-bornman/todos/master/spring_todos/assets/screenshot_1.png" width="600" alt="Screenshot 1" />
</p>

This is an implementation of the [Todos Application](../README.md) using the Spring framework. The application consists
of a user interface built with Thymeleaf and an API. Both the user interface and API are protected with BASIC authentication; and,
data is persisted using a MySQL database.

### Built With
* [Spring](https://spring.io)
* [Apache Maven](https://maven.apache.org)
* [Thymeleaf](https://www.thymeleaf.org)
* [MySQL](https://www.mysql.com)


<!-- GETTING STARTED -->
## Getting Started
If this is your first Spring Application, you may want to see [A Guide on Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/).
To build this project you will need to have Java 15 and [Apache Maven](https://maven.apache.org) installed.
You can ensure that the project compiles by running the following command from this folder:
```shell script
mvn clean install
```

Next, the application will require access to a MySQL Database Instance. See the following to help you Get Started with a MySQL database:
* [Getting Started with MySQL](https://dev.mysql.com/doc/mysql-getting-started/en/)
* [Accessing data with MySQL using Spring](https://spring.io/guides/gs/accessing-data-mysql/)

After you have completed all the steps above, you are ready to [run the application](#Usage).


<!-- USAGE EXAMPLES -->
## Usage
Importantly, the application requires a few environment variables to be set when running it. Run the application as follows:
```
mvn spring-boot:run 
-DDB_URL={The URL to your database instance}
-DDB_USERNAME={The username for your database user}
-DDB_PASSWORD={The password for your database user}
```

The application should now be up and running. Assuming your application is running on Port 8080, you can access the home
page at http://localhost:8080 and the API documentation is available at http://localhost:8080/api.


<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/marcus-bornman/todos/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
## Contributing

Unfortunately, this project is not open for any contributions. However, anyone is welcome to fork the repository.



<!-- LICENSE -->
## License

Distributed under the MIT License. See [LICENSE](../LICENSE) for more information.



<!-- CONTACT -->
## Contact

Marcus Bornman - [marcusbornman.com](https://www.marcusbornman.com) - [marcus.bornman@gmail.com](mailto:marcus.bornman@gmail.com)

Project Link: [https://github.com/marcus-bornman/todos](https://github.com/marcus-bornman/todos)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [Shields IO](https://shields.io)
* [Open Source Licenses](https://choosealicense.com)