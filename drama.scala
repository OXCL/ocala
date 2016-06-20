// mw agent-stage model
// Refs http://www.scala-lang.org/old/node/242
//      http://www.ibm.com/developerworks/cn/java/j-jvmc5/index.html
//      http://www.ibm.com/developerworks/cn/java/j-jvmc6/index.html

// import scala.actors._  // Actor
// import scala.actors.Actor._  // actions
import scala.collection.immutable.{Map => _, Set => _, _}  // Import Vector, List, Range; excludes Map, Set
import scala.collection.mutable._  // ArrayBuffer, StringBuilder, HashMap or HashSet
import scala.collection.JavaConversions._  // Java ops
import scala.concurrent.duration._ // actor msg duration
import scala.io.Source._  // file ops
import scala.language._  // postfixOps
import scala.math._  // the math
import scala.reflect.runtime.universe._  // reflection
import scala.runtime._ // runtime things
import scala.sys.process._  // work with shell
import scala.util.continuations._  // cont...
import scala.util.control.Breaks._  // break loops
import scala.util.matching.Regex._  // regex
import scala.util.parsing._  // parsing toolbox
import scala.util.Random._  // random numbers
import scala.util.Sorting._  // quickSort etc
import akka.actor._ // use akka actor
import akka.event._
import akka.dispatch._
import akka.pattern._
import akka.util._



object drama extends App {
  case class Msg1(x: Int)
  case class Msg2(x: Int)
  case class Msg3()
  case class Msg4()
  case class Msg0(x: Int)
  case class Init()
  case class SetNext(agtArr: Array[Int])

  val stagent = ActorSystem("stage-agent-on-akka")
  val stg = stagent.actorOf(Props[Stage])
  val agt = for (i <- 0 until 3) yield stagent.actorOf(Props( new Agent(i) ))

  class Agent(id: Int) extends Actor {
    var nextAgt: Array[Int] = new Array[Int](3)
    var state: Int = 0
    var upper: Int = 10
    var lower: Int = -10
    // ...
    def receive() = {
      case Init =>
        state = 0
        println("Agent["+id+"] is ready. State: "+state)
      case Msg0(i) =>
        state = 0
        println("Agent["+id+"] is reset by Agent["+i+"]")
      case SetNext(agtArr) =>
        nextAgt = agtArr
        for (i <- nextAgt) println("Agent["+id+"] has next Agent: "+i)
      case Msg3 =>
        state += 1
        if (state >= upper) {
            println("Agent["+id+"] hyperpolarized. State: "+state)
            upper += 10
            lower = -10
            stg ! Msg1(id)
            for (i <- nextAgt) agt(i) ! Msg0(id)
            }
      case Msg4 =>
        state -= 1
        if (state <= lower) {
            println("Agent["+id+"] depolarized. State: "+state)
            lower -= 10
            upper = 10
            stg ! Msg2(id)
            for (i <- nextAgt) agt(i) ! Msg0(id)
            }
    }
  }


  class Stage() extends Actor {
    def receive() = {
      case Init =>
        println("Stage is ready.")
      case Msg1(x) =>
        println("Checking Agent["+x+"] hyperpolarization.")
      case Msg2(x) =>
        println("Checking Agent["+x+"] depolarization.")
    }
  }


  // ---------------------

  agt(0) ! SetNext(Array(1, 2))
  agt(1) ! SetNext(Array(2))
  agt(2) ! SetNext(Array(0))

  for (i <- agt) i ! Init
  stg ! Init

  for (i <- 1 to 1000; j <- 0 until 3) {
    agt(j) ! (if (nextFloat >0.5) Msg3 else Msg4)
  }

  Thread sleep 100
  stagent shutdown
}
