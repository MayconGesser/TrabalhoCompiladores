# -*- coding: utf-8 -*-

from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from PyQt5.QtPrintSupport import *

import os
import sys

#fonte: https://stackoverflow.com/questions/36768033/pyqt-how-to-open-new-window
class Second(QMainWindow):
    def __init__(self, parent=None):
        super(Second, self).__init__(parent)

class MainWindow(QMainWindow):

    def __init__(self, *args, **kwargs):
        super(MainWindow, self).__init__(*args, **kwargs)

        layout = QVBoxLayout()
        self.editor = QPlainTextEdit()  # Could also use a QTextEdit and set self.editor.setAcceptRichText(False)


        # Setup the QTextEdit editor configuration
        fixedfont = QFontDatabase.systemFont(QFontDatabase.FixedFont)
        fixedfont.setPointSize(12)
        self.editor.setFont(fixedfont)

        # self.path holds the path of the currently open file.
        # If none, we haven't got a file open yet (or creating new).
        self.path = None

        layout.addWidget(self.editor)

        container = QWidget()
        container.setLayout(layout)
        self.setCentralWidget(container)

        self.status = QStatusBar()
        self.setStatusBar(self.status)

        file_toolbar = QToolBar("Arquivo")
        file_toolbar.setIconSize(QSize(14, 14))
        self.addToolBar(file_toolbar)
        file_menu = self.menuBar().addMenu("&Arquivo")

        open_file_action = QAction(QIcon(os.path.join('images', 'blue-folder-open-document.png')), "Abrir arquivo...", self)
        open_file_action.setStatusTip("Abrir arquivo")
        open_file_action.triggered.connect(self.file_open)
        file_menu.addAction(open_file_action)
        file_toolbar.addAction(open_file_action)

        save_file_action = QAction(QIcon(os.path.join('images', 'disk.png')), "Save", self)
        save_file_action.setStatusTip("Salvar estado atual")
        save_file_action.triggered.connect(self.file_save)
        file_menu.addAction(save_file_action)
        file_toolbar.addAction(save_file_action)

        saveas_file_action = QAction(QIcon(os.path.join('images', 'disk--pencil.png')), "Save As...", self)
        saveas_file_action.setStatusTip("Salvar estado atual para arquivo específico")
        saveas_file_action.triggered.connect(self.file_saveas)
        file_menu.addAction(saveas_file_action)
        file_toolbar.addAction(saveas_file_action)

        print_action = QAction(QIcon(os.path.join('images', 'printer.png')), "Print...", self)
        print_action.setStatusTip("Imprimir arquivo atual")
        print_action.triggered.connect(self.file_print)
        file_menu.addAction(print_action)
        file_toolbar.addAction(print_action)

        ajuda_toolbar = QToolBar("Editar")
        ajuda_toolbar.setIconSize(QSize(16, 16))
        self.addToolBar(ajuda_toolbar)
        edit_menu = self.menuBar().addMenu("&Editar")

        undo_action = QAction(QIcon(os.path.join('images', 'arrow-curve-180-left.png')), "Undo", self)
        undo_action.setStatusTip("Desfazer a última mudança")
        undo_action.triggered.connect(self.editor.undo)
        edit_menu.addAction(undo_action)

        redo_action = QAction(QIcon(os.path.join('images', 'arrow-curve.png')), "Redo", self)
        redo_action.setStatusTip("Refazer a última mudança")
        redo_action.triggered.connect(self.editor.redo)
        ajuda_toolbar.addAction(redo_action)
        edit_menu.addAction(redo_action)

        edit_menu.addSeparator()

        cut_action = QAction(QIcon(os.path.join('images', 'scissors.png')), "Cut", self)
        cut_action.setStatusTip("Cortar texto selecionado")
        cut_action.triggered.connect(self.editor.cut)
        ajuda_toolbar.addAction(cut_action)
        edit_menu.addAction(cut_action)

        copy_action = QAction(QIcon(os.path.join('images', 'document-copy.png')), "Copy", self)
        copy_action.setStatusTip("Copiar texto selecionado")
        copy_action.triggered.connect(self.editor.copy)
        ajuda_toolbar.addAction(copy_action)
        edit_menu.addAction(copy_action)

        paste_action = QAction(QIcon(os.path.join('images', 'clipboard-paste-document-text.png')), "Paste", self)
        paste_action.setStatusTip("Colar do clipboard")
        paste_action.triggered.connect(self.on_pushButton_clicked)
        self.dialogs = list()
        ajuda_toolbar.addAction(paste_action)
        edit_menu.addAction(paste_action)

        select_action = QAction(QIcon(os.path.join('images', 'selection-input.png')), "Select all", self)
        select_action.setStatusTip("Selecionar todo o texto")
        select_action.triggered.connect(self.editor.selectAll)
        edit_menu.addAction(select_action)

        edit_menu.addSeparator()

        wrap_action = QAction(QIcon(os.path.join('images', 'arrow-continue.png')), "Wrap text to window", self)
        wrap_action.setStatusTip("Ativar ancoragem de texto à janela")
        wrap_action.setCheckable(True)
        wrap_action.setChecked(True)
        wrap_action.triggered.connect(self.edit_toggle_wrap)
        edit_menu.addAction(wrap_action)

        lexico_toolbar = QToolBar("Léxico")
        lexico_toolbar.setIconSize(QSize(16, 16))
        self.addToolBar(lexico_toolbar)
        lexico_menu = self.menuBar().addMenu("&Léxico")

        sintatico_toolbar = QToolBar("Sintático")
        sintatico_toolbar.setIconSize(QSize(16, 16))
        self.addToolBar(sintatico_toolbar)
        sintatico_menu = self.menuBar().addMenu("&Sintático")

        semantico_toolbar = QToolBar("Semântico")
        semantico_toolbar.setIconSize(QSize(16, 16))
        self.addToolBar(semantico_toolbar)
        semantico_menu = self.menuBar().addMenu("&Semântico")

        codigo_toolbar = QToolBar("Código")
        codigo_toolbar.setIconSize(QSize(16, 16))
        self.addToolBar(codigo_toolbar)
        codigo_menu = self.menuBar().addMenu("&Código")

        ajuda_toolbar = QToolBar("Ajuda")
        ajuda_toolbar.setIconSize(QSize(16, 16))
        self.addToolBar(ajuda_toolbar)
        ajuda_menu = self.menuBar().addMenu("&Ajuda")

        self.update_title()
        self.show()

    def on_pushButton_clicked(self):
        dialog = Second(self)
        self.dialogs.append(dialog)
        dialog.show()

    def dialog_critical(self, s):
        dlg = QMessageBox(self)
        dlg.setText(s)
        dlg.setIcon(QMessageBox.Critical)
        dlg.show()

    def file_open(self):
        path, _ = QFileDialog.getOpenFileName(self, "Abrir arquivos", "", "Text documents (*.txt);All files (*.*)")

        try:
            with open(path, 'rU') as f:
                text = f.read()

        except Exception as e:
            self.dialog_critical(str(e))

        else:
            self.path = path
            self.editor.setPlainText(text)
            self.update_title()

    def file_save(self):
        if self.path is None:
            # If we do not have a path, we need to use Save As.
            return self.file_saveas()

        text = self.editor.toPlainText()
        try:
            with open(self.path, 'w') as f:
                f.write(text)

        except Exception as e:
            self.dialog_critical(str(e))

    def file_saveas(self):
        path, _ = QFileDialog.getSaveFileName(self, "Salvar arquivo", "", "Text documents (*.txt);All files (*.*)")
        text = self.editor.toPlainText()

        if not path:
            # If dialog is cancelled, will return ''
            return

        try:
            with open(path, 'w') as f:
                f.write(text)

        except Exception as e:
            self.dialog_critical(str(e))

        else:
            self.path = path
            self.update_title()

    def file_print(self):
        dlg = QPrintDialog()
        if dlg.exec_():
            self.editor.print_(dlg.printer())

    def update_title(self):
        self.setWindowTitle("%s - No2Pads" % (os.path.basename(self.path) if self.path else "Sem Título"))

    def edit_toggle_wrap(self):
        self.editor.setLineWrapMode( 1 if self.editor.lineWrapMode() == 0 else 0 )


if __name__ == '__main__':

    app = QApplication(sys.argv)
    app.setApplicationName("No2Pads")

    window = MainWindow()
    app.exec_()