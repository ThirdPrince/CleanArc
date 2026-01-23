# CleanArc

This project is a sample Android application to demonstrate a modern, clean architecture approach.

## Architecture

This project follows the principles of **Clean Architecture**. The codebase is separated into layers to achieve separation of concerns, making it more scalable, maintainable, and testable. The presentation layer uses the **Model-View-ViewModel (MVVM)** pattern.

The main layers are:
-   **Presentation (UI):** Built with Jetpack Compose. Responsible for displaying the UI and handling user interactions. `ViewModel`s are used to manage UI state and expose data to the Composables.
-   **Domain:** This layer contains the core business logic and use cases.
-   **Data:** Responsible for providing data to the application. It uses the **Repository Pattern** to abstract data sources (network or local).

## Tech Stack & Libraries

-   **[Kotlin](https://kotlinlang.org/)**: First-party and official programming language for Android development.
-   **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: Android’s modern toolkit for building native UI.
-   **[Koin](https://insert-koin.io/)**: A pragmatic and lightweight dependency injection framework for Kotlin.
-   **[Retrofit](https://square.github.io/retrofit/)**: A type-safe HTTP client for Android and Java.
-   **[Coil](https://coil-kt.github.io/coil/)**: An image loading library for Android backed by Kotlin Coroutines.
-   **[Android Architecture Components](https://developer.android.com/topic/libraries/architecture)**:
    -   **[Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)**: Manages UI component lifecycle events.
    -   **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)**: Stores UI-related data that isn't destroyed on UI component recreation.

## How to Build

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Let Gradle sync the dependencies.
4.  Build and run the application.
