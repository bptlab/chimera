# Troubleshooting

### Chimera crashes while persisting after adding new annotated classes or attributes in the code  or changing the code that do the persistence.

This can happen when the Database schema doesnt match the annotated classes and attributes. This can happen because there are no table for the newly added classes because new tables aren't generated from EclipseLink on startup. This happens when in the persistencexml the property "eclipselink.ddl-generation" doesnt have the values "create-table" or "drop-and-create-table". But even when these values are set and all classes are annotated correctly there are sometimes error. Therfore it's often necessary to drop and create the database schema manually before redeploying Chimera.


### Errors while redeploying with "tomcat7:redeploy -DskipTests"
When you use maven with the goal mvn tomcat:redeploy -DskipTests (For example by creating such a run configuration in Eclipse or using the shell command "mvn tomcat7:redepoy -DskipTests") after making major changes in the code, it  can happen that there are stille errors despite the code works correct. To avoid this, its a good idea from time to time to deploy manually via the following staps.
  1. open a shell in the chimera base directory (where the pom.xml is located)
  2. run "mvn clean install -DskipTests"
  3. now you find in the folder "target" in your chimera base directory a file ".war" file (Chimera.war). Copy this file
  4. navigate in your tomcat directory and open the "webapp" folder
  (4.a If tomcat is currently running, stop it!)
  5. If you previously deployed Chimera you will there find a file like "Chimera.war" and a corresponding directory like "Chimera". Delete the file and the directory.
  6. paste the ".war" file  (Chimera.war)
  7. start tomcat 
  8. Chimera should now be correcly deployed and automatically is executed with the startup of tomcat.


