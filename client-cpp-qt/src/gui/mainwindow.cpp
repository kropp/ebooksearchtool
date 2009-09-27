#include <QtGui>
#include <QtNetwork>

#include "mainwindow.h"

HttpWindow::HttpWindow(QWidget *parent) : QDialog(parent) {
	urlLineEdit = new QLineEdit("http://");

	urlLabel = new QLabel(tr("&URL:"));
	urlLabel->setBuddy(urlLineEdit);
	statusLabel = new QLabel(tr("Please enter the URL of a file you want to "
		                     "download."));

	downloadButton = new QPushButton(tr("Download"));
	downloadButton->setDefault(true);
	quitButton = new QPushButton(tr("Quit"));
	quitButton->setAutoDefault(false);

	buttonBox = new QDialogButtonBox;
	buttonBox->addButton(downloadButton, QDialogButtonBox::ActionRole);
	buttonBox->addButton(quitButton, QDialogButtonBox::RejectRole);

	progressDialog = new QProgressDialog(this);

	myHttp = new QHttp(this);

	connect(urlLineEdit, SIGNAL(textChanged(const QString &)),
		 this, SLOT(enableDownloadButton()));
	connect(myHttp, SIGNAL(requestFinished(int, bool)),
		 this, SLOT(httpRequestFinished(int, bool)));
	connect(myHttp, SIGNAL(dataReadProgress(int, int)),
		 this, SLOT(updateDataReadProgress(int, int)));
	connect(myHttp, SIGNAL(responseHeaderReceived(const QHttpResponseHeader &)),
		 this, SLOT(readResponseHeader(const QHttpResponseHeader &)));
	connect(progressDialog, SIGNAL(canceled()), this, SLOT(cancelDownload()));
	connect(downloadButton, SIGNAL(clicked()), this, SLOT(downloadFile()));
	connect(quitButton, SIGNAL(clicked()), this, SLOT(close()));

	QHBoxLayout *topLayout = new QHBoxLayout;
	topLayout->addWidget(urlLabel);
	topLayout->addWidget(urlLineEdit);

	QVBoxLayout *mainLayout = new QVBoxLayout;
	mainLayout->addLayout(topLayout);
	mainLayout->addWidget(statusLabel);
	mainLayout->addWidget(buttonBox);
	setLayout(mainLayout);

	setWindowTitle(tr("HTTP"));
	urlLineEdit->setFocus();
}

void HttpWindow::downloadFile() {
	QUrl url(urlLineEdit->text());
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

	myHttpRequestAborted = false;
	myHttpGetId = myHttp->get(url.path(), myFile);

	progressDialog->setWindowTitle(tr("HTTP"));
	progressDialog->setLabelText(tr("Downloading %1.").arg(fileName));
	downloadButton->setEnabled(false);
}

void HttpWindow::cancelDownload() {
	statusLabel->setText(tr("Download canceled."));
	myHttpRequestAborted = true;
	myHttp->abort();
	downloadButton->setEnabled(true);
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
		progressDialog->hide();
		return;
	}

	if (requestId != myHttpGetId)
		return;

	progressDialog->hide();
	myFile->close();

    if (error) {
        myFile->remove();
        QMessageBox::information(this, tr("HTTP"),
                                  tr("Download failed: %1.")
                                  .arg(myHttp->errorString()));
    } else {
        QString fileName = QFileInfo(QUrl(urlLineEdit->text()).path()).fileName();
        statusLabel->setText(tr("Downloaded %1 to current directory.").arg(fileName));
    }

    downloadButton->setEnabled(true);
    delete myFile;
    myFile = 0;
}

void HttpWindow::readResponseHeader(const QHttpResponseHeader &responseHeader) {
	if (responseHeader.statusCode() != 200) {
		QMessageBox::information(this, tr("HTTP"),
					          tr("Download failed: %1.")
					          .arg(responseHeader.reasonPhrase()));
		myHttpRequestAborted = true;
		progressDialog->hide();
		myHttp->abort();
		return;
	}
}

void HttpWindow::updateDataReadProgress(int bytesRead, int totalBytes) {
	if (myHttpRequestAborted)
		return;

	progressDialog->setMaximum(totalBytes);
	progressDialog->setValue(bytesRead);
}

void HttpWindow::enableDownloadButton() {
	downloadButton->setEnabled(!urlLineEdit->text().isEmpty());
}

