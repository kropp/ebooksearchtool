#include "XMLInputSource.h"
    
#include <QDebug>

QString XMLInputSource::getData(qint64 fromOffset, qint64 toOffset) const {
    qDebug() << "XMLInputSource::getData";
    //QString strData = data();
  //  QChar* characters = strData.data();
   // qDebug() << "XMLInputSource::getData" << strData;
   return data();
}


