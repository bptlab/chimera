<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
  version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
  <persistence-unit name="CaseModel" transaction-type="RESOURCE_LOCAL">
    <exclude-unlisted-classes>false</exclude-unlisted-classes>
    <properties>
      <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
      <!--<property name="javax.persistence.jdbc.url"    value="jdbc:mysql://$CHIMERA_DB_HOST:$CHIMERA_DB_PORT/$CHIMERA_DB_NAME?characterEncoding=utf8&useUnicode=true" />-->
      <property name="javax.persistence.jdbc.url"    value="jdbc:mysql://$CHIMERA_DB_HOST:$CHIMERA_DB_PORT/$CHIMERA_DB_NAME" />
      <property name="javax.persistence.jdbc.user" value="$CHIMERA_DB_USERNAME" />
      <property name="javax.persistence.jdbc.password" value="$CHIMERA_DB_PASSWORD" />
      <property name="eclipselink.ddl-generation" value="create-or-extend-tables" />
      <property name="eclipselink.ddl-generation.output-mode" value="database" />
      <property name="eclipselink.logging.parameters" value="true"/>
      <property name="eclipselink.logging.level" value="ALL" />
    </properties>
  </persistence-unit>
</persistence>
