#!/bin/bash
cd /home/jmang/JEngine
mvn clean
cov-build --dir cov-int mvn -DskipTests=true compile
cov-emit-java --dir cov-int --war /home/jmang/JEngine/target/JEngine.war
tar czvf jengine.tgz cov-int