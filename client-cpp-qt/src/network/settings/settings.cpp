//# this is the test program for testing QSettings for my needs

#include <QSettings> 

int main() {
	QSettings settings("/home/student/egorova/client-cpp/src/network/settings/config.ini", QSettings::IniFormat);
	settings.beginGroup("network");	
	settings.setValue("proxy", "192.168.0.2");
	settings.setValue("port", 3061);
	settings.setValue("server", "feedbooks.com");
	settings.endGroup();	
//    int margin = settings.value("port").toInt();

/* settings.beginGroup("mainwindow");
     settings.setValue("size", win->size());
     settings.setValue("fullScreen", win->isFullScreen());
     settings.endGroup();
*/

	return 0;
}
