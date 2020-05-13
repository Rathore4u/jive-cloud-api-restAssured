#!/usr/bin/env bash

echo Executing post actions

. $WORKSPACE/common-execution.sh

echo KEEP_INSTANCE = ${KEEP_INSTANCE}


if [ "x${KEEP_INSTANCE}"  = "xtrue" ] ; then
  echo "Keeping instance "
else
  echo "Undeploying instance"
  jcx_undeploy
fi

exit 0
