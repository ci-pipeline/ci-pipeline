
## Setup:

```groovy
library identifier: 'ci-pipeline@master',
        retriever: modernSCM([$class: 'GitSCMSource', remote: 'https://github.com/ci-pipeline/ci-pipeline.git'])

node {
  checkout([
        $class: 'GitSCM', 
        branches: [[name: '**']],
        doGenerateSubmoduleConfigurations: false, 
        extensions: [], 
        submoduleCfg: [], 
        userRemoteConfigs: [[url: '<path to the git repository url>']]
    ])
  ci('.ci-pipeline.yaml')
}
```

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
