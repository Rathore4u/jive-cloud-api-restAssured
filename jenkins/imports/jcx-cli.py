import json
import os
import requests
import sys
import tempfile
import time
import uuid
from datetime import datetime, timedelta
from sys import argv
from requests.adapters import HTTPAdapter
from requests.packages.urllib3.util.retry import Retry

DEFAULT_JCX_HOST = "https://cloud-jcx-api.aws-us-east-1-infra-test.svc.jivehosted.com"
HEADERS = {"Content-Type": "application/json"}
OPERATIONS = ['undeploy', 'reset-password', 'deploy', 'upgrade']

CERTIFICATE = """-----BEGIN CERTIFICATE-----
MIIDkjCCAnqgAwIBAgIRAMpqWCdJy0x2rhQ8kL6Qd7UwDQYJKoZIhvcNAQELBQAw
gYMxJjAkBgNVBAMMHUVuZ2luZWVyaW5nIFBpcGVsaW5lIFNlcnZpY2VzMRQwEgYD
VQQLDAtFbmdpbmVlcmluZzEWMBQGA1UECgwNSml2ZSBTb2Z0d2FyZTERMA8GA1UE
BwwIUG9ydGxhbmQxCzAJBgNVBAgMAk9SMQswCQYDVQQGEwJVUzAeFw0xODEyMDgw
ODAwMDBaFw0yMTEyMDgwODAwMDBaMHMxFjAUBgNVBAMMDVFFLUpDWC1kZXBsb3kx
FjAUBgNVBAoMDUppdmUgU29mdHdhcmUxFDASBgNVBAsMC0VuZ2luZWVyaW5nMQsw
CQYDVQQGEwJVUzELMAkGA1UECAwCT1IxETAPBgNVBAcMCFBvcnRsYW5kMIIBIjAN
BgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmXjghDCnsNgjsEQ/BJq1Pf7SfhY3
UaTgLS+dm+0H+Zxl0gDHVYVONhLo6WQOcOc0LxTa9ij/Crs59RANErpVI/lhdOeA
XfpqoRHRuvh/velAA5+CxYphYrVWu0owkP9skAcEsA3rzQ62cnzahC1RVxe7OQNG
TNGPvchp3njlhzauMc+J5FjaFoTpwIWcgat4Z4MIA0Tw7uXK7vqlV3JrdhOsyEGd
6ZUyl5EAriauOcIg5fv7f2NXdhfvjzzx+Oouq9TRhgls0/t/7TvttOoJHJBmJfKR
Aog5CoqJ3wyY8i54f5ay8j6xa9x2Wgi91OOZRHiEzZKRHGid19zsVGZCuQIDAQAB
oxAwDjAMBgNVHRMBAf8EAjAAMA0GCSqGSIb3DQEBCwUAA4IBAQCFmDs0/94ujfsZ
cpxOtxcxK/70diHhtXBBTBNccwnKDw8s03Z9lU5fTQRJbUe00T4zNgqVeSKugRiC
wkGGE2O//m4WhNW20v7CgbIKF1B3giCVQwKQ3bh1n5Pc01BDF2DnM/6UCzevKvgq
tft2VU3f8s3LzNeaLRDEb4ToxgekVJXupSh94JgYifI7oYBmKFWdgfkNKIzS5u21
Bj9otUF6QiIIB3IVB1vrYD09xAPTUg2r5H/ugvW/X8Oxnqh4jawBAQkA/GgzZasa
Xi/LKxiJSTSzjbBk7FnVs+Fn3yG7B4N4SvnvOeL2NOyG//jDXl9rWkVbEOMsJaSq
m7KYr7nr
-----END CERTIFICATE-----"""

