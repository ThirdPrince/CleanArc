# 用一个小 Demo，带你入门安卓 Clean Architecture

## 前言

你是否曾经觉得自己的项目代码难以维护？业务逻辑和 UI 代码耦合在一起，改动一处，处处报错。如果是这样，那么 **Clean Architecture (整洁架构)** 就是你需要了解的利器。它不是一个具体的框架，而是一种设计思想，旨在通过分层来解耦代码，提高可测试性、可维护性和可扩展性。

本文将以一个简单的安卓项目 `CleanArc` 为例，带你一步步了解 Clean Architecture 在实践中是如何应用的。

## 什么是 Clean Architecture？

简单来说，Clean Architecture 将软件分为几个层次，每一层都有明确的职责。其核心原则是 **依赖倒置**：内层（业务逻辑）不应该依赖于外层（UI、数据库等实现细节）。

我们通常将其分为三层：

1.  **Presentation (表现层)**: 负责 UI 展示和用户交互。在安卓中，这通常是 `Activity`、`Fragment`、`Compose` 和 `ViewModel`。
2.  **Domain (领域层)**: 包含核心业务逻辑。这是最纯净的一层，不依赖任何安卓框架。它定义了业务对象 (`Model`) 和业务规则 (`UseCase`)。
3.  **Data (数据层)**: 负责数据的获取和存储。它实现了领域层定义的接口，但领域层并不知道数据的具体来源（是来自网络 API 还是本地数据库）。

![Clean Architecture Layers](https://user-images.githubusercontent.com/1249596/232757262-129681aa-9874-4530-9b37-8e771d9d2d6c.png)
*(图片来源: Fernando Cejas)*

## `CleanArc` 项目实践

让我们看看 `CleanArc` 项目是如何体现这些分层的。我们的目标很简单：从服务器获取一个用户列表并显示在屏幕上。

### 1. Data 层: 数据的来源

这一层负责“如何”获取数据。

-   **网络请求 (`ApiService.kt`)**: 我们使用 Retrofit 定义一个接口，用于从远端 API 获取数据。
-   **数据模型 (`ApiUser.kt`)**: 这是与 API 响应完全对应的数据类。
-   **仓库实现 (`UserRepositoryImpl.kt`)**: 这是核心。它实现了 `Domain` 层定义的 `UserRepository` 接口，并使用 `ApiService` 来真正地执行网络请求。它知道获取用户的 *具体方法*。

```kotlin
// com.sample.clean.data.repo.UserRepositoryImpl.kt
class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): List<User> {
        // 将 ApiUser 转换成领域层的 User 模型
        return apiService.getUsers().map { it.toDomainModel() }
    }
}
```

### 2. Domain 层: 核心业务逻辑

这一层只关心“做什么”，不关心“怎么做”。它是纯 Kotlin 模块。

-   **仓库接口 (`UserRepository.kt`)**: 它定义了一个契约：”我需要一个能获取用户列表的功能“。但它不关心这个列表是从网络、数据库还是内存中获取的。

```kotlin
// com.sample.clean.domain.repo.UserRepository.kt
interface UserRepository {
    suspend fun getUsers(): List<User>
}
```

-   **用例 (`GetUsersUseCase.kt`)**: 它封装了一个具体的业务操作。在这个例子中，就是“获取用户列表”。`UseCase` 的好处是让业务逻辑变得可复用且高度集中。

```kotlin
// com.sample.clean.domain.usecase.GetUsersUseCase.kt
class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<List<User>> {
        return try {
            Result.success(userRepository.getUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. Presentation 层: UI 展示

这一层负责将数据显示给用户。

-   **ViewModel (`UserViewModel.kt`)**: `ViewModel` 不直接与 `Repository` 交互，而是通过 `GetUsersUseCase`。这使得 `ViewModel` 的职责更单一：管理 UI 状态和响应用户事件。

```kotlin
// com.sample.clean.presentation.UserViewModel.kt
class UserViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {
    // ... 管理 UI State (UserUiState) 的逻辑 ...
}
```

-   **UI (`Composable` / `Activity`)**: 我们的 Composable 函数观察 `ViewModel` 中的 `UiState`，并根据状态（加载中、成功、失败）来渲染不同的界面。

### 4. 依赖注入: 将所有层粘合在一起

我们如何将 `UserRepository` (接口) 和 `UserRepositoryImpl` (实现) 联系起来？答案是 **依赖注入**。本项目使用了 `Koin`。

看看 `userModule` 是如何定义的：

```kotlin
// com.sample.clean.data.di.UserModule.kt
val userModule = module {

    // 当有人需要 UserRepository (Domain层) 时，提供 UserRepositoryImpl (Data层) 的实例
    factory<UserRepository> {
        UserRepositoryImpl(apiService = get())
    }

    // 为 GetUsersUseCase 提供它所需要的 UserRepository
    factory {
        GetUsersUseCase(userRepository = get())
    }

    // 为 UserViewModel 提供它所需要的 GetUsersUseCase
    viewModel {
        UserViewModel(getUsersUseCase = get())
    }
}
```

通过 Koin，我们完美地实现了依赖倒置：`Domain` 层定义的接口，由 `Data` 层来实现，并由 `Presentation` 层来消费，而它们之间没有直接的耦合。

## 总结

通过 `CleanArc` 这个小小的例子，我们看到了 Clean Architecture 的强大之处：

-   **关注点分离**: UI、业务逻辑、数据获取各司其职。
-   **可测试性**: 我们可以轻易地为 `UseCase` 和 `ViewModel` 编写单元测试，因为它们的依赖是接口，可以轻松地被 Mock。
-   **可维护性**: 当需要修改数据来源（比如从网络切换到数据库）时，我们只需要修改 `Data` 层的 `UserRepositoryImpl`，而 `Domain` 层和 `Presentation` 层基本不受影响。

希望这篇文章能帮助你迈出拥抱 Clean Architecture 的第一步！