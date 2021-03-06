//crédito ao usuário Schonge 
//fonte original: https://codereview.stackexchange.com/questions/51175/simple-text-editor-class

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class EditorGUI extends JFrame implements ActionListener {

    //============================================
    // FIELDS
    //============================================

    private Lexico analisadorLexico;
    private Sintatico analisadorSintatico;
    private Semantico analisadorSemantico;

    // Menus
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu lexicoMenu;
    private JMenu sintaticoMenu;
    private JMenu semanticoMenu;
    private JMenuItem newFile, openFile, saveFile, saveAsFile, pageSetup, printFile, exit;
    private JMenuItem undoEdit, redoEdit, selectAll, copy, paste, cut;
    private JMenuItem analisarLexico;
    private JMenuItem analisarSintatica;
    private JMenuItem analisarSemantica;
    // Window
    private JFrame editorWindow;

    // Text Area
    private Border textBorder;
    private JScrollPane scroll;
    private JTextArea textArea;
    private Font textFont;

    // Window
    private JFrame window;

    // Printing
    private PrinterJob job;
    public PageFormat format;

    // Is File Saved/Opened
    private boolean opened = false;
    private boolean saved = false;

    // Record Open File for quick saving
    private File openedFile;

    // Undo manager for managing the storage of the undos
    // so that the can be redone if requested
    private UndoManager undo;

    //============================================
    // CONSTRUCTOR
    //============================================

    public EditorGUI() {
        super("JavaEdit");

        initAnalysers();

        // Create Menus
        fileMenu();
        editMenu();
        lexicoMenu();
        sintaticoMenu();
        semanticoMenu();

        // Create Text Area
        createTextArea();

        // Create Undo Manager for managing undo/redo commands
        undoMan();

        // Create Window
        createEditorWindow();
    }

    private void initAnalysers() {
        analisadorLexico = new Lexico();
        analisadorSintatico = new Sintatico();
        analisadorSemantico = new Semantico();
    }

    private JFrame createEditorWindow() {
        editorWindow = new JFrame("JavaEdit");
        editorWindow.setVisible(true);
        editorWindow.setPreferredSize(new Dimension(500, 500));
        //editorWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
        editorWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Create Menu Bar
        editorWindow.setJMenuBar(createMenuBar());
        editorWindow.add(scroll, BorderLayout.CENTER);
        editorWindow.pack();
        // Centers application on screen
        editorWindow.setLocationRelativeTo(null);

        return editorWindow;
    }

    private JTextArea createTextArea() {
        textBorder = BorderFactory.createBevelBorder(0, Color.RED, Color.RED);
        textArea = new JTextArea(30, 50);
        textArea.setEditable(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(textBorder, BorderFactory.createEmptyBorder(2, 5, 0, 0)));

        textFont = new Font("Verdana", 0, 14);
        textArea.setFont(textFont);

        scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        return textArea;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(lexicoMenu);
        menuBar.add(sintaticoMenu);
        menuBar.add(semanticoMenu);

        return menuBar;
    }

    private UndoManager undoMan() {
        // Listener for undo and redo functions to document
        undo = new UndoManager();
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
            }
        });

        return undo;
    }

    private void fileMenu() {
        // Create File Menu
        fileMenu = new JMenu("File");
        fileMenu.setPreferredSize(new Dimension(40, 20));

        // Add file menu items
        newFile = new JMenuItem("New");
        newFile.addActionListener(this);
        newFile.setPreferredSize(new Dimension(100, 20));
        newFile.setEnabled(true);

        openFile = new JMenuItem("Open...");
        openFile.addActionListener(this);
        openFile.setPreferredSize(new Dimension(100, 20));
        openFile.setEnabled(true);

        saveFile = new JMenuItem("Save");
        saveFile.addActionListener(this);
        saveFile.setPreferredSize(new Dimension(100, 20));
        saveFile.setEnabled(true);

        saveAsFile = new JMenuItem("Save As...");
        saveAsFile.addActionListener(this);
        saveAsFile.setPreferredSize(new Dimension(100, 20));
        saveAsFile.setEnabled(true);

        pageSetup = new JMenuItem("Page Setup...");
        pageSetup.addActionListener(this);
        pageSetup.setPreferredSize(new Dimension(100, 20));
        pageSetup.setEnabled(true);

        printFile = new JMenuItem("Print...");
        printFile.addActionListener(this);
        printFile.setPreferredSize(new Dimension(100, 20));
        printFile.setEnabled(true);

        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setPreferredSize(new Dimension(100, 20));
        exit.setEnabled(true);

        // Add items to menu
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.add(pageSetup);
        fileMenu.add(printFile);
        fileMenu.add(exit);
    }

    private void editMenu() {
        editMenu = new JMenu("Edit");
        editMenu.setPreferredSize(new Dimension(40, 20));

        // Add file menu items
        undoEdit = new JMenuItem("Undo");
        undoEdit.addActionListener(this);
        undoEdit.setPreferredSize(new Dimension(100, 20));
        undoEdit.setEnabled(true);

        redoEdit = new JMenuItem("Redo");
        redoEdit.addActionListener(this);
        redoEdit.setPreferredSize(new Dimension(100, 20));
        redoEdit.setEnabled(true);

        selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setPreferredSize(new Dimension(100, 20));
        selectAll.setEnabled(true);

        copy = new JMenuItem("Copy");
        copy.addActionListener(this);
        copy.setPreferredSize(new Dimension(100, 20));
        copy.setEnabled(true);

        paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setPreferredSize(new Dimension(100, 20));
        paste.setEnabled(true);

        cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        cut.setPreferredSize(new Dimension(100, 20));
        cut.setEnabled(true);

        // Add items to menu
        editMenu.add(undoEdit);
        editMenu.add(redoEdit);
        editMenu.add(selectAll);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(cut);
    }

    private void lexicoMenu() {
        lexicoMenu = new JMenu("Léxico");
        lexicoMenu.setPreferredSize(new Dimension(100, 20));

        // Add file menu items
        analisarLexico = new JMenuItem("Analisar lexicamente...");
        analisarLexico.addActionListener(this);
        analisarLexico.setPreferredSize(new Dimension(200, 20));
        analisarLexico.setEnabled(true);

        // Add items to menu
        lexicoMenu.add(analisarLexico);
    }

    private void sintaticoMenu() {
        sintaticoMenu = new JMenu("Sintático");
        sintaticoMenu.setPreferredSize(new Dimension(100, 20));

        // Add file menu items
        analisarSintatica = new JMenuItem("Analisar Sintaxe...");
        analisarSintatica.addActionListener(this);
        analisarSintatica.setPreferredSize(new Dimension(150, 20));
        analisarSintatica.setEnabled(true);

        // Add items to menu
        sintaticoMenu.add(analisarSintatica);
    }

    private void semanticoMenu() {
        semanticoMenu = new JMenu("Semântico");
        semanticoMenu.setPreferredSize(new Dimension(100, 20));

        // Add file menu items
        analisarSemantica = new JMenuItem("Analisar Semântica...");
        analisarSemantica.addActionListener(this);
        analisarSemantica.setPreferredSize(new Dimension(180, 20));
        analisarSemantica.setEnabled(true);

        // Add items to menu
        semanticoMenu.add(analisarSemantica);
    }

    // Method for saving files - Removes duplication of code
    private void saveFile(File filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(textArea.getText());
            writer.close();
            saved = true;
            window.setTitle("JavaText - " + filename.getName());
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // Method for quick saving files
    private void quickSave(File filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(textArea.getText());
            writer.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // Method for opening files
    private void openingFiles(File filename) {
        try {
            openedFile = filename;
            FileReader reader = new FileReader(filename);
            textArea.read(reader, null);
            opened = true;
            window.setTitle("JavaEdit - " + filename.getName());
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == newFile) {
            new EditorGUI();
        } else if (event.getSource() == openFile) {
            JFileChooser open = new JFileChooser();
            open.showOpenDialog(null);
            File file = open.getSelectedFile();
            openingFiles(file);
        } else if (event.getSource() == saveFile) {
            JFileChooser save = new JFileChooser();
            File filename = save.getSelectedFile();
            if (opened == false && saved == false) {
                save.showSaveDialog(null);
                int confirmationResult;
                if (filename.exists()) {
                    confirmationResult = JOptionPane.showConfirmDialog(saveFile, "Replace existing file?");
                    if (confirmationResult == JOptionPane.YES_OPTION) {
                        saveFile(filename);
                    }
                } else {
                    saveFile(filename);
                }
            } else {
                quickSave(openedFile);
            }
        } else if (event.getSource() == saveAsFile) {
            JFileChooser saveAs = new JFileChooser();
            saveAs.showSaveDialog(null);
            File filename = saveAs.getSelectedFile();
            int confirmationResult;
            if (filename.exists()) {
                confirmationResult = JOptionPane.showConfirmDialog(saveAsFile, "Replace existing file?");
                if (confirmationResult == JOptionPane.YES_OPTION) {
                    saveFile(filename);
                }
            } else {
                saveFile(filename);
            }
        } else if (event.getSource() == pageSetup) {
            job = PrinterJob.getPrinterJob();
            format = job.pageDialog(job.defaultPage());
        } else if (event.getSource() == printFile) {
            job = PrinterJob.getPrinterJob();
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException err) {
                    err.printStackTrace();
                }
            }
        } else if (event.getSource() == exit) {
            System.exit(0);
        } else if (event.getSource() == undoEdit) {
            try {
                undo.undo();
            } catch (CannotUndoException cu) {
                cu.printStackTrace();
            }
        } else if (event.getSource() == redoEdit) {
            try {
                undo.redo();
            } catch (CannotUndoException cur) {
                cur.printStackTrace();
            }
        } else if (event.getSource() == selectAll) {
            textArea.selectAll();
        } else if (event.getSource() == copy) {
            textArea.copy();
        } else if (event.getSource() == paste) {
            textArea.paste();
        } else if (event.getSource() == cut) {
            textArea.cut();
        }
        //Analisadores
        else if (event.getSource() == analisarLexico) {
            analiseLexica();
        } else if (event.getSource() == analisarSintatica) {
            analiseSintatica();
        } else if (event.getSource() == analisarSemantica) {
            analiseSemantica();
        }
    }

    private void analiseSemantica() {
        resetAnalisadores();
        analisadorLexico.setInput(new StringReader(textArea.getText()));
        try {
            analisadorSintatico.parse(analisadorLexico, analisadorSemantico);
            JOptionPane.showMessageDialog(editorWindow, "A análise semântica foi realizada com sucesso!");
        } catch (LexicalError | SyntaticError | SemanticError e) {
            textArea.select(e.getPosition(), e.getPosition() + 1);
            JOptionPane.showMessageDialog(editorWindow, e.getMessage());
        }
    }

    private void analiseSintatica() {
        resetAnalisadores();
        analisadorLexico.setInput(new StringReader(textArea.getText()));
        try {
            analisadorSintatico.parse(analisadorLexico, null);
            JOptionPane.showMessageDialog(editorWindow, "A análise sintática foi realizada com sucesso!");
        } catch (LexicalError | SyntaticError | SemanticError e) {
            textArea.select(e.getPosition(), e.getPosition() + 1);
            JOptionPane.showMessageDialog(editorWindow, e.getMessage());
        }
    }

    public void analiseLexica() {
        resetAnalisadores();
        String textoFonte = textArea.getText();
        analisadorLexico.setInput(new StringReader(textoFonte));
        try {
            Token token = analisadorLexico.nextToken();
            while (token != null) {
                token = analisadorLexico.nextToken();
            }
            JOptionPane.showMessageDialog(editorWindow, "A análise léxica foi realizada com sucesso!");
        } catch (LexicalError lexicalError) {
            textArea.select(lexicalError.getPosition(), lexicalError.getPosition() + 1);
            JOptionPane.showMessageDialog(editorWindow, lexicalError.getMessage());
        }
    }

    private void resetAnalisadores() {
        analisadorLexico = new Lexico();
        analisadorSintatico = new Sintatico();
        analisadorSemantico = new Semantico();
    }
}