PRIVATE_KEY = """-----BEGIN RSA PRIVATE KEY-----
MIIEowIBAAKCAQEAmXjghDCnsNgjsEQ/BJq1Pf7SfhY3UaTgLS+dm+0H+Zxl0gDH
VYVONhLo6WQOcOc0LxTa9ij/Crs59RANErpVI/lhdOeAXfpqoRHRuvh/velAA5+C
xYphYrVWu0owkP9skAcEsA3rzQ62cnzahC1RVxe7OQNGTNGPvchp3njlhzauMc+J
5FjaFoTpwIWcgat4Z4MIA0Tw7uXK7vqlV3JrdhOsyEGd6ZUyl5EAriauOcIg5fv7
f2NXdhfvjzzx+Oouq9TRhgls0/t/7TvttOoJHJBmJfKRAog5CoqJ3wyY8i54f5ay
8j6xa9x2Wgi91OOZRHiEzZKRHGid19zsVGZCuQIDAQABAoIBAGeS6UqtH26835EE
7daopip5wArx6VGDSPuVx/NES36MqzbH6b7woTDjICbqrMhLcK9ZjtBUKJNQebPG
qlv6Pp1xnvDrj3ldF/st8HITZPqA4P/Q9ctlMYYRRcBOOGqSxuyIoARE0N9pz32C
f2J2xw9U57h5Wkdbont9Nt26j4TpOaW/HUcixwVo2XBcPuojAPQlRgO13kW12GUW
0FU90XmkSkaK0bHsAuVbm8L1zz12mKav6AKkWP3+dsmfPoChrwuILverdePJhAzA
mBBDE9mnOBVhOCM2KXgTuUsu4G/e27LxTmh/cACy09MC5vH1DNj6j18P+mL4VYmM
z2IA9EUCgYEAyE9HGxtbOTOe9CF8nOHBWmCJw8o1S+mHG7D3v4LTuvjN/f+OjKUt
5h+I16JLa71WuvIMOBGM2pSZuJ520dChxgCJxG+RknNdnxwS5A5u91QVlS0zlFYb
syEtxy8Pkcdal+y2AsDanBxY96h7bhLoKKqygLm+lGbZmPepaJE+hIcCgYEAxCQD
vcvj5k6kyud2g0WWKTb2DxmmZoSng9r3AbScbYw8VeRXuwPbVremurGWoqWwd2tq
jZFov5SXGsIKfNYhwhH41DEniOLLJ0AXKk00ddZIq4ro/OoEyRwKnT67g+JQKqxx
icRkIeeVkuIcDo/XG/2deW64we0OYd+ImXruDr8CgYBejpX5ZNOOUCD0C67AfA2D
NVkzNIwuNqIR31tEa2S5uWO8SFwpZnwhSreVW0aRKIL9ou9DtH0yXb2nGh7q8rq7
NlvSf/xPDuieCGmegHW/iVDa58iWeIOA7uOmSD/oSBwq5ZISAKnx0avp1P3XO1Oe
ht3WyFf/xAFqJVcyRoYllwKBgQCnIyFlDGdJi951YMPmnNeWi1UmDO91ELlGbMJr
W6Ko3wVTE9+GZ4Ty4lBbapU4SASV3pzPwgcGqfIRRSR2uGnP3EyzVKehx9Dhw4V5
8IwxlcYbF6XuTuSBEzogQKK9gY5artOZWBcTpSoaHj9WUVA2c+lK3wTli8HjuP3H
+678NQKBgBJO/zUa1MoD6xVnI/yvquVc1j87JrGSvX7DA6ko9Llfvbv5pf1zG2OY
+O4dri+zRbQAA1RlR8CajYMgI98QizFcizdVthywu1/l30FM8be4TtpDANSnkumb
qejzWcRr22jJYlBocMUA7rnQPC4WWjhqyzPiiSf4wyUu/nQJFGiH
-----END RSA PRIVATE KEY-----"""

