def call(String yamlName) {

    def yaml

    try {
        yaml = readYaml file: yamlName
    } catch (error) {
        println("Cannot load .ci-pipeline.yaml make sure it is at the root of your source code: "+ error.getMessage())
        currentBuild.result = 'ABORTED'
        return
    }

    ObjectModel model = ObjectModel.load(yaml)
    base(model)
}
