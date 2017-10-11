FROM tomcat:7-jre8

RUN apt-get update && apt-get install -y gettext-base

COPY target/Chimera.war /Chimera/
COPY config.properties.tpl /Chimera/
COPY docker_deploy.sh /Chimera/

CMD /Chimera/docker_deploy.sh

