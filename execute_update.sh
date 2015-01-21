#!/bin/bash
sudo su
cd /home/jmang/JEngine
mvn install -Dmaven.test.skip
service tomcat7 stop
cp /home/jmang/JEngine/JEngine.war /var/lib/tomcat7/webapps/
rm /var/lib/tomcat7/webapps/JEngine
service tomcat7 start