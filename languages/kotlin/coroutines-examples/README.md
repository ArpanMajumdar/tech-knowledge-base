# Coroutines

## Advantages of coroutines

- Threads are resource intensive. Coroutines are lightweight threads.
- They use thread pools.
- Simplify async code, callbacks and synchronization.
- Simple syntax

## Basic concepts

1. **Scope** - Create and run coroutines, provides lifecycle events (`join()`, `cancel()`, etc.)
2. **Context** - Scope provides a context in which the coroutine runs.
3. **Suspending functions** - Functions that can be run in a coroutine (and can be suspended).
4. **Job** - A handle on a coroutine
5. **Deferred** - A future result of a coroutine
6. **Dispatcher** - Manages in which threads coroutines runs on
7. **Error handling**

## Scope

- Provides lifecycle methods for coroutines
- Allows us to start and stop coroutines

There are 3 methods for creating coroutines
1. `GlobalScope.launch()` - Launches a coroutine in the global scope(i.e. scope of entire application).
2. `runBlocking{...}` - Creates a scope and runs in a blocking way
3. `coroutinesScope{...}` - Creates a new scope. Does not complete until all children coroutines are complete.

**Global scope**
```kotlin
GlobalScope.launch {
    delay(500L)
    println("Task from global scope")
}
```

**Run blocking**
```kotlin
runBlocking {
    launch {
        delay(1000L)
        println("Task from local scope")
    }
}
```

**Coroutine scope**
```kotlin
coroutineScope {
    launch {
        delay(1500L)
        println("Task from new coroutine scope")
    }
}
```

## Context

- A context is a set of data that relates to a coroutine.
- All coroutines have an associated context.
- Important elements of a context
    - **Dispatcher** - Decides which thread(or thread pool) the coroutine runs on
    - **Job** - Handle on the coroutine lifecycle
    
## Jobs

- A `launch()` call returns a job
- Job allows us to manipulate coroutine lifecycle
- They live in the hierarchy of other jobs both as parent and children
- Can access lifecycle variables and methods like `cancel()` and `join()`
- If a job is canceled, its parents and children will be canceled too
    
## Dispatchers

- A dispatcher determines which thread or thread pool the coroutine runs on.
- Different dispatchers are available based on the task specificity.
- These are the common dispatcher types
    1. **Main** - Main thread update in UI driven applications. It needs to be defined in gradle.
    2. **Default** - Useful for CPU intensive work.
    3. **IO** - Useful for network communication or reading or writing files.
    4. **Unconfined** - Starts the coroutine in the inherited dispatcher that called it.
    5. **newSingleThreadContext("myThread")** - Forces creation of a new thread. Should not be used unless it is necessary.
    
## async/await

- Just like launch, except it returns a result in the form of `Deferred`.
- Deferred is a future promise of a returned value
- When we actually need the value, we call `await()` (blocking call) on deferred response
    - If value is available, it will return immediately
    - If value is not available, it will pause the thread until it is available

## Exception handling

Exception behaviour depends on the coroutine builder

1. `launch`
    - Propagates through the parent child hierarchy
    - The exception will be thrown immediately ,and the jobs will fail
    - Use try catch or use an exception handler
    
2. `async`
    - Exceptions are deferred until the result is consumed
    - If result is not consumed, exception is never thrown
    - `try-catch` in the coroutine or in the `await()` call
    
