FROM maven:3.6.3-openjdk-11

ENV PROJECT_DIR=/opt/dir

RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD ./discovery/pom.xml $PROJECT_DIR
RUN mvn dependency:resolve

ADD ./discovery/src $PROJECT_DIR/src
RUN mvn install -DskipTests=true

FROM openjdk:11-jdk

ENV PROJECT_DIR=/opt/dir
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=0 $PROJECT_DIR/target/discovery*.jar $PROJECT_DIR/app.jar

EXPOSE 8761

CMD ["java", "-jar", "app.jar"]