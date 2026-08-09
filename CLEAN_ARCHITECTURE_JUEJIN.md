# 2-3人小团队，单 Module 也能玩转整洁架构 (Clean Architecture) 实战

> **前言：** 很多开发者一提到 Clean Architecture，脑子里第一反应就是：**“那得拆多少个 Module 啊？”** 于是，为了强行“整洁”，2人小团队硬生生把项目拆成了 5-6 个 Module，结果每天在 Gradle 编译等待和跨模块修改代码中痛不欲生。
>
> **核心观点：** 对于小团队，**不拆 Module，在单 Module 内通过严整的包结构落地 Clean 架构，才是性价比最高的“实战派”方案。** 本文将以实战项目 [CleanArc](https://github.com/ThirdPrince/CleanArc) 为例，拆解这一套“省心”的架构方案。

---

## 一、 为什么小团队要拒绝“过度工程”？

在一个 2-3 人的 Android 团队里，拆分 Module 解决的是“编译隔离”和“协作边界”，但带来的代价是：
1. **开发成本激增**：改一个简单的功能要跨 3 个 Module，Debug 路径长到怀疑人生。
2. **配置复杂度**：Gradle 配置越来越重，版本冲突此起彼伏。

**实战哲学：** 你为未来可能（甚至可能永远不会）发生的规模扩张，支付了过高的当前复杂度。**我们的目标是：用最低的成本，实现最清晰的架构边界。**

---

## 二、 核心结构：以 Feature 为聚合，按 Package 做隔离

在 `app` 这个单 Module 内，我们建议采用 **“按业务功能（Feature）聚合 + 内部分层”** 的结构：

```text
app/src/main/java/com/sample/clean
├── presentation (表现层)
│   ├── components/  ← 纯 UI 组件 (Compose)
│   ├── state/       ← UI 状态定义 (UserUiState)
│   ├── MainActivity.kt
│   └── UserViewModel.kt
├── domain (领域层 - 最核心)
│   ├── usecase/     ← 业务流程 (GetUsersUseCase)
│   ├── repo/        ← 数据契约 (UserRepository 接口)
│   └── model/       ← 业务模型 (User)
├── data (数据层 - 实现细节)
│   ├── repo/        ← 接口实现 (UserRepositoryImpl)
│   ├── api/         ← 网络请求 (Retrofit 定义)
│   └── di/          ← 依赖注入 (Koin 模块)
```

**依赖原则：** `Presentation` → `Domain` ← `Data`。
所有依赖都指向中心的 `Domain`。这意味着 UI 和数据源（网络/数据库）的细节可以随意换，而核心业务逻辑稳如泰山。

---

## 三、 依赖倒置：Repository 到底放哪？

这是 Clean 架构最容易翻车的地方。

*   ✅ **正确做法**：接口在 `domain`，实现在 `data`。
*   ❌ **常见错误**：接口和实现都塞在 `data`。

**为什么？** 因为 `domain` 是“老板”，它定义了“我需要什么样的数据”；`data` 是“打工人”，负责“我怎么去拿这些数据”。

```kotlin
// domain 层：定义老板的“契约”
interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}

// data 层：打工人的具体执行细节
class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val userList = apiService.getUsers()
            Result.success(userList.toDomainList())
        } catch (e: Exception) {
            // 注意：这里有个关于协程取消的深坑，后文详述
            Result.failure(e)
        }
    }
}
```

---

## 四、 避坑指南：协程取消的“吞掉”危机

在实战中，我们经常会在 Repository 里加 `try-catch`。但如果你写成 `catch (e: Exception)` 却不处理 `CancellationException`，你的架构就出 BUG 了。

当用户按下“返回键”退出 UI 时，`viewModelScope` 会发出取消信号。如果你捕获了它却返回了 `Result.failure`，ViewModel 会认为网络真的出错了，从而弹出一个“加载失败”的提示。

**实战派处理方式：**

```kotlin
    } catch (e: CancellationException) {
        // 捕获到取消异常，必须重新抛出！
        // 让协程框架安静地停止，而不是把它当成一个“失败”反馈给 UI
        throw e 
    } catch (e: Exception) {
        Result.failure(e)
    }
```

---

## 五、 现代 Android 开发的“黄金搭档”

在这个项目中，我们集成了目前最推荐的技术栈：

### 1. 结构化并发与生命周期感知
在 `MainActivity` 中，放弃传统的 `collectAsState()`，改用 **`collectAsStateWithLifecycle()`**。
这确保了当 App 进入后台时，数据收集会自动停止，电量和 CPU 资源瞬间得到释放。

```kotlin
@Composable
fun UserListScreen(viewModel: UserViewModel = koinViewModel()) {
    // 最佳实践：生命周期感知的状态收集
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // ... 渲染 UI
}
```

### 2. 依赖注入的减法：Koin
相比 Hilt，Koin 的配置更轻量，更适合小团队。简单的 `modules` 定义即可完成层级间的解耦。

### 3. 版本控制：libs.versions.toml
使用 Version Catalog 让所有 module（即便以后要拆）共享同一套版本索引。

---

## 六、 总结：架构是为了省心，不是为了装 X

小团队的最优架构，不是最优雅的，而是**理解成本最低、改动最快**的。

*   **单 Module**：降低维护门槛。
*   **Feature 包分层**：预留拆分空间。
*   **Domain 核心化**：保证逻辑不乱。

当你的功能变多，需要拆分时，你只需要直接“搬运”对应的 Feature 目录即可，零重构成本。**能控制复杂度的架构，才是真正的好架构。**

---

> **作者：** [你的昵称]
> **代码实战地址：** [github.com/ThirdPrince/CleanArc](https://github.com/ThirdPrince/CleanArc)
