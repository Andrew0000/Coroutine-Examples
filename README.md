Kotlin Coroutines have a complex cancellation and error handling system. Sometimes it's not obvious.  

Top examples:  
### Many people believe that the following code with `launch` will not crash because of `supervisorScope`
```
supervisorScope {
    launch {
        throw Exception()
    }
}
```
[It fails](https://github.com/Andrew0000/Coroutine-Examples/blob/main/app/src/test/java/com/coroutineexamples/SupervisorScopeTest.kt)

---

### Or this code with `async` because the result is never awaited
```
coroutineScope {
    async {
        throw Exception()
    }
}
```
[It fails](https://github.com/Andrew0000/Coroutine-Examples/blob/main/app/src/test/java/com/coroutineexamples/CoroutineScopeTest.kt)

---

### Use `runCatching` or simple `try/catch` for suspendable code
```
launch {
    runCatching {
        // Some suspendable code
    }
}
```
[It breaks the cancellation mechanism](https://github.com/Andrew0000/Coroutine-Examples/blob/main/app/src/test/java/com/coroutineexamples/CancellationTest.kt)

---

This repository is about such tricky cases.  

See all tests:  
https://github.com/Andrew0000/Coroutine-Examples/tree/main/app/src/test/java/com/coroutineexamples
