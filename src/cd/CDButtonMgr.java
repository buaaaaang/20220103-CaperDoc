package cd;

import cd.button.CDButton;
import cd.button.CDColorButton;
import cd.button.CDContentButton;
import cd.button.CDHierarchyButton;
import cd.button.CDRemainderButton;
import cd.button.CDNeedButton;
import cd.button.CDImplyButton;
import cd.button.CDSideRemainderButton;
import java.awt.Color;
import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;

public class CDButtonMgr {    
    private ArrayList<CDColorButton> mColorButtons = null;
    public ArrayList<CDColorButton> getColorButtons() {
        return this.mColorButtons;
    }
    private void addColorButton(CDColorButton button) {
        this.mColorButtons.add(button);
    }
    
    private ArrayList<CDContentButton> mContentButtons = null;
    public ArrayList<CDContentButton> getContentButtons() {
        return this.mContentButtons;
    }
    public void addContentButton(CDContentButton button) {
        this.mContentButtons.add(button);
    }
    
    private ArrayList<CDNeedButton> mNeedButtons = null;
    public ArrayList<CDNeedButton> getNeedButtons() {
        return this.mNeedButtons;
    }
    public void addNeedButton(CDNeedButton button) {
        this.mNeedButtons.add(button);
    }
    
    private ArrayList<CDImplyButton> mImplyButtons = null;
    public ArrayList<CDImplyButton> getImplyButtons() {
        return this.mImplyButtons;
    }
    public void addImplyButton(CDImplyButton button) {
        for (int i=0; i < this.mImplyButtons.size(); i++) {
            if (this.mImplyButtons.get(i).getContentPosition() > 
                button.getContentPosition()) {
                this.mImplyButtons.add(i, button);
                return;
            }
        }
        this.mImplyButtons.add(button);
    }

    private ArrayList<CDHierarchyButton> mHierarchyButtons = null;
    public ArrayList<CDHierarchyButton> getHierarchyButtons() {
        return this.mHierarchyButtons;
    }
    public void addHierarchyButton(CDHierarchyButton button) {
        for (int i=0; i < this.mHierarchyButtons.size(); i++) {
            if (this.mHierarchyButtons.get(i).getContentPosition() > 
                button.getContentPosition()) {
                this.mHierarchyButtons.add(i, button);
                return;
            }
        }
        this.mHierarchyButtons.add(button);
    }
    
    private CDColorButton mCurWorkingColorButton = null;
    public CDColorButton getCurWorkingColorButton() {
        return this.mCurWorkingColorButton;
    }
    
    private CDContentButton mCurWorkingContentButton = null;
    public CDContentButton getCurWorkingContentButton() {
        return this.mCurWorkingContentButton;
    }
    
    private CDNeedButton mCurWorkingNeedButton = null;
    public CDNeedButton getCurWorkingNeedButton() {
        return this.mCurWorkingNeedButton;
    }
    
    private CDHierarchyButton mCurWorkingHierarchyButton = null;
    public CDHierarchyButton getCurWorkingHierarchyButton() {
        return this.mCurWorkingHierarchyButton;
    }
    
    private CDImplyButton mCurWorkingImplyButton = null;
    public CDImplyButton getCurWorkingImplyButton() {
        return this.mCurWorkingImplyButton;
    }
    
    
    public CDButtonMgr(CD cd) {
        JPanel panel = cd.getButtonViewer();
        
        this.mColorButtons = new ArrayList<>();
        this.addColorButton(new CDColorButton(Color.black, 140, 15, 20, panel));
        this.addColorButton(new CDColorButton(Color.red, 110, 15, 20, panel));
        this.addColorButton(new CDColorButton(Color.blue, 80, 15, 20, panel));
        this.addColorButton(new CDColorButton(new Color(255, 200, 0, 128), 
            50, 15, 20, panel));
        this.mColorButtons.get(0).setHighlight(true);
        
        this.mContentButtons = new ArrayList<>();
        this.mNeedButtons = new ArrayList<>();
        this.mImplyButtons = new ArrayList<>();
        this.mHierarchyButtons = new ArrayList<>();
    }
    
    public CDButton checkButton(Point pt) {
        for (CDColorButton button: this.mColorButtons) {
            if (button.contains(pt)) {
                this.mCurWorkingColorButton = button;
                return button;
            }
        }
        if (pt.x < CD.HIERARCHY_WIDTH) { 
            for (CDImplyButton button: this.mImplyButtons) {
                if (button.contains(pt)) {
                    this.mCurWorkingImplyButton = button;
                    return button;
                }
            }   
            for (CDHierarchyButton button: this.mHierarchyButtons) {
                if (button.contains(pt)) {
                    this.mCurWorkingHierarchyButton = button;
                    return button;
                }
            }   
            return new CDSideRemainderButton();
        } else {
            for (CDContentButton button: this.mContentButtons) {
                if (button.contains(pt)) {
                    this.mCurWorkingContentButton = button;
                    return button;
                }
            }  
            for (CDNeedButton button: this.mNeedButtons) {
                if (button.contains(pt)) {
                    this.mCurWorkingNeedButton = button;
                    return button;
                }
            }   
            return new CDRemainderButton();      
        }
    }
}