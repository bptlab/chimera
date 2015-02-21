#!/bin/bash
cd /home/jmang/JEngine
mvn clean
./cov-build --dir cov-int mvn install -Dmaven.test.skip
./cov-emit-java --dir cov-int --war /home/jmang/JEngine/target/JEngine.war
tar czvf jengine.tgz cov-int
curl --form token=qpKLIQAC2s53hJPBaYV9XA \
  --form email=github@sunny.remote.mx \
  --form file=jengine.tgz \
  --form version="v1" \
  --form description="Work in progress on dev" \
  https://scan.coverity.com/builds?project=Jengine