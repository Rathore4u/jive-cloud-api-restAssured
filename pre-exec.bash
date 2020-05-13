#!/usr/bin/env bash

. $WORKSPACE/common-execution.sh

jcx_init

if [ -z "${INSTANCE_URL}" ] ; then 
    
    jcx_deploy

    jcx_reset_password

    # Override the execution method
else

    echo "We don't need jcx , instance URL : ${INSTANCE_URL}"
fi

if [ -z "${INSTANCE_URL}" ] ; then
   echo "We got no INSTANCE !!!!"
   exit -1
fi

if [ -z "${API_SUITE}" ] ; then

  SUITEOPT=""

else

  SUITEOPT="-Ddefault.api.suite=${API_SUITE}"

fi

export EXECUTION_COMMAND="mvn -Doverride.base.url=https://${INSTANCE_URL}/ ${SUITEOPT} -Pjenkins -DTESTRAIL_PLAN_NAME_IDENTIFIER=${BUILD_NUMBER} clean test"

# Execute the command here
echo running "$EXECUTION_COMMAND"
${EXECUTION_COMMAND}

# Exit successfully 
exit 0
