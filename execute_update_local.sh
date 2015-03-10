#!/bin/bash
mvn clean
mvn install -Dmaven.test.skip
service tomcat7 stop
cp target/JEngine.war /var/lib/tomcat7/webapps/
rm -r /var/lib/tomcat7/webapps/JEngine
service tomcat7 start

