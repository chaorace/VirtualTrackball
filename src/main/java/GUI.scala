import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

/**
 * Created by Chris on 8/4/2015. This is the GUI wrapper for the engine. This can be completely bypassed by console commands
 */
object GUI extends JFXApp {
  stage = new PrimaryStage{
    scene = new Scene{
      content = new BorderPane{

      }
    }
  }
}
