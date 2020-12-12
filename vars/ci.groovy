def call(String yamlName) {

    def yaml = readYaml file: yamlName
    ObjectModel model = ObjectModel.load(yaml)

    steps(model)
}
