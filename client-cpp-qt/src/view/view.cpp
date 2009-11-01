#include "view.h"
#include "bookwidget.h"

View::View (QWidget* parent, Data* data) : QListWidget(parent) {
    resetData(data);
}

void View::resetData(Data* data) {
    myData = data;
    if (myData) {
        const size_t dataSize = myData->getSize();
        for (size_t i = 0; i < dataSize; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            addItem(widget);
            std::cout << "item added to the view\n";
        }
    }
}

