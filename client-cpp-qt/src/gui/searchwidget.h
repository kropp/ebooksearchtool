#include <QWidget>

// field for searching - line edit plus search image
// emit signal after editing the text and pressing "Enter"
// knows opensearch schema for current server

class QLineEdit;
class QPushButton;

class SearchWidget : public QWidget {

    Q_OBJECT

public:
    static void setOpensearchSchema(const QString&);

public:
    SearchWidget(QWidget* parent);
    
public:
    bool isSearching();

signals:
    void search(QString query);

private slots:
    void emitSearch();

private:
    void createLineEdit();
    void createPushButton();
    void setWidgets();

    void fillComboBox();

private:
    QLineEdit* myLineEdit;
    QPushButton* myPushButton;
    static QString ourOpensearchSchema;
};
