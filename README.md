# ocala
seeking original Scala practices

------

0. Always pack code as a package, that is, always begin codes with things like:

```scala
    package main
```

1. Use `Long` instead of `Int`, use `Double` instead of `Float`, use `null` for `no op`.


2. Always declare data type and initial value for simple data type standalone `var/val`:

```scala
    val x :Long = 5
    var y :String = "hi!"
```


3. Read/write/print from explicit source, including `Console`:

```scala
    y = Console.readLine
    Console.println(y+": "+x)
```


4. Define named function through anonymous function with all types declared:

```scala
    val triple = (x:Long) => 3*x :Double
    val fn = (x:Long) =>  {x match { case 0 => 0; case _ => "OK!";}} :Any
```


5. Use `match` with `case _` for the branched structure:

```scala
    Console.println( x%2 match { case 0 => "even"; case 1 => "odd!"; case _ => "WTF!";})
```


6. Use `foreach`, `map`, `filter`, `reduce` etc for loop structures:

```scala
    List(1, 4, 22, 7, 9, 3, 12) filter(x => x>8) foreach(Console.println)
```
