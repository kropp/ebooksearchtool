#include <QApplication>

#include "mainwindow.h"

int main(int argc, char *argv[]) {
	QApplication app(argc, argv);
	HttpWindow httpWin;
	httpWin.show();
	return httpWin.exec();
}
