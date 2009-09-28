#include <QtGui>
#include <QtNetwork>

#include <iostream>
#include "mainwindow.h"

HttpWindow::HttpWindow(QWidget *parent) : QDialog(parent) {
	myUrlLineEdit = new QLineEdit("http://");

	myUrlLabel = new QLabel(tr("&URL:"));
	myUrlLabel->setBuddy(myUrlLineEdit);
	myStatusLabel = new QLabel(tr("Please enter the URL of a file you want to "
		                     "download."));

	myDownloadButton = new QPushButton(tr("Download"));
	myDownloadButton->setDefault(true);
	myQuitButton = new QPushButton(tr("Quit"));
	myQuitButton->setAutoDefault(false);

	myButtonBox = new QDialogButtonBox;
	myButtonBox->addButton(myDownloadButton, QDialogButtonBox::ActionRole);
	myButtonBox->addButton(myQuitButton, QDialogButtonBox::RejectRole);

	myProgressDialog = new QProgressDialog(this);

	myHttp = new QHttp(this);

	connect(myUrlLineEdit, SIGNAL(textChanged(const QString &)),
		this, SLOT(enableDownloadButton()));
	connect(myHttp, SIGNAL(requestFinished(int, bool)),
		 this, SLOT(httpRequestFinished(int, bool)));
	connect(myHttp, SIGNAL(dataReadProgress(int, int)),
		 this, SLOT(updateDataReadProgress(int, int)));
	connect(myHttp, SIGNAL(responseHeaderReceived(const QHttpResponseHeader &)),
		 this, SLOT(readResponseHeader(const QHttpResponseHeader &)));
	connect(myProgressDialog, SIGNAL(canceled()), this, SLOT(cancelDownload()));
	connect(myDownloadButton, SIGNAL(clicked()), this, SLOT(downloadFile()));
	connect(myQuitButton, SIGNAL(clicked()), this, SLOT(close()));

	QHBoxLayout *topLayout = new QHBoxLayout;
	topLayout->addWidget(myUrlLabel);
	topLayout->addWidget(myUrlLineEdit);

	QVBoxLayout *mainLayout = new QVBoxLayout;
	mainLayout->addLayout(topLayout);
	mainLayout->addWidget(myStatusLabel);
	mainLayout->addWidget(myButtonBox);
	setLayout(mainLayout);

	setWindowTitle(tr("HTTP"));
	myUrlLineEdit->setFocus();
}

void HttpWindow::downloadFile() {
	QUrl url(myUrlLineEdit->text());
	QFileInfo fileInfo(url.path());
	QString fileName = fileInfo.fileName();

	if (QFile::exists(fileName)) {
	if (QMessageBox::question(this, tr("HTTP"),
		                       tr("There already exists a file called %1 in "
		                          "the current directory. Overwrite?").arg(fileName),
		                       QMessageBox::Ok|QMessageBox::Cancel, QMessageBox::Cancel)
		 == QMessageBox::Cancel)
		return;
		QFile::remove(fileName);
	}

	myFile = new QFile(fileName);
	if (!myFile->open(QIODevice::WriteOnly)) {
    	QMessageBox::information(this, tr("HTTP"),
                                  tr("Unable to save the file %1: %2.")
                                  .arg(fileName).arg(myFile->errorString()));
		delete myFile;
		myFile = 0;
		return;
	}

	QHttp::ConnectionMode mode = url.scheme().toLower() == "https" ? QHttp::ConnectionModeHttps : QHttp::ConnectionModeHttp;
	myHttp->setHost(url.host(), mode, url.port() == -1 ? 0 : url.port());
	//TODO - add button for setting proxy
	myHttp->setProxy("192.168.0.2", 3128);

	myHttpRequestAborted = false;
	myHttpGetId = myHttp->get(url.path(), myFile);

	myProgressDialog->setWindowTitle(tr("HTTP"));
	myProgressDialog->setLabelText(tr("Downloading %1.").arg(fileName));
	myDownloadButton->setEnabled(false);
}

void HttpWindow::cancelDownload() {
	myStatusLabel->setText(tr("Download canceled."));
	myHttpRequestAborted = true;
	myHttp->abort();
	myDownloadButton->setEnabled(true);
}

void HttpWindow::httpRequestFinished(int requestId, bool error) {
	if (requestId != myHttpGetId)
		return;
	if (myHttpRequestAborted) {
    	if (myFile) {
			myFile->close();
			myFile->remove();
			delete myFile;
			myFile = 0;
		}
		myProgressDialog->hide();
		return;
	}

	if (requestId != myHttpGetId)
		return;

	myProgressDialog->hide();
	myFile->close();

    if (error) {
        myFile->remove();
        QMessageBox::information(this, tr("HTTP"),
                                  tr("Download failed: %1.")
                                  .arg(myHttp->errorString()));
    } else {
    	QString fileName = QFileInfo(QUrl(myUrlLineEdit->text()).path()).fileName();
    	myStatusLabel->setText(tr("Downloaded %1 to current directory.").arg(fileName));
    }

    myDownloadButton->setEnabled(true);
    delete myFile;
    myFile = 0;
}

void HttpWindow::readResponseHeader(const QHttpResponseHeader &responseHeader) {
	if (responseHeader.statusCode() != 200) {
		QMessageBox::information(this, tr("HTTP"),
					          tr("Download failed: %1.")
					          .arg(responseHeader.reasonPhrase()));
		myHttpRequestAborted = true;
		myProgressDialog->hide();
		myHttp->abort();
		return;
	}
}

void HttpWindow::updateDataReadProgress(int bytesRead, int totalBytes) {
	if (myHttpRequestAborted)
		return;

	myProgressDialog->setMaximum(totalBytes);
	myProgressDialog->setValue(bytesRead);
}

void HttpWindow::enableDownloadButton() {
	myDownloadButton->setEnabled(!myUrlLineEdit->text().isEmpty());
}

