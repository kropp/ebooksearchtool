#include "progressviewmodel.h"
#include "../Model/booksearchmanager.h"
#include "../Model/catalogmanager.h"
#include "bookresultviewmodel.h"

ProgressViewModel::ProgressViewModel() {

}

ProgressViewModel::~ProgressViewModel() {

}

void ProgressViewModel::setConnections() {

}

BookSearchProgressViewModel::BookSearchProgressViewModel() {
    setConnections();
}

BookSearchProgressViewModel::~BookSearchProgressViewModel() {

}

void BookSearchProgressViewModel::setConnections() {
    connect (BookSearchManager::getInstance(), SIGNAL(searchStarted()), this, SIGNAL(showProgress()));
    connect (BookSearchManager::getInstance(), SIGNAL(searchFinished()), this, SIGNAL(hideProgress()));
    connect (BookSearchManager::getInstance(), SIGNAL(downloadFinished()), this, SIGNAL(hideProgress()));
}

CatalogProgressViewModel::CatalogProgressViewModel() {
    setConnections();
}

CatalogProgressViewModel::~CatalogProgressViewModel() {

}

void CatalogProgressViewModel::setConnections() {
    connect (CatalogManager::getInstance(), SIGNAL(startedOpening()), this, SIGNAL(showProgress()));
    connect (CatalogManager::getInstance(), SIGNAL(finishedOpening()), this, SIGNAL(hideProgress()));
}

BookDownloadProgressViewModel::BookDownloadProgressViewModel(BookResultViewModel* bookVm) {
    myBookViewModel = bookVm;
    setConnections();
}

BookDownloadProgressViewModel::~BookDownloadProgressViewModel() {

}

void BookDownloadProgressViewModel::setConnections() {
    connect (myBookViewModel, SIGNAL(downloadStarted()), this, SIGNAL(showProgress()));
    connect (myBookViewModel, SIGNAL(downloadFinished()), this, SIGNAL(hideProgress()));
}

