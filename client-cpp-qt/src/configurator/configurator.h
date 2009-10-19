#ifndef _CONFIGURATOR_H_
#define _CONFIGURATOR_H_

#include <string>
#include <map>

typedef std::map<const std::string, std::string* > Map;

class Configurator {

public:
	Configurator();		
	
public:
	void setParameters(const std::string& path, Map& settings);
	void saveSettings(const std::string& path, const Map& settings);
};

#endif //_CONFIGURATOR_H_

