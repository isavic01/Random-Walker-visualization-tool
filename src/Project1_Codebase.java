import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Project1_Codebase {
    //references to some variables we may want to access in a global context
    static int WIDTH = 500; //width of the image
    static int HEIGHT = 500; //height of the image
    static BufferedImage Display; //the image we are displaying
    static JFrame window; //the frame containing our window


    public static void main(String[] args) {
        //run the GUI on the special event dispatch thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                //Create the window and set options
                //The window
                window = new JFrame("RandomWalker");
                window.setPreferredSize(new Dimension(WIDTH + 100, HEIGHT + 50)); //sets the "ideal" window size
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminate program when "x" is clicked
                window.setVisible(true); //show the window
                window.pack(); //make window the preferred size


                //Display panel/image
                JPanel DisplayPanel = new JPanel();
                Display = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
                DisplayPanel.add(new JLabel(new ImageIcon(Display)));
                window.add(DisplayPanel, BorderLayout.CENTER);

                //Config panel
                JPanel Configuration = new JPanel();
                Configuration.setBackground(new Color(230, 230, 230));
                Configuration.setPreferredSize(new Dimension(100, 500));
                Configuration.setLayout(new FlowLayout());

                //Step count input
                JLabel StepCountLabel = new JLabel("Step Count:");
                Configuration.add(StepCountLabel);

                JTextField StepCount = new JTextField("500");
                StepCount.setPreferredSize(new Dimension(100, 25));
                Configuration.add(StepCount);

                //Walker type input
                JLabel WalkerType = new JLabel("Walker Type:");
                Configuration.add(WalkerType);

                ButtonGroup WalkerTypes = new ButtonGroup();//group of buttons
                JRadioButton Standard = new JRadioButton("Standard");//creates a radio button. in a ButtonGroup, only one can be selected at a time
                Standard.setActionCommand("standard");//can be grabbed to see which button is active
                Standard.setSelected(true);//set this one as selected by default
                JRadioButton Skippy = new JRadioButton("Skippy");
                Skippy.setActionCommand("skippy");
                WalkerTypes.add(Standard);//set as part of group
                WalkerTypes.add(Skippy);
                Configuration.add(Standard);//add to panel
                Configuration.add(Skippy);

                //Walker type input
                JLabel Geometry = new JLabel("World Geometry:");
                Configuration.add(Geometry);
                ButtonGroup Geometries = new ButtonGroup();
                JRadioButton Bounded = new JRadioButton("Bounded");
                Bounded.setActionCommand("bounded");
                Bounded.setSelected(true);
                JRadioButton Toroidal = new JRadioButton("Toroidal");
                Toroidal.setActionCommand("toroidal");
                Geometries.add(Bounded);
                Geometries.add(Toroidal);
                Configuration.add(Bounded);
                Configuration.add(Toroidal);

                //Path Rendering input
                JLabel Render = new JLabel("Path Render:");
                Configuration.add(Render);
                ButtonGroup Renderers = new ButtonGroup();
                JRadioButton Black = new JRadioButton("Black");
                Black.setActionCommand("black");

                Black.setSelected(true);
                JRadioButton Gradient = new JRadioButton("Gradient");
                Gradient.setActionCommand("gradient");

                JLabel c1 = new JLabel("Gradient Start:");
                JTextField color1 = new JTextField("0000FF");
                JLabel c2 = new JLabel("Gradient End:");
                JTextField color2 = new JTextField("FFAA00");
                Renderers.add(Black);
                Renderers.add(Gradient);

                Configuration.add(Black);
                Configuration.add(Gradient);
                Configuration.add(c1);
                Configuration.add(color1);
                Configuration.add(c2);
                Configuration.add(color2);


                //=====THIS IS THE MOST RELEVANT SECTION FOR PROJECT 1=====
                //Create the Walk button
                JButton Walk = new JButton("Walk");

                //Assign a behavior to run when the Walk button is pressed
                Walk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int count = Integer.parseInt(StepCount.getText()); //gets the string from a TextField, and read it as an int
                        String walk_type = WalkerTypes.getSelection().getActionCommand();//gets the action command of which radio button is selected, a String describing the type of Walk
                        String geom_type = Geometries.getSelection().getActionCommand();
                        String render_type = Renderers.getSelection().getActionCommand();
                        int grad_start = (int) Long.parseLong(color1.getText(), 16);//Get the color, convert from hex string to int
                        int grad_end = (int) Long.parseLong(color2.getText(), 16);//Get the color, convert from hex string to int

                        //===Walk, Update Display, repaint===
                        //1. Generate a Buffered image in the specified style using the data from above
                        BufferedImage GeneratedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

                        //do the stuff
                        // GeneratedImage.setRGB(WIDTH/2,HEIGHT/2,0xFF000000);
                        if (walk_type.equals("standard")) {
                            standardWalker(GeneratedImage, count, render_type, geom_type, grad_end, grad_start);
                       } else if (walk_type.equals("skippy")) {
                            skippyWalker(GeneratedImage, count, render_type, geom_type, grad_end, grad_start);
                      }

                        //2. Update the display with the generated image
                        UpdateDisplay(GeneratedImage);
                        window.repaint();
                    }
                });

                Configuration.add(Walk);
                window.add(Configuration, BorderLayout.EAST);
            }
        });
    }

    //A method to update the display image to match one generated by you
    static void UpdateDisplay(BufferedImage img) {
        //Below 4 lines draws the input image on the display image
        Graphics2D g = (Graphics2D) Display.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT); //draw over the old image with white
        g.drawImage(img, 0, 0, null); //draw over the old image with the one you generated from the walk.

        //forces the window to redraw its components (i.e., the image)
        window.repaint();
    }

    static void standardWalker(BufferedImage image, int steps, String pathRenderingType, String wrldGeoType, int lastColor, int firstColor) {
        int[] interpolation = Project1_Functions.CreateGradient(firstColor, lastColor, steps);
        int x = (int) (Math.random() * WIDTH);
        int y = (int) (Math.random() * HEIGHT);

        //for looop that goes through every step adn chooses a random direction for each
        for (int i = 0; i < steps; i++) {
            int[] translations = {-1, 0, 1};
            int x2 = translations[(int) (Math.random() * 3)];
            int y2 = translations[(int) (Math.random() * 3)];





            if (wrldGeoType.equals("bounded")) {
                x = Math.min(Math.max(x + x2, 0), WIDTH - 1); // finds the max in between new steps and 0  this will stop it from moving beyond the  stated boundaries
                y = Math.min(Math.max(y + y2, 0), HEIGHT - 1);// then finds the min to do the same thing and catch it if it goes further than boundaries.
            } else if (wrldGeoType.equals("toroidal")) {
                x = (x + x2 + WIDTH) % WIDTH;
                y = (y + y2 + HEIGHT) % HEIGHT;
            }

            if (pathRenderingType.equals("black")) {
                image.setRGB(x, y, 0xFF000000);
            } else if (pathRenderingType.equals("gradient")) {
                System.out.println("rendering the gradient");
                image.setRGB(x, y, interpolation[i]);
                System.out.println("Color at step " + i + ": " + Integer.toHexString(interpolation[i]));
            }
        }
    }




    static void skippyWalker(BufferedImage image, int steps, String pathRenderingType, String wrldGeoType, int lastColor, int firstColor) {
        int[] interpolation = Project1_Functions.CreateGradient(firstColor, lastColor, steps);
        int x = WIDTH / 2;
        int y = HEIGHT / 2;
        int countingSteps = 0;
        int skippingSteps = (int) (Math.random() * 400) + 100; //500
        boolean rendering = true;
        for (int i = 0; i < steps; i++) {
            if (countingSteps == skippingSteps) {
                rendering = !rendering;
                countingSteps = 0;
                skippingSteps = (int) (Math.random() * 400) + 100;//500
            }
            countingSteps++;


            int[] translations = {-1, 0, 1};
            int x2 = translations[(int) (Math.random() * 3)];
            int y2 = translations[(int) (Math.random() * 3)];

            if (wrldGeoType.equals("bounded")) {
                x = Math.min(Math.max(x + x2, 0), WIDTH - 1);
                y = Math.min(Math.max(y + y2, 0), HEIGHT - 1);
            } else if (wrldGeoType.equals("toroidal")) {
                if (x + x2 < 0) {
                    x = WIDTH - 1;
                } else if (x + x2 >= WIDTH) {
                    x = 0;
                } else {
                    x += x2;
                }

                if (y + y2 < 0) {
                    y = HEIGHT - 1;
                } else if (y + y2 >= HEIGHT) {
                    y = 0;
                } else {
                    y += y2;
                }




            } if (rendering) {
                if (pathRenderingType.equals("black")) {
                    image.setRGB(x, y, 0xFF000000);
                } else if (pathRenderingType.equals("gradient")) {
                    image.setRGB(x, y, interpolation[i]);
                }
            }
        }


    }

}































