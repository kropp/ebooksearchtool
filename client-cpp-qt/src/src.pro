######################################################################
# Automatically generated by qmake (2.01a) ?? ????. 2 11:55:58 2010
######################################################################

TEMPLATE = app
QT += xml
QT += network
TARGET = 
DEPENDPATH += . data gui network view xml_parser gui/dialogs
INCLUDEPATH += . gui data view network xml_parser gui/dialogs

# Input
HEADERS += data/book_author.h \
           data/data.h \
           gui/centralwidget.h \
           gui/mainwindow.h \
           gui/searchwidget.h \
           network/connectionParameters.h \
           network/networkmanager.h \
           view/bookActionButtons.h \
           view/bookwidget.h \
           view/moreLessTextLabel.h \
           view/view.h \
           xml_parser/handler.h \
           xml_parser/parser.h \
           gui/dialogs/internetConnectionDialog.h
SOURCES += main.cpp \
           data/book_author.cpp \
           data/data.cpp \
           gui/centralwidget.cpp \
           gui/mainwindow.cpp \
           gui/searchwidget.cpp \
           network/networkmanager.cpp \
           view/bookActionButtons.cpp \
           view/bookwidget.cpp \
           view/moreLessTextLabel.cpp \
           view/view.cpp \
           view/view_slots.cpp \
           xml_parser/handler.cpp \
           xml_parser/parser.cpp \
           gui/dialogs/internetConnectionDialog.cpp
