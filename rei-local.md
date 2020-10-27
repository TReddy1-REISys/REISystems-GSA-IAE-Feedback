# REI - Local development notes

## Application-local.properties
In `src/app/main/resources/` add an `application-local.properties` file. Use the following as reference

```properties
#server.port=8081
spring.datasource.username = postgres
spring.datasource.password = postgres
spring.datasource.url=jdbc:postgresql://ogpsql.reisys.com:5432/feedback_service?stringtype=unspecified&currentSchema=feedbackschema

spring.datasource.hikari.maximum-pool-size=10

mailing.to.list=treddy@reisystems.com diego.ruiz@reisystems.com jmohanta@reisystems.com

mailing.subject=SAM.gov Feedback Responses
mailing.text=Please find feedback report for -
mailing.text1=number of records:
mailing.from.address=donotreply@sam.gov
mailing.footer.message=This email was sent from Local Env

mail.server=st-mail-01.reisys.com
mail.protocol=smtp
```

## Runnning app
Run app with this command (or IDE equivalent):

```bash
mvn spring-boot:run -Dspring-boot.run.fork=false -Plocal
```

Once running, swagger page can be accessed here:

`http://localhost:8080/swagger-ui-custom.html`

## Setting up when porting from GSA
- update pom.xml
    - point repositories away from gsa nexus
    - strip out bsp platform-specific dependencies (heartbeat/audit-logging)
