FROM tomcat:7-jre8

RUN apt-get update && apt-get install -y gettext-base

COPY target/Chimera.war /Chimera/
COPY config.properties.tpl /Chimera/
COPY src/main/resources/META-INF/persistence.xml.tpl /Chimera/
COPY docker_deploy.sh /Chimera/
COPY web.xml.tpl /usr/local/tomcat/conf/web.xml

CMD /Chimera/docker_deploy.sh
