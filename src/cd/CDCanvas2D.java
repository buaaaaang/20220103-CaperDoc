package cd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class CDCanvas2D extends JPanel {
    // fields and constants
    private static final Color COLOR_PT_CURVE_DEFAULT = new Color(0, 0, 0, 255);
    public static final Color COLOR_SELECTION_BOX = new Color(255, 0, 0, 64);
    public static final Color COLOR_CROP_BOX = Color.CYAN;
    private static final Color COLOR_SELECTED_PT_CURVE = Color.ORANGE;
    private static final Color COLOR_INFO = new Color(255,0,0,128);
    
    private static final Stroke STROKE_PT_CURVE_DEFAULT = new BasicStroke(5f,
        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Stroke SAVED_STROKE_PT_CURVE_DEFAULT = 
        new BasicStroke(20f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static final Stroke STROKE_SELECTION_BOX = new BasicStroke(2f);
    public static final Stroke STROKE_CROP_BOX = new BasicStroke(2f);
    
    private static final Font FONT_INFO = 
        new Font("Monospaced", Font.PLAIN, 24);
    private static final int INFO_TOP_ALIGNMENT_X = 250;
    private static final int INFO_TOP_ALIGNMENT_Y = 50;
    
    public static final float STROKE_WIDTH_INCREMENT = 1f;
    public static final float STROKE_MIN_WIDTH = 1f;
    
    private CD mCD = null;
    private Color mCurColorForPtCurve = null;
    public Color getCurColorForPtCurve() {
        return this.mCurColorForPtCurve;
    }
    public void setCurColorForPtCurve(Color c) {
        this.mCurColorForPtCurve = c;
    }
    private Stroke mCurStrokeForPtCurve = null;
    public Stroke getCurStrokeForPtCurve() {
        return this.mCurStrokeForPtCurve;
    }
    public void setCurStrokeForPtCurve(Stroke stroke)  {
        this.mCurStrokeForPtCurve = stroke;
    }
    
    private Stroke mSavedStrokePtCurve = null;
    public Stroke getSavedStrokeForPtCurve() {
        return this.mSavedStrokePtCurve;
    }
    public void setSavedStrokeForPtCurve(Stroke stroke)  {
        this.mSavedStrokePtCurve = stroke;
    }
    
    public CDCanvas2D(CD cd) {
        this.mCD = cd;
        this.mCurColorForPtCurve = CDCanvas2D.COLOR_PT_CURVE_DEFAULT;
        this.mCurStrokeForPtCurve = CDCanvas2D.STROKE_PT_CURVE_DEFAULT;
        this.mSavedStrokePtCurve = 
            CDCanvas2D.SAVED_STROKE_PT_CURVE_DEFAULT;
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        this.setSize(this.mCD.getFrame().getWidth(), 
            this.mCD.getFrame().getHeight());
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.transform(this.mCD.getXform().getCurXformFromWorldToScreen());
        
        // render commom world objects
        this.drawPtCurves(g2);
        this.drawSelectedPtCurves(g2);
        this.drawCurPtCurve(g2);
        
        g2.transform(this.mCD.getXform().getCurXformFromScreenToWorld());
        
        // render the current scene's world objects
        CDScene curScene = (CDScene) this.mCD.getScenarioMgr().getCurScene();
        
        //render common screen objects
        this.drawInfo(g2);    
        
        //render the current scene's screen objects
        curScene.renderScreenObjects(g2);
    }
    
    private void drawPtCurve(Graphics2D g2, CDPtCurve ptCurve, Color c, 
        Stroke s, int branch){
        Path2D.Double path = new Path2D.Double();
        ArrayList<Point2D.Double> PDFPts = ptCurve.getPts();
        if (PDFPts.size() < 2) {
            return;
        }
        double deltaX = branch * CDPDFViewer.PAGE_ROW_INTERVAL;
        double deltaY = this.mCD.getBranchYPoses().get(branch);
        ArrayList<Point2D.Double> pts = new ArrayList<>();
        for (Point2D.Double p : PDFPts) {
            pts.add(new Point2D.Double(p.x + deltaX, p.y + deltaY));
        }
        Point2D.Double pt = pts.get(0);
        path.moveTo(pt.x, pt.y);
        if (pts.size() < 3) {
            pt = pts.get(1);
            path.lineTo(pt.x, pt.y);
        } else {
            Point2D.Double pt0 = null;
            Point2D.Double pt1 = pts.get(0);
            Point2D.Double pt2 = pts.get(1);
            Point2D.Double pt3 = pts.get(2);
            Point2D.Double ctrlPt1 = new Point2D.Double(
                pt1.x*(2.0/3.0)+pt2.x*(1.0/3.0),
                pt1.y*(2.0/3.0)+pt2.y*(1.0/3.0));
            Point2D.Double ctrlPt2 = new Point2D.Double(
                (1.0 / 6.0)*(pt1.x - pt3.x) + pt2.x,
                (1.0 / 6.0)*(pt1.y - pt3.y) + pt2.y);
            path.curveTo(ctrlPt1.x, ctrlPt1.y, ctrlPt2.x, ctrlPt2.y, 
                pt2.x, pt2.y);
            
            for (int i = 1; i < (pts.size() - 2); i++) {
                pt0 = pts.get(i - 1);
                pt1 = pts.get(i);
                pt2 = pts.get(i + 1);
                pt3 = pts.get(i + 2);
                ctrlPt1 = new Point2D.Double(
                    (1.0 / 6.0) * (pt2.x - pt0.x) + pt1.x,
                    (1.0 / 6.0) * (pt2.y - pt0.y) + pt1.y);
                ctrlPt2 = new Point2D.Double(
                    (1.0 / 6.0) * (pt1.x - pt3.x) + pt2.x,
                    (1.0 / 6.0) * (pt1.y - pt3.y) + pt2.y);
                path.curveTo(ctrlPt1.x, ctrlPt1.y, ctrlPt2.x, ctrlPt2.y, 
                    pt2.x, pt2.y);                   
            }
            ctrlPt1 = new Point2D.Double(
                (1.0 / 6.0) * (pt3.x - pt1.x) + pt2.x,
                (1.0 / 6.0) * (pt3.y - pt1.y) + pt2.y);
            ctrlPt2 = new Point2D.Double(
                pt3.x * (2.0 / 3.0) + pt2.x * (1.0 / 3.0),
                pt3.y * (2.0 / 3.0) + pt2.y * (1.0 / 3.0));
            path.curveTo(ctrlPt1.x, ctrlPt1.y, ctrlPt2.x, ctrlPt2.y, 
                pt3.x, pt3.y);          
        }
        
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(path);
    }

    private void drawPtCurves(Graphics2D g2) {
        for (int b=0; b < this.mCD.getBranchYPoses().size(); b++) {
            for (CDPtCurve ptCurve: this.mCD.getPtCurveMgr().getPtCurves()) {
                this.drawPtCurve(g2, ptCurve, ptCurve.getColor(), 
                    ptCurve.getStroke(), b);
            }
        }
    }

    private void drawCurPtCurve(Graphics2D g2) {
        CDPtCurve ptCurve = this.mCD.getPtCurveMgr().getCurPtCurve();
        if(ptCurve != null){
            for (int b=0; b < this.mCD.getBranchYPoses().size(); b++) {
                this.drawPtCurve(g2, ptCurve, ptCurve.getColor(), 
                    ptCurve.getStroke(), b);
            }
        }
    }

    private void drawSelectedPtCurves(Graphics2D g2) {
        for (int b=0; b < this.mCD.getBranchYPoses().size(); b++) {
            for (CDPtCurve ptCurve: 
                this.mCD.getPtCurveMgr().getSelectedPtCurves()) {
                this.drawPtCurve(g2, ptCurve, 
                    CDCanvas2D.COLOR_SELECTED_PT_CURVE, 
                    ptCurve.getStroke(), b);
            };
        }
    }

    private void drawInfo(Graphics2D g2) {
        CDScene curScene = (CDScene) this.mCD.getScenarioMgr().getCurScene();
        String str = curScene.getClass().getSimpleName();
        g2.setColor(CDCanvas2D.COLOR_INFO);
        g2.setFont(CDCanvas2D.FONT_INFO);
        g2.drawString(str, this.getWidth() - CDCanvas2D.INFO_TOP_ALIGNMENT_X, 
            this.getHeight() - CDCanvas2D.INFO_TOP_ALIGNMENT_Y);
    } 

    public void increaseStrokeWidthForCurPtCurve(float f) {
        BasicStroke bs = (BasicStroke) this.mCurStrokeForPtCurve;
        float w = bs.getLineWidth();
        w += f;
        if (w < CDCanvas2D.STROKE_MIN_WIDTH) {
            w = CDCanvas2D.STROKE_MIN_WIDTH;
        }
        this.mCurStrokeForPtCurve = new BasicStroke(w, bs.getEndCap(),
            bs.getLineJoin());
    }
}
