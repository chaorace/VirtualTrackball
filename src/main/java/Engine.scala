import java.awt.Robot
import java.util.logging.{Level, Logger}

import math.geom2d.{Point2D, Vector2D}
import org.jnativehook.mouse.{NativeMouseEvent, NativeMouseInputListener}
import org.jnativehook.{GlobalScreen, NativeHookException}

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType


/**
 * Created by Chris on 8/4/2015. This class actually handles the trackball emulation
 */
class Engine(config: Config) extends Runnable{

  //Setting up engine
  val robot = new Robot
  val noVector = new Vector2D(0,0)
  var lastVector = noVector
  var clicked = false
  var mouseX = 0.0
  var mouseY = 0.0

  //JNativeHook setup
  val logger = Logger.getLogger(classOf[GlobalScreen].getPackage.getName)
  logger.setLevel(Level.WARNING)

  object GlobalMouseListener extends NativeMouseInputListener {
    def nativeMouseClicked(nativeMouseEvent: NativeMouseEvent) = {}

    def nativeMousePressed(nativeMouseEvent: NativeMouseEvent) = {
      clicked = true
    }

    def nativeMouseReleased(nativeMouseEvent: NativeMouseEvent) = {}

    def nativeMouseMoved(nativeMouseEvent: NativeMouseEvent) = {
      mouseX = nativeMouseEvent.getX
      mouseY = nativeMouseEvent.getY
    }

    def nativeMouseDragged(nativeMouseEvent: NativeMouseEvent) = {}
  }
  GlobalScreen.addNativeMouseListener(GlobalMouseListener)
  GlobalScreen.addNativeMouseMotionListener(GlobalMouseListener)
  try {
    GlobalScreen.registerNativeHook()
  } catch {
    case e: NativeHookException =>
      //Can't register the hook properly? Crash!!!
      new Alert(AlertType.Error, "JavaNativeHook2 failed to register! Try using v1.1 or earlier instead")
      sys.error("JavaNativeHook2 failed to register! Try using v1.1 or earlier instead")
      sys.exit(1)
  }




  def run() = while(true){
    //Reset click tracking
    clicked = false
    //Poll for mouse movement and give time to detect clicks
    Thread.sleep(config.pollingRate.getOrElse(10.00).toInt)
    val startPos = new Point2D(mouseX, mouseY)
    Thread.sleep(config.pollingRate.getOrElse(10.00).toInt)
    val endPos = new Point2D(mouseX, mouseY)

    //If this movement was significant
    if(!startPos.almostEquals(endPos, config.giveupThreshold.getOrElse(1)) || clicked){
      //If this movement was violent
      if (!startPos.almostEquals(endPos, config.startupThreshold.getOrElse(5))) {
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
        lastVector = new Vector2D(lastVector.x * config.drag.getOrElse(.95), lastVector.y * config.drag.getOrElse(.95))
        //If the trackball is almost stopped, stop it
        if(lastVector.almostEquals(noVector, 2)){
          lastVector = noVector
        }
      }
    }
  }
}
