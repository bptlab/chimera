#!/bin/bash
#cd /home/jmang/JEngine
#git pull
mvn clean
mvn install -Dmaven.test.skip
service tomcat7 stop
cp /home/jmang/JEngine/target/JEngine.war /var/lib/tomcat7/webapps/
rm -r /var/lib/tomcat7/webapps/JEngine
service tomcat7 start
