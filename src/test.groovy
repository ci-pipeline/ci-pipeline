import org.jenkinsci.plugins.pipeline.utility.steps.shaded.org.yaml.snakeyaml.Yaml


def yaml = new Yaml()

def yamlObject = yaml.load("""
image: golang

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
    only: 
    - develop
    actions: 
    - echo "integration testing step 1"
    - echo "integration testing step 2"
- name: deploy
  only: 
  - release/*
  actions: 
  - echo "do deploy"
  
variables:
  TEST_URL: http://example.com
  FULL_TEST_URL: \$TEST_URL/rest-of-url
""")

ObjectModel model = ObjectModel.load(yamlObject)

println(model.steps[0].isParallelStep())
println(model.steps[1].isParallelStep())
println(model.steps[2].isParallelStep())

println(model.variables)

