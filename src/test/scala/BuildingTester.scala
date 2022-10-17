import core.Building
import org.scalatest.funsuite.AnyFunSuite

/**
 * Tester class for building
 * TODO: to cover all the test cases for Building Class
 */
class BuildingTester extends AnyFunSuite {

  val testBuilding = new Building
  testBuilding.initializeLifts(2)
  testBuilding.initializeFloors(10)

  test("check for no of lifts") {
    testBuilding.lifts.size == 2
  }

  test("check for no of floor") {
    testBuilding.lifts.size == 10
  }

  testBuilding.addRequest(0, 7)
  testBuilding.addRequest(3, 9)
  testBuilding.addRequest(4, 6)

  test("check if request is present") {
    testBuilding.requestsAvailable()
  }

}
