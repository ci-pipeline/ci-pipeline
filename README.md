## ci-pipeline
Run your jenkins pipeline with openiated `.ci-pipeline.yaml` (instead of the generic `Jenkinsfile`).

## Setup:

1. Download the plugin [pipeline-multibranch-defaults-plugin](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md)

2. Follow [steps](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md#create-a-default-jenkinsfile) to create default Jenkinsfile (under `Manage Jenkins` > `Managed files`), providing the following script:
```
library identifier: 'ci-pipeline@master',
        retriever: modernSCM([$class: 'GitSCMSource', remote: 'https://github.com/ci-pipeline/ci-pipeline.git'])

node {
  checkout scm
  ci('.ci-pipeline.yaml')
}
```

3a. [Create a multibranch pipeline](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md#create-a-multibranch-pipeline-job) with Jenkisfile points to the default Jenkinsfile created from the previous step, and configure the Branch Sources to point to your project repository.

3b. Or you can use [Job DSL to create](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md#example-job-dsl-configuration) multibranch pipelines.

```
multibranchPipelineJob('example') {
    // SCM source or additional configuration

    factory {
        pipelineBranchDefaultsProjectFactory {
            // The ID of the default Jenkinsfile to use from the global Config
            // File Management.
            scriptId 'Jenkinsfile'

            // If enabled, the configured default Jenkinsfile will be run within
            // a Groovy sandbox.
            useSandbox true
        }
    }
}

```

4. Your project should have the file `.ci-pipeline.yaml` [see example here](https://github.com/ci-pipeline/example_multibranch):

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
