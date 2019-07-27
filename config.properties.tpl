# this file is used when deploying with Docker (see https://github.com/bptlab/chimera/wiki/ChimeraConfig#deployment-with-docker for more information.

# connection parameter for the mysql database
mysql.schema=$CHIMERA_DB_NAME
mysql.test.schema=$CHIMERA_DB_TEST_NAME
mysql.username=$CHIMERA_DB_USERNAME
mysql.password=$CHIMERA_DB_PASSWORD
mysql.url=jdbc:mysql://$CHIMERA_DB_HOST:$CHIMERA_DB_PORT/$CHIMERA_DB_NAME?createDatabaseIfNotExist=true
mysql.test.url=jdbc:mysql://$CHIMERA_DB_HOST:$CHIMERA_DB_PORT/$CHIMERA_DB_TEST_NAME?createDatabaseIfNotExist=true

# connection parameter for the email task
mailtask.hostname=smtp.googlemail.com
mailtask.port=465
mailtask.mail.username=$CHIMERA_MAIL_USER
mailtask.mail.password=$CHIMERA_MAIL_PASSWORD

# connection to event processing platform Unicorn
unicorn.url=$UNICORN_URL
unicorn.path.deploy=$UNICORN_DEPLOY_PATH
# REST endpoints of Unicorn
unicorn.path.eventtype=/webapi/REST/EventType
unicorn.path.event=/webapi/REST/Event
unicorn.path.query.rest=/webapi/REST/EventQuery/REST
unicorn.path.query.queue=/webapi/REST/EventQuery/Queue
# URL of deployed Chimera instance needed for callbacks from Unicorn
<<<<<<< HEAD
chimera.url=$CHIMERA_URL
=======
chimera.url=http://chimera:8080
>>>>>>> edae2f2... Fix broken stuff. Sorry 😇
chimera.path.deploy=$CHIMERA_DEPLOY_PATH

# version of the API
webapp.version=2
