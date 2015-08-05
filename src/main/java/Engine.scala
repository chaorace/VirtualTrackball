import java.awt.{Robot, MouseInfo}
import java.util.logging.{Level, Logger}

import math.geom2d.{Point2D, Vector2D}
import org.jnativehook.GlobalScreen
import org.jnativehook.mouse.{NativeMouseEvent, NativeMouseListener}


/**
 * Created by Chris on 8/4/2015. This class actually handles
 */
class Engine(pollingRate: Option[Double], startupThreshold: Option[Double], giveupThreshold: Option[Double], drag: Option[Double]) extends Runnable{

  //Setting up engine
  val robot = new Robot
  val noVector = new Vector2D(0,0)
  var lastVector = noVector
  var clicked = false

  //JNativeHook setup
  object GlobalMouseListener extends NativeMouseListener {
    def nativeMouseClicked(nativeMouseEvent: NativeMouseEvent) = {}
    def nativeMousePressed(nativeMouseEvent: NativeMouseEvent) = {clicked = true}
    def nativeMouseReleased(nativeMouseEvent: NativeMouseEvent) = {}
  }
  val logger = Logger.getLogger(classOf[GlobalScreen].getPackage.getName)
  logger.setLevel(Level.WARNING)
  GlobalScreen.registerNativeHook()
  GlobalScreen.addNativeMouseListener(GlobalMouseListener)




  def run() = while(true){
    //Reset click tracking
    clicked = false
    //Poll for mouse movement and give time to detect clicks
    Thread.sleep(pollingRate.getOrElse(10.00).toInt)
    val startPos = new Point2D(MouseInfo.getPointerInfo.getLocation)
    Thread.sleep(pollingRate.getOrElse(10.00).toInt)
    val endPos = new Point2D(MouseInfo.getPointerInfo.getLocation)

    //If this movement was significant
    if(!startPos.almostEquals(endPos, giveupThreshold.getOrElse(1)) || clicked){
      //If this movement was violent
      if(!startPos.almostEquals(endPos, startupThreshold.getOrElse(7))){
        //Use the last polled movement as the new trackball speed
        lastVector = new Vector2D(startPos, endPos)
      }else{
        //The mouse is being used lightly, set trackball speed to nothing
        lastVector = noVector
      }
    //If last polled mouse movement was insignificant
    }else{
      //Only bother if there's a vector to move the mouse along
      if(lastVector != noVector){
        //Move the mouse according to the last trackball speed
        robot.mouseMove((endPos.x + lastVector.x).toInt, (endPos.y + lastVector.y).toInt)
        //Apply friction to trackball
        lastVector = new Vector2D(lastVector.x * drag.getOrElse(.975), lastVector.y * drag.getOrElse(.975))
        //If the trackball is almost stopped, stop it
        if(lastVector.almostEquals(noVector, 2)){
          lastVector = noVector
        }
      }
    }
  }
}