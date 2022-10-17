import core.Lift
import models.{Direction, Floor, LiftState, Request, State}
import org.scalatest.funsuite.AnyFunSuite


/**
 * Tester class for Lift
 * TODO: to cover all test cases for Lift class
 */
class LiftTester extends AnyFunSuite {

  val testLift = new Lift
  testLift.initialize(1, Floor(0), LiftState(State(State.closeState), new Direction(Direction.idle)))

  test("Check if list is initialized") {
    testLift.id == 0
  }

  testLift.upDestinations.enqueue(Request(0, 3))

  test("check if upDestination is non empty") {
    testLift.upDestinations.nonEmpty
  }

  testLift.downDestinations.enqueue(Request(0, 3))

  test("check if dowmDestination is non empty") {
    testLift.downDestinations.nonEmpty
  }


}
