#include <QApplication>

#include "./gui/mainwindow.h"

int main(int argc, char *argv[]) {
	QApplication app(argc, argv);
	HttpWindow httpWindow;
	httpWindow.show();
	return httpWindow.exec();
}
