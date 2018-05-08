#!/bin/bash

#export CHIMERA_DEPLOY_NAME=chimera

DEPLOY_PATH=/usr/local/tomcat/webapps/$CHIMERA_DEPLOY_NAME
rm -rf $DEPLOY_PATH
mkdir -p $DEPLOY_PATH
cd $DEPLOY_PATH
unzip /Chimera/Chimera.war > /dev/null

envsubst < /Chimera/config.properties.tpl > $DEPLOY_PATH/WEB-INF/classes/config.properties
envsubst < /Chimera/persistence.xml.tpl > $DEPLOY_PATH/WEB-INF/classes/META-INF/persistence.xml

catalina.sh run
