
## Setup:

1. Download the plugin [pipeline-multibranch-defaults-plugin](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md)
2. Follow [steps](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md#create-a-default-jenkinsfile) to create default Jenkinsfile, providing the following script:
```
library identifier: 'ci-pipeline@master',
        retriever: modernSCM([$class: 'GitSCMSource', remote: 'https://github.com/ci-pipeline/ci-pipeline.git'])

node {
  checkout scm
  ci('.ci-pipeline.yaml')
}
```
3. [Create a multibranch pipeline](https://github.com/jenkinsci/pipeline-multibranch-defaults-plugin/blob/master/README.md#create-a-multibranch-pipeline-job) with Jenkisfile points to the default Jenkinsfile created from the previous step, and configure the Branch Sources to point to your project repository.
<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/pipeline-multibranch-defaults-plugin.png"  width="400px"/>

4. Your project should have the file `.ci-pipeline.yaml` [see example here](https://github.com/ci-pipeline/example_multibranch):

sample `.ci-pipeline.yaml`

```yaml
image: golang:alpine

steps:
  - name: build
    actions:
      - echo "building step 1"
      - echo "building step 2"
  - parallel:
      - name: unit test
        actions:
          - echo "unit testing"
      - name: integration test
        when: branch == develop
        actions:
          - echo "integration testing step 1"
          - echo "integration testing step 2"
  - name: deploy
  	when: branch == release/*
    actions:
      - echo "do deploy"
```


<img src="https://github.com/ci-pipeline/ci-pipeline/raw/master/etc/pipeline.png"  width="400px"/>