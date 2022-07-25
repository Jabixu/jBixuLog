package org.jabixu.log;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Clase para crear JPanel con logs
 * @author Jabixu
 */
public class JPanelLog extends JPanel{
    
    private JTextPane tpane_log;
    private StyledDocument doc;
    private JButton btnClear;
    private JButton btnCopy;
    private static final String LANG_ES = "es";
    private static final String LANG_EN = "en";
    private String lang;
    /**
     * Constructor de clase
     */
    public JPanelLog(){
        this(LANG_ES);
    }
    /**
     * Constructor de clase
     * @param lang <code>String</code> con el idioma: {@link org.jabixu.log.JPanelLog#LANG_ES} or {@link org.jabixu.log.JPanelLog#LANG_EN}
     */
    public JPanelLog(String lang){
        this.lang = lang;
        addComponentToPane();
    }

    private void addComponentToPane() {
        btnClear = new JButton();
        btnCopy = new JButton();
        if (lang.equals(LANG_ES)){
            btnClear.setText("Limpiar");
            btnCopy.setText("Copiar");
        } else{
            btnClear.setText("Clear");
            btnCopy.setText("Copy");
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
//        spane_log.setPreferredSize(new Dimension(350, 200));
//        spane_log.setMinimumSize(new Dimension(10, 10));
        btnClear.addActionListener((ActionEvent ae) -> {
            tpane_log.setText("");
        });
        btnCopy.addActionListener((ActionEvent ae) -> {
            tpane_log.selectAll();
            tpane_log.copy();
            int end = tpane_log.getSelectionEnd();
            tpane_log.setSelectionStart(end);
            tpane_log.setSelectionEnd(end);
        });
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
//        layout.setAutoCreateGaps(true);
//        layout.setAutoCreateContainerGaps(true);

        //Layout Horizontal
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        hGroup
            .addComponent(spane_log)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnClear, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCopy, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );


        //Layout Vertical
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup
            .addComponent(spane_log)
            .addGap(0, 10, 20)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(btnClear, 30, 30, 30)
                .addComponent(btnCopy, 30, 30, 30)
            );

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
        //pack();
                
    }

    public void disableClearButton(){
        btnClear.setVisible(false);
    }
    
    public void disableCopyButton(){
        btnCopy.setVisible(false);
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