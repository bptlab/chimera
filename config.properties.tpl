# This file lists all configurable parameter of chimera. Feel free to extend the file. 
# You can access your defined property by calling the static method PropertyLoader.getProperty(key).
#connection parameter for the mysql database
mysql.schema=$CHIMERA_DB_NAME
mysql.username=$CHIMERA_DB_USERNAME
mysql.password=$CHIMERA_DB_PASSWORD
mysql.url=jdbc:mysql://$CHIMERA_DB_HOST:$CHIMERA_DB_PORT/$CHIMERA_DB_NAME
mysql.test.schema=$CHIMERA_DB_TEST_NAME
mysql.test.url=jdbc:mysql://$CHIMERA_DB_HOST:$CHIMERA_DB_PORT/$CHIMERA_DB_TEST_NAME
#connection parameter for the email task
mailtask.hostname=smtp.googlemail.com
mailtask.port=465
mailtask.mail.username=
mailtask.mail.password=
#url for event processing platform
#unicorn.url=http://172.16.64.105:8080
#unicorn.path.deploy=/Unicorn-unicorn_BP15_dev
unicorn.url=$UNICORN_URL
unicorn.path.deploy=$UNICORN_DEPLOY_NAME
unicorn.path.eventtype=/webapi/REST/EventType
unicorn.path.event=/webapi/REST/Event
unicorn.path.query.rest=/webapi/REST/EventQuery/REST
unicorn.path.query.queue=/webapi/REST/EventQuery/Queue
#url of deployed instance, needed for the callbacks to the event dispatcher
#chimera.url=http://172.16.64.113:8080
#chimera.path.deploy=${maven.tomcat.path}
chimera.url=$CHIMERA_URL
chimera.path.deploy=$CHIMERA_DEPLOY_PATH
# this file is used to create the database initially
database.schema.file=chimera_db_schema.sql
webapp.version=1.0-SNAPSHOT
