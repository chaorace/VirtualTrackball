import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{VBox, HBox, FlowPane, BorderPane}
import scalafx.scene.paint.Color
import scalafx.Includes._
import scalafx.scene.text.Font


/**
 * Created by Chris on 8/4/2015. This is the GUI wrapper for the engine. This can be completely bypassed by console commands
 */
class Gui extends JFXApp {
  stage = new PrimaryStage {
    title = s"${Main.name} by ${Main.author}"
    scene = new Scene {
      val pollingField = new InputField("Polling Rate", Tooltip(Main.pollingRateDesc))
      val startupField = new InputField("Tolerance", Tooltip(Main.startupThresholdDesc))
      val giveupField = new InputField("Sensitivity", Tooltip(Main.giveupThresholdDesc))
      val dragField = new InputField("Friction", Tooltip(Main.dragDesc))

      fill = Color.Beige
      content = new BorderPane {
        padding = Insets(15)
        top = new Label {
          padding = Insets(15)
          font = Font(21)
          text = s"${Main.name}} Config"
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
        bottom = new ToggleButton{
          var engineThread = new Thread(new Engine(pollingField.value, startupField.value, giveupField.value, dragField.value))
          text <== when(selected) choose "Stop/Reload Settings" otherwise "Start"
          onAction = handle {selected.value match{
            case true =>
              engineThread = new Thread(new Engine(pollingField.value, startupField.value, giveupField.value, dragField.value))
              engineThread.start()
            case false => engineThread.stop()
          }}
        }
      }
    }
  }
}

class InputField(description: String, tt: Tooltip) extends HBox {
  padding = Insets(5)
  private val inputBox = new TextField {
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
