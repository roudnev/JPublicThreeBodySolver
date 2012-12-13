package main;

/*
 * ThreeBodySolverConfiguratorView.java
 */



//import main.ConfiguratorEssentials;
//import main.ThreeBodySolverConfiguratorApp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
//import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import gridGenerator.AutoGridGenerator;
import potentialCollection.PotentialsRegister;
//import java.io.PrintWriter;
//import gridGenerator.MyTextOutputJFrame;
//import org.jdesktop.application.TaskMonitor;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.Timer;
//import javax.swing.Icon;
//import javax.swing.JDialog;
//import javax.swing.JFrame;

/**
 * The application's main frame.
 */
public class ThreeBodySolverConfiguratorView extends FrameView {
// The functionality-related variables and methods
    ConfiguratorEssentials configurator;
    PotentialsRegister potentials;
    public ThreeBodySolverConfiguratorView(SingleFrameApplication app) {
        super(app);
        configurator=new ConfiguratorEssentials();
        potentials=configurator.potentials;
        initComponents();
        this.getFrame().setResizable(false);
        resetGUIControls();
        fixInitializationLocks();
    }

    void fixInitializationLocks()
    { if (configurator.ifIDMarked[0])
      { lock2parameters(true);
        lock3parameters(true);
      }
      else if (configurator.ifIDMarked[1])
        lock3parameters(true);
    }

    void resetGUIControls(){
        m1TextField.setText(""+configurator.particles[0].getM());
        m2TextField.setText(""+configurator.particles[1].getM());
        m3TextField.setText(""+configurator.particles[2].getM());
        reducedMassLabel1.setText("Reduced mass (me)="+configurator.reducedMass[0]);
        reducedMassLabel2.setText("Reduced mass (me)="+configurator.reducedMass[1]);
        reducedMassLabel3.setText("Reduced mass (me)="+configurator.reducedMass[2]);
        if (configurator.particles[0].isIfBoson())
            jBosonButton1.doClick();
        else
            jFermionButton1.doClick();
       if (configurator.particles[1].isIfBoson())
            jBosonButton2.doClick();
        else
            jFermionButton2.doClick();
        if (configurator.particles[2].isIfBoson())
            jBosonButton3.doClick();
        else
            jFermionButton3.doClick();
        jIDCheckBox1.setSelected(configurator.ifIDMarked[0]);
        jIDCheckBox2.setSelected(configurator.ifIDMarked[1]);
        jIDCheckBox3.setSelected(configurator.ifIDMarked[2]);
        p1TextField.setText(configurator.particles[0].getDescription());
        p2TextField.setText(configurator.particles[1].getDescription());
        p3TextField.setText(configurator.particles[2].getDescription());
        potentialBox1.setSelectedIndex(configurator.interactionIndex[0]);
        potentialBox2.setSelectedIndex(configurator.interactionIndex[1]);
        potentialBox3.setSelectedIndex(configurator.interactionIndex[2]);
        //
        nxTextField1.setText(configurator.gridsNx[0]+"");
        nyTextField1.setText(configurator.gridsNy[0]+"");
        nzTextField1.setText(configurator.gridsNz[0]+"");
        //
        nxTextField2.setText(configurator.gridsNx[1]+"");
        nyTextField2.setText(configurator.gridsNy[1]+"");
        nzTextField2.setText(configurator.gridsNz[1]+"");
        //
        nxTextField3.setText(configurator.gridsNx[2]+"");
        nyTextField3.setText(configurator.gridsNy[2]+"");
        nzTextField3.setText(configurator.gridsNz[2]+"");
        //
        gridGeneratedLabel1.setText("The grid is set to "+configurator.gridNames[0]);
        gridGeneratedLabel2.setText("The grid is set to "+configurator.gridNames[1]);
        gridGeneratedLabel3.setText("The grid is set to "+configurator.gridNames[2]);
        //
        yMaxField1.setText(configurator.yMax[0]+"");
        xMax1TextField.setText(configurator.xMax[0]+"");
        if (configurator.nComponents==1)
        {
        yMaxField2.setText(configurator.yMax[0]+"");
        yMaxField3.setText(configurator.yMax[0]+"");
        xMax2TextField.setText(configurator.xMax[0]+"");
        xMax3TextField.setText(configurator.xMax[0]+"");
        }
        else if (configurator.nComponents==2)
        {
        yMaxField2.setText(configurator.yMax[1]+"");
        yMaxField3.setText(configurator.yMax[1]+"");
        xMax2TextField.setText(configurator.xMax[1]+"");
        xMax3TextField.setText(configurator.xMax[1]+"");
        }
        else if (configurator.nComponents==3)
        {
        yMaxField2.setText(configurator.yMax[1]+"");
        yMaxField3.setText(configurator.yMax[2]+"");
        xMax2TextField.setText(configurator.xMax[1]+"");
        xMax3TextField.setText(configurator.xMax[2]+"");
        }
    }
    void lock2parameters(boolean ifLock){
        jIDCheckBox2.setEnabled(!ifLock);
        p2TextField.setEnabled(!ifLock);
        m2TextField.setEnabled(!ifLock);
        potentialBox2.setEnabled(!ifLock);
        jBosonButton2.setEnabled(!ifLock);
        jFermionButton2.setEnabled(!ifLock);
        xMax2TextField.setEditable(!ifLock);
        grid2GenerateButton.setEnabled(!ifLock);
        nxTextField2.setEnabled(!ifLock);
        nyTextField2.setEnabled(!ifLock);
        nzTextField2.setEnabled(!ifLock);
    }
    void lock3parameters(boolean ifLock){
        jIDCheckBox3.setEnabled(!ifLock);
        p3TextField.setEnabled(!ifLock);
        m3TextField.setEnabled(!ifLock);
        potentialBox3.setEnabled(!ifLock);
        jBosonButton3.setEnabled(!ifLock);
        jFermionButton3.setEnabled(!ifLock);
        xMax3TextField.setEditable(!ifLock);
        grid3GenerateButton.setEnabled(!ifLock);
        nxTextField3.setEnabled(!ifLock);
        nyTextField3.setEnabled(!ifLock);
        nzTextField3.setEnabled(!ifLock);
    }

