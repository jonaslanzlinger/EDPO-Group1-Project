FROM maven as build

COPY . /app

RUN mvn -f app/pom.xml --batch-mode --update-snapshots verify

FROM openjdk:21

COPY --from=build /app/target/streamprocessor-0.0.1.jar ./streamprocessor-0.0.1.jar

COPY --from=build /app/src/main/resources/application.yml ./application.yml

CMD java -jar streamprocessor-0.0.1.jar --spring.config.location=application.yml