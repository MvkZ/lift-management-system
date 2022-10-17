/****
 * author: Vignesh Kumar M
 * last edited: Oct 17 2022
 */

package core

import models.{Direction, Floor, LiftClass, LiftState, Request, State}

import scala.collection.mutable
import scala.util.Try
import scala.util.control.Breaks.{break, breakable}

class Lift extends LiftClass with Runnable {

  var id: Int = 0
  var currentFloor: Floor = _
  var liftState: LiftState = _
  var upDestinations: mutable.Queue[Request] = _
  var downDestinations: mutable.Queue[Request] = _

  /**
   * Method to initialize lift with the given parameters
   * @param inputId
   * @param floor
   * @param state
   */
  override def initialize(inputId: Int, floor: Floor, state: LiftState): Unit = {
    id = inputId
    currentFloor = floor
    liftState = state
    upDestinations = mutable.Queue[Request]()
    downDestinations = mutable.Queue[Request]()
  }

  /**
   * method to handle lift to move up, prints finally the total time taken once it is free
   * @param requests
   */
  override def goToFloorUp(requests: List[Request]): Unit = {
    var timeTaken = 0
    if(requests.nonEmpty) {
      requests.foreach(request => {
        liftState.liftState.status = State.closeState
        var presentFloor = currentFloor.number
        if (presentFloor < request.source) {
          liftState.liftDirection.direction = Direction.movingUp
          liftState.liftState.status = State.closeState
          for (i <- presentFloor + 1 to request.source) {
            try {
              Thread.sleep(1000)
//              showPosition()
            }
            timeTaken += 1
            currentFloor.number = i
          }
        } else if (presentFloor > request.source) {
          liftState.liftDirection.direction = Direction.movingDown
          liftState.liftState.status = State.closeState
          for (i <- presentFloor - 1 to request.source by -1) {
            try {
              Thread.sleep(1000)
//              showPosition()
            }
            timeTaken += 1
            currentFloor.number = i
          }
        } else if(presentFloor == request.source) {
          if(request.destination > presentFloor) liftState.liftDirection.direction = Direction.movingUp
          else liftState.liftDirection.direction = Direction.movingDown
          liftState.liftState.status = State.closeState
        }
        liftState.liftState.status = State.openState
        timeTaken += 1
        Thread.sleep(1000)
        presentFloor = currentFloor.number
        liftState.liftState.status = State.closeState
        liftState.liftDirection.direction = Direction.movingUp
        // reached the source floor, now can travel to destination
        for (i <- presentFloor + 1 to request.destination) {
          try {
            Thread.sleep(1000)
//            showPosition()
          }
          timeTaken += 1
          currentFloor.number = i
          if (checkIfUpRequestsCanBeProcesssed(request)) return
        }
        liftState.liftState.status = State.openState
        timeTaken += 1
        Thread.sleep(1000)
        liftState.liftState.status = State.closeState
        liftState.liftDirection.direction = Direction.idle
      })
    }
    println("Lift " + id + ": " + timeTaken + "seconds")
  }

  /**
   * method to process the down requests and prints the time in seconds once free
   * @param requests
   */
  override def goToFloorDown(requests: List[Request]): Unit = {
    var timeTaken = 0
    if(requests.nonEmpty) {
      requests.foreach(request => {
        var presentFloor = currentFloor.number
        if (presentFloor < request.source) {
          liftState.liftDirection.direction = Direction.movingUp
          liftState.liftState.status = State.closeState
          for (i <- presentFloor + 1 to request.source) {
            try {
              Thread.sleep(1000)
//              showPosition()
            }
            timeTaken += 1
            currentFloor.number = i
          }
        } else if (presentFloor > request.source) {
          liftState.liftDirection.direction = Direction.movingDown
          liftState.liftState.status = State.closeState
          for (i <- presentFloor - 1 to request.source by -1) {
            try {
              Thread.sleep(1000)
//              showPosition()
            }
            timeTaken += 1
            currentFloor.number = i
          }
        } else if (presentFloor == request.source) {
          if (request.destination > presentFloor) liftState.liftDirection.direction = Direction.movingUp
          else liftState.liftDirection.direction = Direction.movingDown
          liftState.liftState.status = State.closeState
        }
        liftState.liftState.status = State.openState
        timeTaken += 1
        Thread.sleep(1000)
        presentFloor = currentFloor.number
        liftState.liftState.status = State.closeState
        liftState.liftDirection.direction = Direction.movingDown
        // reached the source floor, now can travel to destination
        breakable {
          for (i <- presentFloor - 1 to request.destination by -1) {
            try {
              Thread.sleep(1000)
//              showPosition()
            }
            timeTaken += 1
            currentFloor.number = i
            if (checkIfDownRequestsCanBeProcesssed(request)) return
          }
        }
        liftState.liftState.status = State.openState
        timeTaken += 1
        Thread.sleep(1000)
        liftState.liftState.status = State.closeState
        liftState.liftDirection.direction = Direction.idle
      })
    }
    println("Lift " + id + ": " + timeTaken + "seconds")
  }

  /**
   * method to check if intermediate requests can be added to the up queue
   * @param request
   * @return
   */
  def checkIfUpRequestsCanBeProcesssed(request: Request): Boolean = {
    if(Try(upDestinations.nonEmpty).getOrElse(false)) {
      val currRequest = upDestinations.dequeue()
      if(currRequest.destination < request.destination && currentFloor.number < currRequest.source) {
        upDestinations.enqueue(currRequest)
        request.source = currRequest.destination
        upDestinations.enqueue(request)
        return true
      }
    }
    false
  }

  /**
   * method to check if the intermediate requests can be added to the down queue
   * @param request
   * @return
   */
  def checkIfDownRequestsCanBeProcesssed(request: Request): Boolean = {
    if (Try(downDestinations.nonEmpty).getOrElse(false)) {
      val currRequest = downDestinations.dequeue()
      if (currRequest.destination > request.destination && currentFloor.number > currRequest.source) {
        downDestinations.enqueue(currRequest)
        request.source = currRequest.destination
        downDestinations.enqueue(request)
        return true
      }
    }
    false
  }

  /**
   * async method called by thread to handle the up and down requests from the building
   */
  def switchOnLift(): Unit = {
    var upCheck = upDestinations.nonEmpty
    var downCheck = downDestinations.nonEmpty
    if(Try(upCheck).getOrElse(false)) {
      goToFloorUp(upDestinations.toList)
    }
    if(Try(downCheck).getOrElse(false)) {
      goToFloorDown(downDestinations.toList)
    }
    upCheck = upDestinations.nonEmpty
    downCheck = downDestinations.nonEmpty
  }

  /**
   * method to check if there are no requests
   * @return
   */
  override def occupancyStatus(): State = {
    if(upDestinations.nonEmpty || downDestinations.nonEmpty) return new State(State.closeState)
    new State(State.openState)
  }

  /**
   * method to get the lift state
   * @return
   */
  override def getLiftState: LiftState = liftState

  /**
   * method to get the lift position
   */
  override def showPosition(): Unit = {
    print("Lift " + id + " ---- > " + currentFloor.number + "(" + liftState.liftState.status + ")")
  }

  /**
   * thread run method
   */
  override def run(): Unit = {
    while(!Thread.interrupted()) {
      switchOnLift()
    }
  }
}
