FROM dtr-1.prod-iae.bsp.gsa.gov/docker-datacenter/openjdk:8-alpine-maven

RUN wget https://artifactory.helix.gsa.gov/artifactory/GS-IAE-Generic/newrelic.zip && unzip newrelic.zip 
  
ADD . .
ADD . /services
WORKDIR /services
RUN mvn package

COPY entrypoint.sh run.sh ./
RUN chmod +x /*.sh


ENTRYPOINT ["/entrypoint.sh"]
EXPOSE 8443

CMD /run.sh ${IAE_ENV}

