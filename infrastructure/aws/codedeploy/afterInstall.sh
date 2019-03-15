#!/bin/bash
  
sudo systemctl stop tomcat.service

sudo rm -rf /opt/tomcat/webapps/docs  /opt/tomcat/webapps/examples /opt/tomcat/webapps/host-manager  /opt/tomcat/webapps/manager /opt/tomcat/webapps/assignment1-0.0.1-SNAPSHOT

sudo chown tomcat:tomcat /opt/tomcat/webapps/assignment1-0.0.1-SNAPSHOT.war

sudo rm -rf /opt/tomcat/logs/catalina*
sudo rm -rf /opt/tomcat/logs/*.log
sudo rm -rf /opt/tomcat/logs/*.txt
