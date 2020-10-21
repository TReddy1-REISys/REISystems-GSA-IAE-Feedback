#!/bin/sh
#
# Service startup script
#

export IAE_ENV=$1
export NEWRELIC_AGENT=true


case "$1" in
    comp)
      ENV_NAME=dev
      NEWRELIC_AGENT=false
    ;;
    
    minc)
      ENV_NAME=test
      NEWRELIC_AGENT=false
    ;;
    
    login)
      ENV_NAME=performance
    ;;
    
    prodlike)
      ENV_NAME=uat
    ;;    

    prod)
      ENV_NAME=prod
    ;;   
     
    *)
      ENV_NAME=$1 
    ;;
esac

export APP_NAME="FEEDBACK-$ENV_NAME"

java -javaagent:/newrelic/newrelic.jar -Dnewrelic.config.agent_enabled=$NEWRELIC_AGENT -Dnewrelic.config.app_name="$APP_NAME;$ENV_NAME" -Dspring.application.name=$APP_NAME -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2 -jar target/feedback-manager-0.1.0.jar --debug