CA_CERTIFICATE = """-----BEGIN CERTIFICATE-----
MIID5TCCAs2gAwIBAgIQQ860d6dqTyupexbMDdja6DANBgkqhkiG9w0BAQsFADCB
gzEmMCQGA1UEAwwdRW5naW5lZXJpbmcgUGlwZWxpbmUgU2VydmljZXMxFDASBgNV
BAsMC0VuZ2luZWVyaW5nMRYwFAYDVQQKDA1KaXZlIFNvZnR3YXJlMREwDwYDVQQH
DAhQb3J0bGFuZDELMAkGA1UECAwCT1IxCzAJBgNVBAYTAlVTMB4XDTE4MTIwNTA3
MDAwMFoXDTIzMTIwNTA3MDAwMFowgYMxJjAkBgNVBAMMHUVuZ2luZWVyaW5nIFBp
cGVsaW5lIFNlcnZpY2VzMRQwEgYDVQQLDAtFbmdpbmVlcmluZzEWMBQGA1UECgwN
Sml2ZSBTb2Z0d2FyZTERMA8GA1UEBwwIUG9ydGxhbmQxCzAJBgNVBAgMAk9SMQsw
CQYDVQQGEwJVUzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAL5KiikV
4KwjIJm42AaVUU7HWnIYI62qZ9tTMgr+ZcJnBOSnaG2ucZjuVjxpeGX2Bx55vNHb
9XgK/UIebRZvn2P5cRvpyTQK9CqomqhW3nXoJcjE1ITkLAposP6UbOHHrNPpn2OG
K+7L/l8Fkw5UN+UFBCKibTtZf4sykfEXMIQr0F/fm+JS8DNgfPVdiuakCMzk+nQn
9tq6nxAmSfL5qVKgspsIpoJ+WpxVkmB7+tT5IEU9oVRnDif1IOJdt7FF2IPfyOYP
a7ltX3Nm/MAYA+wKLKTGBwGJ+k7Sbpuwf/xzr2af0aAP7zkJr7mRPNUmsTaOltrn
8b/WPX72og7b/I8CAwEAAaNTMFEwDwYDVR0TBAgwBgEB/wIBATAdBgNVHQ4EFgQU
mkuqf2kohAAuVtQbYdM+HdrV4OMwHwYDVR0jBBgwFoAUmkuqf2kohAAuVtQbYdM+
HdrV4OMwDQYJKoZIhvcNAQELBQADggEBAJS4vlN9mQbyN9jixUk7+NBBbcV9BvAy
IiYX5oThYRJa70jxgn5eOfsIhLTpzSKoBaIGrVQJQBY97EqiEEXwKGZP+SAzXYD6
9kAIFOMEAQ/hBQSWaPDdahxIffqP6URGM3RBsIBQhMPZbRIsXG773kqqkIq8Vutf
l/xiPyY6TsgKfh+t5JHM3WVPwmEYH4dS3GZpp2/CQdr73P8alyNwcIwKIPQ6pBXG
A0eo+EJOsb5JesFAVDcnizqTzKzi9hN9Smzcn7ZAtlyR9zBA5zCeHHPkNkhMY9L0
O+YvKFcBVFrmR4ERX7mikUKVRuepo1K2XGn7DyfylaqmfLseugRTris=
-----END CERTIFICATE-----"""


