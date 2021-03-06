#include "opds_constants.h"

/*
other opds-tags:

    * prism:issue
    * prism:volume
    * opds:price with three-letter ISO 4217 currencycode attribute
    * opds:paymentgateway (e.g. https://www.paypal.com/us/ or http://checkout.google.com )
    * dc:identifier with ISBN (e.g. <dc:identifier>urn:isbn:9780596806712</dc:identifier>)
    * dcterms:SizeOrDuration with Number of Pages, if available. 
*/

const QString OPDSConstants::NSPASE_OPENSEARCH = "http://a9.com/-/spec/opensearch/1.1/";
const QString OPDSConstants::NSPASE_DCTERMS = "http://purl.org/dc/terms/";
const QString OPDSConstants::NSPACE_ATOM = "http://www.w3.org/2005/Atom";
const QString OPDSConstants::NSPACE_XHTML = "http://www.w3.org/1999/xhtml";

const QString OPDSConstants::TAG_LINK = "link";
const QString OPDSConstants::TAG_TITILE = "title";
const QString OPDSConstants::TAG_ENTRY = "entry";
const QString OPDSConstants::TAG_NAME = "name";
const QString OPDSConstants::TAG_AUTHOR = "author";
const QString OPDSConstants::TAG_PUBLISHER = "publisher"; // dcterms
const QString OPDSConstants::TAG_URI = "uri";
const QString OPDSConstants::TAG_ID = "id";
const QString OPDSConstants::TAG_CONTENT = "content";
const QString OPDSConstants::TAG_SUMMARY = "summary";
const QString OPDSConstants::TAG_UPDATED = "updated"; //atom
const QString OPDSConstants::TAG_RIGHTS = "rights"; //atom
const QString OPDSConstants::TAG_ISSUED = "issued"; //dcterms
const QString OPDSConstants::TAG_CATEGORY = "category"; //atom
const QString OPDSConstants::TAG_LANGUAGE = "language"; //dcterms

const QString OPDSConstants::ATTRIBUTE_TYPE = "type";
const QString OPDSConstants::ATTRIBUTE_REFERENCE = "href";
const QString OPDSConstants::ATTRIBUTE_RELATION = "rel";
const QString OPDSConstants::ATTRIBUTE_TITLE = "title";
const QString OPDSConstants::ATTRIBUTE_TERM = "term";

const QString OPDSConstants::ATTR_VALUE_ACQUISITION = "http://opds-spec.org/acquisition";
const QString OPDSConstants::ATTR_VALUE_ACQUISITION_LOCAL = "http://opds-spec.org/acquisition/local";
const QString OPDSConstants::ATTR_VALUE_COVER = "http://opds-spec.org/cover";
const QString OPDSConstants::ATTR_VALUE_COVER_ = "http://opds-spec.org/opds-cover-image";
const QString OPDSConstants::ATTR_VALUE_COVER_STANZA = "x-stanza-cover-image";

const QString OPDSConstants::ATTR_VALUE_RELATION_NEW = "http://opds-spec.org/sort/new";
const QString OPDSConstants::ATTR_VALUE_RELATION_POPULAR = "http://opds-spec.org/sort/popular";
