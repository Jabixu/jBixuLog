package org.jabixu.log;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Clase para crear JFrame con logs
 * @author Jabixu
 */
public class JFrameLog extends JFrame{
    
    private JTextPane tpane_log;
    private StyledDocument doc;
    public static final String ICON_LOG = "Log";
    public static final String ICON_BIXU = "Bixu";
    public static final String LANG_ES = "es";
    public static final String LANG_EN = "en";
    private JButton btnClear = new JButton("Limpiar");
    private JButton btnCopy = new JButton("Copiar");
    private JButton btnClose = new JButton("Cerrar");
    private String icon;    
    private String lang;    
    
    /**
     * Constructor con título "Log"
     */
    public JFrameLog(){
        this("Log");
    }
    
    
    /**
     * Constructor con título
     * @param titulo <code>String</code> con el título
     */
    public JFrameLog(String titulo){
        this(titulo, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }
    
    /**
     * Constructor con título y lookAndFeel
     * @param titulo <code>String</code> con el título
     * @param lookAndFeel <code>String</code> con el nombre del lookAndFeel
     */
    public JFrameLog(String titulo, String lookAndFeel){
        this(titulo, lookAndFeel, ICON_BIXU);
    }
    /**
     * Constructor con título y lookAndFeel
     * @param titulo <code>String</code> con el título
     * @param lookAndFeel <code>String</code> con el nombre del lookAndFeel
     * @param icon <code>String</code> con el tipo de icono: {@link org.jabixu.log.JFrameLog#ICON_LOG} or {@link org.jabixu.log.JFrameLog#ICON_BIXU}
     */
    public JFrameLog(String titulo, String lookAndFeel, String icon){
        this(titulo, lookAndFeel, icon, LANG_ES);
    }
    /**
     * Constructor con título y lookAndFeel
     * @param titulo <code>String</code> con el título
     * @param lookAndFeel <code>String</code> con el nombre del lookAndFeel
     * @param icon <code>String</code> con el tipo de icono: {@link org.jabixu.log.JFrameLog#ICON_LOG} or {@link org.jabixu.log.JFrameLog#ICON_BIXU}
     * @param lang <code>String</code> con el idioma: {@link org.jabixu.log.JFrameLog#LANG_ES} or {@link org.jabixu.log.JFrameLog#LANG_EN}
     */
    public JFrameLog(String titulo, String lookAndFeel, String icon, String lang){
        super(titulo);
        try {
            UIManager.setLookAndFeel(lookAndFeel);                 
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("No se ha podido configurar el look and feel:"+ e.getMessage( ));
        }
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        if (addComponentToPane(getContentPane()) == false)
            return;

        //Display the window.
        pack();
        setIconImage(new ImageIcon("resources"+File.separator+"Log.jpg").getImage());
        
        GraphicsDevice defaultScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle bounds = defaultScreen.getDefaultConfiguration().getBounds();
        
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(defaultScreen.getDefaultConfiguration());

        Rectangle safeBounds = new Rectangle(bounds);
        safeBounds.x += insets.left;
        safeBounds.y += insets.top;
        safeBounds.width -= (insets.left + insets.right);
        safeBounds.height -= (insets.top + insets.bottom);

        
        int x = (int) safeBounds.getMinX(); //Izda
        // int x = (int) safeBounds.getMaxX() - getWidth(); //Dcha
        // int y = (int) safeBounds.getMinY(); //Arriba
        int y = (int) safeBounds.getMaxY() - getHeight(); //Abajo
        setLocation(x, y);
        

        
        
        this.icon = icon;
        this.lang = lang;
        //setVisible(true);
        
    }
    
    public void setIcon(String icon){
        this.icon = icon;
    }
    
    public void setVisible(){
        ArrayList<Image> images = new ArrayList<>();
        images.add(new ImageIcon(getClass().getResource("/img/"+icon+"20.png")).getImage());
        images.add(new ImageIcon(getClass().getResource("/img/"+icon+"24.png")).getImage());
        images.add(new ImageIcon(getClass().getResource("/img/"+icon+"36.png")).getImage());
        images.add(new ImageIcon(getClass().getResource("/img/"+icon+"48.png")).getImage());
        images.add(new ImageIcon(getClass().getResource("/img/"+icon+"60.png")).getImage());
        setIconImages(images);
        
        setVisible(true);
    }

    public void disableClearButton(){
        btnClear.setVisible(false);
    }
    
    public void disableCopyButton(){
        btnCopy.setVisible(false);
    }
    
    public void disableCloseButton(){
        btnClose.setVisible(false);
    }
    
    private boolean addComponentToPane(Container contentPane) {
        btnClear = new JButton();
        btnCopy = new JButton();
        btnClose = new JButton();
        switch (lang) {
            case LANG_ES:
                btnClear.setText("Limpiar");
                btnCopy.setText("Copiar");
                btnClose.setText("Cerrar");
                break;
            case LANG_EN:
                btnClear.setText("Clear");
                btnCopy.setText("Copy");
                btnClose.setText("Close");
                break;
            default:
                System.err.println("Idioma no configurado");
                return false;
        }
        
        tpane_log = new JTextPane();
        doc = tpane_log.getStyledDocument();
        addStylesToDocument(doc);

//        textPane.setLineWrap(true);
//        textPane.setWrapStyleWord(true);
        tpane_log.setEditable(false);
        JScrollPane spane_log = new JScrollPane(tpane_log, 
                                                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spane_log.setPreferredSize(new Dimension(350, 200));
        spane_log.setMinimumSize(new Dimension(10, 10));
        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tpane_log.setText("");
            }
        });
        btnCopy.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                //tpane_log.copy();
                StringSelection stringSelection = new StringSelection(tpane_log.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
        btnClose.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                dispose();
            }
        });
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        //Layout Horizontal
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        hGroup
            .addComponent(spane_log)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(0, Short.MAX_VALUE)
                .addComponent(btnClear, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCopy, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(0, Short.MAX_VALUE)
            );


        //Layout Vertical
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup
            .addContainerGap(0, 20)
            .addComponent(spane_log)
            .addGap(0, 10, 20)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(btnClear, 0, GroupLayout.DEFAULT_SIZE, 30)
                .addComponent(btnCopy, 0, GroupLayout.DEFAULT_SIZE, 30)
                .addComponent(btnClose, 0, GroupLayout.DEFAULT_SIZE, 30)
            );

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);       
                
        return true;
    }

    /**
     * Método para añadir mensaje de error. No crea una nueva línea después
     * @param text <code>String</code> con el mensaje de error a añadir
     */
    public void addError(String text) {
        try {
            doc.insertString(doc.getLength(), text, doc.getStyle("error"));
        } catch (BadLocationException ex) {
            System.err.println("No se pudo insertar el texto '"+text+"'.");
        }
    }

    /**
     * Método para añadir mensaje de error. Crea una nueva línea después
     * @param text <code>String</code> con el mensaje de error a añadir
     */
    public void addErrorN(String text) {
        try {
            doc.insertString(doc.getLength(), text+"\n", doc.getStyle("error"));
        } catch (BadLocationException ex) {
            System.err.println("No se pudo insertar el texto '"+text+"'.");
        }
    }

    /**
     * Método para añadir mensaje normal. No crea una nueva línea después
     * @param text <code>String</code> con el mensaje normal a añadir
     */
    public void addText(String text) {
        try {
            doc.insertString(doc.getLength(), text, doc.getStyle("regular"));
        } catch (BadLocationException ex) {
            System.err.println("No se pudo insertar el texto '"+text+"'.");
        }
    }

    /**
     * Método para añadir mensaje normal. Crea una nueva línea después
     * @param text <code>String</code> con el mensaje normal a añadir
     */
    public void addTextN(String text) {
        try {
            doc.insertString(doc.getLength(), text+"\n", doc.getStyle("regular"));
        } catch (BadLocationException ex) {
            System.err.println("No se pudo insertar el texto '"+text+"'.");
        }
    }

    /**
     * Método para limpiar los mensajes actuales
     */
    public void clearText() {
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException ex) {
            System.err.println("No se pudo limpiar el texto.");
        }
    }

    /**
     * Método para obtener los mensajes actuales
     * @return <code>String</code> con los mensajes actuales
     */
    public String getText(){
        try {
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            System.err.println("No se pudo obtener el texto.");
            return null;
        }
    }

    /**
     * Método para cerrar la ventana
     */
    public void close(){
        dispose();
    }

    private void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
 
        Style error = doc.addStyle("error", def);
        StyleConstants.setForeground(error, Color.red);
    }
    
    /**
     *
     * @param throwable
     * @return
     */
    public static String getStringException(final Throwable throwable) {
         final StringWriter sw = new StringWriter();
         final PrintWriter pw = new PrintWriter(sw, true);
         throwable.printStackTrace(pw);
         return sw.getBuffer().toString();
    }
    
}