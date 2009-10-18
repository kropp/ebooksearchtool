#include "configurator.h"
#include <fstream>
//#include <csdlib>
#include <iostream>


Configurator::Configurator() {}

void Configurator::setParameters(const std::string& path, Map& settings) {
	std::ifstream f(path.c_str());
	if (!f) {
		std::cerr << "cannot open file " << path.c_str() << "\n";
	}
	const int SIZE = 200;
	char ch[SIZE] ;
	while (f.getline(ch, SIZE)) {
		if ((ch[0] == '#') || (ch[0] == ' ') || (ch[0] == '\0')) {
			continue;
		}
		std::string str(ch);
	//	std::cout << "read " << str.c_str() << "\n";
		int i = str.find_first_of('=');
		std::string name = str.substr(0, i);
		std::string value = str.substr(i + 1, str.size() - 1);
//		std::cout << "name " << name.c_str() << "\n";
//		std::cout << "value " << value.c_str() << "\n";
		Map::iterator it = settings.find(name);
		if (it == settings.end()) {
			continue;
		}
		*(it->second) = value;
	}
}

void Configurator::saveSettings(const std::string&, const Map& ) {}
		

