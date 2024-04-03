# Homely Assist Project


## CMD
- apt-get update
- apt-get install openjdk-11-jre openjdk-11-jdk
- sudo kill -9 $(sudo lsof -t -i:443)
- sudo kill -9 $(sudo lsof -t -i:80)
- java -jar -Dspring.profiles.active=prod backend-homelyassist-0.0.1-SNAPSHOT.jar
- java -jar -Dserver.port=80 -Dspring.profiles.active=prod backend-homelyassist-0.0.1-SNAPSHOT.jar
- java -jar -Dserver.port=443 -Dspring.jpa.hibernate.ddl-auto=drop -Dspring.profiles.active=prod backend-homelyassist-0.0.1-SNAPSHOT.jar
- java -jar -Dserver.port=80 -Dspring.jpa.hibernate.ddl-auto=drop -Dspring.profiles.active=prod backend-homelyassist-0.0.1-SNAPSHOT.jar


Cloudflare:
- mimi.ns.cloudflare.com
- sean.ns.cloudflare.com