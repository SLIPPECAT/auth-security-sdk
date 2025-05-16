
<div align="center">

# auth-security-sdk

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub Stars](https://img.shields.io/github/stars/SLIPPECAT/auth-security-sdk?style=social)](https://github.com/SLIPPECAT/auth-security-sdk/stargazers)

</div>

## Description

This repository contains an SDK for Java, specifically designed to simplify the integration of authentication and security features into Spring Security applications. The SDK provides a convenient way to manage user authentication, authorization, and JWT token handling.

## Features

- **Authentication:** Facilitates user authentication using usernames, passwords, and JWT tokens.
- **Authorization:** Enables role-based access control to secure application endpoints.
- **JWT Token Management:** Generates, validates, and extracts claims from JWT tokens.
- **Spring Boot Auto-Configuration:** Simplifies the integration process with sensible defaults and customizable properties.
- **User Management:** Includes user creation, retrieval, and permission handling functionalities.

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Installation

To integrate the SDK into your Spring Boot project, follow these steps:

1.  **Add the dependency to your `build.gradle` file:**

    ```gradle
    dependencies {
        implementation 'com.github.mimyo:security-sdk-spring-boot-starter:0.1.0-SNAPSHOT'
    }
    ```

2.  **Configure the GitHub Packages repository:**

    Add the following configuration to your `settings.gradle` or `build.gradle` file, ensuring to replace `{GITHUB_USERNAME}` and `{GITHUB_TOKEN}` with your actual GitHub credentials:

    ```gradle
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/SLIPPECAT/auth-security-sdk")
                credentials {
                    username = System.getenv("GITHUB_USERNAME")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
    ```
    or in  `gradle.properties`:
    ```properties
    # GitHub Packages configuration
    gpr.user={GITHUB USERNAME}
    gpr.key={GITHUB TOKEN}
    ```

## Usage

1.  **Enable the SDK in your Spring Boot application:**

    The SDK auto-configures itself when added as a dependency. To customize its behavior, you can modify the `security.sdk` properties in your `application.properties` or `application.yml` file.

2.  **Configuration Properties:**

    The following properties can be configured:

    ```yaml
    security:
      sdk:
        jwt:
          secret: your_secret_key # Replace with a strong, production-ready secret
          token-validity-in-seconds: 86400 # Token validity duration in seconds (default: 24 hours)
    ```

3.  **Using the Authentication Service:**

    Inject the `AuthenticationService` bean into your controllers or services to leverage its authentication, authorization, and JWT management capabilities.

    ```java
    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        private final AuthenticationService authenticationService;

        public AuthController(AuthenticationService authenticationService) {
            this.authenticationService = authenticationService;
        }

        // ... (Implement login, validation, and registration endpoints using the authenticationService)
    }
    ```

## Dependencies

The project relies on the following major dependencies and tools:

-   **Spring Boot Starter:** Simplifies building Spring-based applications.
-   **Spring Security:** Provides comprehensive security features.
-   **JJWT (JSON Web Token):** Facilitates JWT token generation and parsing.
-   **Jackson Databind:** Enables object-to-JSON serialization and deserialization.
-   **SLF4J:** Provides a simple logging facade.
-   **Gradle:** Used as the build automation tool.

## Contributing

We welcome contributions to improve the SDK. To contribute:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Implement your changes, ensuring proper testing.
4.  Submit a pull request with a clear description of your changes.

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

## Contact

For questions, feedback, or to contribute, please contact:

-   Junyeong: ryjun91@gmail.com
```