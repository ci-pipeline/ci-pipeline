## ci-pipeline
Run your jenkins pipeline with openiated `.ci-pipeline.yaml` (instead of the generic `Jenkinsfile`).

## Setup:
1. Run [a preconfigured jenkins instance](https://github.com/ci-pipeline/jenkins) using docker-compose.

2. You will need to add a "multibranch pipeline" job for your project, as follows:

<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/setup.png"  width="700px"/>

As shown, (1) you will need to add your source url. and for (2) you will need to change the build configuration so the mode will be "by default Jenkinsfile", and tick the "sandbox" option. as shown above. 

3. Place the file `.ci-pipeline.yaml` in the root dir of your project [see example here](https://github.com/ci-pipeline/example_multibranch) see next section on an example of the file.

## `.ci-pipeline.yaml`:

```yaml
image: golang:alpine

variables:
  TEST_URL: http://example.com
  POSTGRES_PASSWORD: t@st!p@ssw0rd

services:
  - postgres:latest
  - nginx:latest
  - redis:alpine
  - docker

steps:

  - name: build
    actions:
      - echo "building step 1" && go version
      - echo "building step 2"

  - parallel:

      - name: unit test
        except:
          - master
        actions:
          - echo "unit testing againest test url $TEST_URL and refereing to builtin var $NODE_NAME"

      - name: integration test
        only: [develop, release/*]
        actions:
          - apk add curl && apk add redis
          - echo "testing nginx" && curl nginx:80
          - echo "testing redis" && echo INCR x | redis-cli -h redis

  - name: deploy
    trigger: manual
    only:
      - release/*
    actions:
      - echo "do deploy"
      - docker version

```

<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/pipeline.png"  width="400px"/>
<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/pipeline-2.png"  width="400px"/>

### Documentations


### Inspiration
This project is inspired by [wolox-ci](https://github.com/Wolox/wolox-ci)
