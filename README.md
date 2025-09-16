Kotlin Coroutines have a complex error handling system. Sometimes it's not obvious.  
For example, many people believe that the following code will not crash because of `supervisorScope`:
```
supervisorScope {
    launch {
        throw Exception()
    }
}
```

This repository is about such cases.  
See tests:  
https://github.com/Andrew0000/Coroutine-Examples/tree/main/app/src/test/java/com/coroutineexamples
