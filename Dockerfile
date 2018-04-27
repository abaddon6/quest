FROM mavenqa.got.volvo.net:18443/openjdk:8-jdk-alpine

VOLUME /tmp

User root
RUN mkdir data

ARG JAR_FILE
ADD ${JAR_FILE} /data/app.war

RUN chmod -R +x /data/
RUN chmod -R g+rwX /data/

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=production", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:MaxRAMFraction=2", "-XshowSettings:vm", "-jar", "/data/app.war"]