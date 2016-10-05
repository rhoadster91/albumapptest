/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albumappnew;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @author tararamanan
 */
public class PhotoComponent extends JComponent implements MouseListener, MouseMotionListener, KeyListener {

    public static boolean flipped;
    private BufferedImage image;
    private boolean drawingMode = false;
    private boolean textMode = false;


    private int prevX, prevY;     // The previous location of the mouse.

//public Graphics2D graphicsForDrawing; 

    ArrayList<ArrayList<ArrayList<Integer>>> drawingLines = new ArrayList(); //drawing lines is a list of eachLine's
    ArrayList<ArrayList<Integer>> eachLine = new ArrayList();

    ArrayList<Integer[]> rects = new ArrayList();
    ArrayList<String> postItTexts = new ArrayList();

    private int boundLeft;
    private int boundTop;
    private int boundRight;
    private int boundBottom;

    private int rectX, rectY, rectXEnd, rectYEnd;
    private StringBuffer currentPostItText;
    private int charInputX, charInputY;

    private boolean isDrawing = false;
    private boolean newline = true;

    private boolean textRectAssociationPending = false;


    //constructor
    public PhotoComponent() {
        //call superclass constructor
        super();

        flipped = false;
        setBackground(Color.black);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        //read default image in
        loadPhoto(new File(Constants.PATH_TO_FILE));


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //is this needed?
        g.setColor(Color.black);
        //render image
        //check if state is flipped or not

        if (flipped == true) {
            //draw white image
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            int imageX = image.getMinX();
            int imageY = image.getMinY();
            g.setColor(Color.yellow);
            g.fillRect(imageX, imageY, imageWidth, imageHeight);

            boundLeft = imageX;
            boundRight = imageX + imageWidth;
            boundTop = imageY;
            boundBottom = imageY + imageHeight;

            //draw any old annotations/drawings
            redrawStoredDrawings(g);

            //draw any old postits?
            redrawStoredText(g);
        } else {
            g.drawImage(image, 0, 0, null);
        }


    }

