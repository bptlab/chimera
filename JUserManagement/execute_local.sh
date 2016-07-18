#!/bin/bash
mvn clean
mvn install -Dmaven.test.skip
service tomcat7 stop
cp target/JUserManagement.war /var/lib/tomcat7/webapps/
rm -r /var/lib/tomcat7/webapps/JUserManagement
service tomcat7 start
