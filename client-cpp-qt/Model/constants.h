#ifndef CONSTANTS_H
#define CONSTANTS_H

#include <QString>

    const QString  NSPASE_OPENSEARCH = "http://a9.com/-/spec/opensearch/1.1/";
    const QString  NSPASE_DCTERMS = "http://purl.org/dc/terms/";
    const QString  NSPACE_ATOM = "http://www.w3.org/2005/Atom";
    const QString  NSPACE_XHTML = "http://www.w3.org/1999/xhtml";

    const QString  TAG_LINK = "link";
    const QString  TAG_TITILE = "title";
    const QString  TAG_ENTRY = "entry";
    const QString  TAG_NAME = "name";
    const QString  TAG_AUTHOR = "author";
    const QString  TAG_PUBLISHER = "publisher"; // dcterms
    const QString  TAG_URI = "uri";
    const QString  TAG_ID = "id";
    const QString  TAG_CONTENT = "content";
    const QString  TAG_SUMMARY = "summary";
    const QString  TAG_UPDATED = "updated"; //atom
    const QString  TAG_RIGHTS = "rights"; //atom
    const QString  TAG_ISSUED = "issued"; //dcterms
    const QString  TAG_CATEGORY = "category"; //atom
    const QString  TAG_LANGUAGE = "language"; //dcterms

    const QString  ATTRIBUTE_TYPE = "type";
    const QString  ATTRIBUTE_REFERENCE = "href";
    const QString  ATTRIBUTE_RELATION = "rel";
    const QString  ATTRIBUTE_TITLE = "title";
    const QString  ATTRIBUTE_TERM = "term";

    const QString  ATTR_VALUE_ACQUISITION = "http://opds-spec.org/acquisition";
    const QString  ATTR_VALUE_COVER = "http://opds-spec.org/cover";
    const QString  ATTR_VALUE_COVER_ = "http://opds-spec.org/opds-cover-image";
    const QString  ATTR_VALUE_COVER_STANZA = "x-stanza-cover-image";

    const QString  ATTR_VALUE_RELATION_NEW = "http://opds-spec.org/sort/new";
    const QString  ATTR_VALUE_RELATION_POPULAR = "http://opds-spec.org/sort/popular";


#endif // CONSTANTS_H
