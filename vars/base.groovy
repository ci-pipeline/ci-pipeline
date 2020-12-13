def call(ObjectModel model) {
    timeout(time: 10, unit: 'MINUTES') {
        wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {
            execSteps(model)
        }
    }
}