    public void loadPhoto(File filename) {
        try {
            image = ImageIO.read(filename);
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        } catch (IOException ex) {
            System.out.println("Error while trying to read in image!");
        }
        repaint();
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            System.out.println("double clicked");
        }
    }

    public void flip() {
        if (flipped) //back is shown
        {
            flipped = false;
            //add the last string in
            //postItTexts.add(currentPostItText.toString());
            currentPostItText = new StringBuffer("");
            //System.out.println("adding to postits in flip");

        } else
            flipped = true;
        repaint();
    }

    private void redrawStoredDrawings(Graphics graphics) {
        setupDrawingGraphics(graphics);
        graphics.setColor(Color.black);

        int pX = 0, pY = 0;
        boolean isFirstElem;
        for (ArrayList<ArrayList<Integer>> innerList : drawingLines) {
            System.out.println("line drawn" + innerList.toString());
            isFirstElem = true;
            for (ArrayList<Integer> numberCoord : innerList) {
                //for first element just set the prev vals n continue
                if (isFirstElem) {
                    pX = numberCoord.get(0);
                    pY = numberCoord.get(1);
                    isFirstElem = false;
                } else {
                    System.out.println(pX + "," + pY + "->" + numberCoord.get(0) + "," + numberCoord.get(1));
                    graphics.drawLine(pX, pY, numberCoord.get(0), numberCoord.get(1));
                    pX = numberCoord.get(0);
                    pY = numberCoord.get(1);

                }
            }
        }
        //graphics.dispose();
        //graphics = null;
    }

    private void redrawStoredText(Graphics graphicsForDrawing) {

        System.out.println(rects.toString());
        System.out.println(postItTexts.toString());

        for (int i = 0; i < rects.size(); i++) {
            //Graphics2D graphicsForDrawing = (Graphics2D) getGraphics();
            Integer[] coord = rects.get(i);
            String text = postItTexts.get(i);
            System.out.println("this ran atleast ");
            graphicsForDrawing.setColor(Color.green);
            graphicsForDrawing.fillRect(coord[0], coord[1], coord[2] - coord[0], coord[3] - coord[1]);

            //graphicsForDrawing.dispose();
            //graphicsForDrawing = null;
            //graphicsForDrawing.setColor(Color.black);

            charInputX = coord[0];
            charInputY = coord[1];
            newline = true;

            for (char c : text.toCharArray()) {
                writeTextInRect(graphicsForDrawing, c, coord);
            }
        }


    }


    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        prevX = x;
        prevY = y;

        //setup drawing graphics
        setupDrawingGraphics(getGraphics());

        if (drawingMode) {
            //add this to a new array list.
            ArrayList<Integer> tempList = new ArrayList();
            tempList.add(x);
            tempList.add(y);

        }

        if (textMode) {
            rectX = x;
            rectY = y;
        }
        //append prev text to string--trying out!
        if (currentPostItText != null)// & !(currentPostItText.toString().equals("")))
        {
            //if(currentPostItText.toString().equals(""))
            //{
            //check if an association is pending with a rect if yes:
            if (textRectAssociationPending) {
                postItTexts.add(currentPostItText.toString()); //exception!
                //System.out.println("adding to post it in mouse released");
                textRectAssociationPending = false;
            }
            //}
        }

    }

    public void mouseDragged(MouseEvent e) {
        isDrawing = true;
        int x = e.getX();
        int y = e.getY();

        Graphics graphics = getGraphics();

        if (drawingMode) {
            if (Math.abs(x - prevX) > 0 || Math.abs(y - prevY) > 0) {
                graphics.setColor(Color.black);
                if (x <= boundRight && x >= boundLeft && y >= boundTop && y <= boundBottom)
                    graphics.drawLine(prevX, prevY, x, y);

                //add this to a new array list.
                ArrayList<Integer> tempList = new ArrayList();
                tempList.add(x);
                tempList.add(y);
                eachLine.add(tempList);
                //System.out.println(prevX + ","+prevY+"->"+tempPos[0] + "," + tempPos[1]);
                //repaint();
            }
            prevX = x;
            prevY = y;
        }

        if (textMode) {
            //clear previous rectangle
            //graphics.clearRect(rectX, rectY, WIDTH, HEIGHT);
            if (x <= boundRight && x >= boundLeft && y >= boundTop && y <= boundBottom) {
                if (x < prevX)
                    x = prevX;
                if (y < prevY)
                    y = prevY;
                graphics.setColor(Color.green);
                graphics.fillRect(rectX, rectY, x - rectX, y - rectY);

                prevX = x;
                prevY = y;
            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("isdrawing = " + isDrawing);
        if (drawingMode) {
            //System.out.println("in released fn:Eachline = " + eachLine.toString());


            if (isDrawing) {
                drawingLines.add((ArrayList<ArrayList<Integer>>) eachLine.clone());
                //eachLine.
                eachLine.clear();
                isDrawing = false;
            }
            //System.out.println("drawnglines = " + drawingLines.toString());
        }

        if (textMode) {
            rectXEnd = prevX;
            rectYEnd = prevY;
            if (isDrawing) {
                rects.add(new Integer[]{rectX, rectY, rectXEnd, rectYEnd});
                isDrawing = false;

                //create stringbuffer for text
                currentPostItText = new StringBuffer("");
                charInputX = rectX;
                charInputY = rectY;

                newline = true;

                textRectAssociationPending = true;
            }


        }
        //graphicsForDrawing.dispose();
        //graphicsForDrawing = null;

        System.out.println("post it text = " + postItTexts.toString());
        System.out.println("rects = " + rects.size());
    }

    private void setupDrawingGraphics(Graphics graphics)

    {
        graphics = (Graphics2D) getGraphics();
        if (drawingMode)
            graphics.setColor(Color.black);
        if (textMode)
            graphics.setColor(Color.green);
    }

    private void writeTextInRect(Graphics graphics, char inputChar, Integer[] lastRectDim) {
        //Integer[] lastRectDim = rects.get(rects.size()-1);
        int x1 = lastRectDim[0];
        int y1 = lastRectDim[1];
        int x2 = lastRectDim[2];
        int y2 = lastRectDim[3];

        graphics.setColor(Color.black);

        FontMetrics metrics = graphics.getFontMetrics();
        int fontHeight = metrics.getMaxAscent();
        int fontLeading = metrics.getLeading();
        int fontDescent = metrics.getMaxDescent();
        int charWidth = metrics.charWidth(inputChar);

        //System.out.println("charInputY")

        if (newline) //to start, check if newline is true and reposition
        {
            charInputX = x1 + 3;
            charInputY = charInputY + fontHeight + fontLeading + fontDescent;

            if (charInputY + fontDescent + fontLeading > y2) {
                if (charInputY + fontDescent + fontLeading <= boundBottom) {
                    //increasePostItHeight(x1,y2,x2,y2+3+maxascent+maxdescent
                    graphics.setColor(Color.green);
                    graphics.fillRect(x1, y2, x2 - x1, fontHeight + fontDescent + fontLeading);

                    //modify rect value in array
                    rects.remove(rects.size() - 1);
                    lastRectDim[3] = fontHeight + fontDescent + fontLeading + y2;
                    rects.add(lastRectDim);
                }
            }
            newline = false;
        }

        //output the character
        graphics.setColor(Color.black);
        if (charInputY + fontDescent <= boundBottom)
            graphics.drawString(String.valueOf(inputChar), charInputX, charInputY);

        //set position for next input character
        charInputX = charInputX + charWidth;


        //see where next input char will go. if it is too close to the edge, set newline to true
        if (charInputX + charWidth >= x2)
            newline = true;

        //graphicsForDrawing.dispose();
        //graphicsForDrawing = null;

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        return;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        return;
    }

    void setDrawingMode(boolean b) {
        drawingMode = b;
        textMode = !b;
        this.setFocusable(false);
    }

    void setTextMode(boolean b) {
        textMode = b;
        drawingMode = !b;
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char lastChar = e.getKeyChar();
        currentPostItText.append(lastChar);
        //System.out.println("typed: " + lastChar);

        writeTextInRect(getGraphics(), lastChar, rects.get(rects.size() - 1));


    }

    @Override
    public void keyPressed(KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("blah");
    }


}



