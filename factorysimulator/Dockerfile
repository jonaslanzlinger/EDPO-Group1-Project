FROM maven as build

COPY . /app

RUN mvn -f app/pom.xml --batch-mode --update-snapshots verify

FROM openjdk:21

COPY --from=build /app/target/factorysimulator-0.0.1.jar ./factorysimulator-0.0.1.jar

COPY --from=build /app/src/main/resources/application.yml ./application.yml
COPY --from=build /app/src/main/resources/data.txt ./src/main/resources/data.txt

CMD java -jar factorysimulator-0.0.1.jar --spring.config.location=application.yml