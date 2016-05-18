import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{MenuItem, PopupMenu, SystemTray, TrayIcon}
import java.io.FileInputStream
import java.lang.Boolean
import javafx.beans.value.{ChangeListener, ObservableValue}
import javax.imageio.ImageIO

import org.jnativehook.GlobalScreen

import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.Includes._
import scalafx.scene.text.Font


/**
 * Created by Chris on 8/4/2015. This is the GUI wrapper for the engine. This can be completely bypassed by console commands
 */
class Gui(startHidden: Boolean, config: Config) extends JFXApp {
  Platform.implicitExit = false
  var engineThread:Thread = null
  var engine = false

  //GUI inputfields
  val pollingField = new InputField("Polling Rate", Tooltip(Main.pollingRateDesc)){inputBox.text = config.pollingRate.getOrElse("").toString}
  val startupField = new InputField("Tolerance", Tooltip(Main.startupThresholdDesc)){inputBox.text = config.startupThreshold.getOrElse("").toString}
  val giveupField = new InputField("Sensitivity", Tooltip(Main.giveupThresholdDesc)){inputBox.text = config.giveupThreshold.getOrElse("").toString}
  val dragField = new InputField("Friction", Tooltip(Main.dragDesc)){inputBox.text = config.drag.getOrElse("").toString}

  //Gui setup
  stage = new PrimaryStage {
    title = s"${Main.name} by ${Main.author}"
    scene = new Scene {
      fill = Color.Beige
      content = new BorderPane {
        padding = Insets(15)
        top = new Label {
          padding = Insets(15)
          font = Font(21)
          text = s"${Main.name} Config"
        }
        center = new VBox{
          maxWidth = 300
          children = List(
            new Label{
              font = Font(11)
              text = "Leave fields blank to use default values. Hover for details"
            },
            new FlowPane {
              maxWidth = 400
              children = List(
                pollingField,
                startupField,
                giveupField,
                dragField
              )
            }
          )
        }
        //Engine starting logic. Converts fields to values to be used by the engine
        bottom = new ToggleButton{

          def updateText = text = if(engine) "Stop/Reload Settings" else "Start"
          updateText
          onAction = handle {
            engineToggler()
            updateText
          }
          onShowing = handle{
            updateText
          }
        }
        //If the user minimizes the GUI, send it to the system tray
        iconified.addListener(new ChangeListener[Boolean] {
          override def changed(observable: ObservableValue[_ <: Boolean], oldValue: Boolean, newValue: Boolean): Unit = {
            if(iconified.value){toggleTray(true)}
          }
        })
        //If the user tries closing the GUI while the engine is running, the thread gets shut down
        onCloseRequest = handle {
          GlobalScreen.unregisterNativeHook()
          toggleTray(false)
          if(engineThread != null) engineThread.stop()
          Platform.exit()
        }

      }
    }
  }

  //Tray icon setup
  val tray = SystemTray.getSystemTray
  val defaultAction = new ActionListener(){
    override def actionPerformed(e: ActionEvent): Unit = {
      Platform.runLater(toggleTray(false))
    }
  }
  val engineAction = new ActionListener(){
    override def actionPerformed(e: ActionEvent): Unit = {
      Platform.runLater(engineToggler())
    }
  }
  val showGuiItem = new MenuItem("Show GUI")
  showGuiItem.addActionListener(defaultAction)
  val toggleEngineItem = new MenuItem("On/Off Toggle")
  toggleEngineItem.addActionListener(engineAction)
  val popup = new PopupMenu()
  popup.add(showGuiItem)
  popup.add(toggleEngineItem)
  val image = ImageIO.read(getClass.getResourceAsStream("icon.png"))
  val trayIcon = new TrayIcon(image, Main.name, popup)
  trayIcon.addActionListener(defaultAction)

  //If set to start hidden, start hidden
  if(startHidden){
    toggleTray(true)
  }

  //Hides and shows GUI
  def toggleTray(t: Boolean): Unit ={
    //There were basically no references on the internet for how to do this using JFX8, so AWT it is
    if(t){
      //If true, hide stage and insert tray icon. Otherwise, show stage and remove tray icon

      if(SystemTray.isSupported){
        stage.hide()
        tray.add(trayIcon)
      }
    }else{
      stage.setIconified(false)
      stage.show()
      tray.remove(trayIcon)
    }
  }

  def engineToggler(): Unit ={
    engine match{
      case false =>
        engine = true
        try{
          engineThread = new Thread(new Engine(new Config(pollingField.value, startupField.value, giveupField.value, dragField.value)))
          engineThread.start()
        }catch{
          case e: NumberFormatException =>
            new Alert(AlertType.Error, "Bad value entry").show()
            engine = false
        }
      case true => engineThread.stop(); engine = false
    }
  }
}

class InputField(description: String, tt: Tooltip) extends HBox {
  //Template for input fields with a built-in description and tooltip
  padding = Insets(5)
  val inputBox = new TextField {
    maxWidth = 40
  }
  children = List(new Label(description + ": ") {
    tooltip = tt
    minHeight <== inputBox.minHeight
    minWidth = 70
  }, inputBox)

  def value = inputBox.text.value match{
    case "" => None
    case x: String => Some(x.toDouble)
  }
}
