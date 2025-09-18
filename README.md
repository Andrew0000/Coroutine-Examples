Kotlin Coroutines have a complex cancellation and error handling system. Sometimes it's not obvious.  

Top examples:  
1. Many people believe that the following code will not crash because of `supervisorScope`
```
supervisorScope {
    launch {
        throw Exception()
    }
}
```
2. Use `runCatching` or simple `try/catch` for suspendable code
```
launch {
    runCatching {
        // Some suspendable code
    }
}
```

This repository is about such cases.  
See all tests:  
https://github.com/Andrew0000/Coroutine-Examples/tree/main/app/src/test/java/com/coroutineexamples
