QT += xml
QT += network
OBJECTS_DIR = bin
MOC_DIR = bin
HEADERS += Model/book.h \
    Model/author.h \
    View/mainview.h \
    View/searchview.h \
    ViewModel/searchviewmodel.h \
    ViewModel/bookresultsviewmodel.h \
    View/bookresultsview.h \
    View/standardview.h \
    View/bookresultview.h \
    ViewModel/bookresultviewmodel.h \
    View/bookresultsrearrangeview.h \
    Model/bookdownloader.h \
    Model/opds_parser/parser.h \
    Model/opds_parser/opds_constants.h \
    Model/opds_parser/handler.h \
    Model/opds_writer/opds_writer.h \
    Model/search_result.h \
    View/pageview.h \
    ViewModel/programmodeviewmodel.h \
    View/programmodeselectorview.h \
    Model/downloaderthread.h \
    Model/booksearchmanager.h \
    View/standardcontentview.h \
    View/selectionfilterview.h \
    View/multistatebutton.h
SOURCES += Model/book.cpp \
    View/mainview.cpp \
    main.cpp \
    View/searchview.cpp \
    ViewModel/searchviewmodel.cpp \
    ViewModel/bookresultsviewmodel.cpp \
    View/bookresultsview.cpp \
    View/standardview.cpp \
    View/bookresultview.cpp \
    ViewModel/bookresultviewmodel.cpp \
    View/bookresultsrearrangeview.cpp \
    Model/bookdownloader.cpp \
    Model/opds_parser/parser.cpp \
    Model/opds_parser/opds_constants.cpp \
    Model/opds_parser/handler.cpp \
    Model/opds_writer/opds_writer.cpp \
    Model/search_result.cpp \
    View/pageview.cpp \
    ViewModel/programmodeviewmodel.cpp \
    View/programmodeselectorview.cpp \
    Model/downloaderthread.cpp \
    Model/booksearchmanager.cpp \
    View/standardcontentview.cpp \
    View/selectionfilterview.cpp \
    View/multistatebutton.cpp
RESOURCES += resources.qrc
