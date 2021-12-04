package cd;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CDEventListener implements MouseListener, MouseMotionListener, 
    MouseWheelListener, KeyListener {
    
    private CD mCD = null;
    
    public CDEventListener(CD cd) {
        this.mCD = cd;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        CDScene curScene = (CDScene) this.mCD.getScenarioMgr().getCurScene();
        curScene.handleMouseScroll(e);
        this.mCD.getViewer().repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        CDScene curScene = (CDScene) this.mCD.getScenarioMgr().getCurScene();
        curScene.handleKeyDown(e);
        this.mCD.getViewer().repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}