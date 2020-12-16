## ci-pipeline
Run your jenkins pipeline with openiated `.ci-pipeline.yaml` (instead of the generic `Jenkinsfile`).

## Setup:
1. Run [a preconfigured jenkins instance](https://github.com/ci-pipeline/jenkins) using docker-compose.

2. You will need to add a multibranch pipeline job for your project ([see example](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md#create-a-multibranch-pipeline-job))


3. Place the file `.ci-pipeline.yaml` in the root dir of your project [see example here](https://github.com/ci-pipeline/example_multibranch) see next section on an example of the file.

## `.ci-pipeline.yaml`:

```yaml
image: golang:alpine

variables:
  TEST_URL: http://example.com

services:
  - postgres
  - mysql

steps:
  - name: build
    actions:
      - echo "building step 1"
      - echo "building step 2"
  - parallel:
      - name: unit test
        except:
          - master
        actions:
          - echo "unit testing againest test url= $TEST_URL"
      - name: integration test
        only: [develop, release/*]
        actions:
          - echo "integration testing step 1"
          - echo "integration testing step 2"
  - name: deploy
    trigger: manual
    only:
      - release/*
    actions:
      - echo "do deploy"

```

<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/pipeline.png"  width="400px"/>
<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/pipeline-2.png"  width="400px"/>


### Inspiration
This project is inspired by [wolox-ci](https://github.com/Wolox/wolox-ci)