    @Action

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        generateConfMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        particlesPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        p1TextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m1TextField = new javax.swing.JTextField();
        jIDCheckBox1 = new javax.swing.JCheckBox();
        p2TextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        m2TextField = new javax.swing.JTextField();
        jIDCheckBox2 = new javax.swing.JCheckBox();
        p3TextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        m3TextField = new javax.swing.JTextField();
        jIDCheckBox3 = new javax.swing.JCheckBox();
        gridParametersPane = new javax.swing.JTabbedPane();
        pair1Panel = new javax.swing.JPanel();
        jBosonButton1 = new javax.swing.JRadioButton();
        jFermionButton1 = new javax.swing.JRadioButton();
        potentialBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        reducedMassLabel1 = new javax.swing.JLabel();
        boxSize1Label = new javax.swing.JLabel();
        xMax1TextField = new javax.swing.JTextField();
        grid1GenerateButton = new javax.swing.JButton();
        gridGeneratedLabel1 = new javax.swing.JLabel();
        gridPointsLabel1 = new javax.swing.JLabel();
        nxTextField1 = new javax.swing.JTextField();
        nxLabel1 = new javax.swing.JLabel();
        nyLabel1 = new javax.swing.JLabel();
        nyTextField1 = new javax.swing.JTextField();
        nzLabel1 = new javax.swing.JLabel();
        nzTextField1 = new javax.swing.JTextField();
        yMaxLabel1 = new javax.swing.JLabel();
        yMaxField1 = new javax.swing.JTextField();
        pair2Panel = new javax.swing.JPanel();
        jBosonButton2 = new javax.swing.JRadioButton();
        jFermionButton2 = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        potentialBox2 = new javax.swing.JComboBox();
        reducedMassLabel2 = new javax.swing.JLabel();
        boxSize2Label = new javax.swing.JLabel();
        xMax2TextField = new javax.swing.JTextField();
        grid2GenerateButton = new javax.swing.JButton();
        gridGeneratedLabel2 = new javax.swing.JLabel();
        gridPointsLabel2 = new javax.swing.JLabel();
        nxLabel2 = new javax.swing.JLabel();
        nxTextField2 = new javax.swing.JTextField();
        nyLabel2 = new javax.swing.JLabel();
        nyTextField2 = new javax.swing.JTextField();
        nzLabel2 = new javax.swing.JLabel();
        nzTextField2 = new javax.swing.JTextField();
        yMaxField2 = new javax.swing.JTextField();
        yMaxLabel2 = new javax.swing.JLabel();
        pair3Panel = new javax.swing.JPanel();
        jBosonButton3 = new javax.swing.JRadioButton();
        jFermionButton3 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        potentialBox3 = new javax.swing.JComboBox();
        reducedMassLabel3 = new javax.swing.JLabel();
        boxSize3Label = new javax.swing.JLabel();
        xMax3TextField = new javax.swing.JTextField();
        grid3GenerateButton = new javax.swing.JButton();
        gridGeneratedLabel3 = new javax.swing.JLabel();
        gridPointsLabel3 = new javax.swing.JLabel();
        nxLabel3 = new javax.swing.JLabel();
        nxTextField3 = new javax.swing.JTextField();
        nyLabel3 = new javax.swing.JLabel();
        nyTextField3 = new javax.swing.JTextField();
        nzLabel3 = new javax.swing.JLabel();
        nzTextField3 = new javax.swing.JTextField();
        yMaxField3 = new javax.swing.JTextField();
        yMaxLabel3 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();

