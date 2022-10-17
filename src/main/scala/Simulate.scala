import core.Building

import java.util.Scanner
import scala.collection.mutable.ListBuffer

/**
 * main object to run the Lift by creating a building with plaza
 * accepts Lifts and Floors as inputs at first
 * Once initialized,
 * Input can be given in pairs space separated (e.g 1 4) at any time
 */
object Simulate {

  def main(args: Array[String]) {
    val plaza = new Building
    val scanner = new Scanner(System.in)

    print("No of Lifts: ")
    val liftInput = scala.io.StdIn.readInt()
    plaza.initializeLifts(liftInput)
    print("No of Floors: ")
    val floorInput = scala.io.StdIn.readInt()
    plaza.initializeFloors(floorInput)

    val liftThread = ListBuffer[Thread]()

    plaza.lifts.foreach( x => {
      liftThread.append(new Thread(x))
    })

    liftThread.toList.foreach( x => {
      x.start()
    })

    val newThread = new Thread(plaza)
    newThread.start()

    val showStatus = new ShowStatus(plaza)

    val printThread = new Thread(showStatus)
    printThread.start()

    while (true) {
      val source = scanner.nextInt()
      val destination = scanner.nextInt()
      plaza.addRequest(source, destination)
    }

  }
}
