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

## Documentations

### image:
The image that will be used to run your build process. 
you can choose whatever image you want, it need to have the compiler toolchain commands preinstall so to run the build scripts you the `steps` section below.

### variables:
Variables to be injected in build environment as well as being passed to any running service (see `services` section below)

### services:
A list of docker images to be run while the build process is running. typically it might be a database or cache serivce.

You can pass variables to the image by using the `variables` section above. for example you can define the default username or password for the database in as a variable in the `variables` section above.

> Note: If you need the `docker` command available in your pipeline, for example you need to build and image for your app and push it to the registry, then include `docker` as service in this section (see example above).

### steps:
This is the section where you write your actual build scripts.

>Note: The steps might run in parallel by nesting the steps inside `parallel` (see example above).

Each step consist of the following:

* #### name:
Name of the step (required)

* #### only:
The branch list what the step will run on (optional). uses [ant glob pattern](http://ant.apache.org/manual/Types/fileset.html).

* #### except:
The branch list what the step will not run on (optional). uses [ant glob pattern](http://ant.apache.org/manual/Types/fileset.html).

* #### trigger:
Whether to trigger the job `manual` or `automatic`, the default is `automatic`

* #### actions:
Here is the actual scripts your write to do the build/test/deploy etc...
for examle you can do `mvn clean package` or `go build` or `npm install` or whatever.









### Inspiration
This project is inspired by [wolox-ci](https://github.com/Wolox/wolox-ci)
