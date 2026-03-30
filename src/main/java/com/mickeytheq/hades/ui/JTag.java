package com.mickeytheq.hades.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// a tag component that is used as an indicator of a selection and has a close button/image that can be clicked on
// to remove the tag
public class JTag extends JComponent {
    // horizontal space to place around the image
    private static final int HORIZONTAL_SPACING = 4;

    // height above/below the text
    private static final int HEIGHT_BUFFER = 3;

    private static final Color DEFAULT_BACKGROUND = new Color(0xB8E3E9);

    private final String text;
    private final List<ActionListener> actionListeners = new ArrayList<>();

    private BufferedImage activeImage;

    public JTag(String text, BufferedImage image, BufferedImage hoverImage) {
        this.text = text;
        this.activeImage = image;

        Font font = UIManager.getFont("TextField.font");

        setFont(font);
        setBackground(DEFAULT_BACKGROUND);
        setForeground(Color.BLACK);

        addMouseMotionListener(new MouseMotionAdapter() {
            // switch the image depending on whether the mouse cursor is on or off it
            @Override
            public void mouseMoved(MouseEvent e) {
                if (mouseInImage(e)) {
                    activeImage = hoverImage != null ? hoverImage : image;
                } else {
                    activeImage = image;
                }

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            // the mouse motion listener may not fire when moving quickly from the image to outside the component
            // therefore whenever the mouse leaves the component entirely revert back to the standard image
            @Override
            public void mouseExited(MouseEvent e) {
                activeImage = image;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mouseInImage(e))
                    return;

                fireActionEvent();
            }
        });
    }

    private boolean mouseInImage(MouseEvent e) {
        if (activeImage == null)
            return false;

        int x = e.getX();

        int imageRightEdge = getWidth() - HORIZONTAL_SPACING - 1;
        int imageLeftEdge = imageRightEdge - activeImage.getWidth() + 1;

        if (x > imageRightEdge)
            return false;

        if (x < imageLeftEdge)
            return false;

        int y = e.getY();

        int imageTopEdge = HEIGHT_BUFFER;
        int imageBottomEdge = imageTopEdge + activeImage.getHeight() - 1;

        if (y < imageTopEdge)
            return false;

        if (y > imageBottomEdge)
            return false;

        return true;
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    protected void fireActionEvent() {
        ActionEvent actionEvent = new ActionEvent(this, 0, null);

        for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(actionEvent);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int width = HORIZONTAL_SPACING + fontMetrics.stringWidth(text) + HORIZONTAL_SPACING;

        if (activeImage != null)
            width = width + activeImage.getWidth() + HORIZONTAL_SPACING;

        int height = fontMetrics.getHeight() + 2 * HEIGHT_BUFFER;

        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

        Font font = getFont();

        FontMetrics fontMetrics = getFontMetrics(font);

        g2.setColor(getForeground());
        g2.drawString(text, HORIZONTAL_SPACING, HEIGHT_BUFFER + fontMetrics.getAscent());

        if (activeImage != null) {
            int y = (getHeight() - activeImage.getHeight()) / 2;

            g2.drawImage(activeImage, getWidth() - activeImage.getWidth() - HORIZONTAL_SPACING, y, null);
        }
    }
}
