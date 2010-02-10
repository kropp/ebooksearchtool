#ifndef _opds_constants_h_
#define _opds_constants_h_

#include <QString>

class OPDSConstants {

protected:
    static const QString NSPASE_OPENSEARCH;
    static const QString NSPASE_DCTERMS; 
    static const QString NSPACE_ATOM;

    static const QString TAG_LINK;
    static const QString TAG_TITILE;
    static const QString TAG_ENTRY;
    static const QString TAG_NAME;
    static const QString TAG_URI;
    static const QString TAG_ID;
    static const QString TAG_CONTENT;
    static const QString TAG_SUMMARY;

    static const QString ATTRIBUTE_TYPE;
    static const QString ATTRIBUTE_REFERENCE;
    static const QString ATTRIBUTE_RELATION;
    static const QString ATTRIBUTE_TITLE;

    static const QString ATTR_VALUE_ACQUISITION;
    static const QString ATTR_VALUE_COVER;
};

#endif //_opds_constants_h_