def print_help():
    print("""
    This is script has super powers never seen before, with these super powers you can deploy a release to JCX (or JCA?)
    using latest release available for a branch or a fixed release.
    
    PARAMETERS (all optional):
        -name = Instance name, defaults to banzai-rand-<6 digit random>
        -ttl = time to live in days, defaults to 1
        -role = instance role, test, demo...,  defaults to test
        -newrelic = flag to turn newrelic on
        -seed = seed uri to use as template
        
        -release = the release uri to deploy, defaults to latest for develop branch
        -branch = the branch to search latest release available, defaults to develop
        -branch-fallback = the branch to fallback search latest release available if branch is not found
        
        -host = JCX host to deploy, defaults to JCA/JCX QA environment
        -cert = path for api authentication certificate, default value is provided for QA environment
        -cert-key = path for api authentication certificate's private key, default value is provided for QA environment
        -ca-cert = certificate authority's certificate for accessing api, default value is provided for QA environment  
        
        -email = creator email, defaults to protractor-qa-automation@aurea.com
        -user = user name owner of the instance, defaults to Protractor QA Automation
        
        -replicas = number of replicas to deploy, defaults to 2 
        -cpus = number of cpus to allocate, defaults to 2
        -heap = java heap memory size, defaults to '4000'
        -mem = java memory configuration, defaults to '5000'

        -jive-id = enabled Jive ID? true|false, defaults to false

        -wait = if the script should wait for deployment to finish, defaults to false
        -wait-minutes = how many minutes to wait for instance to be deployed, defaults to 20
        -wait-sleep = how many seconds to sleep between checks, defaults to 10
        
        -o = output file for deployment information: name, deploy-uri and url
        -format = json or null to environment file (properties) format
        
        -info = prints info logs
        -debug = prints debug information
        
    USAGE:
    
    getting help (oops, you are here already):
        python jcx.py -help|-h
        
    deploying latest release for branch develop with random name:
        python jcx.py   # (yeah, that's all)
    
    deploying specific branch (optionally with fallback branch if no release is found for desired branch):
        python jcx.py -branch release/2018.2.1
        python jcx.py -branch release/2018.2.1 -branch-fallback release/2018.2
        
    deploying and waiting deployment to finish (optionally provide wait minutes and sleep time in secs between retries):
        python jcx.py -branch release/2018.2 -wait
        python jcx.py -branch release/2018.2 -wait -wait-minutes 40 -wait-sleep 10
        
    deploying specific release:
        python jcx.py -wait -info -release jcx-rel-f091b3fa4dff4cfca256e08d1c5900ac
    
    deploying with a specific instance name
        python jcx.py -name qe-content-v2-pri1-cd35b
        
    upgrade with downtime
        python jcx.py -upgrade jcx-inst-4kcqkkk4wdovs836x0epis -branch release/2018.2.8 -downtime
        
    debug and info:
        python jcx.py -debug -info
    """)

    sys.exit(0)


class Certificates(object):

    def __init__(self, cert="", key="", ca=""):
        self.cert, self.key, self.ca = cert, key, ca

    def __getitem__(self, item):
        return getattr(self, item)

    @staticmethod
    def write_to_temp_file(content):
        temp_file, path = tempfile.mkstemp()
        with os.fdopen(temp_file, 'w') as f:
            f.write(content)
            f.close()
        return path

    def resolve(self, opts):
        self.cert = opts.get('cert') if opts.get('cert') else self.write_to_temp_file(CERTIFICATE)
        self.key = opts.get('cert-key') if opts.get('cert-key') else self.write_to_temp_file(PRIVATE_KEY)
        self.ca = opts.get('ca-cert') if opts.get('ca-cert') else self.write_to_temp_file(CA_CERTIFICATE)

        return self


CERTS = Certificates()


def rand_id():
    return uuid.uuid4().hex.replace("-", "")[0:8]


def pretty_print_json(data):
    return json.dumps(data, sort_keys=True, indent=4, separators=(',', ': '))


