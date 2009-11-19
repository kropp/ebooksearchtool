#include <QApplication>

#include "showhidetext.h"

int main(int argc, char *argv[]) {
	QApplication app(argc, argv);
	ShowHideText text("little", "the grand piece of text for dropping out", 0);
	text.show();
	return app.exec();
}
