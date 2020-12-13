def call(ObjectModel model, def nextClosure) {
    timeout(time: 600, unit: 'SECONDS') {
        wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {
            nextClosure(model)
        }
    }
}
