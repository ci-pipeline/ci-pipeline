def call(ObjectModel model) {
    timeout(time: 10, unit: 'MINUTES') {
        withEnv(model.variables) {
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {

                try {
                    def image2Container = execService(model)
                    execSteps(model, image2Container)
                } finally {
                    image2Container.each { image, container ->
                        container.stop()
                        deleteImage(image)
                    }
                }
            }
        }
    }
}