def resolve_release_by_branch(opts, branch):
    #if branch refers to tag, we restore original branch name
    branch = branch.replace('refs/tags/', '')
    # release description does not contains /, it's replaced by -
    branch = branch.replace('/', '-')

    if opts.get('info'):
        print('INFO: searching latest releases matching "{0}"'.format(branch))

    request_url = opts['host'] + '/releases'
    response = requests.get(request_url, headers=HEADERS, timeout=60, cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
    releases = json.loads(response.text)

    # filter releases by desired branch
    releases_for_branch = [release for release in releases if release['description'] == branch]
    if opts.get('info'):
        print('INFO: found {0} releases matching {1}'.format(len(releases_for_branch), branch))

    # get latest by buildTime
    releases_for_branch = sorted(releases_for_branch, key=lambda rel: rel['buildTime'], reverse=True)
    return releases_for_branch[0] if len(releases_for_branch) > 0 else None


def resolve_release(opts):
    branch = opts.get('branch', 'develop')
    branch_fallback = opts.get('branch-fallback')

    release = resolve_release_by_branch(opts, branch)
    release = release if release else resolve_release_by_branch(opts, branch_fallback) if branch_fallback else None
    release_uri = release['uri'] if release else None

    if not release_uri:
        print('ERROR: could not find available release for branch "{0}" fallback "{1}"'.format(branch, branch_fallback))
        sys.exit(-1)

    if opts.get('info'):
        print('INFO: resolved release {0}'.format(release_uri))

    return release_uri


def setup_from_arguments(opts):
    CERTS.resolve(opts)

    if opts.get('debug'):
        try:
            import http.client as http_client
        except ImportError:
            import httplib as http_client
        http_client.HTTPConnection.debuglevel = 1


def defaults_args(opts):
    opts['host'] = opts.get('host', DEFAULT_JCX_HOST)

    opts['name'] = opts.get('name', 'autotest-rand-' + rand_id())
    opts['ttl'] = int(opts.get('ttl', 1))
    opts['role'] = opts.get('role', 'test')
    opts['env'] = opts.get('env', 'aws-us-east-1-infra-test')
    opts['seed'] = opts.get('seed', '')
    opts['newrelic'] = opts.get('newrelic', False)


    opts['email'] = opts.get('email', 'protractor-qa-automation@aurea.com')
    opts['user'] = opts.get('user', 'Protractor QA Automation Aurea')

    opts['replicas'] = opts.get('replicas', '2')
    opts['cpus'] = opts.get('cpus', '2')
    opts['heap'] = opts.get('heap', '4000')
    opts['mem'] = opts.get('mem', '5000')

    opts['jive-id'] = opts.get('jive-id', 'false')

    opts['wait'] = opts.get('wait', False)
    opts['wait-minutes'] = int(opts.get('wait-minutes', 40))
    opts['wait-sleep'] = int(opts.get('wait-sleep', 5))

    opts['downtime'] = opts.get('downtime', False)

    # setup certificates for calling jcx instance
    setup_from_arguments(opts)

    # resolve the operation to be performed
    opts['operation'] = 'deploy'
    for op in OPERATIONS:
        if opts.get(op):
            opts['operation'] = op
            break

    # variables that depends on calling jcx instance
    if opts['operation'] == 'deploy':
        if not opts.get('release'):
            opts['release'] = resolve_release(opts)

    if opts['operation'] == 'upgrade':
        if not opts.get('release'):
            opts['release'] = resolve_release(opts)

    return opts


def parse_args(opts):
    result = {}
    while opts:
        if opts[0][0] == '-':
            result[opts[0][1:]] = opts[1] if len(opts) > 1 and not opts[1][0] == '-' else True
        opts = opts[1:]
    return defaults_args(result)


def clean_up():
    os.remove(CERTS.cert)
    os.remove(CERTS.key)
    os.remove(CERTS.ca)


def generate_deploy_request_data(opts):
    name = opts['name']
    expiration_date = datetime.now() + timedelta(days=opts['ttl'])
    newrelic = opts['newrelic']
    seed = opts['seed']

    environment = opts['env']
    jive_id_enabled = opts['jive-id']
    release = opts['release']
    creator_email = opts['email']
    creator_name = opts['user']
    role = opts['role']
    replicas = opts['replicas']
    cpus = opts['cpus']
    heap = opts['heap']
    mem = opts['mem']

    return {
        "name": name,
        "expirationTimestamp": expiration_date.strftime("%Y-%m-%dT%H:%M:%SZ"),
        "environment": environment,
        "jiveIDEnabled": jive_id_enabled,
        "releaseUri": release,
        "creatorEmail": creator_email,
        "creatorFullName": creator_name,
        "role": role,
        "webappReplicas": replicas,
        "webappMetaCPU": cpus,
        "webappJavaHeapMb": heap,
        "webappMemoryMb": mem,
        "seedInstanceUri": seed,
        "newrelicEnabled": newrelic
    }

def generate_upgrade_request_data(opts):
    release = opts['release']
    downtime = opts['downtime']

    return {
        "releaseUri": release,
	"downtime": downtime
    }


def print_server_response(opts, resp):
    if not resp.status_code in [200, 201, 202, 409]:
        print('ERROR: request failed with status code {0}'.format(str(resp.status_code)))
        if 'json' in resp.headers['Content-Type']:
            print(pretty_print_json(json.loads(resp.text)))
        else:
            print(resp.text)
        sys.exit(-1)

    json_res = json.loads(resp.text)
    if opts.get('info'):
        print('INFO: response from server {0}'.format(pretty_print_json(json_res)))

    return json_res


def execute_deploy_request(opts, data):
    request_url = opts['host'] + '/instances'
    json_req = pretty_print_json(data)

    if opts.get('info'):
        print('INFO: sending request to {0} with data {1}'.format(request_url, json_req))

    s = requests.Session()
    retries = Retry(total=5,
                backoff_factor=0.1,
                method_whitelist=frozenset(['GET', 'POST']))
    
    ## s.mount(request_url, HTTPAdapter(max_retries=retries))

    response = s.post(request_url, data=json_req, headers=HEADERS, timeout=200,
                             cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
    json_res = print_server_response(opts, response)

    if json_res.get('status', "") == 'CONFLICT':
#we have problem, should switch to new instance, but should find it by name
        confl_request_url = opts['host'] + '/instances?name=' + data['name']
        if opts.get('info'):
            print('INFO: conflict found, redirecting. sending request to {0}'.format(confl_request_url))
        
        s = requests.Session()
        retries = Retry(total=5,
                backoff_factor=0.1,
                method_whitelist=frozenset(['GET', 'POST']))
        
        s.mount(request_url, HTTPAdapter(max_retries=retries))
        
        response = s.get(confl_request_url, data=json_req, headers=HEADERS, timeout=120,
                             cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
        json_res = print_server_response(opts, response)
        
        
        instance_uri = json_res['items'][0]['uri']        
        json_res['instanceUri'] = instance_uri
        json_res['instance']['url'] = data['name'] + '.jivelandia.com'
        json_res['created'] = json_res['items'][0]['creationTimestamp']
    else:
	    instance_uri = json_res['instanceUri']
    
    assert instance_uri is not None and instance_uri is not ""

    return json_res

def execute_upgrade_request(opts, data):
    deployment_uri = opts.get('upgrade')
    request_url = opts['host'] + "/instances/{0}/upgrade".format(deployment_uri)
    json_req = pretty_print_json(data)

    if opts.get('info'):
        print('INFO: sending request to {0} with data {1}'.format(request_url, json_req))

    response = requests.post(request_url, data=json_req, headers=HEADERS, timeout=60,
                             cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
    json_res = print_server_response(opts, response)

    instance_uri = json_res['instanceUri']
    assert instance_uri is not None and instance_uri is not ""

    return json_res


def undeploy_instance(opts):
    deployment_uri = opts.get('undeploy')
    if deployment_uri:
        delete_url = opts['host'] + "/instances/{0}".format(deployment_uri)

        response = requests.delete(delete_url, headers=HEADERS, cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
        print_server_response(opts, response)


def reset_admin_credentials(opts):
    data = json.dumps({
        'newPassword': opts.get('password', 'admin')
    })
    request_url = "https://{0}.i.jivelandia.com/api/jcx/v1/setup/updateAdminPasswordTo".format(opts['instance-url'])
    if opts.get('info'):
        print('INFO: requesting password reset on {0}'.format(request_url))

    response = requests.put(request_url, data=data, headers=HEADERS,
                            cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
    print_server_response(opts, response)


def wait_till_deploy_is_finalized(opts, deploy_data):
    if not args['wait']:
        return True

    wait_minutes = args['wait-minutes']
    delay_in_secs = args['wait-sleep']
    instance_uri = deploy_data['instanceUri']

    attempts = int(wait_minutes * 60 / delay_in_secs)

    request_url = opts['host'] + '/instances/finalized?uri={0}'.format(instance_uri)

    for i in range(attempts):
        time.sleep(delay_in_secs)
        try:
            if opts.get('info'):
                print('INFO: checking if finalized attempt {0} of {1}'.format(i, attempts))

            resp = requests.get(request_url, headers=HEADERS, cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
            if not resp.status_code == 200:
                continue

            finalized = json.loads(resp.text)["finalized"]
            if not finalized:
                continue

            if opts.get('info'):
                print('INFO: deployment finalized successfully')
            return True

        except requests.RequestException as exception:
            print('ERROR: problem with request: {0}'.format(str(exception)))

    print('ERROR: instance {0} deployment was not finalized after {1} minutes'.format(instance_uri, str(wait_minutes)))
    return False

def wait_till_upgrade_is_done(opts, deploy_data):
    if not args['wait']:
        return True

    wait_minutes = args['wait-minutes']
    delay_in_secs = args['wait-sleep']
    jcxTaskId = deploy_data['jcxTaskId']

    attempts = int(wait_minutes * 60 / delay_in_secs)

    request_url = opts['host'] + '/tasks/{0}'.format(jcxTaskId)

    for i in range(attempts):
        time.sleep(delay_in_secs)
        try:
            if opts.get('info'):
                print('INFO: checking if finalized attempt {0} of {1}'.format(i, attempts))

            resp = requests.get(request_url, headers=HEADERS, cert=(CERTS.cert, CERTS.key), verify=CERTS.ca)
            if not resp.status_code == 200:
                continue

            jcxTaskState = json.loads(resp.text)["jcxTaskState"]
            if opts.get('info'):
                print('INFO: current task state - {0}'.format(jcxTaskState))

            if jcxTaskState == 'PENDING' or jcxTaskState ==  'STARTED':
                continue

            if opts.get('info'):
                print('INFO: task finished')
            return True

        except requests.RequestException as exception:
            print('ERROR: problem with request: {0}'.format(str(exception)))

    print('ERROR: task {0} was not finalized after {1} minutes'.format(jcxTaskId, str(wait_minutes)))
    return False


def output_values(opts, response):
    name = response['instance']['name']
    url = response['instance']['url']
    uri = response['instanceUri']
    expiration = response['instance']['expirationTimestamp']
    created = response['created']

    output_format = opts.get('format')
    if output_format == 'json':
        output = pretty_print_json({'name': name, 'uri': uri, 'url': url, 'expiration': expiration, 'created': created})
    else:
        output = "\n".join([
            "INSTANCE_NAME={name}",
            "INSTANCE_URI={uri}",
            "INSTANCE_URL={url}",
            "INSTANCE_EXPIRATION={expiration}",
            "INSTANCE_CREATED={created}" ]) \
            .format(name=name, uri=uri, url=url, expiration=expiration, created=created)
    print(output)

    if args.get('o'):
        output_file = args.get('o')
        with open(output_file, 'w') as f:
            f.write(output)
            f.close()

def output_upgrade_values(opts, response):
    name = response['instanceName']
    url = response['instanceName'] + '.jivelandia.com'
    uri = response['instanceUri']

    output_format = opts.get('format')
    if output_format == 'json':
        output = pretty_print_json({'name': name, 'uri': uri, 'url': url})
    else:
        output = "\n".join([
            "INSTANCE_NAME={name}",
            "INSTANCE_URI={uri}",
            "INSTANCE_URL={url}" ]) \
            .format(name=name, uri=uri, url=url)
    print(output)

    if args.get('o'):
        output_file = args.get('o')
        with open(output_file, 'w') as f:
            f.write(output)
            f.close()



if __name__ == '__main__':
    try:
        args = parse_args(argv)
        if args.get('h') or args.get('help'):
            print_help()

        if args.get('undeploy'):
            undeploy_instance(args)
        elif args.get('reset-password'):
            reset_admin_credentials(args)
        elif args.get('upgrade'):
            req = generate_upgrade_request_data(args)
            res = execute_upgrade_request(args, req)
            if not wait_till_upgrade_is_done(args, res):
                sys.exit(-1)
            output_upgrade_values(args, res)
        else:  # defaults to deploy
            req = generate_deploy_request_data(args)
            res = execute_deploy_request(args, req)
            if not wait_till_deploy_is_finalized(args, res):
                sys.exit(-1)
            output_values(args, res)
    finally:
        clean_up()
