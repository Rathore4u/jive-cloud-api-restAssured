#!/usr/bin/env bash

export IMPORTS=${WORKSPACE}/jenkins/imports
export ENV_FILE=${ENV_FILE:-'_environment.env'}

function read_env_file() {

    [[ -f ${ENV_FILE} ]] && source ${ENV_FILE}

    export INSTANCE_NAME=${INSTANCE_NAME:-}
    export INSTANCE_URL=${INSTANCE_URL:-}
    export INSTANCE_URI=${INSTANCE_URI:-}
}

function jcx_deploy() {
    OTHER=${PARAMETERS:-}
    python3 ${IMPORTS}/jcx-cli.py -wait ${OTHER} -info -branch ${BRANCH} -branch-fallback develop -o ${ENV_FILE}
}

function jcx_deploy_ttl() {
    OTHER=${PARAMETERS:-}
    python3 ${IMPORTS}/jcx-cli.py -wait ${OTHER} -info -ttl ${TTL} -branch ${BRANCH} -env ${ENV} -branch-fallback develop -o ${ENV_FILE}
}

function jcx_reset_password() {
    read_env_file
    python3 ${IMPORTS}/jcx-cli.py -reset-password -info -instance-url ${INSTANCE_URL} -password ${DEFAULT_ADMIN_PASSWORD}
}

function jcx_undeploy() {
    read_env_file
    echo "Undeploying ${INSTANCE_URI} "
    python3 ${IMPORTS}/jcx-cli.py -undeploy ${INSTANCE_URI} -info
}

function jcx_init() {
    #echo Installing Pip
    # curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
    #python get-pip.py

    echo "Installing jcx python deps"
    pip3 install --user requests
}