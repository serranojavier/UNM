/**
 * @author Javier Serran
 * date: 11/9/12
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DLA_CtrlFrame extends JFrame implements ActionListener, ChangeListener
{
  public static final int FRAME_WIDTH = 300;

  /*A reference is needed to the instance of DLA_Sliders created in
   *  main so that when the user clicks the pause or reset button, methods
   *  in this class can call non-static public methods that instance
   *  of the DLA_Slider class.
   */
  private DLA_Sliders myDLA;

  private JToggleButton butPause;
  private JButton butReset;

  private JSlider slider_seedCount;
  private JLabel label_seedCount;

  private JSlider slider_particleCount;
  private JLabel label_particleCount;

  private JSlider slider_edgeProbability;
  private JLabel label_edgeProbability;

  private JSlider slider_cornerProbability;
  private JLabel label_cornerProbability;

  private Container contentPane;

  /*
   * This boolean stops the user from hitting the pause button
   * without having hit 'reset' first, after the user hits reset
   * this is set to true and the user can click pause and unpause.
   */
  private boolean canClickPause = false;

  /**
   * This constructor created and placed the GUI widgets.
   */
  public DLA_CtrlFrame()
  {
    this.setTitle("DLA_CtrlFrame");

    int frameHeight = 670;
    this.setSize(FRAME_WIDTH, frameHeight);
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    Insets inset = this.getInsets();
    int insideWidth = FRAME_WIDTH - inset.left - inset.right;


    contentPane = this.getContentPane();
    contentPane.setLayout(null);


    int edge =  10;
    int buttonWidth = 100;
    int sliderWidth = insideWidth - 2 * edge;
    int rowHeight = 30;

    butReset = addButton("Reset");
    butReset.setBounds(edge, 60, buttonWidth, rowHeight);


    butPause = addToggleButton("Unpause", true);
    butPause.setBounds(edge, 120, buttonWidth, rowHeight);

    // seed count
    slider_seedCount = addSlider(1, 15, 5);
    slider_seedCount.setMajorTickSpacing(2);
    slider_seedCount.setMinorTickSpacing(1);

    // particle count
    slider_particleCount = addSlider(1, 25001, 10000);
    slider_particleCount.setMajorTickSpacing(5000);
    slider_particleCount.setMinorTickSpacing(2500);

    slider_edgeProbability = addSlider(0, 100, 100);
    slider_edgeProbability.setMajorTickSpacing(10);
    slider_edgeProbability.setMinorTickSpacing(5);

    slider_cornerProbability = addSlider(0, 100, 0);
    slider_cornerProbability.setMajorTickSpacing(10);
    slider_cornerProbability.setMinorTickSpacing(5);

    // Create the labels
    label_seedCount = new JLabel();
    label_particleCount = new JLabel();
    label_edgeProbability = new JLabel();
    label_cornerProbability = new JLabel();

    contentPane.add(label_seedCount);
    contentPane.add(label_particleCount);
    contentPane.add(label_edgeProbability);
    contentPane.add(label_cornerProbability);

    // Position them on the window
    label_seedCount.setBounds(edge, 150, sliderWidth, rowHeight);
    slider_seedCount.setBounds(edge, 175, sliderWidth, rowHeight * 2);

    label_particleCount.setBounds(edge, 250, sliderWidth, rowHeight);
    slider_particleCount.setBounds(edge, 275, sliderWidth, rowHeight * 2);

    label_edgeProbability.setBounds(edge, 350, sliderWidth, rowHeight * 2);
    slider_edgeProbability.setBounds(edge, 385, sliderWidth, rowHeight * 2);

    label_cornerProbability.setBounds(edge, 450, sliderWidth, rowHeight * 2);
    slider_cornerProbability.setBounds(edge, 485, sliderWidth, rowHeight * 2);

    /*
     * The value was already set when the slider was constructed.
     * By setting it again, a change event is triggered.
     * This is used to update the slider's label.
     * Note: in order to trigger the event, the value must be different from
     *  the value used when the slider was created.
     */
    // Otherwise it doesn't show the labels until the sliders are moved.
    slider_seedCount.setValue(1);
    slider_particleCount.setValue(10000);
    slider_edgeProbability.setValue(100);
    slider_cornerProbability.setValue(0);
  }

  /**
   * Helper method that created a JToggleButton, adds it to the
   * DLA_CtrlFrame's contentPane and sets DLA_CtrlFrame to be its
   * action listener.
   * @param str String label for the button.
   * @param state It initializes to this state.
   * @return The button object.
   */
  private JToggleButton addToggleButton(String str, boolean state)
  {
    JToggleButton id = new JToggleButton(str, state);
    contentPane.add(id);
    id.addActionListener(this);
    return id;
  }

  /**
   * Helper method that created a JButton, adds it to the DLA_CtrlFrame's
   * contentPane and sets DLA_CtrlFrame to be its actionListener.
   * @param str String label for the button.
   * @return Button object.
   */
  private JButton addButton(String str)
  {
    JButton id = new JButton(str);
    contentPane.add(id);
    id.addActionListener(this);
    return id;
  }

  /**
   * Helper method that creates a JSlider, adds it to the DLA_CtrlFrame's
   * contentPane and sets DLA_CtrlFrame to be its changeListener.
   * @param min Minimum value for the slider.
   * @param max Maximum value for the slider.
   * @param value Initial value to the slider.
   * @return Slider object.
   */
  private JSlider addSlider(int min, int max, int value)
  {
    JSlider mySlider = new JSlider(JSlider.HORIZONTAL, min, max, value);
    contentPane.add(mySlider);
    mySlider.setPaintTicks(true);
    mySlider.setPaintLabels(true);
    mySlider.addChangeListener(this);
    return mySlider;
  }

  /**
   * Callback that responds to button clicks
   * Note: the action events generated by the timer in DLA_Sliders
   *     does NOT call this actionPerformed because only the instance of
   *     DLA_Slider that created the timer was registered as a listener
   *     for those timer events.
   *     Similarly, actionPreformed in DLA_Sliders does NOT get called
   *     on button clicks because in addButton above, we have:
   *        id.addActionListener(this);
   *     there this is this instance of DLA_CtrlFrame.
   *     If we want DLA_Sliders's actionPreformed to also listen to the
   *     button clicks, then in addButton we could add the line:
   *        id.addActionListener(myDLA);
   */
  public void actionPerformed(ActionEvent arg0)
  {
    // Retrieve the object where the event occurred
    Object obj = arg0.getSource();

    if (obj == butPause && canClickPause != false)
    {
      // pause button was pressed, retrieve and set its new value.
      myDLA.setPause(butPause.isSelected());

      // Retrieve butPause state
      boolean pauseState = butPause.isSelected();

      // pauseState is true(paused) switch button text
      if (pauseState)
      {
        butPause.setText("Unpause");
      }
      else
      {
        butPause.setText("Pause");
      }
    }
    else if (obj == butReset && canClickPause == false) // Called
    {
      // User click reset; enable the pause button.
      if (canClickPause == false)
      {
        canClickPause = true;
        butPause.setEnabled(true);
      }

      /*
       * Get the new values of seedCount and particleCount and pass
       * them to init() to reset everything.
       */
      int seedCount = slider_seedCount.getValue();
      int particleCount = slider_particleCount.getValue();
      myDLA.init(seedCount, particleCount);
    }
    else if (obj == butReset)
    {
      pauseTheProgram();
      /*
       * Get the new values of seedCount and particleCount and pass
       * them to init() to reset everything.
       */
      int seedCount = slider_seedCount.getValue();
      int particleCount = slider_particleCount.getValue();
      myDLA.init(seedCount, particleCount);
    }
  }

  /**
   * Callback method that responds to slider movements.
   * @param event
   */
  public void stateChanged(ChangeEvent event)
  {
    /*
     * Retrieve the object where the event occurred.
     * then identify which slider was moved, then change its value.
     */
    Object obj = event.getSource();

    if (obj == slider_seedCount)
    {
      int seedCount = slider_seedCount.getValue();
      label_seedCount.setText("Number of Seed Crystals: "+seedCount);
    }
    else if (obj == slider_particleCount)
    {
      int particleCount = slider_particleCount.getValue();
      label_particleCount.setText("Number of Particles: " + particleCount);
    }
    else if (obj == slider_edgeProbability)
    {
      int edgeProb = slider_edgeProbability.getValue();
      label_edgeProbability.setText("Edge crystallization probability: " + edgeProb + "%");
    }
    else if (obj == slider_cornerProbability)
    {
      int cornerProb = slider_cornerProbability.getValue();
      label_cornerProbability.setText("Corner crystallization probability: " + cornerProb + "%");
    }
  }

  /**
   * Gets the probability value from the edge probability slider and returns
   * it as a two decimal float.
   * @return Probability value from the slider as two decimal float.
   */
  public double getEdgeProbability()
  {
    return (double)(slider_edgeProbability.getValue()) / 100.0;
  }

  /**
   * Gets the probability value from the corner probability slider and
   * returns it as a two decimal float.
   * @return
   */
  public double getCornerProbability()
  {
    return (double) slider_cornerProbability.getValue() / 100.0;
  }

  /**
   * Called every time the user hits the reset button.
   * This method changed the text of the button to 'pause'
   * as well as pausing the timer.
   */
  public void pauseTheProgram()
  {
    // Pause the timer; set the toggle to clicked and change its label.
    myDLA.setPause(true);
    butPause.setSelected(true);
    butPause.setText("Unpause");
  }

  /**
   * Entry point of the program
   * Creates both the control frame and the DLA frame.
   */
  public static void main(String[] args)
  {
    DLA_CtrlFrame control = new DLA_CtrlFrame();
    control.myDLA = new DLA_Sliders(control);
  }
}