        jMenuBar1.setMaximumSize(new java.awt.Dimension(32769, 32769));
        jMenuBar1.setMinimumSize(new java.awt.Dimension(640, 35));
        jMenuBar1.setName("jMenuBar1"); // NOI18N
        jMenuBar1.setPreferredSize(new java.awt.Dimension(640, 35));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(main.ThreeBodySolverConfiguratorApp.class).getContext().getActionMap(ThreeBodySolverConfiguratorView.class, this);
        jMenu1.setAction(actionMap.get("quit")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(main.ThreeBodySolverConfiguratorApp.class).getContext().getResourceMap(ThreeBodySolverConfiguratorView.class);
        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        generateConfMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        generateConfMenuItem.setText(resourceMap.getString("generateConfMenuItem.text")); // NOI18N
        generateConfMenuItem.setName("generateConfMenuItem"); // NOI18N
        generateConfMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateConfMenuItemActionPerformed(evt);
            }
        });
        generateConfMenuItem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                generateConfMenuItemFocusGained(evt);
            }
        });
        jMenu1.add(generateConfMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N
        jMenuBar1.add(jMenu2);

        jPanel1.setMinimumSize(new java.awt.Dimension(640, 480));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(640, 480));

        particlesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("particlesPanel.border.title"))); // NOI18N
        particlesPanel.setFont(resourceMap.getFont("particlesPanel.font")); // NOI18N
        particlesPanel.setMinimumSize(new java.awt.Dimension(619, 187));
        particlesPanel.setName("particlesPanel"); // NOI18N
        particlesPanel.setPreferredSize(new java.awt.Dimension(619, 187));

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        p1TextField.setText(resourceMap.getString("p1TextField.text")); // NOI18N
        p1TextField.setName("p1TextField"); // NOI18N
        p1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p1TextFieldActionPerformed(evt);
            }
        });
        p1TextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                p1TextFieldFocusLost(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        m1TextField.setText(resourceMap.getString("m1TextField.text")); // NOI18N
        m1TextField.setName("m1TextField"); // NOI18N
        m1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m1TextFieldActionPerformed(evt);
            }
        });
        m1TextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m1TextFieldFocusLost(evt);
            }
        });

        jIDCheckBox1.setName("jIDCheckBox1"); // NOI18N
        jIDCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIDCheckBox1ActionPerformed(evt);
            }
        });

        p2TextField.setText(resourceMap.getString("p2TextField.text")); // NOI18N
        p2TextField.setName("p2TextField"); // NOI18N
        p2TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p2TextFieldActionPerformed(evt);
            }
        });
        p2TextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                p2TextFieldFocusLost(evt);
            }
        });

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        m2TextField.setText(resourceMap.getString("m2TextField.text")); // NOI18N
        m2TextField.setName("m2TextField"); // NOI18N
        m2TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m2TextFieldActionPerformed(evt);
            }
        });
        m2TextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m2TextFieldFocusLost(evt);
            }
        });

        jIDCheckBox2.setName("jIDCheckBox2"); // NOI18N
        jIDCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIDCheckBox2ActionPerformed(evt);
            }
        });

        p3TextField.setText(resourceMap.getString("p3TextField.text")); // NOI18N
        p3TextField.setName("p3TextField"); // NOI18N
        p3TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p3TextFieldActionPerformed(evt);
            }
        });
        p3TextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                p3TextFieldFocusLost(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        m3TextField.setText(resourceMap.getString("m3TextField.text")); // NOI18N
        m3TextField.setName("m3TextField"); // NOI18N
        m3TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m3TextFieldActionPerformed(evt);
            }
        });
        m3TextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m3TextFieldFocusLost(evt);
            }
        });

        jIDCheckBox3.setName("jIDCheckBox3"); // NOI18N
        jIDCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIDCheckBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout particlesPanelLayout = new javax.swing.GroupLayout(particlesPanel);
        particlesPanel.setLayout(particlesPanelLayout);
        particlesPanelLayout.setHorizontalGroup(
            particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(particlesPanelLayout.createSequentialGroup()
                .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(particlesPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(p3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(p2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(p1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(2, 2, 2)
                        .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(m1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(particlesPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)))
                .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(particlesPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel4))
                    .addGroup(particlesPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jIDCheckBox2)
                            .addComponent(jIDCheckBox1)
                            .addComponent(jIDCheckBox3))))
                .addContainerGap(250, Short.MAX_VALUE))
        );
        particlesPanelLayout.setVerticalGroup(
            particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(particlesPanelLayout.createSequentialGroup()
                .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(p1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(m1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jIDCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(p2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(m2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jIDCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(particlesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(p3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(m3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jIDCheckBox3))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        gridParametersPane.setMinimumSize(new java.awt.Dimension(617, 263));
        gridParametersPane.setName("pairsTabbedPane"); // NOI18N
        gridParametersPane.setPreferredSize(new java.awt.Dimension(627, 263));

        pair1Panel.setName("pair1Panel"); // NOI18N

        buttonGroup1.add(jBosonButton1);
        jBosonButton1.setText(resourceMap.getString("jBosonButton1.text")); // NOI18N
        jBosonButton1.setName("jBosonButton1"); // NOI18N
        jBosonButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jBosonButton1FocusGained(evt);
            }
        });

        buttonGroup1.add(jFermionButton1);
        jFermionButton1.setText(resourceMap.getString("jFermionButton1.text")); // NOI18N
        jFermionButton1.setName("jFermionButton1"); // NOI18N
        jFermionButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFermionButton1FocusGained(evt);
            }
        });

        potentialBox1.setModel(new javax.swing.DefaultComboBoxModel(potentials.getPotentials()));
        potentialBox1.setName("potentialBox1"); // NOI18N
        potentialBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                potentialBox1ActionPerformed(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        reducedMassLabel1.setText(resourceMap.getString("reducedMassLabel1.text")); // NOI18N
        reducedMassLabel1.setName("reducedMassLabel1"); // NOI18N

        boxSize1Label.setText(resourceMap.getString("boxSize1Label.text")); // NOI18N
        boxSize1Label.setName("boxSize1Label"); // NOI18N

        xMax1TextField.setText(resourceMap.getString("xMax1TextField.text")); // NOI18N
        xMax1TextField.setMinimumSize(new java.awt.Dimension(185, 31));
        xMax1TextField.setName("xMax1TextField"); // NOI18N
        xMax1TextField.setPreferredSize(new java.awt.Dimension(185, 31));

        grid1GenerateButton.setText(resourceMap.getString("grid1GenerateButton.text")); // NOI18N
        grid1GenerateButton.setName("grid1GenerateButton"); // NOI18N
        grid1GenerateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grid1GenerateButtonActionPerformed(evt);
            }
        });

        gridGeneratedLabel1.setText(resourceMap.getString("gridGeneratedLabel1.text")); // NOI18N
        gridGeneratedLabel1.setName("gridGeneratedLabel1"); // NOI18N

        gridPointsLabel1.setText(resourceMap.getString("gridPointsLabel1.text")); // NOI18N
        gridPointsLabel1.setName("gridPointsLabel1"); // NOI18N

        nxTextField1.setText(resourceMap.getString("nxTextField1.text")); // NOI18N
        nxTextField1.setName("nxTextField1"); // NOI18N
        nxTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxTextField1ActionPerformed(evt);
            }
        });
        nxTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nxTextField1FocusLost(evt);
            }
        });

        nxLabel1.setText(resourceMap.getString("nxLabel1.text")); // NOI18N
        nxLabel1.setName("nxLabel1"); // NOI18N

        nyLabel1.setText(resourceMap.getString("nyLabel1.text")); // NOI18N
        nyLabel1.setName("nyLabel1"); // NOI18N

        nyTextField1.setText(resourceMap.getString("nyTextField1.text")); // NOI18N
        nyTextField1.setName("nyTextField1"); // NOI18N
        nyTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyTextField1ActionPerformed(evt);
            }
        });
        nyTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nyTextField1FocusLost(evt);
            }
        });

        nzLabel1.setText(resourceMap.getString("nzLabel1.text")); // NOI18N
        nzLabel1.setName("nzLabel1"); // NOI18N

        nzTextField1.setText(resourceMap.getString("nzTextField1.text")); // NOI18N
        nzTextField1.setName("nzTextField1"); // NOI18N
        nzTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nzTextField1ActionPerformed(evt);
            }
        });
        nzTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nzTextField1FocusLost(evt);
            }
        });

        yMaxLabel1.setText(resourceMap.getString("yMaxLabel1.text")); // NOI18N
        yMaxLabel1.setName("yMaxLabel1"); // NOI18N

        yMaxField1.setText(resourceMap.getString("yMaxField1.text")); // NOI18N
        yMaxField1.setName("yMaxField1"); // NOI18N
        yMaxField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yMaxField1ActionPerformed(evt);
            }
        });
        yMaxField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                yMaxField1FocusLost(evt);
            }
        });

        javax.swing.GroupLayout pair1PanelLayout = new javax.swing.GroupLayout(pair1Panel);
        pair1Panel.setLayout(pair1PanelLayout);
        pair1PanelLayout.setHorizontalGroup(
            pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pair1PanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reducedMassLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                    .addComponent(gridGeneratedLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                    .addGroup(pair1PanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(potentialBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBosonButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFermionButton1))
                    .addGroup(pair1PanelLayout.createSequentialGroup()
                        .addComponent(boxSize1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xMax1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grid1GenerateButton)))
                .addGap(14, 14, 14))
            .addGroup(pair1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gridPointsLabel1)
                .addGap(18, 18, 18)
                .addComponent(nxLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nxTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pair1PanelLayout.createSequentialGroup()
                        .addComponent(yMaxLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yMaxField1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pair1PanelLayout.createSequentialGroup()
                        .addComponent(nyLabel1)
                        .addGap(11, 11, 11)
                        .addComponent(nyTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nzLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nzTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(190, 190, 190))
        );

        pair1PanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nxLabel1, nxTextField1, nyLabel1, nyTextField1, nzLabel1, nzTextField1});

        pair1PanelLayout.setVerticalGroup(
            pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pair1PanelLayout.createSequentialGroup()
                .addGroup(pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jBosonButton1)
                    .addComponent(jFermionButton1)
                    .addComponent(potentialBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reducedMassLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grid1GenerateButton)
                    .addComponent(boxSize1Label)
                    .addComponent(xMax1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gridGeneratedLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gridPointsLabel1)
                    .addComponent(nxLabel1)
                    .addComponent(nxTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nyLabel1)
                    .addComponent(nyTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nzLabel1)
                    .addComponent(nzTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yMaxLabel1)
                    .addComponent(yMaxField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        gridParametersPane.addTab(resourceMap.getString("pair1Panel.TabConstraints.tabTitle"), pair1Panel); // NOI18N

        pair2Panel.setName("pair2Panel"); // NOI18N

        buttonGroup2.add(jBosonButton2);
        jBosonButton2.setText(resourceMap.getString("jBosonButton2.text")); // NOI18N
        jBosonButton2.setName("jBosonButton2"); // NOI18N
        jBosonButton2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jBosonButton2FocusGained(evt);
            }
        });

        buttonGroup2.add(jFermionButton2);
        jFermionButton2.setText(resourceMap.getString("jFermionButton2.text")); // NOI18N
        jFermionButton2.setName("jFermionButton2"); // NOI18N
        jFermionButton2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFermionButton2FocusGained(evt);
            }
        });

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        potentialBox2.setModel(new javax.swing.DefaultComboBoxModel(potentials.getPotentials()));
        potentialBox2.setName("potentialBox2"); // NOI18N
        potentialBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                potentialBox2ActionPerformed(evt);
            }
        });

        reducedMassLabel2.setText(resourceMap.getString("reducedMassLabel2.text")); // NOI18N
        reducedMassLabel2.setName("reducedMassLabel2"); // NOI18N

        boxSize2Label.setText(resourceMap.getString("boxSize2Label.text")); // NOI18N
        boxSize2Label.setName("boxSize2Label"); // NOI18N

        xMax2TextField.setText(resourceMap.getString("xMax2TextField.text")); // NOI18N
        xMax2TextField.setMinimumSize(new java.awt.Dimension(185, 31));
        xMax2TextField.setName("xMax2TextField"); // NOI18N

        grid2GenerateButton.setText(resourceMap.getString("grid2GenerateButton.text")); // NOI18N
        grid2GenerateButton.setName("grid2GenerateButton"); // NOI18N
        grid2GenerateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grid2GenerateButtonActionPerformed(evt);
            }
        });

        gridGeneratedLabel2.setText(resourceMap.getString("gridGeneratedLabel2.text")); // NOI18N
        gridGeneratedLabel2.setName("gridGeneratedLabel2"); // NOI18N

        gridPointsLabel2.setText(resourceMap.getString("gridPointsLabel2.text")); // NOI18N
        gridPointsLabel2.setName("gridPointsLabel2"); // NOI18N

        nxLabel2.setText(resourceMap.getString("nxLabel2.text")); // NOI18N
        nxLabel2.setName("nxLabel2"); // NOI18N

        nxTextField2.setText(resourceMap.getString("nxTextField2.text")); // NOI18N
        nxTextField2.setName("nxTextField2"); // NOI18N
        nxTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxTextField2ActionPerformed(evt);
            }
        });
        nxTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nxTextField2FocusLost(evt);
            }
        });

        nyLabel2.setText(resourceMap.getString("nyLabel2.text")); // NOI18N
        nyLabel2.setName("nyLabel2"); // NOI18N

        nyTextField2.setText(resourceMap.getString("nyTextField2.text")); // NOI18N
        nyTextField2.setName("nyTextField2"); // NOI18N
        nyTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyTextField2ActionPerformed(evt);
            }
        });
        nyTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nyTextField2FocusLost(evt);
            }
        });

        nzLabel2.setText(resourceMap.getString("nzLabel2.text")); // NOI18N
        nzLabel2.setName("nzLabel2"); // NOI18N

        nzTextField2.setText(resourceMap.getString("nzTextField2.text")); // NOI18N
        nzTextField2.setName("nzTextField2"); // NOI18N
        nzTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nzTextField2ActionPerformed(evt);
            }
        });
        nzTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nzTextField2FocusLost(evt);
            }
        });

        yMaxField2.setText(resourceMap.getString("yMaxField2.text")); // NOI18N
        yMaxField2.setName("yMaxField2"); // NOI18N
        yMaxField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yMaxField2ActionPerformed(evt);
            }
        });
        yMaxField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                yMaxField2FocusLost(evt);
            }
        });

        yMaxLabel2.setText(resourceMap.getString("yMaxLabel2.text")); // NOI18N
        yMaxLabel2.setName("yMaxLabel2"); // NOI18N

        javax.swing.GroupLayout pair2PanelLayout = new javax.swing.GroupLayout(pair2Panel);
        pair2Panel.setLayout(pair2PanelLayout);
        pair2PanelLayout.setHorizontalGroup(
            pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pair2PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reducedMassLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                    .addComponent(gridGeneratedLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                    .addGroup(pair2PanelLayout.createSequentialGroup()
                        .addComponent(boxSize2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xMax2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grid2GenerateButton))
                    .addGroup(pair2PanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(potentialBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBosonButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFermionButton2))
                    .addGroup(pair2PanelLayout.createSequentialGroup()
                        .addComponent(gridPointsLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(nxLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nxTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pair2PanelLayout.createSequentialGroup()
                                .addComponent(yMaxLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yMaxField2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pair2PanelLayout.createSequentialGroup()
                                .addComponent(nyLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nyTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nzLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nzTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(177, 177, 177)))
                .addContainerGap())
        );

        pair2PanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nxLabel2, nxTextField2, nyLabel2, nyTextField2, nzLabel2, nzTextField2});

        pair2PanelLayout.setVerticalGroup(
            pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pair2PanelLayout.createSequentialGroup()
                .addGroup(pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(potentialBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jFermionButton2)
                    .addComponent(jBosonButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reducedMassLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxSize2Label)
                    .addComponent(xMax2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grid2GenerateButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gridGeneratedLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gridPointsLabel2)
                    .addComponent(nxLabel2)
                    .addComponent(nxTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nyLabel2)
                    .addComponent(nyTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nzLabel2)
                    .addComponent(nzTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yMaxLabel2)
                    .addComponent(yMaxField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        gridParametersPane.addTab(resourceMap.getString("pair2Panel.TabConstraints.tabTitle"), pair2Panel); // NOI18N

        pair3Panel.setName("pair3Panel"); // NOI18N

        buttonGroup3.add(jBosonButton3);
        jBosonButton3.setText(resourceMap.getString("jBosonButton3.text")); // NOI18N
        jBosonButton3.setName("jBosonButton3"); // NOI18N
        jBosonButton3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jBosonButton3FocusGained(evt);
            }
        });

        buttonGroup3.add(jFermionButton3);
        jFermionButton3.setText(resourceMap.getString("jFermionButton3.text")); // NOI18N
        jFermionButton3.setName("jFermionButton3"); // NOI18N
        jFermionButton3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFermionButton3FocusGained(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        potentialBox3.setModel(new javax.swing.DefaultComboBoxModel(potentials.getPotentials()));
        potentialBox3.setName("potentialBox3"); // NOI18N
        potentialBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                potentialBox3ActionPerformed(evt);
            }
        });

        reducedMassLabel3.setText(resourceMap.getString("reducedMassLabel3.text")); // NOI18N
        reducedMassLabel3.setName("reducedMassLabel3"); // NOI18N

        boxSize3Label.setText(resourceMap.getString("boxSize3Label.text")); // NOI18N
        boxSize3Label.setName("boxSize3Label"); // NOI18N

        xMax3TextField.setText(resourceMap.getString("xMax3TextField.text")); // NOI18N
        xMax3TextField.setName("xMax3TextField"); // NOI18N

        grid3GenerateButton.setText(resourceMap.getString("grid3GenerateButton.text")); // NOI18N
        grid3GenerateButton.setName("grid3GenerateButton"); // NOI18N
        grid3GenerateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grid3GenerateButtonActionPerformed(evt);
            }
        });

        gridGeneratedLabel3.setText(resourceMap.getString("gridGeneratedLabel3.text")); // NOI18N
        gridGeneratedLabel3.setName("gridGeneratedLabel3"); // NOI18N

        gridPointsLabel3.setText(resourceMap.getString("gridPointsLabel3.text")); // NOI18N
        gridPointsLabel3.setName("gridPointsLabel3"); // NOI18N

        nxLabel3.setText(resourceMap.getString("nxLabel3.text")); // NOI18N
        nxLabel3.setName("nxLabel3"); // NOI18N

        nxTextField3.setText(resourceMap.getString("nxTextField3.text")); // NOI18N
        nxTextField3.setName("nxTextField3"); // NOI18N
        nxTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxTextField3ActionPerformed(evt);
            }
        });
        nxTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nxTextField3FocusLost(evt);
            }
        });

        nyLabel3.setText(resourceMap.getString("nyLabel3.text")); // NOI18N
        nyLabel3.setName("nyLabel3"); // NOI18N

        nyTextField3.setText(resourceMap.getString("nyTextField3.text")); // NOI18N
        nyTextField3.setName("nyTextField3"); // NOI18N
        nyTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyTextField3ActionPerformed(evt);
            }
        });
        nyTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nyTextField3FocusLost(evt);
            }
        });

        nzLabel3.setText(resourceMap.getString("nzLabel3.text")); // NOI18N
        nzLabel3.setName("nzLabel3"); // NOI18N

        nzTextField3.setText(resourceMap.getString("nzTextField3.text")); // NOI18N
        nzTextField3.setName("nzTextField3"); // NOI18N
        nzTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nzTextField3ActionPerformed(evt);
            }
        });
        nzTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nzTextField3FocusLost(evt);
            }
        });

        yMaxField3.setText(resourceMap.getString("yMaxField3.text")); // NOI18N
        yMaxField3.setName("yMaxField3"); // NOI18N
        yMaxField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yMaxField3ActionPerformed(evt);
            }
        });
        yMaxField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                yMaxField3FocusLost(evt);
            }
        });

        yMaxLabel3.setText(resourceMap.getString("yMaxLabel3.text")); // NOI18N
        yMaxLabel3.setName("yMaxLabel3"); // NOI18N

        javax.swing.GroupLayout pair3PanelLayout = new javax.swing.GroupLayout(pair3Panel);
        pair3Panel.setLayout(pair3PanelLayout);
        pair3PanelLayout.setHorizontalGroup(
            pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pair3PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boxSize3Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xMax3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grid3GenerateButton)
                .addContainerGap(182, Short.MAX_VALUE))
            .addGroup(pair3PanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(potentialBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBosonButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFermionButton3)
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(pair3PanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(gridGeneratedLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(pair3PanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(reducedMassLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(pair3PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gridPointsLabel3)
                .addGap(18, 18, 18)
                .addComponent(nxLabel3)
                .addGap(11, 11, 11)
                .addComponent(nxTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pair3PanelLayout.createSequentialGroup()
                        .addComponent(yMaxLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yMaxField3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pair3PanelLayout.createSequentialGroup()
                        .addComponent(nyLabel3)
                        .addGap(11, 11, 11)
                        .addComponent(nyTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nzLabel3)
                        .addGap(11, 11, 11)
                        .addComponent(nzTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(145, Short.MAX_VALUE))
        );

        pair3PanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nxLabel3, nxTextField3, nyLabel3, nyTextField3, nzLabel3, nzTextField3});

        pair3PanelLayout.setVerticalGroup(
            pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pair3PanelLayout.createSequentialGroup()
                .addGroup(pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(potentialBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jBosonButton3)
                    .addComponent(jFermionButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reducedMassLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxSize3Label)
                    .addComponent(grid3GenerateButton)
                    .addComponent(xMax3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gridGeneratedLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gridPointsLabel3)
                    .addComponent(nxTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nxLabel3)
                    .addComponent(nyTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nyLabel3)
                    .addComponent(nzTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nzLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pair3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yMaxLabel3)
                    .addComponent(yMaxField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        gridParametersPane.addTab(resourceMap.getString("pair3Panel.TabConstraints.tabTitle"), pair3Panel); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gridParametersPane, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(particlesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(particlesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gridParametersPane, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridParametersPane.getAccessibleContext().setAccessibleName(resourceMap.getString("pairsTabbedPane.AccessibleContext.accessibleName")); // NOI18N

        jPanel1.getAccessibleContext().setAccessibleName(resourceMap.getString("jPanel1.AccessibleContext.accessibleName")); // NOI18N

        setComponent(jPanel1);
        setMenuBar(jMenuBar1);
    }// </editor-fold>//GEN-END:initComponents

    private void p1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p1TextFieldActionPerformed
        configurator.particles[0].setDescription(p1TextField.getText());
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println(configurator.particles[0].getDescription());
}//GEN-LAST:event_p1TextFieldActionPerformed

    private void p1TextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p1TextFieldFocusLost
        configurator.particles[0].setDescription(p1TextField.getText());
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println(configurator.particles[0].getDescription());
}//GEN-LAST:event_p1TextFieldFocusLost

    private void m1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m1TextFieldActionPerformed
        configurator.particles[0].setM(Double.parseDouble(m1TextField.getText()));
        configurator.performIDCheck();
        configurator.recalculateReducedMasses();
        resetGUIControls();
//        System.out.println(configurator.particles[0].getM());
    }//GEN-LAST:event_m1TextFieldActionPerformed

    private void m1TextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m1TextFieldFocusLost
        configurator.particles[0].setM(Double.parseDouble(m1TextField.getText()));
        configurator.performIDCheck();
        configurator.recalculateReducedMasses();
        resetGUIControls();
//        System.out.println(configurator.particles[0].getM());
    }//GEN-LAST:event_m1TextFieldFocusLost

    private void p2TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p2TextFieldActionPerformed
        configurator.particles[1].setDescription(p2TextField.getText());
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println(configurator.particles[1].getDescription());
    }//GEN-LAST:event_p2TextFieldActionPerformed

    private void p2TextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p2TextFieldFocusLost
        configurator.particles[1].setDescription(p2TextField.getText());
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println(configurator.particles[1].getDescription());
    }//GEN-LAST:event_p2TextFieldFocusLost

    private void m2TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m2TextFieldActionPerformed
        configurator.particles[1].setM(Double.parseDouble(m2TextField.getText()));
        configurator.performIDCheck();
        configurator.recalculateReducedMasses();
        resetGUIControls();
//        System.out.println(configurator.particles[1].getM());
    }//GEN-LAST:event_m2TextFieldActionPerformed

    private void m2TextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m2TextFieldFocusLost
        configurator.particles[1].setM(Double.parseDouble(m2TextField.getText()));
        configurator.performIDCheck();
        configurator.recalculateReducedMasses();
        resetGUIControls();
//        System.out.println(configurator.particles[1].getM());
    }//GEN-LAST:event_m2TextFieldFocusLost

    private void p3TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p3TextFieldActionPerformed
        configurator.particles[2].setDescription(p3TextField.getText());
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println(configurator.particles[2].getDescription());
    }//GEN-LAST:event_p3TextFieldActionPerformed

    private void p3TextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p3TextFieldFocusLost
        configurator.particles[2].setDescription(p3TextField.getText());
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println(configurator.particles[2].getDescription());
    }//GEN-LAST:event_p3TextFieldFocusLost

    private void m3TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m3TextFieldActionPerformed
        configurator.particles[2].setM(Double.parseDouble(m3TextField.getText()));
        configurator.performIDCheck();
        configurator.recalculateReducedMasses();
        resetGUIControls();
