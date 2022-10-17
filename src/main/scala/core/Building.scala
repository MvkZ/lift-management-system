package core

import models._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Try

class Building extends BuildingStructure with Runnable {

  override var lifts: List[Lift] = List[Lift]()
  override var floors: List[Floor] = List[Floor]()
  override var requests: mutable.Queue[Request] = mutable.Queue[Request]()

  /**
   * initializing the no of lifts for the building
   * @param no
   */
  override def initializeLifts(no: Int): Unit = {
    val tempLifts = ListBuffer[Lift]()
    for( x <- 1 to no ) {
      tempLifts.append(new Lift)
    }
    lifts = tempLifts.toList
    lifts.zipWithIndex.foreach( lift => {
      val floor = Floor(0)
      val state = State(State.closeState)
      val direction = new Direction(Direction.idle)
      lift._1.initialize(lift._2 + 1, floor, new LiftState(state, direction))
    })
    requests = new mutable.Queue[Request]()
  }

  /**
   * initializing the no of floors for the building
   * @param no
   */
  override def initializeFloors(no: Int): Unit = {
    val tempFloors = ListBuffer[Floor]()
    for ( floorNo <- 0 to no) {
      val floor = new Floor(floorNo)
      tempFloors += floor
    }
    floors = tempFloors.toList
  }

  /**
   * method to add requests into the request queue of building
   * @param source
   * @param destination
   */
  override def addRequest(source: Int, destination: Int): Unit = {
    val newRequest = Request(source, destination)
    requests.enqueue(newRequest)
  }

  /**
   * method to check if methods are available
   * @return
   */
  def requestsAvailable(): Boolean = {
    if(Try(requests.nonEmpty).getOrElse(false)) return true
    false
  }

  /**
   * This method is called in a separate thread with 1 sec sleep for processing the requests async
   * This is the main logic of the building to handle lifts
   * TODO: get the nearest lift to process the requests
   */
  override def workLifts(): Unit = {
    var check = requestsAvailable()
    if(check) {
      lifts.foreach(selectLift => {
        if (check) {
          val liftState = selectLift.liftState.liftState.status
          val liftDirection = selectLift.liftState.liftDirection.direction
          if (liftDirection == Direction.movingUp) {
            val currRequest = requests.front
            if (selectLift.currentFloor.number < currRequest.source && selectLift.currentFloor.number < currRequest.destination) {
              requests.dequeue()
              selectLift.upDestinations.enqueue(currRequest)
              return
            }
          }
          else if (liftDirection == Direction.movingDown) {
            val currRequest = requests.front
            if (selectLift.currentFloor.number > currRequest.source && selectLift.currentFloor.number > currRequest.destination) {
              requests.dequeue()
              selectLift.downDestinations.enqueue(currRequest)
              return
            }
          } else if (liftState == State.closeState && liftDirection == Direction.idle) {
            val currRequest = requests.front
            if (selectLift.currentFloor.number < currRequest.destination) {
              requests.dequeue()
              selectLift.upDestinations.enqueue(currRequest)
              return
            } else {
              requests.dequeue()
              selectLift.downDestinations.enqueue(currRequest)
              return
            }
          }
          check = requestsAvailable()
        }
      })
    }
  }

  /**
   * overridden run method
   */
  override def run(): Unit = {
    while (true) {
      workLifts()
      Thread.sleep(1000)
//      println(requests)
    }
  }
}
