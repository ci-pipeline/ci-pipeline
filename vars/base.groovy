def call(ObjectModel model) {
    timeout(time: 10, unit: 'MINUTES') {
        withEnv(model.variables) {
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {
                execSteps(model)
            }
        }
    }
}