//        System.out.println(configurator.particles[2].getM());
    }//GEN-LAST:event_m3TextFieldActionPerformed

    private void m3TextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m3TextFieldFocusLost
        configurator.particles[2].setM(Double.parseDouble(m3TextField.getText()));
        configurator.performIDCheck();
        configurator.recalculateReducedMasses();
        resetGUIControls();
//        System.out.println(configurator.particles[2].getM());
    }//GEN-LAST:event_m3TextFieldFocusLost

    private void generateConfMenuItemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_generateConfMenuItemFocusGained
        configurator.writeConfigurations();
//        System.out.println("something... something...");
}//GEN-LAST:event_generateConfMenuItemFocusGained

    private void generateConfMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateConfMenuItemActionPerformed
        configurator.writeConfigurations();
//        System.out.println("something... something...else");
}//GEN-LAST:event_generateConfMenuItemActionPerformed

    private void jIDCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIDCheckBox1ActionPerformed
        configurator.ifIDMarked[0]=jIDCheckBox1.isSelected();
        if (configurator.ifIDMarked[0]){
          if (!configurator.ifIDMarked[1]) jIDCheckBox2.doClick();
          lock2parameters(true);
          lock3parameters(true);
        }
        else{
          lock2parameters(false);
          lock3parameters(false);
          jIDCheckBox2.doClick();
        }
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println("Particle 1 is marked identical to something="+configurator.ifIDMarked[0]);
    }//GEN-LAST:event_jIDCheckBox1ActionPerformed

    private void jIDCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIDCheckBox2ActionPerformed
        configurator.ifIDMarked[1]=jIDCheckBox2.isSelected();
        if (configurator.ifIDMarked[1]){
          if (!configurator.ifIDMarked[2])    jIDCheckBox3.doClick();
          lock3parameters(true);
        }
        else{
            lock3parameters(false);
            jIDCheckBox3.doClick();
        }
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println("Particle 2 is marked identical to something="+configurator.ifIDMarked[1]);
    }//GEN-LAST:event_jIDCheckBox2ActionPerformed

    private void jIDCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIDCheckBox3ActionPerformed
        configurator.ifIDMarked[2]=jIDCheckBox2.isSelected();
        configurator.performIDCheck();
        resetGUIControls();
