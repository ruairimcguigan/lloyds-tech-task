# MVI Clean Architecture Offline-First Scalable Tech Submission

This project demonstrates a slightly simplified, clean, scalable Android application architecture with a focus on offline-first functionality. It uses modern libraries and frameworks to ensure best practices and efficient development.

![](https://github.com/ruairimcguigan/lloyds-tech-task/blob/main/output.gif)

## Project Structure

The project is structured as a Gradle modularized project by features, promoting separation of concerns and scalability.


## Project configuration — Gradle modularization

The alternative approach, becoming more popular recently, is called “modularization by feature”

- **app** that everyone is familiar with and has access to all other modules,
- **core** that gathers all reusable components used by multiple features and doesn’t “see” other modules,
- **feature** that sits between these two modules in terms of dependencies (and like any other feature module).
  

## Technologies and Libraries

- **Architecture**: 
  - **Clean Architecture** with **MVI (Model-View-Intent)** pattern in the presentation layer.
- **UI**: 
  - **Jetpack Compose** with **Material3** design for the UI layer.
- **Concurrency and Reactive Programming**: 
  - **Kotlin Coroutines** and **Kotlin Flow**.
- **Serialization**:
  - **Kotlin Serialization** for JSON parsing.
- **Networking**: 
  - **Retrofit** for making network requests.
- **Dependency Injection**: 
  - **Hilt**.
- **Database**: 
  - **Room** for local database management.
- **Image Loading**: 
  - **Coil**.
- **Dependency Management**: 
  - **Version Catalog** for managing dependencies.
- **Logging**: 
  - **Timber** for logging.
- **Testing**: 
  - **JUnit5**, **Turbine**, and **MockK** for unit tests.
  - Jetpack Compose test dependencies and **Maestro** for UI tests.
- **Code Quality**: 
  - **KtLint** and **Detekt** for code linting.

## Features

- Offline-first approach: Data is always retrieved from the local database first and updated from the network when necessary.
- Modular architecture: Separate Gradle modules for different features to ensure clean architecture and maintainability.
- Modern UI: Built with Jetpack Compose and Material3 design principles.
- Theming: Supports automatic light and dark mode theming.
- Testing: Comprehensive unit and UI testing setup.
