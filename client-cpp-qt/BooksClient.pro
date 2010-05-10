QT += xml
QT += network
OBJECTS_DIR += bin
MOC_DIR += bin
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
    View/libraryview.h \
    ViewModel/libraryviewmodel.h \
    ViewModel/librarybookresultsviewmodel.h \
    Model/librarymanager.h \
    View/bookresultslibraryview.h \
    Model/catalog.h \
    Model/downloaderthread.h \
    Model/booksearchmanager.h \
    Model/catalogmanager.h \
    ViewModel/catalogviewmodel.h \
    View/catalogview.h \
    ViewModel/catalogresultsviewmodel.h \
    View/catalogresultsview.h \
    View/catalogresultview.h \
    ViewModel/catalogresultviewmodel.h \
    ViewModel/catalogbookresultsviewmodel.h \
    View/bookresultscatalogview.h \
    ViewModel/catalogbrowsebarviewmodel.h \
    View/catalogbrowsebarpanel.h \
    Model/catalogdownloader.h \
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
    View/libraryview.cpp \
    ViewModel/libraryviewmodel.cpp \
    ViewModel/librarybookresultsviewmodel.cpp \
    Model/librarymanager.cpp \
    View/bookresultslibraryview.cpp \
    Model/catalog.cpp \
    Model/downloaderthread.cpp \
    Model/booksearchmanager.cpp \
    Model/catalogmanager.cpp \
    ViewModel/catalogviewmodel.cpp \
    View/catalogview.cpp \
    ViewModel/catalogresultsviewmodel.cpp \
    View/catalogresultsview.cpp \
    View/catalogresultview.cpp \
    ViewModel/catalogresultviewmodel.cpp \
    ViewModel/catalogbookresultsviewmodel.cpp \
    View/bookresultscatalogview.cpp \
    ViewModel/catalogbrowsebarviewmodel.cpp \
    View/catalogbrowsebarpanel.cpp \
    Model/catalogdownloader.cpp \
    View/standardcontentview.cpp \
    View/selectionfilterview.cpp \
    View/multistatebutton.cpp
RESOURCES += resources.qrc