//        System.out.println("Particle 3 is marked identical to something="+configurator.ifIDMarked[2]);
    }//GEN-LAST:event_jIDCheckBox3ActionPerformed

    private void grid3GenerateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_grid3GenerateButtonActionPerformed
    {//GEN-HEADEREND:event_grid3GenerateButtonActionPerformed
      // TODO add your handling code here:
      String potentialName=configurator.interactions[2];
      double reducedMass=configurator.reducedMass[2];
      double massNorm=configurator.massNorm;
      double cutOff=Double.parseDouble(xMax3TextField.getText());
      configurator.xMax[2]=cutOff;
      try
      {
        AutoGridGenerator gridGenerator = new AutoGridGenerator(potentialName, reducedMass, cutOff, massNorm);
        Thread thr=new Thread(gridGenerator);
        thr.start();
        configurator.gridNames[2]=gridGenerator.getGridName();
        gridGeneratedLabel3.setText("The grid is set to "+configurator.gridNames[2]);
      }
      catch (Exception ex)
      {
        Logger.getLogger(ThreeBodySolverConfiguratorView.class.getName()).log(Level.SEVERE, null, ex);
      }
}//GEN-LAST:event_grid3GenerateButtonActionPerformed

    private void potentialBox3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_potentialBox3ActionPerformed
    {//GEN-HEADEREND:event_potentialBox3ActionPerformed
      // TODO add your handling code here:
      configurator.interactions[2]=(String)potentialBox3.getSelectedItem();
      configurator.interactionIndex[2]=potentialBox3.getSelectedIndex();
      //      configurator.performIDCheck();
      //      resetGUIControls();
      //      String str=(String)potentialBox3.getSelectedItem();
      //      System.out.print("Interaction set to ");
      //      System.out.print(configurator.interactions[2]);
      //      System.out.println(str);
}//GEN-LAST:event_potentialBox3ActionPerformed

    private void jFermionButton3FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_jFermionButton3FocusGained
    {//GEN-HEADEREND:event_jFermionButton3FocusGained
      configurator.particles[2].setIfBoson(false);
      configurator.performIDCheck();
      resetGUIControls();
      //        System.out.println("particle 3 is a boson =" +configurator.particles[2].isIfBoson());
}//GEN-LAST:event_jFermionButton3FocusGained

    private void jBosonButton3FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_jBosonButton3FocusGained
    {//GEN-HEADEREND:event_jBosonButton3FocusGained
      configurator.particles[2].setIfBoson(true);
      configurator.performIDCheck();
      resetGUIControls();
      //        System.out.println("particle 3 is a boson =" +configurator.particles[2].isIfBoson());
}//GEN-LAST:event_jBosonButton3FocusGained

    private void grid2GenerateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_grid2GenerateButtonActionPerformed
    {//GEN-HEADEREND:event_grid2GenerateButtonActionPerformed
      // TODO add your handling code here:
      String potentialName=configurator.interactions[1];
      double reducedMass=configurator.reducedMass[1];
      double massNorm=configurator.massNorm;
      double cutOff=Double.parseDouble(xMax2TextField.getText());
      configurator.xMax[1]=cutOff;
      try
      {
        AutoGridGenerator gridGenerator = new AutoGridGenerator(potentialName, reducedMass, cutOff, massNorm);
        Thread thr=new Thread(gridGenerator);
        thr.start();
        configurator.gridNames[1]=gridGenerator.getGridName();
        gridGeneratedLabel2.setText("The grid is set to "+configurator.gridNames[1]);
      }
      catch (Exception ex)
      {
        Logger.getLogger(ThreeBodySolverConfiguratorView.class.getName()).log(Level.SEVERE, null, ex);
      }
}//GEN-LAST:event_grid2GenerateButtonActionPerformed

    private void potentialBox2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_potentialBox2ActionPerformed
    {//GEN-HEADEREND:event_potentialBox2ActionPerformed
      // TODO add your handling code here:
      configurator.interactions[1]=(String)potentialBox2.getSelectedItem();
      configurator.interactionIndex[1]=potentialBox2.getSelectedIndex();
      configurator.performIDCheck();
      //      resetGUIControls();
      //       String str=(String)potentialBox2.getSelectedItem();
      //      System.out.print("Interaction set to ");
      //      System.out.print(configurator.interactions[1]);
      //      System.out.println(str);
}//GEN-LAST:event_potentialBox2ActionPerformed

    private void jFermionButton2FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_jFermionButton2FocusGained
    {//GEN-HEADEREND:event_jFermionButton2FocusGained
      configurator.particles[1].setIfBoson(false);
      configurator.performIDCheck();
      resetGUIControls();
      //        System.out.println("particle 2 is a boson =" +configurator.particles[1].isIfBoson());
}//GEN-LAST:event_jFermionButton2FocusGained

    private void jBosonButton2FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_jBosonButton2FocusGained
    {//GEN-HEADEREND:event_jBosonButton2FocusGained
      configurator.particles[1].setIfBoson(true);
      configurator.performIDCheck();
      resetGUIControls();
      //        System.out.println("particle 2 is a boson =" +configurator.particles[1].isIfBoson());
}//GEN-LAST:event_jBosonButton2FocusGained

    private void grid1GenerateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_grid1GenerateButtonActionPerformed
    {//GEN-HEADEREND:event_grid1GenerateButtonActionPerformed
      // TODO add your handling code here:
      String potentialName=configurator.interactions[0];
      double reducedMass=configurator.reducedMass[0];
      double massNorm=configurator.massNorm;
      double cutOff=Double.parseDouble(xMax1TextField.getText());
      configurator.xMax[0]=cutOff;
      try
      {
        AutoGridGenerator gridGenerator = new AutoGridGenerator(potentialName, reducedMass, cutOff, massNorm);
        Thread thr=new Thread(gridGenerator);
        thr.start();
        configurator.gridNames[0]=gridGenerator.getGridName();
        gridGeneratedLabel1.setText("The grid is set to "+configurator.gridNames[0]);
      }
      catch (Exception ex)
      {
        Logger.getLogger(ThreeBodySolverConfiguratorView.class.getName()).log(Level.SEVERE, null, ex);
      }
}//GEN-LAST:event_grid1GenerateButtonActionPerformed

    private void potentialBox1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_potentialBox1ActionPerformed
    {//GEN-HEADEREND:event_potentialBox1ActionPerformed
      // TODO add your handling code here:
      configurator.interactions[0]=(String)potentialBox1.getSelectedItem();
      configurator.interactionIndex[0]=potentialBox1.getSelectedIndex();
      configurator.performIDCheck();
      //      resetGUIControls();
      //      String str=(String)potentialBox1.getSelectedItem();
      //      System.out.print("Interaction set to ");
      //      System.out.print(configurator.interactions[0]);
      //      System.out.println(str);
}//GEN-LAST:event_potentialBox1ActionPerformed

    private void jFermionButton1FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_jFermionButton1FocusGained
    {//GEN-HEADEREND:event_jFermionButton1FocusGained
      configurator.particles[0].setIfBoson(false);
      configurator.performIDCheck();
      resetGUIControls();
      //        System.out.println("particle 1 is a boson =" +configurator.particles[0].isIfBoson());
}//GEN-LAST:event_jFermionButton1FocusGained

    private void jBosonButton1FocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_jBosonButton1FocusGained
    {//GEN-HEADEREND:event_jBosonButton1FocusGained
      configurator.particles[0].setIfBoson(true);
      configurator.performIDCheck();
      resetGUIControls();
      //        System.out.println("particle 1 is a boson =" +configurator.particles[0].isIfBoson());
}//GEN-LAST:event_jBosonButton1FocusGained

    private void nxTextField1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nxTextField1ActionPerformed
    {//GEN-HEADEREND:event_nxTextField1ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNx[0]=Integer.parseInt(nxTextField1.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nxTextField1ActionPerformed

    private void nxTextField1FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nxTextField1FocusLost
    {//GEN-HEADEREND:event_nxTextField1FocusLost
      // TODO add your handling code here:
      configurator.gridsNx[0]=Integer.parseInt(nxTextField1.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nxTextField1FocusLost

    private void nyTextField1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nyTextField1ActionPerformed
    {//GEN-HEADEREND:event_nyTextField1ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNy[0]=Integer.parseInt(nyTextField1.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nyTextField1ActionPerformed

    private void nyTextField1FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nyTextField1FocusLost
    {//GEN-HEADEREND:event_nyTextField1FocusLost
      // TODO add your handling code here:
      configurator.gridsNy[0]=Integer.parseInt(nyTextField1.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nyTextField1FocusLost

    private void nzTextField1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nzTextField1ActionPerformed
    {//GEN-HEADEREND:event_nzTextField1ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNz[0]=Integer.parseInt(nzTextField1.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nzTextField1ActionPerformed

    private void nzTextField1FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nzTextField1FocusLost
    {//GEN-HEADEREND:event_nzTextField1FocusLost
      // TODO add your handling code here:
      configurator.gridsNz[0]=Integer.parseInt(nzTextField1.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nzTextField1FocusLost

    private void nxTextField2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nxTextField2ActionPerformed
    {//GEN-HEADEREND:event_nxTextField2ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNx[1]=Integer.parseInt(nxTextField2.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nxTextField2ActionPerformed

    private void nxTextField2FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nxTextField2FocusLost
    {//GEN-HEADEREND:event_nxTextField2FocusLost
      // TODO add your handling code here:
      configurator.gridsNx[1]=Integer.parseInt(nxTextField2.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nxTextField2FocusLost

    private void nyTextField2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nyTextField2ActionPerformed
    {//GEN-HEADEREND:event_nyTextField2ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNy[1]=Integer.parseInt(nyTextField2.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nyTextField2ActionPerformed

    private void nyTextField2FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nyTextField2FocusLost
    {//GEN-HEADEREND:event_nyTextField2FocusLost
      // TODO add your handling code here:
      configurator.gridsNy[1]=Integer.parseInt(nyTextField2.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nyTextField2FocusLost

    private void nzTextField2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nzTextField2ActionPerformed
    {//GEN-HEADEREND:event_nzTextField2ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNz[1]=Integer.parseInt(nzTextField2.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nzTextField2ActionPerformed

    private void nzTextField2FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nzTextField2FocusLost
    {//GEN-HEADEREND:event_nzTextField2FocusLost
      // TODO add your handling code here:
      configurator.gridsNz[1]=Integer.parseInt(nzTextField2.getText());
      configurator.performIDCheck();
      resetGUIControls();
    }//GEN-LAST:event_nzTextField2FocusLost

    private void nxTextField3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nxTextField3ActionPerformed
    {//GEN-HEADEREND:event_nxTextField3ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNx[2]=Integer.parseInt(nxTextField3.getText());
    }//GEN-LAST:event_nxTextField3ActionPerformed

    private void nxTextField3FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nxTextField3FocusLost
    {//GEN-HEADEREND:event_nxTextField3FocusLost
      // TODO add your handling code here:
      configurator.gridsNx[2]=Integer.parseInt(nxTextField3.getText());
    }//GEN-LAST:event_nxTextField3FocusLost

    private void nyTextField3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nyTextField3ActionPerformed
    {//GEN-HEADEREND:event_nyTextField3ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNy[2]=Integer.parseInt(nyTextField3.getText());
    }//GEN-LAST:event_nyTextField3ActionPerformed

    private void nyTextField3FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nyTextField3FocusLost
    {//GEN-HEADEREND:event_nyTextField3FocusLost
      // TODO add your handling code here:
      configurator.gridsNy[2]=Integer.parseInt(nyTextField3.getText());
    }//GEN-LAST:event_nyTextField3FocusLost

    private void nzTextField3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nzTextField3ActionPerformed
    {//GEN-HEADEREND:event_nzTextField3ActionPerformed
      // TODO add your handling code here:
      configurator.gridsNz[2]=Integer.parseInt(nzTextField3.getText());
    }//GEN-LAST:event_nzTextField3ActionPerformed

    private void nzTextField3FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nzTextField3FocusLost
    {//GEN-HEADEREND:event_nzTextField3FocusLost
      // TODO add your handling code here:
      configurator.gridsNz[2]=Integer.parseInt(nzTextField3.getText());
    }//GEN-LAST:event_nzTextField3FocusLost

    private void yMaxField1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_yMaxField1ActionPerformed
    {//GEN-HEADEREND:event_yMaxField1ActionPerformed
      // TODO add your handling code here:
      configurator.yMax[0]=Double.parseDouble(yMaxField1.getText());
    }//GEN-LAST:event_yMaxField1ActionPerformed

    private void yMaxField1FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_yMaxField1FocusLost
    {//GEN-HEADEREND:event_yMaxField1FocusLost
      // TODO add your handling code here:
      configurator.yMax[0]=Double.parseDouble(yMaxField1.getText());
    }//GEN-LAST:event_yMaxField1FocusLost

    private void yMaxField2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_yMaxField2ActionPerformed
    {//GEN-HEADEREND:event_yMaxField2ActionPerformed
      // TODO add your handling code here:
      configurator.yMax[1]=Double.parseDouble(yMaxField2.getText());
    }//GEN-LAST:event_yMaxField2ActionPerformed

    private void yMaxField2FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_yMaxField2FocusLost
    {//GEN-HEADEREND:event_yMaxField2FocusLost
      // TODO add your handling code here:
      configurator.yMax[1]=Double.parseDouble(yMaxField2.getText());
    }//GEN-LAST:event_yMaxField2FocusLost

    private void yMaxField3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_yMaxField3ActionPerformed
    {//GEN-HEADEREND:event_yMaxField3ActionPerformed
      // TODO add your handling code here:
      configurator.yMax[2]=Double.parseDouble(yMaxField3.getText());
    }//GEN-LAST:event_yMaxField3ActionPerformed

    private void yMaxField3FocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_yMaxField3FocusLost
    {//GEN-HEADEREND:event_yMaxField3FocusLost
      // TODO add your handling code here:
      configurator.yMax[2]=Double.parseDouble(yMaxField3.getText());
    }//GEN-LAST:event_yMaxField3FocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel boxSize1Label;
    private javax.swing.JLabel boxSize2Label;
    private javax.swing.JLabel boxSize3Label;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem generateConfMenuItem;
    private javax.swing.JButton grid1GenerateButton;
    private javax.swing.JButton grid2GenerateButton;
    private javax.swing.JButton grid3GenerateButton;
    private javax.swing.JLabel gridGeneratedLabel1;
    private javax.swing.JLabel gridGeneratedLabel2;
    private javax.swing.JLabel gridGeneratedLabel3;
    private javax.swing.JTabbedPane gridParametersPane;
    private javax.swing.JLabel gridPointsLabel1;
    private javax.swing.JLabel gridPointsLabel2;
    private javax.swing.JLabel gridPointsLabel3;
    private javax.swing.JRadioButton jBosonButton1;
    private javax.swing.JRadioButton jBosonButton2;
    private javax.swing.JRadioButton jBosonButton3;
    private javax.swing.JRadioButton jFermionButton1;
    private javax.swing.JRadioButton jFermionButton2;
    private javax.swing.JRadioButton jFermionButton3;
    private javax.swing.JCheckBox jIDCheckBox1;
    private javax.swing.JCheckBox jIDCheckBox2;
    private javax.swing.JCheckBox jIDCheckBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField m1TextField;
    private javax.swing.JTextField m2TextField;
    private javax.swing.JTextField m3TextField;
    private javax.swing.JLabel nxLabel1;
    private javax.swing.JLabel nxLabel2;
    private javax.swing.JLabel nxLabel3;
    private javax.swing.JTextField nxTextField1;
    private javax.swing.JTextField nxTextField2;
    private javax.swing.JTextField nxTextField3;
    private javax.swing.JLabel nyLabel1;
    private javax.swing.JLabel nyLabel2;
    private javax.swing.JLabel nyLabel3;
    private javax.swing.JTextField nyTextField1;
    private javax.swing.JTextField nyTextField2;
    private javax.swing.JTextField nyTextField3;
    private javax.swing.JLabel nzLabel1;
    private javax.swing.JLabel nzLabel2;
    private javax.swing.JLabel nzLabel3;
    private javax.swing.JTextField nzTextField1;
    private javax.swing.JTextField nzTextField2;
    private javax.swing.JTextField nzTextField3;
    private javax.swing.JTextField p1TextField;
    private javax.swing.JTextField p2TextField;
    private javax.swing.JTextField p3TextField;
    private javax.swing.JPanel pair1Panel;
    private javax.swing.JPanel pair2Panel;
    private javax.swing.JPanel pair3Panel;
    private javax.swing.JPanel particlesPanel;
    private javax.swing.JComboBox potentialBox1;
    private javax.swing.JComboBox potentialBox2;
    private javax.swing.JComboBox potentialBox3;
    private javax.swing.JLabel reducedMassLabel1;
    private javax.swing.JLabel reducedMassLabel2;
    private javax.swing.JLabel reducedMassLabel3;
    private javax.swing.JTextField xMax1TextField;
    private javax.swing.JTextField xMax2TextField;
    private javax.swing.JTextField xMax3TextField;
    private javax.swing.JTextField yMaxField1;
    private javax.swing.JTextField yMaxField2;
    private javax.swing.JTextField yMaxField3;
    private javax.swing.JLabel yMaxLabel1;
    private javax.swing.JLabel yMaxLabel2;
    private javax.swing.JLabel yMaxLabel3;
    // End of variables declaration//GEN-END:variables

}
