#include <QApplication>

#include "moreLessTextLabel.h"

int main(int argc, char *argv[]) {
	QApplication app(argc, argv);
	MoreLessTextLabel text("Summary: begining of the summary", "Summary: begining of the summary and continious the grand piece of text for dropping out", 0);
	text.show();
	return app.exec();
}
